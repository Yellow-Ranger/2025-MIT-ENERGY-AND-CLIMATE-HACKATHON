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

function smooth(prev: number | null, next: number | null, alpha: number): number | null {
  if (next === null) return prev;
  if (prev === null) return next;
  return prev + alpha * (next - prev);
}

// Handle circular smoothing for heading to avoid wrap jitter near 0/360
function smoothAngle(prev: number | null, next: number | null, alpha: number): number | null {
  if (next === null) return prev;
  if (prev === null) return next;
  const diff = ((next - prev + 540) % 360) - 180; // -180..180
  const blended = prev + alpha * diff;
  let normalized = blended % 360;
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
  const smoothHeadingRef = useRef<number | null>(null);
  const smoothPitchRef = useRef<number | null>(null);
  const smoothRollRef = useRef<number | null>(null);

  // Smoothing factors (lower = smoother but slower)
  const HEADING_ALPHA = 0.08;
  const PITCH_ALPHA = 0.12;
  const ROLL_ALPHA = 0.12;

  useEffect(() => {
    let mounted = true;

    const startListening = async () => {
      try {
        // Set update interval to ~60 Hz (16.67ms)
        DeviceMotion.setUpdateInterval(30); // ~33 Hz for smoother motion without noise

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
            const rawHeading = normalizeHeading(toDegrees(data.rotation.alpha));
            const rawBeta = toDegrees(data.rotation.beta);
            // Map device beta to photosphere pitch:
            // beta ~90 when device is vertical, <90 when tilted upward, >90 when tilted downward
            // We want pitch +90 (ceiling) -> 0 (horizon) -> -90 (floor)
            const photospherePitch = rawBeta !== null ? 90 - rawBeta : null;
            const rawRoll = toDegrees(data.rotation.gamma);

            smoothHeadingRef.current = smoothAngle(smoothHeadingRef.current, rawHeading, HEADING_ALPHA);
            smoothPitchRef.current = smooth(smoothPitchRef.current, photospherePitch, PITCH_ALPHA);
            smoothRollRef.current = smooth(smoothRollRef.current, rawRoll, ROLL_ALPHA);

              setOrientation({
                heading: smoothHeadingRef.current,
                pitch: smoothPitchRef.current,
                roll: smoothRollRef.current,
                updateRate: rate,
              });
            }
          } else {
            // Normal update (no rate calculation)
            if (data.rotation) {
              // Convert beta to photosphere pitch:
              // DeviceMotion beta: 0° (flat) -> 90° (upright portrait) -> 180° (flat upside down)
              // Photosphere pitch: -90° (floor) -> 0° (straight ahead) -> +90° (ceiling)
              const rawBeta = toDegrees(data.rotation.beta);
              const photospherePitch = rawBeta !== null ? 90 - rawBeta : null;

              const smoothedPitch = smooth(
                smoothPitchRef.current,
                photospherePitch,
                PITCH_ALPHA
              );

              setOrientation(prev => ({
                heading: smoothAngle(
                  smoothHeadingRef.current,
                  normalizeHeading(toDegrees(data.rotation.alpha)),
                  HEADING_ALPHA
                ),
                pitch: smoothedPitch,
                roll: smooth(
                  smoothRollRef.current,
                  toDegrees(data.rotation.gamma),
                  ROLL_ALPHA
                ),
                updateRate: prev.updateRate,
              }));

              // Persist smoothed refs to keep continuity
              smoothHeadingRef.current = smoothHeadingRef.current;
              smoothPitchRef.current = smoothedPitch;
              smoothRollRef.current = smoothRollRef.current;
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
