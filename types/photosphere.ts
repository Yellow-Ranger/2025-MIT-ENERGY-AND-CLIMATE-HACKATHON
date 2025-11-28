export type CaptureMode = 'horizontal' | 'ceiling' | 'floor';

export type CaptureStep =
  | 'initial-alignment'
  | 'capturing-horizontal'
  | 'ceiling-prompt'
  | 'capturing-ceiling'
  | 'floor-prompt'
  | 'capturing-floor'
  | 'completion'
  | 'uploading';

export interface DotPosition {
  x: number;
  y: number;
  distance: number;
  scale: number;
  color: 'white' | 'blue';
  opacity: number;
}

export interface CapturedImage {
  blob: string;              // File path
  timestamp: number;
  heading: number | null;
  pitch: number | null;
  segment: number;
  captureType: CaptureMode;
  targetHeading?: number;
  targetPitch?: number;
  position: 1 | 2;
}
