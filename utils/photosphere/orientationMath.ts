/**
 * Calculate the smallest angle difference between two headings
 * Handles the 0°/360° boundary correctly
 */
export function smallestAngleDiff(a: number, b: number): number {
  let diff = Math.abs(a - b);
  if (diff > 180) diff = 360 - diff;
  return diff;
}

/**
 * Normalize heading to 0-360 range
 */
export function normalizeHeading(heading: number | null): number | null {
  if (heading === null || Number.isNaN(heading)) return null;
  let normalized = heading % 360;
  if (normalized < 0) normalized += 360;
  return normalized;
}

/**
 * Calculate the target heading for a given segment
 * @param segment - Segment number (0-23 for horizontal, 0-7 for vertical)
 * @param initialHeading - The heading when capture started
 * @param totalSegments - Total number of segments (24 for horizontal, 8 for vertical)
 */
export function calculateTargetHeading(
  segment: number,
  initialHeading: number,
  totalSegments: number = 24
): number {
  const segmentAngle = (segment * 360) / totalSegments;
  return (initialHeading + segmentAngle) % 360;
}

/**
 * Find the first missing segment in the captured set
 * @param capturedSegments - Set of already captured segment numbers
 * @param totalSegments - Total number of segments
 * @returns First missing segment number, or null if all captured
 */
export function firstMissingSegment(
  capturedSegments: Set<number>,
  totalSegments: number
): number | null {
  for (let i = 0; i < totalSegments; i++) {
    if (!capturedSegments.has(i)) return i;
  }
  return null;
}

/**
 * Check if pitch is in target range for ceiling capture
 */
export function isInCeilingRange(pitch: number | null): boolean {
  if (pitch === null) return false;
  return pitch >= 45 && pitch <= 60;
}

/**
 * Check if pitch is in target range for floor capture
 */
export function isInFloorRange(pitch: number | null): boolean {
  if (pitch === null) return false;
  return pitch >= -60 && pitch <= -45;
}
