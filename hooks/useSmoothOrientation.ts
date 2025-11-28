import { useState, useEffect, useRef } from 'react';
import { DeviceMotion } from 'expo-sensors';
import type { DeviceMotionMeasurement } from 'expo-sensors';

export interface SmoothOrientationData {
  heading: number | null;  // 0-360° (yaw/azimuth)
  pitch: number | null;    // -90 to 90° (elevation)
  roll: number | null;     // -180 to 180°
  updateRate: number;      // Actual Hz
}

const RAD_TO_DEG = 180 / Math.PI;

function toDegrees(value: number | null | undefined): number | null {
  if (value === null || value === undefined || Number.isNaN(value)) return null;
  return value * RAD_TO_DEG;
}

function normalizeHeading(alpha: number | null): number | null {
  if (alpha === null || Number.isNaN(alpha)) return null;
  let normalized = alpha % 360;
  if (normalized < 0) normalized += 360;
  return normalized;
}

export function useSmoothOrientation() {
  const [orientation, setOrientation] = useState<SmoothOrientationData>({
    heading: null,
    pitch: null,
    roll: null,
    updateRate: 0,
  });

  const subscriptionRef = useRef<any>(null);
  const frameCountRef = useRef(0);
  const lastRateCheckRef = useRef(Date.now());

  useEffect(() => {
    let mounted = true;

    const startListening = async () => {
      try {
        // Set update interval to ~60 Hz (16.67ms)
        DeviceMotion.setUpdateInterval(16);

        subscriptionRef.current = DeviceMotion.addListener((data: DeviceMotionMeasurement) => {
          if (!mounted) return;

          // Calculate actual update rate
          frameCountRef.current++;
          const now = Date.now();

          if (now - lastRateCheckRef.current >= 1000) {
            const rate = frameCountRef.current;
            frameCountRef.current = 0;
            lastRateCheckRef.current = now;

            // Update with rate info
            if (data.rotation) {
              setOrientation({
                heading: normalizeHeading(toDegrees(data.rotation.alpha)),
                pitch: toDegrees(data.rotation.beta),
                roll: toDegrees(data.rotation.gamma),
                updateRate: rate,
              });
            }
          } else {
            // Normal update (no rate calculation)
            if (data.rotation) {
              setOrientation(prev => ({
                heading: normalizeHeading(toDegrees(data.rotation.alpha)),
                pitch: toDegrees(data.rotation.beta),
                roll: toDegrees(data.rotation.gamma),
                updateRate: prev.updateRate,
              }));
            }
          }
        });

        console.log('[useSmoothOrientation] DeviceMotion listener started');
      } catch (err) {
        console.error('[useSmoothOrientation] Failed to start:', err);
      }
    };

    startListening();

    return () => {
      mounted = false;
      if (subscriptionRef.current) {
        subscriptionRef.current.remove();
        subscriptionRef.current = null;
        console.log('[useSmoothOrientation] DeviceMotion listener stopped');
      }
    };
  }, []);

  return orientation;
}
