import React from 'react';
import { View, StyleSheet } from 'react-native';

/**
 * CaptureFrame component
 * Displays the rectangular frame and central hollow circle for alignment
 */
export function CaptureFrame() {
  return (
    <View style={styles.container} pointerEvents="none">
      {/* Rectangular camera frame */}
      <View style={styles.frame} />

      {/* Central hollow circle (alignment target) */}
      <View style={styles.centerCircle}>
        <View style={styles.centerCircleInner} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    width: '100%',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },
  frame: {
    width: 280,
    height: 420,
    borderWidth: 2,
    borderColor: 'rgba(255, 255, 255, 0.5)',
    borderRadius: 12,
  },
  centerCircle: {
    position: 'absolute',
    width: 64,
    height: 64,
    borderRadius: 32,
    borderWidth: 3,
    borderColor: '#FFFFFF',
    backgroundColor: 'transparent',
    alignItems: 'center',
    justifyContent: 'center',
  },
  centerCircleInner: {
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: '#FFFFFF',
    opacity: 0.6,
  },
});
