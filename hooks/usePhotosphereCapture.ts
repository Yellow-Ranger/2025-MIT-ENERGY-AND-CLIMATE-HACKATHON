import { useState, useRef } from 'react';
import type { CaptureStep, CapturedImage, CaptureMode } from '../types/photosphere';

// Free-form spherical photosphere capture
// No segments - images can be captured in any order and orientation
export function usePhotosphereCapture() {
  const [step, setStep] = useState<CaptureStep>('initial-alignment');
  const [images, setImages] = useState<CapturedImage[]>([]);
  const captureCountRef = useRef(0);
  const initialHeadingRef = useRef<number | null>(null);

  // Minimum images for rough coverage (can be adjusted)
  const MIN_IMAGES = 40;

  const startCapture = (currentHeading: number) => {
    initialHeadingRef.current = currentHeading;
    setStep('capturing-horizontal'); // Keep same step name for compatibility
    console.log('[usePhotosphereCapture] Starting free-form spherical capture');
  };

  const captureImage = (
    heading: number,
    pitch: number,
    photoUri: string,
    captureType: CaptureMode = 'horizontal'
  ) => {
    captureCountRef.current += 1;
    const count = captureCountRef.current;

    const newImage: CapturedImage = {
      blob: photoUri,
      timestamp: Date.now(),
      heading,
      pitch,
      segment: count, // Use count as unique ID instead of segment number
      captureType,
      targetHeading: undefined, // No target in free-form mode
      position: 1,
    };

    console.log('[usePhotosphereCapture] Captured image:', {
      count,
      heading: heading.toFixed(1),
      pitch: pitch.toFixed(1),
      captureType,
      uri: photoUri,
    });

    setImages(prev => [...prev, newImage]);

    // Auto-complete when minimum images reached
    if (count >= MIN_IMAGES) {
      console.log('[usePhotosphereCapture] Minimum images reached, completing capture');
      setStep('completion');
    }
  };

  // Legacy method for compatibility with existing code
  const captureSegment = (
    segment: number,
    heading: number,
    pitch: number,
    photoUri: string,
    captureType: CaptureMode
  ) => {
    // Just forward to captureImage, ignoring segment parameter
    captureImage(heading, pitch, photoUri, captureType);
  };

  const completeCapture = () => {
    console.log('[usePhotosphereCapture] Manual completion with', images.length, 'images');
    setStep('completion');
  };

  const skipCeiling = () => {
    // In free-form mode, just complete
    console.log('[usePhotosphereCapture] Completing capture (ceiling skipped)');
    setStep('completion');
  };

  const skipFloor = () => {
    console.log('[usePhotosphereCapture] Completing capture (floor skipped)');
    setStep('completion');
  };

  const startCeiling = () => {
    console.log('[usePhotosphereCapture] Continuing free-form capture (ceiling mode)');
    setStep('capturing-ceiling');
  };

  const startFloor = () => {
    console.log('[usePhotosphereCapture] Continuing free-form capture (floor mode)');
    setStep('capturing-floor');
  };

  const reset = () => {
    console.log('[usePhotosphereCapture] Resetting capture state');
    setStep('initial-alignment');
    setImages([]);
    captureCountRef.current = 0;
    initialHeadingRef.current = null;
  };

  return {
    step,
    images,
    imageCount: captureCountRef.current,
    initialHeading: initialHeadingRef.current,
    startCapture,
    captureImage,
    captureSegment, // Keep for backward compatibility
    completeCapture,
    skipCeiling,
    skipFloor,
    startCeiling,
    startFloor,
    reset,
    // Legacy exports for compatibility (empty sets)
    horizontalSegments: new Set<number>(),
    ceilingSegments: new Set<number>(),
    floorSegments: new Set<number>(),
  };
}
