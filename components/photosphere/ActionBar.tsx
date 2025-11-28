import React from 'react';
import { View, TouchableOpacity, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';

interface ActionBarProps {
  onUndo?: () => void;
  onConfirm?: () => void;
  onCancel: () => void;
  showUndo?: boolean;
  showConfirm?: boolean;
}

/**
 * ActionBar component
 * Bottom action bar with undo, confirm, and cancel buttons
 */
export function ActionBar({
  onUndo,
  onConfirm,
  onCancel,
  showUndo = false,
  showConfirm = false
}: ActionBarProps) {
  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={[styles.button, !showUndo && styles.buttonDisabled]}
        onPress={onUndo}
        disabled={!showUndo}
      >
        <Ionicons name="arrow-undo" size={24} color="#FFFFFF" />
      </TouchableOpacity>

      <TouchableOpacity
        style={[styles.button, styles.confirmButton, !showConfirm && styles.buttonDisabled]}
        onPress={onConfirm}
        disabled={!showConfirm}
      >
        <Ionicons name="checkmark" size={32} color="#FFFFFF" />
      </TouchableOpacity>

      <TouchableOpacity style={styles.button} onPress={onCancel}>
        <Ionicons name="close" size={24} color="#FFFFFF" />
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
    paddingVertical: 20,
    paddingHorizontal: 40,
  },
  button: {
    width: 54,
    height: 54,
    borderRadius: 27,
    backgroundColor: 'rgba(255, 255, 255, 0.15)',
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255, 255, 255, 0.3)',
  },
  confirmButton: {
    width: 64,
    height: 64,
    borderRadius: 32,
    backgroundColor: '#4285F4',
    borderColor: '#4285F4',
  },
  buttonDisabled: {
    opacity: 0.3,
  },
});
