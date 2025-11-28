# Google Photosphere Flow Updates

## Summary
Updated the capture logic to match Google Photosphere's user experience, where the guide dot smoothly moves toward the center circle when the user aligns their camera, automatically captures when aligned, and includes metadata for 360° panorama stitching.

## Key Changes

### 1. Dynamic Dot Movement (app/native-scan.tsx:1009-1118)
- **Before**: Dot was stationary at a fixed radius from the center circle
- **After**: Dot smoothly moves from outside the circle toward the center as the user aligns
- Movement starts when within 25° of target heading
- Dot reaches center (radius = 0) when perfectly aligned
- Works for all capture modes: horizontal rotation, ceiling, and floor

### 2. Tighter Alignment Tolerance
- Changed from 10° to 8° tolerance for more precise captures
- Matches Google Photosphere's accuracy requirements
- Ensures better image overlap for stitching

### 3. Visual Feedback
- Dot turns green (`psDotAligned` style) when within capture range
- Provides clear visual confirmation to the user
- Helps users understand when capture will occur

### 4. Panorama Metadata System

#### New File: utils/panoramaMetadata.ts
Created comprehensive metadata tracking for 360° panorama stitching:

**PanoramaMetadata Interface:**
```typescript
{
  version: string;
  captureDate: string;
  totalImages: number;
  positions: number;
  horizontalSegments: 24;
  verticalSegments: 8;
  images: PanoramaImageMetadata[];
}
```

**PanoramaImageMetadata Interface:**
```typescript
{
  filePath: string;
  timestamp: number;
  position: 1 | 2;
  segment: number;
  captureType: 'horizontal' | 'ceiling' | 'floor';
  heading: number | null;
  pitch: number | null;
  targetHeading?: number;
  targetPitch?: number;
  spherical: {
    azimuth: number;  // 0-360 degrees
    elevation: number; // -90 to 90 degrees
  };
}
```

**Key Functions:**
- `createPanoramaMetadata()`: Converts captured images to metadata format
- `savePanoramaMetadata()`: Saves metadata as JSON file
- `generateStitchingScript()`: Creates instructions for external panorama software (Hugin, PTGui)

### 5. Enhanced CapturedImage Interface (utils/captureStore.ts)
Extended with stitching metadata:
```typescript
{
  blob: any;
  timestamp: number;
  heading: number | null;
  pitch: number | null;
  position: 1 | 2;
  segment: number;              // NEW
  captureType: string;          // NEW
  targetHeading?: number;       // NEW
  targetPitch?: number;         // NEW
}
```

### 6. Updated Capture Flow

**Horizontal Rotation:**
- 24 segments around 360°
- Dot moves toward center when within 25° of target
- Auto-captures when within 8° alignment
- Stores target heading for each segment

**Ceiling Capture:**
- 8 segments at ~52.5° pitch
- Vertical dot movement based on pitch alignment
- Target pitch: middle of ceiling range (45-60°)
- Requires pitch within 5° of target

**Floor Capture:**
- 8 segments at ~-52.5° pitch
- Vertical dot movement based on pitch alignment
- Target pitch: middle of floor range (-60 to -45°)
- Requires pitch within 5° of target

### 7. Scan Completion Enhancements
When a scan completes, the system now:
1. Creates panorama metadata from all captured images
2. Saves metadata as `{scanId}_metadata.json`
3. Generates stitching instructions as `{scanId}_stitching.txt`
4. Includes metadata path in scan storage
5. Updates success message to mention "360° panorama data"

## Files Modified

1. **app/native-scan.tsx**
   - Updated `renderDot()` function with dynamic movement logic
   - Enhanced `attemptCapture()` with captureType parameter
   - Updated `captureFrame()` to store stitching metadata
   - Modified `handleComplete()` to save panorama metadata
   - Added imports for panorama utilities

2. **utils/captureStore.ts**
   - Extended `CapturedImage` interface with metadata fields
   - Added segment, captureType, targetHeading, targetPitch

3. **utils/panoramaMetadata.ts** (NEW)
   - Complete panorama metadata system
   - JSON export/import functionality
   - Stitching script generation

## Benefits

### User Experience
- ✅ Intuitive visual guidance matching Google Photosphere
- ✅ Clear feedback when camera is aligned
- ✅ Automatic capture when aligned (no manual tapping needed)
- ✅ Smoother, more polished capture flow

### Technical
- ✅ Precise capture alignment (8° tolerance)
- ✅ Complete metadata for panorama stitching
- ✅ Compatible with external stitching tools (Hugin, PTGui)
- ✅ Spherical coordinates calculated for each image
- ✅ Organized by position and segment for consistent ordering

### Future Integration
The metadata format enables:
- Server-side panorama stitching
- 360° viewer integration
- Virtual reality experiences
- Equirectangular projection export
- Integration with photogrammetry pipelines

## Testing Recommendations

1. **Visual Testing**
   - Verify dot moves smoothly toward center when rotating
   - Confirm dot turns green when aligned
   - Check dot reaches exact center before capture

2. **Capture Testing**
   - Verify automatic capture triggers at correct alignment
   - Check all 24 horizontal segments are captured
   - Verify ceiling/floor captures work correctly

3. **Metadata Testing**
   - Confirm metadata JSON is saved after scan
   - Verify stitching script is generated
   - Check spherical coordinates are correct
   - Validate metadata includes all captured images

4. **Integration Testing**
   - Test with external panorama stitching software
   - Verify images can be reconstructed into 360° panorama
   - Check metadata format is compatible with standard tools

## Next Steps (Optional Enhancements)

1. **Real-time Panorama Preview**
   - Show partial panorama as user captures
   - Display coverage map

2. **Client-Side Stitching**
   - Implement basic panorama stitching in-app
   - Generate equirectangular projection
   - Add 360° viewer component

3. **Advanced Metadata**
   - Include camera intrinsics (focal length, sensor size)
   - Add exposure data for HDR stitching
   - Store distortion correction parameters

4. **Performance Optimization**
   - Parallel image processing
   - Progressive JPEG encoding
   - Thumbnail generation for preview
