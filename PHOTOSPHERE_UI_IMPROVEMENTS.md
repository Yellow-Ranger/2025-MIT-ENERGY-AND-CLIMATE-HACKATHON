# Google Photosphere UI Improvements

## Summary
Updated the UI to match Google Photosphere's visual design and user experience, with proper dot styling, smooth movement, segment indicators, and panoramic preview.

## Key Changes

### 1. Dot Appearance & Behavior

#### Before:
- Small dot (28x28px) with orange color
- Turned green when aligned
- Stationary at fixed positions

#### After:
- **Large white dot (48x48px)** matching Google Photosphere
- **White by default**, turns **blue (#4285f4)** when inside the circle (ready to capture)
- **Shadow effect** for better visibility
- Smooth movement toward center circle as user aligns

**Dot Styling:**
```typescript
{
  width: 48,
  height: 48,
  borderRadius: 24,
  backgroundColor: "#ffffff",  // White by default
  borderWidth: 3,
  borderColor: "#ffffff",
  shadowColor: "#000",
  shadowOffset: { width: 0, height: 2 },
  shadowOpacity: 0.3,
  shadowRadius: 4,
}

// When aligned (inside circle):
{
  backgroundColor: "#4285f4",  // Google blue
  borderColor: "#4285f4",
}
```

### 2. Smooth Dot Movement

**Movement Logic:**
- Dot starts at **80px radius** from center when far from target (>30°)
- As user rotates toward target heading, dot smoothly moves toward center
- At **12° or less** from target, dot turns blue and enters circle
- At perfect alignment, dot reaches exact center (radius = 0)
- Photo automatically captures when dot is blue and centered

**Key Parameters:**
```typescript
const maxRadius = 80;           // Distance when not aligned
const alignmentThreshold = 30;  // Start moving when within this angle
const captureThreshold = 12;    // Turn blue when within this angle
```

**Movement Formula:**
```typescript
if (angleDiff > alignmentThreshold) {
  radius = maxRadius;
} else {
  // Linear interpolation from maxRadius to 0
  radius = maxRadius * (angleDiff / alignmentThreshold);
}
```

### 3. Segment Indicators

Added small dots around the viewport circle showing capture progress:

**Features:**
- **24 indicator dots** arranged in a circle around the focus ring
- **White/transparent** for uncaptured segments
- **Blue (#4285f4)** and larger for captured segments
- Updates in real-time as user captures images

**Styling:**
```typescript
// Uncaptured segment:
{
  width: 8,
  height: 8,
  borderRadius: 4,
  backgroundColor: "rgba(255, 255, 255, 0.3)",
  borderWidth: 1,
  borderColor: "rgba(255, 255, 255, 0.5)",
}

// Captured segment:
{
  backgroundColor: "#4285f4",
  borderColor: "#4285f4",
  width: 10,
  height: 10,
  borderRadius: 5,
}
```

**Placement:**
```typescript
// Positioned in a circle around the focus ring
const ringRadius = 85;
for (let i = 0; i < 24; i++) {
  const angle = (i * 360) / 24;
  const rad = ((angle - 90) * Math.PI) / 180;
  const x = Math.cos(rad) * ringRadius;
  const y = Math.sin(rad) * ringRadius;
}
```

### 4. Panoramic Preview Strip

Replaced simple thumbnail grid with a **panoramic preview strip** that shows captured segments stitching together:

**Features:**
- Shows only horizontal rotation images (current position)
- **Sorted by segment number** (0-23) for correct panoramic order
- Displays as continuous horizontal strip
- Images appear side-by-side showing the 360° progression
- Blue border to indicate captured segments

**Layout:**
```typescript
{
  position: "absolute",
  bottom: 120,
  left: 0,
  right: 0,
  height: 80,
  backgroundColor: "rgba(0, 0, 0, 0.4)",
}
```

**Thumbnail Styling:**
```typescript
{
  width: 60,
  height: 70,
  marginHorizontal: 1,
  borderRadius: 4,
  borderWidth: 1,
  borderColor: "rgba(74, 133, 244, 0.5)",
}
```

**Implementation:**
```typescript
const horizontalImages = images
  .filter(img => img.captureType === 'horizontal' && img.position === currentPosition)
  .sort((a, b) => a.segment - b.segment);
```

### 5. Visual Feedback Improvements

#### Color Scheme:
- **White (#ffffff)**: Dot default state, not aligned
- **Blue (#4285f4)**: Dot when inside circle, ready to capture (Google brand blue)
- **Blue indicators**: Show which segments are captured
- **Semi-transparent overlays**: Don't obscure camera view

#### Alignment Thresholds:
- **30°**: Start moving dot toward center
- **12°**: Dot turns blue and enters circle
- **8°**: Automatic capture triggers (for vertical captures)

### 6. User Experience Flow

**Horizontal Rotation:**
1. User sees white dot outside circle pointing to next segment
2. As they rotate toward target, dot moves smoothly toward circle center
3. When within 12° of target, dot turns blue and enters circle
4. Photo captures automatically
5. Segment indicator turns blue
6. Thumbnail appears in panorama preview strip
7. Dot jumps to next uncaptured segment

**Visual Progression:**
```
Far away (>30°):   ○ (white, outside)
Getting closer:    ○ (white, moving in)
Inside circle:     ● (blue, in center)
Captured!          [Photo] → [Preview] → [Next segment]
```

## Files Modified

### app/native-scan.tsx

**Imports:**
- Added `Animated` from React Native (prepared for future smooth animations)

**Constants:**
```typescript
const ALIGN_TOLERANCE_DEGREES = 12; // Capture when aligned
```

**New Functions:**
- `renderSegmentIndicators()`: Draw 24 dots showing capture progress
- `renderPanoramaPreview()`: Display stitched panorama preview strip

**Updated Functions:**
- `renderDot()`: Improved movement logic and thresholds
  - Changed radius calculation for smoother movement
  - Updated alignment thresholds (30° start, 12° capture)
  - Works for horizontal, ceiling, and floor modes

**Styles Added:**
```typescript
psDot: {
  width: 48,
  height: 48,
  backgroundColor: "#ffffff",
  // ... shadow and border styling
}

psDotAligned: {
  backgroundColor: "#4285f4",
  borderColor: "#4285f4",
}

segmentIndicator: {
  width: 8,
  height: 8,
  backgroundColor: "rgba(255, 255, 255, 0.3)",
}

segmentIndicatorCaptured: {
  backgroundColor: "#4285f4",
  width: 10,
  height: 10,
}

panoramaPreview: {
  position: "absolute",
  bottom: 120,
  height: 80,
  // ... styling
}
```

## Visual Comparison

### Dot Behavior

| Aspect | Before | After |
|--------|--------|-------|
| Size | 28x28px | 48x48px |
| Color (not aligned) | Orange | White |
| Color (aligned) | Green | Blue (#4285f4) |
| Movement | Static position jumps | Smooth transition to center |
| Alignment trigger | 8° | 12° (more forgiving) |

### UI Elements

| Element | Before | After |
|---------|--------|-------|
| Segment indicators | None | 24 dots around circle |
| Preview | 4 recent thumbnails | Panoramic strip of all segments |
| Captured feedback | None | Blue segment indicators |
| Preview ordering | By time | By segment (panoramic order) |

## Benefits

### User Experience
✅ Matches familiar Google Photosphere UI
✅ Clear visual feedback with white → blue transition
✅ Larger dot easier to see and align
✅ Smooth movement feels more polished
✅ Segment indicators show progress at a glance
✅ Panorama preview shows 360° stitching in real-time

### Technical
✅ More forgiving alignment (12° vs 8°)
✅ Better visual hierarchy with blue brand color
✅ Organized preview sorted by segment
✅ Real-time progress visualization
✅ Smoother capture experience

### Visual Design
✅ Professional shadow effects on dot
✅ Consistent blue color scheme (#4285f4)
✅ Semi-transparent overlays
✅ Clear captured/uncaptured states
✅ Panoramic preview shows 360° context

## Testing Checklist

### Dot Behavior
- [ ] Dot is white and 48x48px by default
- [ ] Dot moves smoothly toward center when rotating toward target
- [ ] Dot turns blue when within 12° of target
- [ ] Dot reaches exact center before capture
- [ ] Dot movement is smooth, not jerky

### Segment Indicators
- [ ] 24 small white dots appear around circle
- [ ] Dots turn blue and grow when segment is captured
- [ ] Indicators update immediately after capture
- [ ] All 24 segments are visible around the circle

### Panorama Preview
- [ ] Preview strip appears at bottom after first capture
- [ ] Images are ordered by segment number (0-23)
- [ ] Preview shows continuous panoramic progression
- [ ] New captures appear in correct position
- [ ] Preview scrolls horizontally

### Capture Flow
- [ ] Auto-capture triggers when dot is blue and centered
- [ ] Captures happen at correct alignment threshold
- [ ] Progress indicators update in real-time
- [ ] UI doesn't lag or freeze during capture

## Future Enhancements

1. **Animated Transitions**
   - Use React Native Animated API for spring-based dot movement
   - Smooth color transitions white → blue

2. **Advanced Preview**
   - Stitch thumbnails together seamlessly
   - Show equirectangular projection preview
   - Add 360° viewer in preview

3. **Haptic Feedback**
   - Vibrate when dot enters circle
   - Subtle haptic on successful capture

4. **Audio Feedback**
   - Shutter sound on capture
   - Ding sound when segment completes

5. **Progress Animation**
   - Animate segment indicators appearing
   - Progress bar fills with smooth animation
   - Completion celebration animation
