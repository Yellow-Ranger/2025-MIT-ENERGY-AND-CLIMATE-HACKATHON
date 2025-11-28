import { useState, useRef } from 'react';
import type { CaptureStep, CapturedImage, CaptureMode } from '../types/photosphere';
import { calculateTargetHeading } from '../utils/photosphere/orientationMath';

export function usePhotosphereCapture() {
  const [step, setStep] = useState<CaptureStep>('initial-alignment');
  const [horizontalSegments, setHorizontalSegments] = useState<Set<number>>(new Set());
  const [ceilingSegments, setCeilingSegments] = useState<Set<number>>(new Set());
  const [floorSegments, setFloorSegments] = useState<Set<number>>(new Set());
  const [images, setImages] = useState<CapturedImage[]>([]);

  const initialHeadingRef = useRef<number | null>(null);

  const startCapture = (currentHeading: number) => {
    initialHeadingRef.current = currentHeading;
    setStep('capturing-horizontal');
    console.log('[usePhotosphereCapture] Starting capture at heading:', currentHeading);
  };

  const captureSegment = (
    segment: number,
    heading: number,
    pitch: number,
    photoUri: string,
    captureType: CaptureMode
  ) => {
    const targetHeading = captureType === 'horizontal'
      ? calculateTargetHeading(segment, initialHeadingRef.current!, 24)
      : undefined;

    const newImage: CapturedImage = {
      blob: photoUri,
      timestamp: Date.now(),
      heading,
      pitch,
      segment,
      captureType,
      targetHeading,
      position: 1,
    };

    setImages(prev => [...prev, newImage]);

    if (captureType === 'horizontal') {
      const newSegments = new Set([...horizontalSegments, segment]);
      setHorizontalSegments(newSegments);

      console.log(`[usePhotosphereCapture] Captured horizontal segment ${segment}/24`);

      // Auto-advance to ceiling prompt when horizontal complete
      if (newSegments.size >= 24) {
        console.log('[usePhotosphereCapture] Horizontal capture complete, showing ceiling prompt');
        setStep('ceiling-prompt');
      }
    } else if (captureType === 'ceiling') {
      const newSegments = new Set([...ceilingSegments, segment]);
      setCeilingSegments(newSegments);

      console.log(`[usePhotosphereCapture] Captured ceiling segment ${segment}/8`);

      // Auto-advance to floor prompt when ceiling complete
      if (newSegments.size >= 8) {
        console.log('[usePhotosphereCapture] Ceiling capture complete, showing floor prompt');
        setStep('floor-prompt');
      }
    } else if (captureType === 'floor') {
      const newSegments = new Set([...floorSegments, segment]);
      setFloorSegments(newSegments);

      console.log(`[usePhotosphereCapture] Captured floor segment ${segment}/8`);

      // Auto-advance to completion when floor complete
      if (newSegments.size >= 8) {
        console.log('[usePhotosphereCapture] Floor capture complete, photosphere finished');
        setStep('completion');
      }
    }
  };

  const skipCeiling = () => {
    console.log('[usePhotosphereCapture] Skipping ceiling capture');
    setStep('floor-prompt');
  };

  const skipFloor = () => {
    console.log('[usePhotosphereCapture] Skipping floor capture');
    setStep('completion');
  };

  const startCeiling = () => {
    console.log('[usePhotosphereCapture] Starting ceiling capture');
    setStep('capturing-ceiling');
  };

  const startFloor = () => {
    console.log('[usePhotosphereCapture] Starting floor capture');
    setStep('capturing-floor');
  };

  const reset = () => {
    console.log('[usePhotosphereCapture] Resetting capture state');
    setStep('initial-alignment');
    setHorizontalSegments(new Set());
    setCeilingSegments(new Set());
    setFloorSegments(new Set());
    setImages([]);
    initialHeadingRef.current = null;
  };

  return {
    step,
    horizontalSegments,
    ceilingSegments,
    floorSegments,
    images,
    initialHeading: initialHeadingRef.current,
    startCapture,
    captureSegment,
    skipCeiling,
    skipFloor,
    startCeiling,
    startFloor,
    reset,
  };
}
