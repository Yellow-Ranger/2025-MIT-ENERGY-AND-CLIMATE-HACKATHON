import { View, Text, StyleSheet, Pressable, ScrollView } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { useRouter } from "expo-router";
import { LinearGradient } from "expo-linear-gradient";

export default function Home() {
  const router = useRouter();

  return (
    <SafeAreaView style={styles.container}>
      <LinearGradient
        colors={["#000000", "#1a1a2e", "#0f3460"]}
        style={styles.gradient}
      >
        <ScrollView
          contentContainerStyle={styles.content}
          showsVerticalScrollIndicator={false}
        >
          <View style={styles.header}>
            <Text style={styles.title}>360° Room Scanner</Text>
            <Text style={styles.subtitle}>
              Create precise 3D models of your rooms using guided 360°
              photogrammetry
            </Text>
          </View>

          <View style={styles.features}>
            <FeatureCard
              icon="📷"
              title="Guided Capture"
              description="Follow on-screen instructions for perfect 360° coverage"
            />
            <FeatureCard
              icon="🧊"
              title="3D Reconstruction"
              description="Automatic photogrammetry processing in the cloud"
            />
            <FeatureCard
              icon="📐"
              title="STL Export"
              description="Download ready-to-use models for COMSOL simulation"
            />
          </View>

          <View style={styles.buttonContainer}>
            <Pressable
              style={({ pressed }) => [
                styles.secondaryButton,
                pressed && styles.buttonPressed,
              ]}
              onPress={() => router.push("/gallery")}
            >
              <Text style={styles.secondaryButtonText}>View Gallery</Text>
            </Pressable>

            <Pressable
              style={({ pressed }) => [
                styles.scanButton,
                pressed && styles.buttonPressed,
              ]}
              onPress={() => router.push("/native-scan")}
            >
              <Text style={styles.scanButtonText}>Start Scanning</Text>
            </Pressable>
          </View>
        </ScrollView>
      </LinearGradient>
    </SafeAreaView>
  );
}

function FeatureCard({
  icon,
  title,
  description,
}: {
  icon: string;
  title: string;
  description: string;
}) {
  return (
    <View style={styles.featureCard}>
      <Text style={styles.featureIcon}>{icon}</Text>
      <Text style={styles.featureTitle}>{title}</Text>
      <Text style={styles.featureDescription}>{description}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#000",
  },
  gradient: {
    flex: 1,
  },
  content: {
    padding: 24,
    paddingBottom: 100,
  },
  header: {
    marginVertical: 20,
    alignItems: "center",
  },
  title: {
    fontSize: 36,
    fontWeight: "700",
    color: "#fff",
    textAlign: "center",
    marginBottom: 12,
    letterSpacing: -1,
  },
  subtitle: {
    fontSize: 16,
    color: "#a0a0a0",
    textAlign: "center",
    lineHeight: 24,
    paddingHorizontal: 20,
  },
  features: {
    gap: 16,
  },
  featureCard: {
    backgroundColor: "rgba(255, 255, 255, 0.05)",
    borderRadius: 16,
    padding: 20,
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.1)",
  },
  featureIcon: {
    fontSize: 32,
    marginBottom: 8,
  },
  featureTitle: {
    fontSize: 18,
    fontWeight: "600",
    color: "#fff",
    marginBottom: 4,
  },
  featureDescription: {
    fontSize: 14,
    color: "#a0a0a0",
    lineHeight: 20,
  },
  buttonContainer: {
    gap: 12,
    marginTop: 20,
    marginBottom: 40,
  },
  scanButton: {
    backgroundColor: "#00d4ff",
    paddingVertical: 18,
    paddingHorizontal: 32,
    borderRadius: 12,
    alignItems: "center",
    shadowColor: "#00d4ff",
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 8,
  },
  secondaryButton: {
    backgroundColor: "rgba(255, 255, 255, 0.1)",
    paddingVertical: 18,
    paddingHorizontal: 32,
    borderRadius: 12,
    alignItems: "center",
    borderWidth: 1,
    borderColor: "rgba(255, 255, 255, 0.2)",
  },
  buttonPressed: {
    opacity: 0.8,
    transform: [{ scale: 0.98 }],
  },
  scanButtonText: {
    color: "#000",
    fontSize: 18,
    fontWeight: "700",
    letterSpacing: 0.5,
  },
  secondaryButtonText: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "600",
    letterSpacing: 0.5,
  },
});
