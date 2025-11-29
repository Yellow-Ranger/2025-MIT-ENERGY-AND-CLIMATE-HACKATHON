import React, { useEffect, useRef } from 'react';
import { Animated, StyleSheet } from 'react-native';
import type { DotPosition } from '../../types/photosphere';

interface ActiveDotProps {
  position: DotPosition;
}

export function ActiveDot({ position }: ActiveDotProps) {
  // Animated values for transforms (native driver)
  const translateXAnim = useRef(new Animated.Value(0)).current;
  const translateYAnim = useRef(new Animated.Value(0)).current;
  const scaleAnim = useRef(new Animated.Value(1)).current;

  // Animate position changes with native driver
  useEffect(() => {
    Animated.parallel([
      Animated.spring(translateXAnim, {
        toValue: position.x,
        useNativeDriver: true,
        tension: 40,
        friction: 7,
      }),
      Animated.spring(translateYAnim, {
        toValue: position.y,
        useNativeDriver: true,
        tension: 40,
        friction: 7,
      }),
      Animated.spring(scaleAnim, {
        toValue: position.scale,
        useNativeDriver: true,
        tension: 40,
        friction: 7,
      }),
    ]).start();
  }, [position.x, position.y, position.scale]);

  // Use direct color instead of animation (simpler and avoids mixing drivers)
  const backgroundColor = position.color === 'blue' ? '#4285F4' : '#FFFFFF';

  return (
    <Animated.View
      style={[
        styles.dot,
        {
          transform: [
            { translateX: translateXAnim },
            { translateY: translateYAnim },
            { scale: scaleAnim },
          ],
          backgroundColor,
          opacity: position.opacity,
        },
      ]}
    />
  );
}

const styles = StyleSheet.create({
  dot: {
    position: 'absolute',
    top: '50%',
    left: '50%',
    marginTop: -24,  // half of height to center the dot
    marginLeft: -24, // half of width to center the dot
    width: 48,
    height: 48,
    borderRadius: 24,
    borderWidth: 3,
    borderColor: '#FFFFFF',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
    elevation: 5,
    zIndex: 7,
  },
});
