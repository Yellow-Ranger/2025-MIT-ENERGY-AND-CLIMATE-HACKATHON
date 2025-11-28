import type { DotPosition } from '../../types/photosphere';
import { smallestAngleDiff } from './orientationMath';

const FRAME_RADIUS = 120;          // pixels - distance to edge of frame
const ALIGN_THRESHOLD = 12;        // degrees - turn blue when aligned
const APPROACH_THRESHOLD = 40;     // degrees - start moving when within this angle

/**
 * Calculate dot position for horizontal capture
 * Dot moves from edge of frame toward center as user aligns with target heading
 */
export function calculateDotPosition(
  currentHeading: number,
  targetHeading: number
): DotPosition {
  const angleDiff = smallestAngleDiff(currentHeading, targetHeading);

  // Calculate distance from center
  let distance: number;
  if (angleDiff > APPROACH_THRESHOLD) {
    distance = FRAME_RADIUS;  // Stay at edge
  } else {
    // Linear interpolation: edge → center
    distance = FRAME_RADIUS * (angleDiff / APPROACH_THRESHOLD);
  }

  // Convert polar coordinates to Cartesian (x, y)
  const angleRad = ((targetHeading - currentHeading) * Math.PI) / 180;
  const x = Math.sin(angleRad) * distance;
  const y = -Math.cos(angleRad) * distance;

  // Scale: 1.0 at edge → 1.5 at center (dot expands as it approaches center)
  const scaleProgress = 1 - (distance / FRAME_RADIUS);
  const scale = 1.0 + (0.5 * scaleProgress);

  // Color: white when far, blue when aligned
  const color = angleDiff < ALIGN_THRESHOLD ? 'blue' : 'white';

  // Opacity: slightly more visible at center
  const opacity = 0.9 + (0.1 * scaleProgress);

  return { x, y, distance, scale, color, opacity };
}

/**
 * Calculate dot position for vertical capture (ceiling/floor)
 * Dot moves vertically up (ceiling) or down (floor) as user tilts to target pitch
 */
export function calculateVerticalDotPosition(
  currentPitch: number,
  targetPitch: number,
  direction: 'up' | 'down'
): DotPosition {
  const pitchDiff = Math.abs(currentPitch - targetPitch);
  const maxDistance = 80;
  const alignmentThreshold = 20;

  let distance: number;
  if (pitchDiff > alignmentThreshold) {
    distance = maxDistance;
  } else {
    distance = maxDistance * (pitchDiff / alignmentThreshold);
  }

  const aligned = pitchDiff < 8;
  const scale = 1.0 + (0.3 * (1 - distance / maxDistance));
  const color = aligned ? 'blue' : 'white';

  return {
    x: 0,
    y: direction === 'up' ? -distance : distance,
    distance,
    scale,
    color,
    opacity: 0.95,
  };
}
