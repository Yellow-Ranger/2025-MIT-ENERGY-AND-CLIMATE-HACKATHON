const { getDefaultConfig } = require('expo/metro-config');

/**
 * Extend Metro so it treats .stl files as static assets that can be required
 * with Asset.fromModule. This fixes bundler errors when loading example scans.
 */
const config = getDefaultConfig(__dirname);

config.resolver.assetExts.push('stl');

module.exports = config;
