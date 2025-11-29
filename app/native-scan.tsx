import React, { useRef, useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { CameraView, useCameraPermissions } from 'expo-camera';
import { useRouter } from 'expo-router';
import { Paths, Directory, File } from 'expo-file-system';

// Hooks
import { useSmoothOrientation } from '../hooks/useSmoothOrientation';
import { usePhotosphereCapture } from '../hooks/usePhotosphereCapture';

// Components
import { CaptureFrame } from '../components/photosphere/CaptureFrame';
import { PanoramaOverlay } from '../components/photosphere/PanoramaOverlay';

// Utils
import { savePanoramaMetadata } from '../utils/panoramaMetadata';

export default function NativeScan() {
  const router = useRouter();
  const cameraRef = useRef<any>(null);
  const [permission, requestPermission] = useCameraPermissions();
  const isCapturingRef = useRef(false);

  // Hooks
  const orientation = useSmoothOrientation();
  const capture = usePhotosphereCapture();

  // Manual capture handler
  const handleManualCapture = async () => {
    if (isCapturingRef.current || !cameraRef.current) return;
    if (orientation.heading === null || orientation.pitch === null) return;

    isCapturingRef.current = true;

    try {
      const photo = await cameraRef.current.takePictureAsync({
        quality: 0.8,
        skipProcessing: true,
      });

      // Save to file system
      const timestamp = Date.now();
      const filename = `scan_${timestamp}.jpg`;

      const scansDir = new Directory(Paths.document, 'scans');
      if (!scansDir.exists) {
        scansDir.create();
      }

      const targetFile = new File(scansDir, filename);
      const sourceFile = new File(photo.uri);
      sourceFile.copy(targetFile);

      const path = targetFile.uri;

      // Capture with current orientation
      capture.captureImage(
        orientation.heading,
        orientation.pitch,
        path,
        'horizontal' // Default mode
      );
    } catch (err) {
      console.error('Capture failed:', err);
    } finally {
      isCapturingRef.current = false;
    }
  };

  // Save and exit on completion
  useEffect(() => {
    if (capture.step === 'completion') {
      const saveAndExit = async () => {
        // Generate unique scan ID
        const scanId = `scan_${Date.now()}`;

        // Create metadata from captured images
        const metadata = {
          version: '1.0.0',
          captureDate: new Date().toISOString(),
          totalImages: capture.images.length,
          positions: Math.max(...capture.images.map(img => img.position)),
          horizontalSegments: 24,
          verticalSegments: 8,
          images: capture.images.map(img => ({
            filePath: img.blob,
            timestamp: img.timestamp,
            position: img.position,
            segment: img.segment,
            captureType: img.captureType,
            heading: img.heading,
            pitch: img.pitch,
            targetHeading: img.targetHeading,
            targetPitch: img.targetPitch,
            spherical: {
              azimuth: img.targetHeading ?? img.heading ?? 0,
              elevation: img.captureType === 'ceiling'
                ? (img.targetPitch ?? 52.5)
                : img.captureType === 'floor'
                ? (img.targetPitch ?? -52.5)
                : (img.pitch ?? 0),
            },
          })),
        };

        await savePanoramaMetadata(scanId, metadata);
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
            <View style={styles.topBar}>
              <Text style={styles.instructionTitle}>Position camera for first image</Text>
              <Text style={styles.instructionSubtitle}>Tap confirm to begin free-form capture</Text>
            </View>
            <View style={styles.bottomBar}>
              <TouchableOpacity
                style={styles.button}
                onPress={() => router.back()}
              >
                <Text style={styles.buttonText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.button, styles.primaryButton]}
                onPress={() => capture.startCapture(orientation.heading || 0)}
                disabled={orientation.heading === null}
              >
                <Text style={styles.buttonText}>Start</Text>
              </TouchableOpacity>
            </View>
          </>
        );

      case 'capturing-horizontal':
      case 'capturing-ceiling':
      case 'capturing-floor':
        return (
          <>
            <CaptureFrame />
            <PanoramaOverlay
              images={capture.images}
              currentHeading={orientation.heading}
              currentPitch={orientation.pitch}
            />

            <View style={styles.topBar}>
              <Text style={styles.instructionTitle}>Free-form Photosphere Capture</Text>
              <Text style={styles.instructionSubtitle}>
                {capture.images.length} / 40 images captured
              </Text>
              <Text style={styles.orientationInfo}>
                Heading: {orientation.heading?.toFixed(0) ?? '—'}° |
                Pitch: {orientation.pitch?.toFixed(0) ?? '—'}°
              </Text>
            </View>

            <View style={styles.bottomBar}>
              <TouchableOpacity
                style={styles.button}
                onPress={() => router.back()}
              >
                <Text style={styles.buttonText}>Cancel</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={[styles.captureButton, isCapturingRef.current && styles.captureButtonDisabled]}
                onPress={handleManualCapture}
                disabled={isCapturingRef.current || orientation.heading === null}
              >
                <View style={styles.captureButtonInner} />
              </TouchableOpacity>

              <TouchableOpacity
                style={[styles.button, styles.primaryButton, capture.images.length < 20 && { opacity: 0.5 }]}
                onPress={capture.completeCapture}
                disabled={capture.images.length < 20}
              >
                <Text style={styles.buttonText}>Done</Text>
              </TouchableOpacity>
            </View>
          </>
        );

      case 'completion':
        return (
          <View style={styles.completionOverlay}>
            <Text style={styles.completionTitle}>Photosphere Complete!</Text>
            <Text style={styles.completionSubtitle}>Saving {capture.imageCount} images...</Text>
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
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
    paddingHorizontal: 20,
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
  orientationInfo: {
    fontSize: 12,
    color: 'rgba(255, 255, 255, 0.5)',
    marginTop: 8,
  },
  button: {
    paddingVertical: 12,
    paddingHorizontal: 24,
    backgroundColor: 'rgba(255, 255, 255, 0.2)',
    borderRadius: 8,
  },
  primaryButton: {
    backgroundColor: '#4285F4',
  },
  buttonText: {
    fontSize: 16,
    fontWeight: '600',
    color: '#FFFFFF',
  },
  captureButton: {
    width: 70,
    height: 70,
    borderRadius: 35,
    backgroundColor: 'rgba(255, 255, 255, 0.3)',
    borderWidth: 4,
    borderColor: '#FFFFFF',
    justifyContent: 'center',
    alignItems: 'center',
  },
  captureButtonDisabled: {
    opacity: 0.5,
  },
  captureButtonInner: {
    width: 54,
    height: 54,
    borderRadius: 27,
    backgroundColor: '#FFFFFF',
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
