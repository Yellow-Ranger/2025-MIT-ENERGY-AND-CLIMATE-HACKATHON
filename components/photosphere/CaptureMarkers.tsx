import React, { useMemo } from 'react';
import { View, StyleSheet } from 'react-native';
import { buildCardinalMarkers } from '../../utils/photosphere/dotPositioning';

interface CaptureMarkersProps {
  currentHeading: number | null;
  targetHeading: number | null;
}

/**
 * CaptureMarkers renders four cardinal dots; the needed direction grows/brightens
 * as the user sweeps toward the next heading.
 */
export function CaptureMarkers({ currentHeading, targetHeading }: CaptureMarkersProps) {
  const markers = useMemo(() => {
    if (currentHeading === null) return [];
    return buildCardinalMarkers(currentHeading, targetHeading ?? null);
  }, [currentHeading, targetHeading]);

  if (markers.length === 0) return null;

  return (
    <View style={styles.container} pointerEvents="none">
      {markers.map(marker => {
        const opacity = marker.opacity;
        const scale = marker.scale;

        return (
          <View
            key={marker.key}
            style={[
              styles.marker,
              {
                transform: [
                  { translateX: marker.x },
                  { translateY: marker.y },
                  { scale },
                ],
                opacity,
                backgroundColor: marker.color === 'blue' ? '#4285F4' : '#F1F5F9',
              },
            ]}
          />
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 3,
  },
  marker: {
    position: 'absolute',
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: '#F1F5F9',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.2,
    shadowRadius: 2,
    elevation: 2,
  },
});
