import { useState, useEffect, useRef } from "react";
import {
  View,
  Text,
  StyleSheet,
  Pressable,
  Alert,
  Image,
  ScrollView,
  ActivityIndicator,
  Platform,
  NativeModules,
  Animated,
} from "react-native";
import { CameraView, useCameraPermissions } from "expo-camera";
import { Ionicons } from "@expo/vector-icons";
import { useRouter, useLocalSearchParams } from "expo-router";
import { Asset } from "expo-asset";
import { useNativeOrientation } from "@/hooks/useNativeOrientation";
import { useCaptureStore } from "@/utils/captureStore";
import {
  normalizeHeading,
  isInCeilingRange,
  isInFloorRange,
  calculateRotationSpeed,
  isSpeedAcceptable,
} from "@/utils/nativeCaptureUtils";
import { saveScan } from "@/utils/scanStorage";
import { hasRoomPlan } from "@/utils/deviceDetection";
import { ENABLE_ROOMPLAN, GENERATE_MOCK_STL } from "@/utils/featureFlags";
import * as FileSystem from "expo-file-system/legacy";
import { generateMockSTLFromPhotos } from "@/utils/mockStlGenerator";
import {
  createPanoramaMetadata,
  savePanoramaMetadata,
  generateStitchingScript,
} from "@/utils/panoramaMetadata";

// Capture throttles (ms)
const HORIZONTAL_CAPTURE_DELAY = 200;
const VERTICAL_CAPTURE_DELAY = 140; // quicker cadence for ceiling/floor sweeps
const ALIGN_TOLERANCE_DEGREES = 12; // Tolerance for automatic capture

const segmentCenterHeading = (segment: number, totalSegments: number) => {
  const segmentSize = 360 / totalSegments;
  return segment * segmentSize + segmentSize / 2;
};

const firstMissingSegment = (segments: Set<number>, totalSegments: number) => {
  for (let i = 0; i < totalSegments; i++) {
    if (!segments.has(i)) return i;
  }
  return null;
};

const smallestAngleDiff = (a: number, b: number) => {
  const diff = Math.abs(((a - b + 540) % 360) - 180);
  return diff;
};

type RoomPlanExportEvent = {
  nativeEvent?: {
    scanUrl?: string;
    jsonUrl?: string;
  };
};

type RoomPlanModule = typeof import("expo-roomplan");

type RoomPlanScannerProps = {
  roomPlanModule: RoomPlanModule;
};

const isRoomPlanNativeModuleAvailable = () =>
  Platform.OS === "ios" && Boolean((NativeModules as any)?.ExpoRoomPlan);

async function copyRoom2STLToScans(): Promise<string> {
  const asset = Asset.fromModule(require("../assets/room2.stl")); 
  if (!asset.localUri) {
    await asset.downloadAsync();
  }

  const sourceUri = asset.localUri ?? asset.uri;
  const directory = `${FileSystem.documentDirectory}scans/`;
  const dirInfo = await FileSystem.getInfoAsync(directory);
  if (!dirInfo.exists) {
    await FileSystem.makeDirectoryAsync(directory, { intermediates: true });
  }

  const destination = `${directory}room2_${Date.now()}.stl`;
  await FileSystem.copyAsync({
    from: sourceUri,
    to: destination,
  });

  return destination;
}

function RoomPlanScanner({ roomPlanModule }: RoomPlanScannerProps) {
  const { useRoomPlanView, RoomPlanView, ExportType } = roomPlanModule;
  const router = useRouter();
  const [showScanner, setShowScanner] = useState(false);
  const [saving, setSaving] = useState(false);
  const [status, setStatus] = useState("Ready to scan with RoomPlan");
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [userFriendlyMessage, setUserFriendlyMessage] = useState<string | null>(
    null
  );
  const [thumbnailUri, setThumbnailUri] = useState<string | undefined>();
  const hasSavedRef = useRef(false);
  const cameraRef = useRef<any>(null);
  const messageTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  // Helper function to convert technical messages to user-friendly ones
  const formatUserFriendlyMessage = (
    status: string,
    error?: string
  ): string | null => {
    // Clear previous timeout
    if (messageTimeoutRef.current) {
      clearTimeout(messageTimeoutRef.current);
    }

    // Handle error messages
    if (error) {
      const lowerError = error.toLowerCase();
      if (lowerError.includes("light") || lowerError.includes("dark")) {
        return "💡 More light required - Move to a brighter area";
      }
      if (lowerError.includes("texture") || lowerError.includes("feature")) {
        return "🎨 Not enough detail - Point at textured surfaces";
      }
      if (lowerError.includes("motion") || lowerError.includes("fast")) {
        return "🐌 Moving too fast - Slow down your movements";
      }
      if (lowerError.includes("distance")) {
        return "📏 Too close or too far - Adjust your distance";
      }
      // Generic error message
      return `⚠️ ${error}`;
    }

    // Handle status messages
    const lowerStatus = status.toLowerCase();
    if (lowerStatus.includes("scanning") || lowerStatus === "running") {
      return "📱 Scanning in progress - Slowly pan around the room";
    }
    if (lowerStatus.includes("initializing")) {
      return "🔄 Getting ready to scan...";
    }
    if (lowerStatus.includes("done") || lowerStatus.includes("complete")) {
      return "✅ Scan complete!";
    }
    if (lowerStatus.includes("preview")) {
      return "👀 Review your scan";
    }

    return null;
  };

  const handleStatus = (e: {
    nativeEvent: { status: any; errorMessage?: string };
  }) => {
    const { status: nextStatus, errorMessage: error } = e.nativeEvent;
    console.log("[RoomPlan] status:", nextStatus, error ? `- ${error}` : "");

    if (nextStatus) {
      setStatus(nextStatus);
    }

    // Update error message
    setErrorMessage(error || null);

    // Generate user-friendly message
    const friendlyMsg = formatUserFriendlyMessage(nextStatus, error);
    setUserFriendlyMessage(friendlyMsg);

    // Auto-hide non-error messages after 5 seconds
    if (friendlyMsg && !error) {
      messageTimeoutRef.current = setTimeout(() => {
        setUserFriendlyMessage(null);
      }, 5000);
    }
  };

  const handleExported = async (e: {
    nativeEvent: { scanUrl?: string; jsonUrl?: string };
  }) => {
    console.log("[RoomPlan] exported:", e.nativeEvent);
    if (hasSavedRef.current) return;
    hasSavedRef.current = true;
    await handleSave(e.nativeEvent.scanUrl, e.nativeEvent.jsonUrl);
  };

  const handlePreview = () => {
    console.log("[RoomPlan] preview presented");
    setStatus("Previewing capture");
  };

  const { viewProps, controls, state } = useRoomPlanView({
    scanName: `room-${Date.now()}`,
    exportType: ExportType.Parametric,
    exportOnFinish: true,
    sendFileLoc: true,
    autoCloseOnTerminalStatus: false,
    onStatus: handleStatus,
    onPreview: handlePreview,
    onExported: handleExported,
  });

  useEffect(() => {
    if (showScanner && !state.isRunning && !saving) {
      setShowScanner(false);
    }
  }, [showScanner, state.isRunning, saving]);

  // Cleanup timeout on unmount
  useEffect(() => {
    return () => {
      if (messageTimeoutRef.current) {
        clearTimeout(messageTimeoutRef.current);
      }
    };
  }, []);

  const handleSave = async (usdzUrl?: string, jsonUrl?: string) => {
    setSaving(true);
    try {
      await saveScan({
        scanMode: "lidar",
        pointCount: 0,
        data: { points: [] },
        roomPlan: {
          usdzUrl,
          jsonUrl,
          exportType: "parametric",
        },
        thumbnail: thumbnailUri,
      });

      Alert.alert(
        "RoomPlan Scan Saved",
        "Your LiDAR scan has been exported. You can find it in the Gallery.",
        [
          {
            text: "View Gallery",
            onPress: () => router.push("/gallery"),
          },
          {
            text: "OK",
            style: "default",
          },
        ]
      );
    } catch (error) {
      console.error("Failed to save RoomPlan scan:", error);
      Alert.alert("Error", "Failed to save RoomPlan scan.");
    } finally {
      setSaving(false);
      setShowScanner(false);
      setThumbnailUri(undefined);
    }
  };

  const captureThumbnail = async () => {
    if (!cameraRef.current) return;

    try {
      const photo = await cameraRef.current.takePictureAsync({
        quality: 0.5,
        base64: false,
      });

      if (photo && photo.uri) {
        const filename = `roomplan_thumb_${Date.now()}.jpg`;
        const directory = `${FileSystem.documentDirectory}thumbnails/`;

        const dirInfo = await FileSystem.getInfoAsync(directory);
        if (!dirInfo.exists) {
          await FileSystem.makeDirectoryAsync(directory, {
            intermediates: true,
          });
        }

        const newPath = `${directory}${filename}`;
        await FileSystem.moveAsync({
          from: photo.uri,
          to: newPath,
        });

        setThumbnailUri(newPath);
      }
    } catch (error) {
      console.error("Failed to capture thumbnail:", error);
    }
  };

  const openScanner = async () => {
    // Capture a thumbnail before starting the scan
    await captureThumbnail();

    hasSavedRef.current = false;
    setStatus("Scanning...");
    setShowScanner(true);
    controls.start();
  };

  const onCancel = () => {
    controls.cancel();
    setShowScanner(false);
  };

  const onFinish = () => {
    controls.finishScan();
  };

  const onNewRoom = () => {
    controls.addRoom();
  };

  return (
    <View style={styles.container}>
      {!showScanner && !saving && (
        <>
          <CameraView ref={cameraRef} style={styles.camera} facing="back" />
          <View style={styles.overlay}>
            <View style={styles.instructionsBox}>
              <Text style={styles.title}>LiDAR Scan (RoomPlan)</Text>
              <Text style={styles.subtitle}>
                This device supports Apple RoomPlan. Start a LiDAR-native
                capture for a quick, high-fidelity room mesh.
              </Text>
              <Pressable style={styles.startButton} onPress={openScanner}>
                <Text style={styles.startButtonText}>Start RoomPlan Scan</Text>
              </Pressable>
              <Text style={styles.statusText}>Status: {status}</Text>
            </View>
          </View>
        </>
      )}

      {showScanner && (
        <View style={styles.overlayFull}>
          <RoomPlanView style={StyleSheet.absoluteFill} {...viewProps} />

          {/* User-friendly message overlay */}
          {userFriendlyMessage && (
            <View style={styles.messageOverlay}>
              <View
                style={[
                  styles.messageBox,
                  errorMessage ? styles.messageBoxError : styles.messageBoxInfo,
                ]}
              >
                <Text style={styles.messageText}>{userFriendlyMessage}</Text>
              </View>
            </View>
          )}

          <View style={styles.roomPlanControls}>
            <Pressable style={styles.roomPlanControlButton} onPress={onCancel}>
              <Text style={styles.roomPlanControlText}>Cancel</Text>
            </Pressable>
            <Pressable style={styles.roomPlanControlButton} onPress={onFinish}>
              <Text style={styles.roomPlanControlText}>Finish</Text>
            </Pressable>
            <Pressable style={styles.roomPlanControlButton} onPress={onNewRoom}>
              <Text style={styles.roomPlanControlText}>Add Room</Text>
            </Pressable>
          </View>
        </View>
      )}

      {saving && (
        <View style={styles.overlay}>
          <View style={styles.instructionsBox}>
            <Text style={styles.title}>💾 Saving RoomPlan...</Text>
            <Text style={styles.subtitle}>Exporting USDZ locally</Text>
            <ActivityIndicator color="#00d4ff" style={{ marginTop: 12 }} />
          </View>
        </View>
      )}
    </View>
  );
}

export default function NativeScan() {
  const router = useRouter();
  const params = useLocalSearchParams();
  const selectedScanMode =
    (params.scanMode as "auto" | "lidar" | "photo") || "auto";
  const [cameraPermission, requestCameraPermission] = useCameraPermissions();
  const cameraRef = useRef<any>(null);

  const {
    orientation,
    permission: orientationPermission,
    requestPermission: requestOrientationPermission,
  } = useNativeOrientation();

  const lastCaptureTimeRef = useRef<number>(0);
  const lastHeadingRef = useRef<number | null>(null);
  const lastHeadingTimeRef = useRef<number>(0);
  const isCapturingRef = useRef<boolean>(false);
  const initialHeadingRef = useRef<number | null>(null); // Starting heading becomes center
  const [speedWarning, setSpeedWarning] = useState<boolean>(false);
  const [capturedImages, setCapturedImages] = useState<string[]>([]);
  const [previewUrls, setPreviewUrls] = useState<string[]>([]);
  const [roomPlanAvailable, setRoomPlanAvailable] = useState<boolean>(false);
  const [roomPlanModule, setRoomPlanModule] = useState<RoomPlanModule | null>(
    null
  );
  const [roomPlanError, setRoomPlanError] = useState<string | null>(null);
  const [deviceCheckComplete, setDeviceCheckComplete] =
    useState<boolean>(false);

  useEffect(() => {
    let mounted = true;
    const check = async () => {
      try {
        // Only check for RoomPlan if the feature flag is enabled
        const deviceSupportsRoomPlan = ENABLE_ROOMPLAN
          ? await hasRoomPlan()
          : false;

        // Respect user's scan mode selection
        let shouldUseRoomPlan = false;
        if (selectedScanMode === "auto") {
          // Auto mode: use RoomPlan if available
          shouldUseRoomPlan = deviceSupportsRoomPlan;
        } else if (selectedScanMode === "lidar") {
          // Force LiDAR mode: use RoomPlan if available
          shouldUseRoomPlan = deviceSupportsRoomPlan;
        } else if (selectedScanMode === "photo") {
          // Force photo mode: never use RoomPlan
          shouldUseRoomPlan = false;
        }

        if (mounted) {
          setRoomPlanAvailable(shouldUseRoomPlan);
        }
      } catch (err) {
        console.warn("RoomPlan capability check failed", err);
      } finally {
        if (mounted) {
          setDeviceCheckComplete(true);
        }
      }
    };

    check();
    return () => {
      mounted = false;
    };
  }, [selectedScanMode]);

  useEffect(() => {
    let cancelled = false;

    if (!roomPlanAvailable) {
      setRoomPlanModule(null);
      setRoomPlanError(null);
      return;
    }

    // if (!isRoomPlanNativeModuleAvailable()) {
    //   setRoomPlanError(
    //     "RoomPlan native module missing. Run a custom dev client (expo run:ios) or an EAS build with expo-roomplan; Expo Go cannot load RoomPlan."
    //   );
    //   setRoomPlanModule(null);
    //   return;
    // }

    (async () => {
      try {
        const mod = await import("expo-roomplan");
        if (!cancelled) {
          setRoomPlanModule(mod);
        }
      } catch (error) {
        console.warn("RoomPlan module unavailable", error);
        if (!cancelled) {
          setRoomPlanError(
            "RoomPlan native module missing. Rebuild the dev client or create a new EAS build with expo-roomplan installed."
          );
        }
      }
    })();

    return () => {
      cancelled = true;
    };
  }, [roomPlanAvailable]);

  // Refs to track segments for the capture loop
  const coveredSegmentsRef = useRef<Set<number>>(new Set());
  const ceilingSegmentsRef = useRef<Set<number>>(new Set());
  const floorSegmentsRef = useRef<Set<number>>(new Set());
  const orientationRef = useRef(orientation);
  const currentPositionRef = useRef<number>(1);

  const {
    step,
    currentPosition,
    coveredSegments,
    ceilingSegments,
    floorSegments,
    images,
    setStep,
    setPosition,
    markSegmentCovered,
    markCeilingSegmentCovered,
    markFloorSegmentCovered,
    addImage,
    canProceedToVertical,
    canProceedToFloor,
    canFinishPosition,
    reset,
  } = useCaptureStore();

  // Keep refs in sync with state
  useEffect(() => {
    coveredSegmentsRef.current = coveredSegments;
  }, [coveredSegments]);

  useEffect(() => {
    ceilingSegmentsRef.current = ceilingSegments;
  }, [ceilingSegments]);

  useEffect(() => {
    floorSegmentsRef.current = floorSegments;
  }, [floorSegments]);

  useEffect(() => {
    orientationRef.current = orientation;
  }, [orientation]);

  useEffect(() => {
    currentPositionRef.current = currentPosition;
  }, [currentPosition]);

  // Maintain preview URLs for recent captures
  useEffect(() => {
    if (capturedImages.length === 0) {
      setPreviewUrls([]);
      return;
    }

    const latestImages = capturedImages.slice(-4);
    setPreviewUrls(latestImages);
  }, [capturedImages]);

  // Capture loop
  useEffect(() => {
    if (roomPlanAvailable || !deviceCheckComplete) return;

    if (
      step === "initial" ||
      step === "uploading" ||
      step === "second-position-prompt"
    ) {
      console.log("⚠️ Capture loop not starting:", { step });
      return;
    }

    if (!cameraRef.current) {
      console.log("⚠️ Camera ref not ready yet");
      return;
    }

    console.log("🎬 Starting capture loop for step:", step);

    const interval = setInterval(async () => {
      const now = Date.now();
      const currentOrientation = orientationRef.current;
      const heading = normalizeHeading(currentOrientation.alpha);
      const pitch = currentOrientation.beta;

      // rotation speed warning (non-blocking)
      if (heading !== null && lastHeadingRef.current !== null) {
        const deltaTime = now - lastHeadingTimeRef.current;
        if (deltaTime > 0) {
          const speed = calculateRotationSpeed(
            lastHeadingRef.current,
            heading,
            deltaTime
          );
          if (!isSpeedAcceptable(speed) && !speedWarning) {
            setSpeedWarning(true);
            setTimeout(() => setSpeedWarning(false), 1500);
          }
        }
      }

      const attemptCapture = async (
        targetSegment: number | null,
        totalSegments: number,
        eligible: boolean,
        delay: number,
        markCovered: (segment: number) => void,
        captureType: 'horizontal' | 'ceiling' | 'floor'
      ) => {
        if (
          targetSegment === null ||
          heading === null ||
          !eligible ||
          now - lastCaptureTimeRef.current < delay
        ) {
          return;
        }

        const targetHeading = segmentCenterHeading(targetSegment, totalSegments);
        const aligned =
          smallestAngleDiff(targetHeading, heading) <= ALIGN_TOLERANCE_DEGREES;

        if (!aligned) return;

        try {
          await captureFrame(targetSegment, heading, pitch, captureType, targetHeading);
          markCovered(targetSegment);
          lastCaptureTimeRef.current = now;
          console.log(
            `✓ Captured segment ${targetSegment}/${totalSegments} for step ${step}`
          );
        } catch (err) {
          console.error("Failed to capture frame:", err);
        }
      };

      // Horizontal rotation capture (PhotoSphere-style: auto when aligned)
      if (step === "horizontal-rotation" || step === "second-rotation") {
        const targetSegment = firstMissingSegment(coveredSegmentsRef.current, 24);
        await attemptCapture(
          targetSegment,
          24,
          true,
          HORIZONTAL_CAPTURE_DELAY,
          markSegmentCovered,
          'horizontal'
        );
      }

      // Ceiling capture: requires tilt in range + close to target pitch
      if (step === "ceiling-capture") {
        const targetSegment = firstMissingSegment(ceilingSegmentsRef.current, 8);
        const targetPitch = 52.5; // Middle of ceiling range
        const pitchDiff = pitch !== null ? Math.abs(pitch - targetPitch) : 999;
        const eligible = pitch !== null && isInCeilingRange(pitch) && pitchDiff < 5;
        await attemptCapture(
          targetSegment,
          8,
          eligible,
          VERTICAL_CAPTURE_DELAY,
          markCeilingSegmentCovered,
          'ceiling'
        );
      }

      // Floor capture: requires tilt in range + close to target pitch
      if (step === "floor-capture") {
        const targetSegment = firstMissingSegment(floorSegmentsRef.current, 8);
        const targetPitch = -52.5; // Middle of floor range
        const pitchDiff = pitch !== null ? Math.abs(pitch - targetPitch) : 999;
        const eligible = pitch !== null && isInFloorRange(pitch) && pitchDiff < 5;
        await attemptCapture(
          targetSegment,
          8,
          eligible,
          VERTICAL_CAPTURE_DELAY,
          markFloorSegmentCovered,
          'floor'
        );
      }

      // track last heading time
      if (heading !== null) {
        lastHeadingRef.current = heading;
        lastHeadingTimeRef.current = now;
      }
    }, 100);

    return () => {
      console.log("🛑 Stopping capture loop for step:", step);
      clearInterval(interval);
    };
  }, [step, roomPlanAvailable, deviceCheckComplete]);

  // Auto-advance steps - complete when all 24 segments captured
  useEffect(() => {
    if (roomPlanAvailable || !deviceCheckComplete) return;

    if (step === "horizontal-rotation" || step === "second-rotation") {
      if (coveredSegments.size >= 24) {
        handleComplete();
      }
    }
  }, [coveredSegments, step, roomPlanAvailable, deviceCheckComplete]);

  const captureFrame = async (
    segment: number,
    heading: number | null,
    pitch: number | null,
    captureType: 'horizontal' | 'ceiling' | 'floor',
    targetHeading?: number
  ) => {
    // Prevent concurrent captures
    if (isCapturingRef.current) {
      console.log("Skipping capture - already capturing");
      return;
    }

    // Check if camera is still mounted
    if (!cameraRef.current) {
      console.warn("Camera ref not available, skipping capture");
      return;
    }

    isCapturingRef.current = true;

    try {
      const photo = await cameraRef.current.takePictureAsync({
        quality: 0.8,
        base64: false,
        skipProcessing: true, // Skip processing for faster capture
      });

      if (!photo || !photo.uri) {
        console.warn("Photo capture returned invalid result");
        isCapturingRef.current = false;
        return;
      }

      // Save to file system
      const filename = `scan_${Date.now()}_${segment}.jpg`;
      const directory = `${FileSystem.documentDirectory}scans/`;

      // Ensure directory exists
      const dirInfo = await FileSystem.getInfoAsync(directory);
      if (!dirInfo.exists) {
        await FileSystem.makeDirectoryAsync(directory, { intermediates: true });
      }

      const newPath = `${directory}${filename}`;
      const normalizedPath = newPath.startsWith("file://")
        ? newPath
        : `file://${newPath}`;
      await FileSystem.moveAsync({
        from: photo.uri,
        to: normalizedPath,
      });

      // Store file path
      setCapturedImages((prev) => [...prev, normalizedPath]);

      // Calculate target pitch for vertical captures
      let targetPitch: number | undefined;
      if (captureType === 'ceiling') {
        targetPitch = 52.5; // Middle of ceiling range
      } else if (captureType === 'floor') {
        targetPitch = -52.5; // Middle of floor range
      }

      // Add to store with stitching metadata
      addImage({
        blob: normalizedPath as any, // Store file path instead of blob
        timestamp: Date.now(),
        heading,
        pitch,
        position: currentPositionRef.current as 1 | 2,
        segment,
        captureType,
        targetHeading,
        targetPitch,
      });

      console.log(
        `Captured frame: segment ${segment}, type: ${captureType}, path: ${normalizedPath}`
      );
    } catch (error) {
      console.error("Error capturing frame:", error);
      // Don't throw - just log and continue
    } finally {
      isCapturingRef.current = false;
    }
  };

  const handleComplete = async () => {
    setStep("uploading");

    try {
      let stlUrl: string | undefined;

      try {
        stlUrl = await copyRoom2STLToScans();
      } catch (assetError) {
        console.warn("Failed to attach room2 STL:", assetError);
        if (GENERATE_MOCK_STL) {
          try {
            stlUrl = await generateMockSTLFromPhotos(
              Math.max(capturedImages.length, 1)
            );
          } catch (stlError) {
            console.warn("Failed to generate mock STL:", stlError);
          }
        }
      }

      // Save scan metadata
      const scanId = `scan_${Date.now()}`;
      const thumbnailUri = capturedImages[0] || undefined;

      // Create and save panorama metadata for 360 stitching
      const panoramaMetadata = createPanoramaMetadata(images);
      const metadataPath = await savePanoramaMetadata(scanId, panoramaMetadata);

      // Generate stitching script for external tools
      const stitchingScript = generateStitchingScript(panoramaMetadata);
      const scriptPath = `${FileSystem.documentDirectory}scans/${scanId}_stitching.txt`;
      await FileSystem.writeAsStringAsync(scriptPath, stitchingScript);

      console.log(`Saved ${images.length} images with panorama metadata`);
      console.log(`Metadata: ${metadataPath}`);
      console.log(`Stitching script: ${scriptPath}`);

      await saveScan({
        scanMode: "photo",
        pointCount: capturedImages.length,
        data: {
          points: [], // No point cloud data yet
        },
        backendScanId: scanId,
        thumbnail: thumbnailUri,
        stlUrl,
        panoramaMetadataPath: metadataPath,
      });

      Alert.alert(
        "Scan Complete!",
        `Captured ${capturedImages.length} images with 360° panorama data`,
        [
          {
            text: "View Gallery",
            onPress: () => router.push("/gallery"),
          },
          {
            text: "OK",
            style: "default",
          },
        ]
      );
    } catch (error) {
      console.error("Error saving scan:", error);
      Alert.alert("Error", "Failed to save scan");
    }
  };

  if (!deviceCheckComplete) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#00d4ff" />
        <Text style={styles.loadingText}>Checking device capabilities...</Text>
      </View>
    );
  }

  if (roomPlanAvailable) {
    if (!cameraPermission) {
      return <View style={styles.container} />;
    }

    if (!cameraPermission.granted) {
      return (
        <View style={styles.container}>
          <View style={styles.permissionContainer}>
            <Text style={styles.permissionText}>
              Camera permission required for RoomPlan
            </Text>
            <Pressable
              style={styles.permissionButton}
              onPress={requestCameraPermission}
            >
              <Text style={styles.permissionButtonText}>Grant Permission</Text>
            </Pressable>
          </View>
        </View>
      );
    }

    if (roomPlanError) {
      return (
        <View style={styles.container}>
          <View style={styles.permissionContainer}>
            <Text style={styles.permissionText}>{roomPlanError}</Text>
            <Text style={styles.permissionText}>
              If you just added expo-roomplan, rebuild your dev client or make a
              new EAS build.
            </Text>
          </View>
        </View>
      );
    }

    if (!roomPlanModule) {
      return (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#00d4ff" />
          <Text style={styles.loadingText}>Loading RoomPlan module...</Text>
        </View>
      );
    }

    return <RoomPlanScanner roomPlanModule={roomPlanModule} />;
  }

  if (!cameraPermission || !orientationPermission) {
    return <View style={styles.container} />;
  }

  if (!cameraPermission.granted) {
    return (
      <View style={styles.container}>
        <View style={styles.permissionContainer}>
          <Text style={styles.permissionText}>Camera permission required</Text>
          <Pressable
            style={styles.permissionButton}
            onPress={requestCameraPermission}
          >
            <Text style={styles.permissionButtonText}>Grant Permission</Text>
          </Pressable>
        </View>
      </View>
    );
  }

  if (orientationPermission !== "granted") {
    return (
      <View style={styles.container}>
        <View style={styles.permissionContainer}>
          <Text style={styles.permissionText}>
            Device orientation permission required
          </Text>
          <Pressable
            style={styles.permissionButton}
            onPress={requestOrientationPermission}
          >
            <Text style={styles.permissionButtonText}>Grant Permission</Text>
          </Pressable>
        </View>
      </View>
    );
  }

  const clearCaptureRefs = () => {
    lastCaptureTimeRef.current = 0;
    lastHeadingRef.current = null;
    lastHeadingTimeRef.current = 0;
    coveredSegmentsRef.current = new Set();
    ceilingSegmentsRef.current = new Set();
    floorSegmentsRef.current = new Set();
  };

  const resetCaptureState = () => {
    reset();
    clearCaptureRefs();
    setCapturedImages([]);
    setPreviewUrls([]);
    setPosition(1);
  };

  const handleCancelScan = () => {
    resetCaptureState();
    router.back();
  };

  const handleBackStep = () => {
    if (step === "horizontal-rotation" || step === "second-rotation") {
      resetCaptureState();
      setStep("initial");
      return;
    }
    if (step === "ceiling-capture") {
      setStep("horizontal-rotation");
      return;
    }
    if (step === "floor-capture") {
      setStep("ceiling-capture");
      return;
    }
    if (step === "second-position-prompt") {
      setStep("horizontal-rotation");
    }
  };

  const handlePrimaryAction = () => {
    if (step === "initial") {
      clearCaptureRefs();
      // Set initial heading as the center point
      const currentHeading = normalizeHeading(orientation.alpha);
      if (currentHeading !== null) {
        initialHeadingRef.current = currentHeading;
        console.log(`Starting capture with initial heading: ${currentHeading}°`);
      }
      setStep("horizontal-rotation");
      return;
    }
    if (step === "horizontal-rotation" || step === "second-rotation") {
      // Complete when all 24 segments are captured
      if (coveredSegments.size >= 24) {
        handleComplete();
      }
      return;
    }
  };

  const renderContent = () => {
    const heading = orientation.alpha;
    const pitch = orientation.beta;
    const horizontalProgress = Math.round((coveredSegments.size / 24) * 100);
    const ceilingProgress = Math.round((ceilingSegments.size / 8) * 100);
    const floorProgress = Math.round((floorSegments.size / 8) * 100);

    // Render guide dots at the edges of the frame showing next capture positions
    const renderGuideDots = (mode: "horizontal" | "ceiling" | "floor") => {
      if (mode === "horizontal") {
        const guideDots = [];
        const frameWidth = 240; // Width of psFrame in percentage terms
        const frameHeight = 360; // Approximate height

        // Place dots at left, right, top, bottom edges of frame
        const positions = [
          { x: -frameWidth / 2, y: 0, segment: -6 }, // Left
          { x: frameWidth / 2, y: 0, segment: 6 },   // Right
          { x: 0, y: -frameHeight / 2.5, segment: -12 }, // Top
          { x: 0, y: frameHeight / 2.5, segment: 12 },  // Bottom
        ];

        positions.forEach((pos, idx) => {
          if (heading !== null) {
            const currentSegment = Math.floor((heading / 360) * 24);
            const targetSegment = (currentSegment + pos.segment + 24) % 24;
            const isCaptured = coveredSegments.has(targetSegment);

            if (!isCaptured) {
              guideDots.push(
                <View
                  key={idx}
                  style={[
                    styles.guideDot,
                    { transform: [{ translateX: pos.x }, { translateY: pos.y }] },
                  ]}
                />
              );
            }
          }
        });

        return guideDots;
      }
      return null;
    };

    const renderDot = (mode: "horizontal" | "ceiling" | "floor") => {
      if (mode === "horizontal") {
        const nextSegment = firstMissingSegment(coveredSegments, 24);
        if (heading == null || nextSegment === null || initialHeadingRef.current === null) {
          return <View style={[styles.psDot, styles.psDotIdle]} />;
        }

        // Calculate target heading relative to initial heading
        const segmentAngle = (nextSegment * 360) / 24;
        const targetHeading = (initialHeadingRef.current + segmentAngle) % 360;
        const angleDiff = smallestAngleDiff(heading, targetHeading);

        // Calculate position - dot moves from edge toward center
        const frameWidth = 120; // Max distance from center (edge of frame)
        const alignmentThreshold = 40; // Start moving when within this angle
        const captureThreshold = 12; // Turn blue when very close

        let distance;
        if (angleDiff > alignmentThreshold) {
          distance = frameWidth; // Stay at edge
        } else {
          // Move from edge to center
          distance = frameWidth * (angleDiff / alignmentThreshold);
        }

        const rad = ((targetHeading - heading) * Math.PI) / 180;
        const x = Math.sin(rad) * distance;
        const y = -Math.cos(rad) * distance;
        const aligned = angleDiff < captureThreshold;

        // Calculate dot scale - expands as it gets closer to center
        const baseScale = 1;
        const maxScale = 1.5; // Expand to 1.5x size when at center
        const scaleProgress = 1 - (distance / frameWidth); // 0 at edge, 1 at center
        const scale = baseScale + (maxScale - baseScale) * scaleProgress;

        return (
          <View
            style={[
              styles.psDot,
              {
                transform: [
                  { translateX: x },
                  { translateY: y },
                  { scale: scale }
                ]
              },
              aligned && styles.psDotAligned,
            ]}
          />
        );
      }

      if (mode === "ceiling") {
        const nextSegment = firstMissingSegment(ceilingSegments, 8);
        if (pitch === null || nextSegment === null) {
          return <View style={[styles.psDot, styles.psDotIdle, { transform: [{ translateY: -80 }] }]} />;
        }

        // Calculate how close we are to the target pitch range
        const inRange = isInCeilingRange(pitch);
        const targetPitch = 52.5; // Middle of ceiling range (45-60)
        const pitchDiff = Math.abs(pitch - targetPitch);

        // Move dot vertically based on alignment
        const maxDistance = 80;
        const alignmentThreshold = 20;

        let distance;
        if (pitchDiff > alignmentThreshold || !inRange) {
          distance = maxDistance;
        } else {
          distance = maxDistance * (pitchDiff / alignmentThreshold);
        }

        const aligned = inRange && pitchDiff < 8;

        return (
          <View
            style={[
              styles.psDot,
              styles.psDotVertical,
              { transform: [{ translateY: -distance }] },
              aligned && styles.psDotAligned,
            ]}
          />
        );
      }

      // Floor mode
      const nextSegment = firstMissingSegment(floorSegments, 8);
      if (pitch === null || nextSegment === null) {
        return <View style={[styles.psDot, styles.psDotIdle, { transform: [{ translateY: 80 }] }]} />;
      }

      const inRange = isInFloorRange(pitch);
      const targetPitch = -52.5; // Middle of floor range (-60 to -45)
      const pitchDiff = Math.abs(pitch - targetPitch);

      // Move dot vertically based on alignment
      const maxDistance = 80;
      const alignmentThreshold = 20;

      let distance;
      if (pitchDiff > alignmentThreshold || !inRange) {
        distance = maxDistance;
      } else {
        distance = maxDistance * (pitchDiff / alignmentThreshold);
      }

      const aligned = inRange && pitchDiff < 8;

      return (
        <View
          style={[
            styles.psDot,
            styles.psDotVertical,
            { transform: [{ translateY: distance }] },
            aligned && styles.psDotAligned,
          ]}
        />
      );
    };

    const renderHud = ({
      title,
      subtitle,
      progressPercent,
      mode,
      ready,
      progressDetail,
    }: {
      title: string;
      subtitle: string;
      progressPercent: number;
      mode: "horizontal" | "ceiling" | "floor";
      ready?: boolean;
      progressDetail?: string;
    }) => (
      <View style={styles.photosphereOverlay}>
        <View style={styles.psTopBar}>
          <Text style={styles.psInstructionTitle}>{title}</Text>
          <Text style={styles.psInstructionSubtitle}>{subtitle}</Text>
          {speedWarning && (
            <Text style={styles.psSpeedWarning}>Too fast — move slower</Text>
          )}
        </View>

        <View style={styles.psViewport}>
          <View style={styles.psFrame} />
          <View style={styles.psFocusRing} />
          {renderGuideDots(mode)}
          {renderDot(mode)}
        </View>

        <View style={styles.psProgressRow}>
          <View style={styles.psProgressBar}>
            <View
              style={[
                styles.psProgressFill,
                { width: `${Math.min(100, progressPercent)}%` },
              ]}
            />
          </View>
          <Text style={styles.psProgressLabel}>
            {progressPercent}% · {progressDetail || `${capturedImages.length} photos`}
          </Text>
        </View>

        <View style={styles.psBottomBar}>
          <Pressable style={styles.psNavButton} onPress={handleBackStep}>
            <Ionicons name="arrow-undo" size={30} color="#fff" />
          </Pressable>

          <Pressable
            style={[
              styles.psShutter,
              ready ? styles.psShutterReady : styles.psShutterIdle,
            ]}
            onPress={ready ? handlePrimaryAction : undefined}
            disabled={!ready}
          >
            {ready ? (
              <Ionicons name="checkmark" size={28} color="#fff" />
            ) : (
              <>
                <View style={styles.psShutterInner} />
                <View style={styles.psShutterNotch} />
              </>
            )}
          </Pressable>

          <Pressable style={styles.psNavButton} onPress={handleCancelScan}>
            <Ionicons name="close" size={30} color="#fff" />
          </Pressable>
        </View>
      </View>
    );

    switch (step) {
      case "initial":
        return renderHud({
          title: "To start, keep the dot inside the circle",
          subtitle: "Tap the check to begin a PhotoSphere-style capture",
          progressPercent: 0,
          mode: "horizontal",
          ready: true,
          progressDetail: "Ready when you are",
        });

      case "horizontal-rotation":
      case "second-rotation":
        return renderHud({
          title: "Capture 360° Panorama",
          subtitle: "Slowly rotate around - dot will move to center when aligned",
          progressPercent: horizontalProgress,
          mode: "horizontal",
          ready: coveredSegments.size >= 24,
          progressDetail: `${coveredSegments.size}/24 segments`,
        });

      case "uploading":
        return (
          <View style={styles.overlay}>
            <View style={styles.instructionsBox}>
              <Text style={styles.title}>💾 Saving Scan...</Text>
              <Text style={styles.subtitle}>
                {capturedImages.length} images captured
              </Text>
            </View>
          </View>
        );

      default:
        return null;
    }
  };

  // Render captured photos overlaid on the camera view
  const renderCapturedPhotosOverlay = () => {
    if (step === "initial" || step === "uploading" || images.length === 0) {
      return null;
    }

    const currentHeading = orientation.alpha;
    if (currentHeading === null) return null;

    // Only show horizontal rotation images for current position
    const horizontalImages = images
      .filter(img => img.captureType === 'horizontal' && img.position === currentPosition);

    if (horizontalImages.length === 0) return null;

    return (
      <View style={styles.photoOverlayContainer}>
        {horizontalImages.map((img, index) => {
          // Calculate where this photo should be positioned relative to current heading
          const imgHeading = img.targetHeading ?? img.heading ?? 0;

          // Calculate angle difference
          let angleDiff = imgHeading - currentHeading;
          // Normalize to -180 to 180
          if (angleDiff > 180) angleDiff -= 360;
          if (angleDiff < -180) angleDiff += 360;

          // Only show images within ~90 degrees of current view
          if (Math.abs(angleDiff) > 90) return null;

          // Calculate horizontal offset based on angle difference
          // Each segment is 15 degrees (360/24), map to screen width
          const screenWidth = 400; // Approximate screen width in viewport
          const offsetX = (angleDiff / 90) * (screenWidth / 2);

          // Calculate opacity - fade out images further from center
          const opacity = Math.max(0.3, 1 - Math.abs(angleDiff) / 90);

          return (
            <Image
              key={index}
              source={{ uri: img.blob }}
              style={[
                styles.overlayPhoto,
                {
                  opacity,
                  transform: [{ translateX: offsetX }],
                },
              ]}
              resizeMode="cover"
            />
          );
        })}
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <CameraView ref={cameraRef} style={styles.camera} facing="back">
        {/* Real-time photo overlay showing captured segments */}
        {renderCapturedPhotosOverlay()}

        {renderContent()}
      </CameraView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#000",
  },
  camera: {
    flex: 1,
  },
  permissionContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 24,
  },
  permissionText: {
    fontSize: 16,
    color: "#fff",
    textAlign: "center",
    marginBottom: 24,
  },
  permissionButton: {
    backgroundColor: "#00d4ff",
    paddingVertical: 16,
    paddingHorizontal: 32,
    borderRadius: 12,
  },
  permissionButtonText: {
    color: "#000",
    fontSize: 16,
    fontWeight: "700",
  },
  overlay: {
    flex: 1,
    backgroundColor: "rgba(0, 0, 0, 0.6)",
  },
  overlayFull: {
    ...StyleSheet.absoluteFillObject,
    zIndex: 100,
    height: "100%",
  },
  topInfo: {
    position: "absolute",
    top: 60,
    left: 24,
    right: 24,
    backgroundColor: "rgba(0, 0, 0, 0.4)",
    padding: 16,
    borderRadius: 16,
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.1)",
  },
  infoTitle: {
    fontSize: 24,
    fontWeight: "700",
    color: "#fff",
    marginBottom: 8,
  },
  infoText: {
    fontSize: 14,
    color: "rgba(160, 160, 160, 1)",
    lineHeight: 20,
  },
  pitchText: {
    fontSize: 14,
    color: "rgba(160, 160, 160, 1)",
    marginTop: 8,
  },
  warningText: {
    fontSize: 14,
    color: "#fbbf24",
    marginTop: 8,
    fontWeight: "600",
  },
  centerRing: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  centerText: {
    position: "absolute",
    alignItems: "center",
  },
  percentageText: {
    fontSize: 40,
    fontWeight: "700",
    color: "#fff",
  },
  segmentText: {
    fontSize: 14,
    color: "rgba(160, 160, 160, 1)",
    marginTop: 4,
  },
  bottomInfo: {
    position: "absolute",
    bottom: 100,
    left: 24,
    right: 24,
    alignItems: "center",
  },
  progressText: {
    fontSize: 14,
    color: "rgba(160, 160, 160, 1)",
    fontWeight: "500",
  },
  photoOverlayContainer: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: "center",
    alignItems: "center",
    pointerEvents: "none",
  },
  overlayPhoto: {
    position: "absolute",
    width: "100%",
    height: "100%",
    opacity: 0.4,
  },
  previewContainer: {
    position: "absolute",
    bottom: 16,
    left: 0,
    right: 0,
    alignItems: "center",
  },
  previewScroll: {
    gap: 8,
    paddingHorizontal: 12,
    paddingVertical: 8,
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    borderRadius: 16,
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.1)",
  },
  previewImageContainer: {
    width: 64,
    height: 64,
    borderRadius: 8,
    overflow: "hidden",
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.2)",
  },
  previewImage: {
    width: "100%",
    height: "100%",
  },
  instructionsBox: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 32,
    backgroundColor: "rgba(0, 0, 0, 0.4)",
    margin: 32,
    borderRadius: 24,
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.1)",
  },
  title: {
    fontSize: 32,
    fontWeight: "700",
    color: "#fff",
    marginBottom: 16,
    textAlign: "center",
    letterSpacing: -0.5,
  },
  subtitle: {
    fontSize: 16,
    color: "rgba(160, 160, 160, 1)",
    textAlign: "center",
    marginBottom: 32,
    lineHeight: 24,
    paddingHorizontal: 20,
  },
  startButton: {
    backgroundColor: "#00d4ff",
    paddingVertical: 18,
    paddingHorizontal: 48,
    borderRadius: 12,
    marginBottom: 16,
    shadowColor: "#00d4ff",
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 8,
  },
  startButtonText: {
    color: "#000",
    fontSize: 18,
    fontWeight: "700",
    letterSpacing: 0.5,
  },
  secondaryButton: {
    backgroundColor: "rgba(255, 255, 255, 0.1)",
    paddingVertical: 18,
    paddingHorizontal: 48,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.2)",
  },
  secondaryButtonText: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "600",
    letterSpacing: 0.5,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#000",
  },
  loadingText: {
    marginTop: 12,
    color: "#fff",
    fontSize: 16,
  },
  statusText: {
    marginTop: 12,
    color: "rgba(160, 160, 160, 1)",
    fontSize: 14,
    textAlign: "center",
  },
  roomPlanControls: {
    position: "absolute",
    top: 48,
    left: 16,
    right: 16,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 8,
  },
  roomPlanControlButton: {
    backgroundColor: "rgba(0, 0, 0, 0.6)",
    paddingVertical: 10,
    paddingHorizontal: 14,
    borderRadius: 10,
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.2)",
  },
  roomPlanControlText: {
    color: "#fff",
    fontWeight: "600",
    fontSize: 14,
  },
  messageOverlay: {
    position: "absolute",
    top: 120,
    left: 16,
    right: 16,
    alignItems: "center",
    zIndex: 1000,
  },
  messageBox: {
    paddingVertical: 14,
    paddingHorizontal: 20,
    borderRadius: 12,
    maxWidth: "90%",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
    elevation: 5,
  },
  messageBoxInfo: {
    backgroundColor: "rgba(0, 212, 255, 0.95)",
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.3)",
  },
  messageBoxError: {
    backgroundColor: "rgba(251, 191, 36, 0.95)",
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.3)",
  },
  messageText: {
    color: "#000",
    fontSize: 15,
    fontWeight: "600",
    textAlign: "center",
    lineHeight: 20,
  },
  photosphereOverlay: {
    flex: 1,
    backgroundColor: "rgba(10, 10, 10, 0.35)",
    justifyContent: "space-between",
    paddingTop: 48,
    paddingBottom: 28,
    paddingHorizontal: 20,
  },
  psTopBar: {
    alignItems: "center",
    gap: 8,
  },
  psInstructionTitle: {
    color: "#f4f4f4",
    fontSize: 20,
    fontWeight: "700",
    textAlign: "center",
  },
  psInstructionSubtitle: {
    color: "rgba(255,255,255,0.8)",
    fontSize: 15,
    textAlign: "center",
  },
  psSpeedWarning: {
    color: "#fbbf24",
    fontWeight: "700",
    fontSize: 14,
    marginTop: 4,
  },
  psViewport: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  psFrame: {
    position: "absolute",
    width: "64%",
    aspectRatio: 9 / 16,
    borderWidth: 1.5,
    borderColor: "rgba(255,255,255,0.7)",
    borderRadius: 8,
  },
  psFocusRing: {
    width: 140,
    height: 140,
    borderRadius: 70,
    borderWidth: 3,
    borderColor: "rgba(255,255,255,0.75)",
    justifyContent: "center",
    alignItems: "center",
  },
  psDot: {
    position: "absolute",
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: "#ffffff",
    borderWidth: 3,
    borderColor: "#ffffff",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
    elevation: 5,
  },
  psDotVertical: {
    width: 48,
    height: 48,
    borderRadius: 24,
  },
  psDotIdle: {
    opacity: 0.9,
  },
  psDotAligned: {
    backgroundColor: "#4285f4",
    borderColor: "#4285f4",
  },
  guideDot: {
    position: "absolute",
    width: 16,
    height: 16,
    borderRadius: 8,
    backgroundColor: "rgba(255, 255, 255, 0.7)",
    borderWidth: 2,
    borderColor: "#ffffff",
  },
  psProgressRow: {
    width: "100%",
    gap: 10,
  },
  psProgressBar: {
    width: "100%",
    height: 6,
    borderRadius: 12,
    backgroundColor: "rgba(255,255,255,0.15)",
    overflow: "hidden",
  },
  psProgressFill: {
    height: "100%",
    backgroundColor: "#f59e0b",
  },
  psProgressLabel: {
    color: "rgba(255,255,255,0.8)",
    fontSize: 14,
    textAlign: "center",
  },
  psBottomBar: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 16,
  },
  psNavButton: {
    width: 54,
    height: 54,
    borderRadius: 27,
    backgroundColor: "rgba(0,0,0,0.35)",
    alignItems: "center",
    justifyContent: "center",
    borderWidth: 1,
    borderColor: "rgba(255,255,255,0.15)",
  },
  psShutter: {
    width: 82,
    height: 82,
    borderRadius: 41,
    alignItems: "center",
    justifyContent: "center",
    borderWidth: 3,
    borderColor: "#fff",
    backgroundColor: "#0b0b0b",
  },
  psShutterInner: {
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: "#fff",
  },
  psShutterNotch: {
    position: "absolute",
    top: 8,
    width: 18,
    height: 10,
    borderTopLeftRadius: 18,
    borderTopRightRadius: 18,
    backgroundColor: "#f59e0b",
  },
  psShutterReady: {
    backgroundColor: "#f59e0b",
    borderColor: "#f59e0b",
  },
  psShutterIdle: {
    opacity: 0.9,
  },
});
