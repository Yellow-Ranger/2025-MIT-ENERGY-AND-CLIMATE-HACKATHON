import type { DotPosition } from '../../types/photosphere';
import { smallestAngleDiff, calculateTargetHeading } from './orientationMath';

const FRAME_RADIUS = 120;          // pixels - distance to edge of frame
const ALIGN_THRESHOLD = 12;        // degrees - turn blue when aligned
const APPROACH_THRESHOLD = 40;     // degrees - start moving when within this angle
const MARKER_RADIUS = 160;         // pixels - distance from center for progression markers
const ALIGN_THRESHOLD_CARDINAL = 10;

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
 * Build four cardinal markers (top/bottom/left/right). Only one direction
 * is emphasized based on which way the user needs to rotate toward the target.
 */
export function buildCardinalMarkers(
  currentHeading: number,
  targetHeading: number | null
) {
  // Signed angle diff (-180..180): positive means rotate right/clockwise
  const rawDiff = targetHeading === null
    ? 0
    : (((targetHeading - currentHeading + 540) % 360) - 180);

  const activeDirection = targetHeading === null
    ? null
    : rawDiff > 0 ? 'right' : 'left';

  const closeness = targetHeading === null
    ? 0
    : Math.max(0, 1 - Math.abs(rawDiff) / 120); // grows as you align

  const makeMarker = (key: 'top' | 'bottom' | 'left' | 'right') => {
    const base = { x: 0, y: 0 };
    if (key === 'top') base.y = -MARKER_RADIUS;
    if (key === 'bottom') base.y = MARKER_RADIUS;
    if (key === 'left') base.x = -MARKER_RADIUS;
    if (key === 'right') base.x = MARKER_RADIUS;

    const isActive = activeDirection === key;
    const aligned = targetHeading !== null && Math.abs(rawDiff) < ALIGN_THRESHOLD_CARDINAL && isActive;

    const scale = isActive ? 1 + closeness * 0.6 : 0.9;
    const opacity = isActive ? 0.35 + closeness * 0.55 : 0.3;
    const color = aligned ? 'blue' : 'white';

    return { ...base, scale, opacity, color, key };
  };

  return [
    makeMarker('top'),
    makeMarker('bottom'),
    makeMarker('left'),
    makeMarker('right'),
  ];
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
