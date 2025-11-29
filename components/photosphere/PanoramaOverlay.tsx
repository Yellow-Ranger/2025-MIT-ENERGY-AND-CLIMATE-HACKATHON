import React from 'react';
import { View, Image, StyleSheet, Dimensions } from 'react-native';
import type { CapturedImage } from '../../types/photosphere';

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

export function PanoramaOverlay({ images, currentHeading }: PanoramaOverlayProps) {
  if (currentHeading === null || images.length === 0) return null;

  const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('window');
  const FRAME_WIDTH = 280;
  const FRAME_HEIGHT = 420;
  const IMAGE_WIDTH = FRAME_WIDTH;
  const IMAGE_HEIGHT = FRAME_HEIGHT;

  // World sizing
  const WORLD_WIDTH = IMAGE_WIDTH * 24;   // 24 segments across 360°
  const WORLD_HEIGHT = IMAGE_HEIGHT * 6;  // vertical band to allow pitch offsets

  const wrap = (value: number, mod: number) => {
    const wrapped = value % mod;
    return wrapped < 0 ? wrapped + mod : wrapped;
  };

  const headingToX = (heading: number) => (heading / 360) * WORLD_WIDTH;
  const pitchToY = (pitch: number | null) => {
    const clamped = Math.max(-90, Math.min(90, pitch ?? 0));
    return ((clamped + 90) / 180) * WORLD_HEIGHT;
  };

  const cameraX = wrap(headingToX(currentHeading), WORLD_WIDTH);
  const cameraY = WORLD_HEIGHT / 2; // center view vertically (pitch per tile only)

  const stitched = images
    .filter(img => img.captureType === 'horizontal' && img.targetHeading !== undefined)
    .flatMap((img, index) => {
      const baseX = headingToX(img.targetHeading!) - IMAGE_WIDTH / 2;
      const baseY = pitchToY(img.pitch ?? 0) - IMAGE_HEIGHT / 2;

      return [-WORLD_WIDTH, 0, WORLD_WIDTH].map((wrapOffset, copyIndex) => {
        const worldLeft = baseX + wrapOffset;

        return (
          <Image
            key={`${img.timestamp}-${index}-${copyIndex}`}
            source={{ uri: img.blob }}
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
          />
        );
      });
    })
    .filter((node): node is JSX.Element => Boolean(node));

  if (__DEV__ && ENABLE_PANO_LOG) {
    const sample = images
      .filter(img => img.captureType === 'horizontal' && img.targetHeading !== undefined)
      .slice(-2)
      .map(img => ({ seg: img.segment, tgt: img.targetHeading, uri: img.blob.split('/').pop() }));
    // console.log('[PanoramaOverlay]', { count: images.length, stitched: stitched.length, heading: currentHeading, sample });
  }

  return (
    <View style={styles.container} pointerEvents="none">
      <View
        style={{
          position: 'absolute',
          width: WORLD_WIDTH * 3,
          height: WORLD_HEIGHT,
          transform: [
            { translateX: SCREEN_WIDTH / 2 - cameraX },
            { translateY: SCREEN_HEIGHT / 2 - cameraY },
          ],
        }}
      >
        {stitched}
      </View>
      <View style={styles.fadeMask} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    zIndex: 1,
    overflow: 'hidden',
  },
  overlayImage: {
    position: 'absolute',
  },
  fadeMask: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(0, 0, 0, 0.15)',
  },
});
