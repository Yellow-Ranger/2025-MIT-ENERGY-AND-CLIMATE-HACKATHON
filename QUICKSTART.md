# Quick Start Guide

## Get Started in 3 Steps

### 1. Install Dependencies

```bash
npm install
```

### 2. Start the Development Server

```bash
npm start
```

### 3. Run on Your Device

- **iOS**: Press `i` in the terminal or scan the QR code with your iPhone Camera app
- **Android**: Press `a` or scan the QR code with Expo Go app
- **Web**: Press `w` (limited 3D features)

## First Time Setup

### Required Tools

- Node.js 18+
- Expo CLI: `npm install -g expo-cli`
- For iOS: Xcode (macOS only)
- For Android: Android Studio

### Permissions

When you first launch the app, you'll need to grant:

- **Camera permission** - Required for room scanning
- **Photo library permission** - Required for saving STL files

## Testing the App

### On a Device Without LiDAR

1. Launch the app
2. Tap "Start Scanning"
3. Grant camera permissions
4. The app will automatically use Photo Mode
5. Follow on-screen instructions
6. Preview and export your scan

### On a LiDAR-Enabled Device

1. Launch the app
2. Tap "Start Scanning"
3. Grant camera permissions
4. You'll see "📡 LiDAR Mode" indicator
5. Move your device slowly around the room
6. Watch the point cloud build in real-time
7. Preview and export as STL

## Troubleshooting

### "Metro bundler is not running"

```bash
# Clear the cache and restart
npm start -- --clear
```

### "Unable to resolve module"

```bash
# Reinstall dependencies
rm -rf node_modules
npm install
```

### Camera not working

- Ensure you granted camera permissions
- Check Settings > Privacy > Camera
- Restart the app

## Next Steps

- Read the full [README.md](README.md) for detailed documentation
- Explore the code in the `app/` directory
- Customize the UI in component files
- Add your own 3D processing algorithms

## Need Help?

- Check the [Expo Documentation](https://docs.expo.dev)
- Review Three.js examples at [threejs.org](https://threejs.org)
- Open an issue on GitHub

Happy scanning! 🎉
