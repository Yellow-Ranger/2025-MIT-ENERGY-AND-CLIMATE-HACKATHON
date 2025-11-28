import React from 'react';
import { View, Image, StyleSheet, Dimensions } from 'react-native';
import type { CapturedImage } from '../../types/photosphere';
import { smallestAngleDiff } from '../../utils/photosphere/orientationMath';

interface PanoramaOverlayProps {
  images: CapturedImage[];
  currentHeading: number | null;
}

/**
 * PanoramaOverlay component
 * Displays captured images overlaid on the camera view, positioned based on heading
 * Creates a progressive panorama reveal as user captures segments
 */
export function PanoramaOverlay({ images, currentHeading }: PanoramaOverlayProps) {
  if (currentHeading === null || images.length === 0) return null;

  // Frame dimensions (matching CaptureFrame.tsx)
  const FRAME_WIDTH = 280;
  const FRAME_HEIGHT = 420;

  return (
    <View style={styles.container} pointerEvents="none">
      {/* Constrain to capture frame area */}
      <View style={[styles.frameContainer, { width: FRAME_WIDTH, height: FRAME_HEIGHT }]}>
        {images
          .filter(img => img.captureType === 'horizontal')
          .map((img, index) => {
            if (!img.targetHeading) return null;

            const angleDiff = smallestAngleDiff(img.targetHeading, currentHeading);

            // Only render images within ±45° of current view (tighter for frame)
            if (Math.abs(angleDiff) > 45) return null;

            // Calculate horizontal offset based on angle difference
            // Map ±45° to ±FRAME_WIDTH for smooth panning
            const offsetX = (angleDiff / 45) * FRAME_WIDTH;

            // Fade opacity at edges for seamless blending
            const opacity = Math.max(0.3, 1 - Math.abs(angleDiff) / 45);

            return (
              <Image
                key={index}
                source={{ uri: img.blob }}
                style={[
                  styles.overlayImage,
                  {
                    width: FRAME_WIDTH,
                    height: FRAME_HEIGHT,
                    opacity,
                    transform: [{ translateX: offsetX }],
                  },
                ]}
                resizeMode="cover"
              />
            );
          })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    zIndex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  frameContainer: {
    position: 'relative',
    overflow: 'hidden',
    borderRadius: 12,
  },
  overlayImage: {
    position: 'absolute',
  },
});
