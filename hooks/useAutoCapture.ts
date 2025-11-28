import { useState, useEffect, useRef } from 'react';
import { smallestAngleDiff } from '../utils/photosphere/orientationMath';

interface AlignmentState {
  isAligned: boolean;
  isStable: boolean;
  alignmentProgress: number; // 0-1
  angleDiff: number;
}

const ALIGN_THRESHOLD = 12;        // degrees - trigger auto-capture
const APPROACH_THRESHOLD = 40;     // degrees - start moving dot
const STABILITY_DURATION = 300;    // ms - must hold alignment
const CAPTURE_THROTTLE = 500;      // ms - minimum time between captures

export function useAutoCapture(
  currentHeading: number | null,
  targetHeading: number,
  onCapture: () => void
) {
  const [alignment, setAlignment] = useState<AlignmentState>({
    isAligned: false,
    isStable: false,
    alignmentProgress: 0,
    angleDiff: 180,
  });

  const stableStartTimeRef = useRef<number>(0);
  const lastCaptureTimeRef = useRef<number>(0);
  const onCaptureRef = useRef(onCapture);

  // Keep callback ref up to date
  useEffect(() => {
    onCaptureRef.current = onCapture;
  }, [onCapture]);

  useEffect(() => {
    if (currentHeading === null) return;

    const now = Date.now();
    const angleDiff = smallestAngleDiff(currentHeading, targetHeading);
    const isAligned = angleDiff < ALIGN_THRESHOLD;
    const progress = Math.max(0, 1 - (angleDiff / APPROACH_THRESHOLD));

    let isStable = false;

    if (isAligned) {
      if (stableStartTimeRef.current === 0) {
        stableStartTimeRef.current = now;
      }

      const stableDuration = now - stableStartTimeRef.current;
      isStable = stableDuration >= STABILITY_DURATION;

      // Trigger capture if stable and throttle respected
      if (isStable && (now - lastCaptureTimeRef.current) > CAPTURE_THROTTLE) {
        lastCaptureTimeRef.current = now;
        console.log('[useAutoCapture] Triggering capture - aligned and stable');
        onCaptureRef.current();
      }
    } else {
      stableStartTimeRef.current = 0;
    }

    setAlignment({ isAligned, isStable, alignmentProgress: progress, angleDiff });
  }, [currentHeading, targetHeading]);

  return alignment;
}
