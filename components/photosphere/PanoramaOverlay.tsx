import React from "react";
import { View, Image, StyleSheet, Dimensions } from "react-native";
import type { CapturedImage } from "../../types/photosphere";

interface PanoramaOverlayProps {
  images: CapturedImage[];
  currentHeading: number | null;
}

/**
 * PanoramaOverlay component
 * Displays captured images stitched around the user with a parallax band.
 * Images orbit around the center instead of staying inside the capture box.
 */
const ENABLE_PANO_LOG = true; // set to false to silence overlay debug

export function PanoramaOverlay({
  images,
  currentHeading,
}: PanoramaOverlayProps) {
  if (currentHeading === null || images.length === 0) return null;

  const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } =
    Dimensions.get("window");
  const FRAME_WIDTH = 280;
  const FRAME_HEIGHT = 420;
  const IMAGE_WIDTH = FRAME_WIDTH;
  const IMAGE_HEIGHT = FRAME_HEIGHT;

  // World sizing - simplified to just horizontal wrapping
  const WORLD_WIDTH = IMAGE_WIDTH * 24; // 24 segments across 360°

  const wrap = (value: number, mod: number) => {
    const wrapped = value % mod;
    return wrapped < 0 ? wrapped + mod : wrapped;
  };

  const headingToX = (heading: number) => (heading / 360) * WORLD_WIDTH;

  const cameraX = wrap(headingToX(currentHeading), WORLD_WIDTH);

  const stitched = images
    .filter(
      (img) =>
        img.captureType === "horizontal" && img.targetHeading !== undefined
    )
    .flatMap((img, index) => {
      const baseX = headingToX(img.targetHeading!) - IMAGE_WIDTH / 2;
      // Center images vertically on screen (no complex pitch calculations)
      const baseY = 0;

      // Ensure the URI is properly formatted for React Native
      const imageUri = img.blob.startsWith("file://")
        ? img.blob
        : `file://${img.blob}`;

      return [-WORLD_WIDTH, 0, WORLD_WIDTH].map((wrapOffset, copyIndex) => {
        const worldLeft = baseX + wrapOffset;

        return (
          <Image
            key={`${img.timestamp}-${index}-${copyIndex}`}
            source={{ uri: imageUri }}
            style={[
              styles.overlayImage,
              {
                width: IMAGE_WIDTH,
                height: IMAGE_HEIGHT,
                left: worldLeft,
                top: baseY,
                opacity: 0.98,
              },
            ]}
            resizeMode="cover"
            onError={(error) => {
              if (__DEV__) {
                console.error("[PanoramaOverlay] Image load error:", {
                  uri: imageUri,
                  segment: img.segment,
                  error,
                });
              }
            }}
            onLoad={() => {
              if (__DEV__ && ENABLE_PANO_LOG) {
                console.log("[PanoramaOverlay] Image loaded:", {
                  segment: img.segment,
                  uri: imageUri.split("/").pop(),
                });
              }
            }}
          />
        );
      });
    })
    .filter((node): node is JSX.Element => Boolean(node));

  if (__DEV__ && ENABLE_PANO_LOG) {
    const horizontalImages = images.filter(
      (img) =>
        img.captureType === "horizontal" && img.targetHeading !== undefined
    );
    const sample = horizontalImages.slice(-2).map((img) => ({
      seg: img.segment,
      tgt: img.targetHeading,
      baseX: headingToX(img.targetHeading!) - IMAGE_WIDTH / 2,
    }));
    console.log("[PanoramaOverlay] Render check:", {
      totalImages: images.length,
      horizontalImages: horizontalImages.length,
      stitchedElements: stitched.length,
      heading: currentHeading,
      cameraX,
      SCREEN_WIDTH,
      SCREEN_HEIGHT,
      translateX: SCREEN_WIDTH / 2 - cameraX,
      translateY: (SCREEN_HEIGHT - IMAGE_HEIGHT) / 2,
      sample,
    });
  }

  return (
    <View style={styles.container} pointerEvents="none">
      <View
        style={{
          position: "absolute",
          width: WORLD_WIDTH * 3,
          height: IMAGE_HEIGHT,
          left: 0,
          top: 0,
          transform: [
            { translateX: SCREEN_WIDTH / 2 - cameraX },
            { translateY: (SCREEN_HEIGHT - IMAGE_HEIGHT) / 2 },
          ],
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
    zIndex: 5,
    overflow: "hidden",
  },
  overlayImage: {
    position: "absolute",
    borderWidth: __DEV__ && ENABLE_PANO_LOG ? 2 : 0,
    borderColor: "red",
  },
  fadeMask: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: "rgba(0, 0, 0, 0.15)",
    pointerEvents: "none",
  },
});
