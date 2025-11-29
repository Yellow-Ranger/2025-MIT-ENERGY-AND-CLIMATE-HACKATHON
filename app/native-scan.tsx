import React, { useRef, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { CameraView, useCameraPermissions } from 'expo-camera';
import { useRouter } from 'expo-router';
import { Paths, Directory, File } from 'expo-file-system';

// Hooks
import { useSmoothOrientation } from '../hooks/useSmoothOrientation';
import { usePhotosphereCapture } from '../hooks/usePhotosphereCapture';
import { useAutoCapture } from '../hooks/useAutoCapture';

// Components
import { ActiveDot } from '../components/photosphere/ActiveDot';
import { CaptureFrame } from '../components/photosphere/CaptureFrame';
import { PanoramaOverlay } from '../components/photosphere/PanoramaOverlay';
import { ProgressIndicator } from '../components/photosphere/ProgressIndicator';
import { ActionBar } from '../components/photosphere/ActionBar';
import { CaptureMarkers } from '../components/photosphere/CaptureMarkers';

// Utils
import { calculateDotPosition } from '../utils/photosphere/dotPositioning';
import { firstMissingSegment, calculateTargetHeading } from '../utils/photosphere/orientationMath';
import { savePanoramaMetadata } from '../utils/panoramaMetadata';

export default function NativeScan() {
  const router = useRouter();
  const cameraRef = useRef<any>(null);
  const [permission, requestPermission] = useCameraPermissions();
  const isCapturingRef = useRef(false);

  // Hooks
  const orientation = useSmoothOrientation();
  const capture = usePhotosphereCapture();

  // Determine current capture mode
  const currentMode: 'horizontal' | 'ceiling' | 'floor' =
    capture.step === 'capturing-horizontal' ? 'horizontal' :
    capture.step === 'capturing-ceiling' ? 'ceiling' : 'floor';

  const segmentCount = currentMode === 'horizontal' ? 24 : 8;

  // Find next segment to capture
  const nextSegment =
    currentMode === 'horizontal' ? firstMissingSegment(capture.horizontalSegments, 24) :
    currentMode === 'ceiling' ? firstMissingSegment(capture.ceilingSegments, 8) :
    firstMissingSegment(capture.floorSegments, 8);

  // Calculate target heading for next segment
  const targetHeading = nextSegment !== null && capture.initialHeading !== null
    ? calculateTargetHeading(nextSegment, capture.initialHeading, segmentCount)
    : 0;

  // Handle auto-capture
  const handleAutoCapture = async () => {
    // Prevent concurrent captures
    if (isCapturingRef.current || !cameraRef.current || nextSegment === null) return;

    isCapturingRef.current = true;

    try {
      const photo = await cameraRef.current.takePictureAsync({
        quality: 0.8,
        skipProcessing: true,
      });

      // Save to file system using new expo-file-system API
      const timestamp = Date.now();
      const filename = `scan_${timestamp}_${nextSegment}.jpg`;

      // Create scans directory (check if exists first)
      const scansDir = new Directory(Paths.document, 'scans');
      if (!scansDir.exists) {
        scansDir.create();
      }

      // Create file reference
      const targetFile = new File(scansDir, filename);

      // Copy photo to target location
      const sourceFile = new File(photo.uri);
      sourceFile.copy(targetFile);

      const path = targetFile.uri;

      capture.captureSegment(
        nextSegment,
        orientation.heading!,
        orientation.pitch!,
        path,
        currentMode
      );
    } catch (err) {
      console.error('Capture failed:', err);
    } finally {
      isCapturingRef.current = false;
    }
  };

  // Auto-capture alignment detection (only during active capture, not initial alignment)
  const shouldAutoCapture =
    capture.step === 'capturing-horizontal' ||
    capture.step === 'capturing-ceiling' ||
    capture.step === 'capturing-floor';

  const alignment = useAutoCapture(
    shouldAutoCapture ? orientation.heading : null,
    targetHeading,
    handleAutoCapture
  );

  // Calculate dot position for horizontal capture
  const dotPosition =
    capture.step === 'capturing-horizontal' &&
    orientation.heading !== null &&
    capture.initialHeading !== null &&
    nextSegment !== null
      ? calculateDotPosition(orientation.heading, targetHeading)
      : null;

  const showMarkers =
    capture.step === 'capturing-horizontal' &&
    orientation.heading !== null;

  // Save and exit on completion
  useEffect(() => {
    if (capture.step === 'completion') {
      const saveAndExit = async () => {
        await savePanoramaMetadata(capture.images);
        setTimeout(() => router.push('/gallery'), 1500);
      };
      saveAndExit();
    }
  }, [capture.step]);

  // Render content based on current step
  const renderContent = () => {
    switch (capture.step) {
      case 'initial-alignment':
        return (
          <>
            <CaptureFrame />
            <ActiveDot position={{ x: 0, y: 0, distance: 0, scale: 1, color: 'white', opacity: 1 }} />
            <View style={styles.topBar}>
              <Text style={styles.instructionTitle}>To start, keep dot inside circle</Text>
              <Text style={styles.instructionSubtitle}>Hold steady, then tap confirm</Text>
            </View>
            <View style={styles.bottomBar}>
              <ActionBar
                onConfirm={() => capture.startCapture(orientation.heading!)}
                onCancel={() => router.back()}
                showConfirm={orientation.heading !== null}
              />
            </View>
          </>
        );

      case 'capturing-horizontal':
        return (
          <>
            <PanoramaOverlay images={capture.images} currentHeading={orientation.heading} />
            <CaptureFrame />
            {showMarkers && (
              <CaptureMarkers
                currentHeading={orientation.heading}
                targetHeading={targetHeading}
              />
            )}
            {dotPosition ? (
              <ActiveDot position={dotPosition} />
            ) : (
              // Fallback: show centered dot if position not calculated yet
              <ActiveDot position={{ x: 0, y: 0, distance: 0, scale: 1, color: 'white', opacity: 1 }} />
            )}
            <View style={styles.topBar}>
              <ProgressIndicator
                current={capture.horizontalSegments.size}
                total={24}
                title="Rotate slowly"
                subtitle="Follow the blue dot"
              />
            </View>
            <View style={styles.bottomBar}>
              <ActionBar onCancel={() => router.back()} />
            </View>
          </>
        );

      case 'ceiling-prompt':
        return (
          <View style={styles.promptOverlay}>
            <Text style={styles.promptTitle}>Capture ceiling?</Text>
            <Text style={styles.promptSubtitle}>Tilt up to capture overhead view (optional)</Text>
            <View style={styles.promptButtons}>
              <TouchableOpacity style={styles.promptButton} onPress={capture.startCeiling}>
                <Text style={styles.promptButtonText}>Yes</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.promptButton, styles.promptButtonSecondary]}
                onPress={capture.skipCeiling}
              >
                <Text style={styles.promptButtonText}>Skip</Text>
              </TouchableOpacity>
            </View>
          </View>
        );

      case 'floor-prompt':
        return (
          <View style={styles.promptOverlay}>
            <Text style={styles.promptTitle}>Capture floor?</Text>
            <Text style={styles.promptSubtitle}>Tilt down to capture ground view (optional)</Text>
            <View style={styles.promptButtons}>
              <TouchableOpacity style={styles.promptButton} onPress={capture.startFloor}>
                <Text style={styles.promptButtonText}>Yes</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.promptButton, styles.promptButtonSecondary]}
                onPress={capture.skipFloor}
              >
                <Text style={styles.promptButtonText}>Skip</Text>
              </TouchableOpacity>
            </View>
          </View>
        );

      case 'completion':
        return (
          <View style={styles.completionOverlay}>
            <Text style={styles.completionTitle}>Photosphere Complete!</Text>
            <Text style={styles.completionSubtitle}>Saving...</Text>
          </View>
        );

      default:
        return null;
    }
  };

  // Handle camera permissions
  if (!permission?.granted) {
    return (
      <View style={styles.permissionContainer}>
        <Text style={styles.permissionText}>Camera permission required</Text>
        <TouchableOpacity onPress={requestPermission}>
          <Text style={styles.permissionButton}>Grant Permission</Text>
        </TouchableOpacity>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <CameraView ref={cameraRef} style={styles.camera} facing="back" />
      {renderContent()}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  camera: {
    flex: 1,
  },
  topBar: {
    position: 'absolute',
    top: 60,
    left: 0,
    right: 0,
    alignItems: 'center',
    zIndex: 10,
  },
  bottomBar: {
    position: 'absolute',
    bottom: 40,
    left: 0,
    right: 0,
    zIndex: 10,
  },
  instructionTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: '#FFFFFF',
    marginBottom: 4,
  },
  instructionSubtitle: {
    fontSize: 14,
    color: 'rgba(255, 255, 255, 0.7)',
  },
  promptOverlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(0, 0, 0, 0.8)',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 20,
  },
  promptTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#FFFFFF',
    marginBottom: 12,
  },
  promptSubtitle: {
    fontSize: 16,
    color: 'rgba(255, 255, 255, 0.8)',
    marginBottom: 32,
    textAlign: 'center',
    paddingHorizontal: 40,
  },
  promptButtons: {
    flexDirection: 'row',
    gap: 16,
  },
  promptButton: {
    paddingVertical: 14,
    paddingHorizontal: 32,
    backgroundColor: '#4285F4',
    borderRadius: 8,
  },
  promptButtonSecondary: {
    backgroundColor: 'rgba(255, 255, 255, 0.2)',
  },
  promptButtonText: {
    fontSize: 16,
    fontWeight: '600',
    color: '#FFFFFF',
  },
  completionOverlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(0, 0, 0, 0.9)',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 20,
  },
  completionTitle: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#FFFFFF',
    marginBottom: 12,
  },
  completionSubtitle: {
    fontSize: 16,
    color: 'rgba(255, 255, 255, 0.7)',
  },
  permissionContainer: {
    flex: 1,
    backgroundColor: '#000',
    alignItems: 'center',
    justifyContent: 'center',
  },
  permissionText: {
    fontSize: 18,
    color: '#FFFFFF',
    marginBottom: 20,
  },
  permissionButton: {
    fontSize: 16,
    color: '#4285F4',
    fontWeight: '600',
  },
});
