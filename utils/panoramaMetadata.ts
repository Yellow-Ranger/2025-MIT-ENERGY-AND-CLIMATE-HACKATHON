import * as FileSystem from "expo-file-system/legacy";
import { CapturedImage } from "./captureStore";

export interface PanoramaMetadata {
  version: string;
  captureDate: string;
  totalImages: number;
  positions: number;
  horizontalSegments: number;
  verticalSegments: number;
  images: PanoramaImageMetadata[];
}

export interface PanoramaImageMetadata {
  filePath: string;
  timestamp: number;
  position: 1 | 2;
  segment: number;
  captureType: 'horizontal' | 'ceiling' | 'floor';
  // Camera orientation at capture
  heading: number | null;
  pitch: number | null;
  // Target orientation for this segment
  targetHeading?: number;
  targetPitch?: number;
  // Calculated spherical coordinates for stitching
  spherical: {
    azimuth: number; // 0-360 degrees
    elevation: number; // -90 to 90 degrees
  };
}

/**
 * Convert captured images to panorama metadata format
 */
export function createPanoramaMetadata(
  images: CapturedImage[]
): PanoramaMetadata {
  const imageMetadata: PanoramaImageMetadata[] = images.map((img) => {
    // Calculate spherical coordinates for stitching
    const azimuth = img.targetHeading ?? img.heading ?? 0;
    let elevation: number;

    if (img.captureType === 'ceiling') {
      elevation = img.targetPitch ?? 52.5;
    } else if (img.captureType === 'floor') {
      elevation = img.targetPitch ?? -52.5;
    } else {
      elevation = img.pitch ?? 0;
    }

    return {
      filePath: img.blob as string,
      timestamp: img.timestamp,
      position: img.position,
      segment: img.segment,
      captureType: img.captureType,
      heading: img.heading,
      pitch: img.pitch,
      targetHeading: img.targetHeading,
      targetPitch: img.targetPitch,
      spherical: {
        azimuth,
        elevation,
      },
    };
  });

  // Sort by position and segment for consistent ordering
  imageMetadata.sort((a, b) => {
    if (a.position !== b.position) {
      return a.position - b.position;
    }
    return a.segment - b.segment;
  });

  return {
    version: "1.0.0",
    captureDate: new Date().toISOString(),
    totalImages: images.length,
    positions: Math.max(...images.map(img => img.position)),
    horizontalSegments: 24,
    verticalSegments: 8,
    images: imageMetadata,
  };
}

/**
 * Save panorama metadata to a JSON file
 */
export async function savePanoramaMetadata(
  scanId: string,
  metadata: PanoramaMetadata
): Promise<string> {
  const directory = `${FileSystem.documentDirectory}scans/`;

  // Ensure directory exists
  const dirInfo = await FileSystem.getInfoAsync(directory);
  if (!dirInfo.exists) {
    await FileSystem.makeDirectoryAsync(directory, { intermediates: true });
  }

  const metadataPath = `${directory}${scanId}_metadata.json`;
  await FileSystem.writeAsStringAsync(
    metadataPath,
    JSON.stringify(metadata, null, 2)
  );

  console.log(`Saved panorama metadata to: ${metadataPath}`);
  return metadataPath;
}

/**
 * Load panorama metadata from a JSON file
 */
export async function loadPanoramaMetadata(
  metadataPath: string
): Promise<PanoramaMetadata | null> {
  try {
    const content = await FileSystem.readAsStringAsync(metadataPath);
    return JSON.parse(content) as PanoramaMetadata;
  } catch (error) {
    console.error("Failed to load panorama metadata:", error);
    return null;
  }
}

/**
 * Generate stitching instructions for external panorama software
 * This creates a format compatible with tools like Hugin or PTGui
 */
export function generateStitchingScript(
  metadata: PanoramaMetadata
): string {
  let script = `# Panorama Stitching Script\n`;
  script += `# Generated: ${metadata.captureDate}\n`;
  script += `# Total Images: ${metadata.totalImages}\n\n`;

  script += `# Image order and spherical coordinates:\n`;
  metadata.images.forEach((img, index) => {
    script += `# Image ${index}: ${img.filePath}\n`;
    script += `#   Position: ${img.position}, Segment: ${img.segment}, Type: ${img.captureType}\n`;
    script += `#   Azimuth: ${img.spherical.azimuth.toFixed(2)}°, Elevation: ${img.spherical.elevation.toFixed(2)}°\n`;
  });

  script += `\n# For stitching with Hugin or similar tools:\n`;
  script += `# 1. Import all images in order\n`;
  script += `# 2. Set projection type to Equirectangular\n`;
  script += `# 3. Use the azimuth/elevation values for initial positioning\n`;
  script += `# 4. Run optimization and blending\n`;

  return script;
}
