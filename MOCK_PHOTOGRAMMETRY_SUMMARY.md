# Mock Photogrammetry Feature - Implementation Summary

## Overview

Added a fast "mock" photogrammetry mode that uses a pre-existing STL file instead of running the full COLMAP reconstruction pipeline. This is now **enabled by default** for faster development and testing.

## Changes Made

### 1. Backend - photogrammetry.py

**File:** [backend/photogrammetry.py](backend/photogrammetry.py)

**Changes:**
- Added environment variable configuration for mock mode (lines 29-32)
  - `USE_MOCK_PHOTOGRAMMETRY` - defaults to `true`
  - `MOCK_STL_PATH` - points to `/Users/anesu/Documents/comsol-physics/assets/room2.stl`
- Added new `process_images_mock()` function (lines 45-113)
  - Validates images are present
  - Simulates processing steps with realistic progress updates
  - Copies pre-existing STL file to output directory
  - Returns mesh statistics
- Modified `process_images()` to check mock mode flag (lines 142-145)
  - Routes to mock processing when enabled
  - Preserves original COLMAP processing when disabled

### 2. Backend - main.py

**File:** [backend/main.py](backend/main.py)

**Changes:**
- Added import of mock mode configuration (line 11)
- Added startup banner showing current mode (lines 15-26)
- Updated `/api/health` endpoint to include mode information (lines 52-60)
  - Added `mockMode` boolean field
  - Added `photogrammetryMode` string field ("mock" or "colmap")

### 3. Helper Scripts

Created three new scripts in the `backend/` directory:

#### run_mock.sh
- Explicitly runs backend in mock mode
- Validates mock STL file exists
- Sets environment variables

#### restart.sh
- Stops existing backend server
- Starts new server with latest code
- Supports `--real` or `--colmap` flag to disable mock mode
- Usage:
  ```bash
  ./restart.sh              # Start in mock mode (default)
  ./restart.sh --real       # Start with real COLMAP processing
  ```

### 4. Documentation

#### MOCK_MODE.md
Comprehensive documentation covering:
- Feature overview and use cases
- Configuration options
- Speed comparison table
- Implementation details
- File references

## Usage

### Quick Start (Mock Mode - Default)

1. Start the backend:
   ```bash
   cd backend
   python3 main.py
   ```
   You'll see:
   ```
   ============================================================
   🚀 MOCK PHOTOGRAMMETRY MODE ENABLED
      Using STL file: /Users/anesu/Documents/comsol-physics/assets/room2.stl
      Set USE_MOCK_PHOTOGRAMMETRY=false to use real COLMAP processing
   ============================================================
   ```

2. Capture images with your app as normal

3. Processing completes in ~2 seconds instead of 5-15 minutes

### Switch to Real COLMAP Processing

```bash
cd backend
USE_MOCK_PHOTOGRAMMETRY=false python3 main.py
```

Or use the restart script:
```bash
cd backend
./restart.sh --real
```

## Benefits

1. **Fast Iteration** - Test UI/UX changes without waiting for reconstruction
2. **Reliable Testing** - Consistent output for testing the full pipeline
3. **Easy Toggle** - Single environment variable to switch modes
4. **No Code Changes** - Original COLMAP code is completely preserved
5. **Development Speed** - ~2 seconds vs 5-15 minutes per test cycle

## Technical Details

### Processing Flow

**Mock Mode:**
```
Images Upload → Validation → Copy STL → Return Results (~2 sec)
```

**Real Mode:**
```
Images Upload → COLMAP Feature Extraction → Sparse Reconstruction →
Dense Reconstruction → Mesh Generation → STL Export (~5-15 min)
```

### Files Modified

1. [backend/photogrammetry.py](backend/photogrammetry.py) - Core processing logic
2. [backend/main.py](backend/main.py) - FastAPI server configuration

### Files Created

1. [backend/MOCK_MODE.md](backend/MOCK_MODE.md) - Feature documentation
2. [backend/run_mock.sh](backend/run_mock.sh) - Helper script for mock mode
3. [backend/restart.sh](backend/restart.sh) - Server restart script
4. [MOCK_PHOTOGRAMMETRY_SUMMARY.md](MOCK_PHOTOGRAMMETRY_SUMMARY.md) - This file

### Mock STL File

Location: `/Users/anesu/Documents/comsol-physics/assets/room2.stl`
- Size: ~85KB
- Contains a complete room model
- Can be customized via `MOCK_STL_PATH` environment variable

## Verification

Check if mock mode is active:

```bash
curl http://localhost:8000/api/health | python3 -m json.tool
```

Expected response:
```json
{
  "status": "ok",
  "version": "1.0",
  "mockMode": true,
  "photogrammetryMode": "mock"
}
```

## Next Steps

To use the new mock mode:

1. **Restart your backend server** to load the new code:
   ```bash
   cd backend
   ./restart.sh
   ```

2. The server will start in mock mode by default

3. Test the capture flow - processing should complete in ~2 seconds

4. When you need real reconstruction, restart with:
   ```bash
   ./restart.sh --real
   ```

## Rollback

If you need to disable mock mode completely, set:
```bash
export USE_MOCK_PHOTOGRAMMETRY=false
```

The original COLMAP processing code is completely untouched and will work exactly as before.
