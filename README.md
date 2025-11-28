# 360° Room Scanner with Photogrammetry

A complete solution for scanning rooms using guided 360° photogrammetry, converting to 3D meshes, and integrating with COMSOL for physics simulations.

## Architecture

```
┌─────────────────────────────────────────────────┐
│         Expo Mobile App (React Native)          │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐ │
│  │Home Screen │  │  WebView   │  │  Gallery   │ │
│  │            │─→│  (Full)    │←─│            │ │
│  └────────────┘  └────────────┘  └────────────┘ │
└────────────────────────┬────────────────────────┘
                         │ postMessage
                         ↓
┌─────────────────────────────────────────────────┐
│           Web App (Next.js/React)                │
│  • 360° Guided Capture UI                       │
│  • Coverage ring + device orientation           │
│  • Image capture (60-100 frames)                │
│  • 3D Viewer + Material Assignment              │
└────────────────────────┬────────────────────────┘
                         │ HTTP API
                         ↓
┌─────────────────────────────────────────────────┐
│        Backend (FastAPI + Python)                │
│  • Image upload & storage                       │
│  • Photogrammetry processing                    │
│  • Mesh generation → STL                        │
│  • COMSOL integration                           │
└─────────────────────────────────────────────────┘
```

## Features

### Mobile App (Expo)

- Native iOS/Android app built with React Native
- WebView integration for seamless web-based capture
- Gallery for viewing previous scans
- PostMessage bridge for Expo ↔ Web communication

### Web App (Next.js)

- **Guided 360° Capture Interface**

  - Step 1: Corner positioning with visual guidance
  - Step 2: Horizontal 360° rotation with coverage ring (24 segments)
  - Step 3: Ceiling capture (tilt up to 45-70°)
  - Step 4: Floor capture (tilt down to -45° to -70°)
  - Step 5: Optional second position for better accuracy

- **Quality Controls**

  - Rotation speed detection (warns if too fast)
  - Segment coverage tracking (requires 90%+ coverage)
  - Automatic frame capture at optimal intervals
  - Device orientation API integration

- **3D Viewer**
  - Three.js-based interactive preview
  - Orbit controls for navigation
  - STL download functionality

### Backend (FastAPI)

- RESTful API for image upload and processing
- Photogrammetry pipeline (currently simplified, ready for integration with:
  - OpenMVG/OpenMVS for full photogrammetry
  - AliceVision/Meshroom for advanced reconstruction
  - Commercial APIs like Polycam)
- STL export
- Integration with existing COMSOL Python scripts

## Setup Instructions

### Prerequisites

- Node.js 18+ and npm
- Python 3.10+
- Expo CLI (`npm install -g expo-cli`)
- iOS Simulator (for Mac) or Android Studio (for Android testing)

### 1. Install Dependencies

#### Mobile App (Expo)

```bash
npm install
```

#### Web App

```bash
cd web
npm install
```

#### Backend

```bash
cd backend
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```

### 2. Configuration

#### Web App Environment Variables

Create `web/.env.local`:

```env
NEXT_PUBLIC_API_URL=http://localhost:8000
```

When testing on a physical device on the same Wi‑Fi (no tunnels), use your computer's local IP:

```env
NEXT_PUBLIC_API_URL=http://192.168.1.XXX:8000
```

When using the ngrok dev script (described below), you don't need to set this manually – the script injects `NEXT_PUBLIC_API_URL` with the backend's ngrok URL when starting `npm run dev`.

#### Mobile App Configuration

The Expo app's WebView uses the `WEB_APP_URL` constant in `app/native-scan.tsx`, which is resolved as:

```ts
const WEB_APP_URL =
  process.env.EXPO_PUBLIC_WEB_APP_URL ||
  (__DEV__
    ? "http://10.189.106.226:3000" // Fallback: local dev
    : "https://your-deployed-web-app.vercel.app"); // Production
```

For most setups you should **not** edit this constant directly:

- When using the ngrok dev script, it automatically sets `EXPO_PUBLIC_WEB_APP_URL` to the web app's ngrok URL.
- For manual LAN testing on a simulator, you can rely on the fallback and change it to `http://localhost:3000` if needed.
- For manual LAN testing on a physical device, change the fallback to your machine's IP (e.g. `http://192.168.1.XXX:3000`).

### 3. Run the Applications

You can either use the **one‑command ngrok dev script** (recommended for physical devices / restrictive networks) or start each part manually.

#### Option A: One‑command dev with ngrok (recommended)

Prerequisites:

- `ngrok` installed and authenticated (run `ngrok config add-authtoken <token>` once).

Then from the repo root:

```bash
chmod +x scripts/dev-with-ngrok.sh   # first time only
./scripts/dev-with-ngrok.sh
```

This script will:

- Start the FastAPI backend on port `8000`.
- Start an ngrok tunnel for the backend and set `NEXT_PUBLIC_API_URL` to the backend's public URL for the Next.js app.
- Start the Next.js web app on port `3000`.
- Start a second ngrok tunnel for the web app and set `EXPO_PUBLIC_WEB_APP_URL` to that public URL for the Expo app/WebView.
- Launch `expo start --tunnel` so your phone can load the native bundle.

After the script is running, open the Expo app (simulator or device) and tap **"Start Scanning"** – the WebView will load the web capture UI via the ngrok URL and all API calls will go through the backend tunnel.

#### Option B: Manual local setup (LAN)

For local development on simulator:

```typescript
const WEB_APP_URL = "http://localhost:3000";
```

For physical device testing:

```typescript
const WEB_APP_URL = "http://192.168.1.XXX:3000"; // Your computer's IP
```

#### Terminal 1: Backend API

```bash
cd backend
source venv/bin/activate
python main.py
```

The API will be available at `http://localhost:8000`

#### Terminal 2: Web App

```bash
cd web
npm run dev
```

The web app will be available at `http://localhost:3000`

#### Terminal 3: Expo App

```bash
npm start
```

Then press:

- `i` for iOS simulator
- `a` for Android emulator
- Scan QR code with Expo Go app for physical device

### 4. Testing the Full Flow

1. **Open the Expo app** in your simulator/emulator or physical device
2. **Tap "Start Scanning"** - This opens the WebView with the web app
3. **Grant permissions** when prompted:
   - Camera access
   - Device orientation (iOS 13+ requires explicit permission)
4. **Follow the guided capture flow**:
   - Position yourself in a corner
   - Tap "Start Scan"
   - Rotate slowly in a full circle (watch the coverage ring)
   - Tilt up to capture ceiling
   - Tilt down to capture floor
   - Optionally move to opposite corner and repeat
5. **Upload & Processing**:
   - Images are automatically uploaded to the backend
   - Processing starts (you'll see progress updates)
   - Navigate to the processing page
6. **View Results**:
   - 3D model preview in Three.js viewer
   - Download STL button
   - Configure materials (future feature)

## Project Structure

```
comsol-physics/
├── app/                          # Expo/React Native app
│   ├── index.tsx                 # Home screen
│   ├── native-scan.tsx          # WebView screen (NEW)
│   ├── scanner.tsx               # Old scanner (deprecated)
│   ├── preview.tsx               # Old preview (deprecated)
│   └── gallery.tsx               # Gallery screen
├── web/                          # Next.js web app (NEW)
│   ├── app/
│   │   ├── page.tsx              # Home page
│   │   ├── capture/page.tsx      # 360° capture interface
│   │   └── processing/[scanId]/page.tsx  # Processing status
│   ├── components/
│   │   ├── CameraPreview.tsx     # WebRTC camera component
│   │   ├── CoverageRing.tsx      # Coverage indicator
│   │   ├── VerticalCoverageBar.tsx  # Ceiling/floor capture UI
│   │   └── ModelViewer.tsx       # Three.js 3D viewer
│   ├── hooks/
│   │   └── useDeviceOrientation.ts  # Device orientation hook
│   └── lib/
│       ├── captureStore.ts       # Zustand state management
│       ├── captureUtils.ts       # Capture helper functions
│       └── apiClient.ts          # Backend API client
├── backend/                      # FastAPI backend (NEW)
│   ├── main.py                   # API server
│   ├── photogrammetry.py         # Processing pipeline
│   ├── uploads/                  # Uploaded images
│   └── outputs/                  # Generated STL files
├── stl_to_comsol/               # Existing COMSOL integration
│   └── ...                       # Python scripts for COMSOL
└── README.md                     # This file
```

## API Endpoints

### POST `/api/upload-images`

Upload captured images with metadata

**Request:** `multipart/form-data`

- `images`: List of image files
- `metadata`: JSON string with orientation data per image

**Response:**

```json
{
  "scanId": "uuid",
  "imageCount": 87,
  "status": "uploaded"
}
```

### POST `/api/process-photogrammetry`

Start processing uploaded images

**Request:**

```json
{
  "scanId": "uuid"
}
```

### GET `/api/scan/{scanId}/status`

Get processing status

**Response:**

```json
{
  "scanId": "uuid",
  "status": "processing", // or "complete", "error"
  "progress": 45,
  "currentStep": "Generating point cloud..."
}
```

### GET `/api/scan/{scanId}`

Get scan result

**Response:**

```json
{
  "scanId": "uuid",
  "stlUrl": "/api/scan/{scanId}/download-stl",
  "thumbnailUrl": null
}
```

### GET `/api/scan/{scanId}/download-stl`

Download STL file

## Development Notes

### Current Implementation

- **Web capture interface**: Fully functional with device orientation tracking
- **Backend photogrammetry**: Simplified implementation that generates demo meshes
- **For production**: Integrate with actual photogrammetry libraries:
  - OpenMVG/OpenMVS (open source, C++ with Python bindings)
  - AliceVision/Meshroom (open source, command-line tools)
  - Commercial APIs (Polycam, Sketchfab, etc.)

### WebAssembly Integration (Future)

For client-side processing, consider:

- Compile OpenCV to WASM for feature detection
- Use Three.js for mesh generation in browser
- Offload heavy computation to web workers

### Device Orientation Requirements

- **iOS 13+**: Requires user permission via `DeviceOrientationEvent.requestPermission()`
- **Android**: Works automatically
- **Desktop browsers**: Limited support (mainly for testing)

### Testing on Physical Devices

1. Ensure all three services (backend, web, Expo) are running
2. Find your computer's local IP: `ifconfig` (Mac) or `ipconfig` (Windows)
3. Update URLs in web app and Expo app to use this IP
4. Ensure devices are on the same WiFi network
5. Test camera permissions and device orientation

## Next Steps

### Immediate Improvements

1. **Integrate real photogrammetry**:

   - Install OpenMVG/OpenMVS
   - Or integrate with commercial API
   - Update `backend/photogrammetry.py`

2. **Material assignment UI**:

   - Create web page for material configuration
   - Integrate with existing AI material suggestion (LLM interface)
   - Connect to COMSOL pipeline

3. **Gallery enhancements**:
   - Fetch scans from backend API
   - Show thumbnails and metadata
   - Allow viewing in WebView

### Future Enhancements

- Progressive Web App (PWA) for standalone web use
- Real-time processing progress via WebSockets
- Multi-user support with authentication
- Cloud storage integration (S3, R2)
- Advanced mesh optimization and simplification
- Material library expansion
- COMSOL simulation automation

## Troubleshooting

### Camera not working in WebView

- Ensure camera permissions in `app.json`
- Check WebView props: `mediaPlaybackRequiresUserAction={false}`
- Test in browser first before WebView

### Device orientation not working

- iOS requires explicit permission request
- Check browser console for permission errors
- Test with `DeviceOrientationEvent.requestPermission()`

### Backend connection failed

- Verify backend is running: `curl http://localhost:8000`
- Check firewall settings
- Ensure correct IP address for physical devices
- Verify CORS configuration in FastAPI

### Images not being captured

- Check browser console for errors
- Verify video element is rendering
- Test `getUserMedia` permissions
- Check capture interval timing

## License

MIT

## Contributors

Built with Claude Code for automated room scanning and COMSOL simulation integration.
