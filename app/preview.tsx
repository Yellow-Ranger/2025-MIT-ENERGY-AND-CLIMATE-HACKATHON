import React, { useEffect, useState } from 'react';
import { ActivityIndicator, Alert, StyleSheet, View, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { WebView } from 'react-native-webview';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { getScanById, SavedScan } from '@/utils/scanStorage';

// Reuse the same web app URL selection logic as the scanner
const WEB_APP_URL =
  process.env.EXPO_PUBLIC_WEB_APP_URL ||
  (__DEV__
    ? 'http://10.189.106.226:3000' // Fallback: local dev
    : 'https://your-deployed-web-app.vercel.app'); // Production

export default function PreviewScreen() {
  const router = useRouter();
  const params = useLocalSearchParams<{ scanId?: string }>();
  const [scan, setScan] = useState<SavedScan | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const scanId = params.scanId as string | undefined;

  useEffect(() => {
    const loadScan = async () => {
      try {
        if (!scanId) {
          setError('Missing scan ID');
          setLoading(false);
          return;
        }

        const savedScan = await getScanById(scanId);

        if (!savedScan) {
          setError('Scan not found');
          setLoading(false);
          return;
        }

        if (!savedScan.backendScanId) {
          setError('This scan does not have a 3D model available.');
          setLoading(false);
          return;
        }

        setScan(savedScan);
        setLoading(false);
      } catch (err) {
        console.error('Failed to load scan for preview:', err);
        setError('Failed to load scan');
        setLoading(false);
      }
    };

    loadScan();
  }, [scanId]);

  useEffect(() => {
    if (error) {
      Alert.alert('Preview Error', error, [
        {
          text: 'OK',
          onPress: () => router.back(),
        },
      ]);
    }
  }, [error]);

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.centerContent}>
          <ActivityIndicator size="large" color="#00d4ff" />
          <Text style={styles.loadingText}>Loading scan...</Text>
        </View>
      </SafeAreaView>
    );
  }

  if (!scan || !scan.backendScanId) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.centerContent}>
          <Text style={styles.errorText}>Unable to load scan preview.</Text>
        </View>
      </SafeAreaView>
    );
  }

  const processingUrl = `${WEB_APP_URL}/processing/${scan.backendScanId}`;

  return (
    <SafeAreaView style={styles.container}>
      <WebView
        source={{ uri: processingUrl }}
        style={styles.webview}
        javaScriptEnabled
        domStorageEnabled
        startInLoadingState
        renderLoading={() => (
          <View style={styles.centerContent}>
            <ActivityIndicator size="large" color="#00d4ff" />
            <Text style={styles.loadingText}>Loading 3D preview...</Text>
          </View>
        )}
        onError={(event) => {
          console.warn('Preview WebView error:', event.nativeEvent);
          Alert.alert(
            'Unable to load preview',
            event.nativeEvent.description || 'Check that the web app is reachable.',
            [{ text: 'OK', onPress: () => router.back() }],
          );
        }}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  webview: {
    flex: 1,
  },
  centerContent: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    marginTop: 12,
    color: '#fff',
    fontSize: 16,
  },
  errorText: {
    color: '#ff6b6b',
    fontSize: 16,
  },
});

