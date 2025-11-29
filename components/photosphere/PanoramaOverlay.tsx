import React from "react";
import { View, Image, StyleSheet, Dimensions } from "react-native";
import { LinearGradient } from "expo-linear-gradient";
import type { CapturedImage } from "../../types/photosphere";

interface PanoramaOverlayProps {
  images: CapturedImage[];
  currentHeading: number | null;
  currentPitch: number | null;
}

/**
 * PanoramaOverlay component
 * Displays captured images stitched around the user with spherical projection.
 * Images are anchored to their exact capture position (heading + pitch).
 */
const ENABLE_PANO_LOG = true; // set to false to silence overlay debug

// Overlap percentage - images will overlap by this much to enable blending
const IMAGE_OVERLAP_PERCENT = 0.15; // 15% overlap on each side

export function PanoramaOverlay({
  images,
  currentHeading,
  currentPitch,
}: PanoramaOverlayProps) {
  if (currentHeading === null || currentPitch === null || images.length === 0) return null;

  const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } =
    Dimensions.get("window");
  const FRAME_WIDTH = 280;
  const FRAME_HEIGHT = 420;

  // Spherical projection - map the full 360° to screen pixels
  // Assume ~60° horizontal FOV for the camera (typical for phone cameras)
  const CAMERA_HFOV = 60; // degrees
  const PIXELS_PER_DEGREE = SCREEN_WIDTH / CAMERA_HFOV;

  // World width: full 360° mapped to pixels
  const WORLD_WIDTH = 360 * PIXELS_PER_DEGREE; // e.g., 402/60 * 360 = 2412px

  // Image dimensions with overlap for stitching
  const BASE_IMAGE_WIDTH = FRAME_WIDTH;
  const IMAGE_WIDTH = BASE_IMAGE_WIDTH * (1 + IMAGE_OVERLAP_PERCENT * 2);
  const IMAGE_HEIGHT = FRAME_HEIGHT;

  // Convert heading (0-360°) to world X coordinate
  const headingToX = (heading: number) => (heading / 360) * WORLD_WIDTH;

  // Camera position in world space
  const cameraX = headingToX(currentHeading % 360);

  // Calculate world container transform first (needed for debug logging)
  const worldTranslateX = SCREEN_WIDTH / 2 - cameraX;
  const worldTranslateY = SCREEN_HEIGHT / 2;

  const stitched = images
    .filter((img) => img.heading !== null) // Only show images with valid heading
    .flatMap((img, index) => {
      // Use ACTUAL capture heading, not target heading
      const captureHeading = img.heading!;
      const capturePitch = img.pitch ?? 0;

      // Position image at its actual capture heading
      const centerX = headingToX(captureHeading);
      const baseX = centerX - IMAGE_WIDTH / 2;

      // Vertical positioning based on pitch difference (spherical projection)
      // Position image relative to current view pitch
      // If image was captured at same pitch as current view, it should be centered
      const pitchDifference = capturePitch - currentPitch; // Degrees difference

      // Convert pitch difference to pixels
      const PIXELS_PER_DEGREE_VERTICAL = SCREEN_HEIGHT / 90; // Camera vertical FOV ~60-90°
      const verticalOffset = -pitchDifference * PIXELS_PER_DEGREE_VERTICAL; // Negative because screen Y is inverted

      // Position relative to container's vertical center (SCREEN_HEIGHT * 1.5 is middle of 3x height container)
      const baseY = SCREEN_HEIGHT * 1.5 + verticalOffset;

      if (__DEV__ && ENABLE_PANO_LOG && index === 0) {
        console.log("[PanoramaOverlay] Image vertical positioning:", {
          capturePitch: capturePitch.toFixed(1),
          currentPitch: currentPitch.toFixed(1),
          pitchDiff: pitchDifference.toFixed(1),
          verticalOffset: verticalOffset.toFixed(1),
          baseY: baseY.toFixed(1),
          SCREEN_HEIGHT,
        });
      }

      // Ensure the URI is properly formatted for React Native
      const imageUri = img.blob.startsWith("file://")
        ? img.blob
        : `file://${img.blob}`;

      // Create three copies for seamless 360° wrapping
      return [-WORLD_WIDTH, 0, WORLD_WIDTH].map((wrapOffset, copyIndex) => {
        const worldLeft = baseX + wrapOffset;

        // Calculate distance from current camera view for potential optimizations
        const imageCenterX = centerX + wrapOffset;
        const headingDistance = Math.min(
          Math.abs(imageCenterX - cameraX),
          WORLD_WIDTH - Math.abs(imageCenterX - cameraX)
        );

        // Only render images within view (performance optimization)
        // Disable culling for now to debug
        // if (headingDistance > SCREEN_WIDTH * 1.5) {
        //   return null;
        // }

        return (
          <View
            key={`${img.timestamp}-${index}-${copyIndex}`}
            style={{
              position: "absolute",
              width: IMAGE_WIDTH,
              height: IMAGE_HEIGHT,
              left: worldLeft,
              top: baseY,
              overflow: "hidden",
              borderWidth: 2,
              borderColor: "lime", // Debug: make it visible
              backgroundColor: "rgba(0,255,0,0.2)", // Debug: transparent green
            }}
          >
            <Image
              source={{ uri: imageUri }}
              style={{
                width: IMAGE_WIDTH,
                height: IMAGE_HEIGHT,
              }}
              resizeMode="cover"
              onError={(error) => {
                if (__DEV__) {
                  console.error("[PanoramaOverlay] Image load error:", {
                    uri: imageUri,
                    heading: captureHeading,
                    pitch: capturePitch,
                    error,
                  });
                }
              }}
              onLoad={() => {
                if (__DEV__ && ENABLE_PANO_LOG) {
                  console.log("[PanoramaOverlay] Image loaded:", {
                    heading: captureHeading.toFixed(1),
                    pitch: capturePitch.toFixed(1),
                    uri: imageUri.split("/").pop(),
                  });
                }
              }}
            />
            {/* Edge feathering gradients for seamless blending */}
            <LinearGradient
              colors={["rgba(0,0,0,0.3)", "transparent"]}
              start={{ x: 0, y: 0 }}
              end={{ x: 0.12, y: 0 }}
              style={styles.leftGradient}
              pointerEvents="none"
            />
            <LinearGradient
              colors={["transparent", "rgba(0,0,0,0.3)"]}
              start={{ x: 0.88, y: 0 }}
              end={{ x: 1, y: 0 }}
              style={styles.rightGradient}
              pointerEvents="none"
            />
          </View>
        );
      });
    })
    .filter((node): node is JSX.Element => Boolean(node));

  if (__DEV__ && ENABLE_PANO_LOG) {
    const validImages = images.filter((img) => img.heading !== null);
    const sample = validImages.slice(-2).map((img) => {
      const imgCenterX = headingToX(img.heading!);
      const imgBaseX = imgCenterX - IMAGE_WIDTH / 2;
      const finalScreenX = imgCenterX + worldTranslateX;
      return {
        heading: img.heading?.toFixed(1),
        pitch: img.pitch?.toFixed(1),
        centerX: imgCenterX.toFixed(1),
        baseX: imgBaseX.toFixed(1),
        finalScreenX: finalScreenX.toFixed(1),
      };
    });
    console.log("[PanoramaOverlay] Spherical render:", {
      totalImages: images.length,
      validImages: validImages.length,
      stitchedElements: stitched.length,
      currentHeading: currentHeading.toFixed(1),
      currentPitch: currentPitch.toFixed(1),
      cameraX: cameraX.toFixed(1),
      worldTranslateX: worldTranslateX.toFixed(1),
      screenCenter: (SCREEN_WIDTH / 2).toFixed(1),
      WORLD_WIDTH,
      SCREEN_WIDTH,
      PIXELS_PER_DEGREE: PIXELS_PER_DEGREE.toFixed(2),
      IMAGE_WIDTH,
      sample,
    });
  }

  return (
    <View style={styles.container} pointerEvents="box-none">
      <View
        style={{
          position: "absolute",
          width: WORLD_WIDTH * 3, // Triple width for wrapping (-1x, 0, +1x)
          height: SCREEN_HEIGHT * 3, // Large enough for vertical pitch range
          left: 0,
          top: -SCREEN_HEIGHT, // Position container so its center (1.5x height) can align with screen center
          overflow: "visible", // CRITICAL: Don't clip children
          transform: [
            { translateX: worldTranslateX },
          ],
          backgroundColor: "rgba(255,0,0,0.1)", // Debug: show world container
        }}
      >
        {stitched}
      </View>
      {stitched.length > 0 && <View style={styles.fadeMask} />}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    zIndex: 10, // Above camera (1) and capture frame (5)
    overflow: "hidden",
  },
  overlayImage: {
    position: "absolute",
    borderWidth: 0, // Remove debug borders for cleaner stitching
    borderColor: "red",
  },
  fadeMask: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: "rgba(0, 0, 0, 0.1)",
    pointerEvents: "none",
  },
  leftGradient: {
    position: "absolute",
    left: 0,
    top: 0,
    bottom: 0,
    width: "15%",
  },
  rightGradient: {
    position: "absolute",
    right: 0,
    top: 0,
    bottom: 0,
    width: "15%",
  },
});
