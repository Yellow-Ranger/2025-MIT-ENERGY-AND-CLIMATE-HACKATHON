"""
STL Geometry Analyzer

This module analyzes STL files to extract geometric information and identify
components (walls, floor, ceiling, furniture, etc.) using heuristic analysis.
"""

import struct
import numpy as np
from pathlib import Path
from typing import Dict, List, Tuple, Optional
from dataclasses import dataclass
from collections import defaultdict


@dataclass
class BoundingBox:
    """3D bounding box."""
    min_x: float
    max_x: float
    min_y: float
    max_y: float
    min_z: float
    max_z: float

    @property
    def width(self) -> float:
        return self.max_x - self.min_x

    @property
    def depth(self) -> float:
        return self.max_y - self.min_y

    @property
    def height(self) -> float:
        return self.max_z - self.min_z

    @property
    def center(self) -> Tuple[float, float, float]:
        return (
            (self.min_x + self.max_x) / 2,
            (self.min_y + self.max_y) / 2,
            (self.min_z + self.max_z) / 2
        )

    @property
    def volume(self) -> float:
        return self.width * self.depth * self.height

    def __str__(self):
        return f"BBox: {self.width:.2f} x {self.depth:.2f} x {self.height:.2f} m"


@dataclass
class Triangle:
    """3D triangle with normal vector."""
    normal: np.ndarray
    vertices: np.ndarray  # 3x3 array

    @property
    def center(self) -> np.ndarray:
        return np.mean(self.vertices, axis=0)

    @property
    def area(self) -> float:
        v1 = self.vertices[1] - self.vertices[0]
        v2 = self.vertices[2] - self.vertices[0]
        return 0.5 * np.linalg.norm(np.cross(v1, v2))


@dataclass
class SurfaceGroup:
    """Group of triangles forming a surface."""
    triangles: List[Triangle]
    normal_direction: str  # 'horizontal', 'vertical_x', 'vertical_y', 'other'
    average_normal: np.ndarray
    bounding_box: BoundingBox
    total_area: float
    classification: Optional[str] = None  # 'floor', 'ceiling', 'wall', 'furniture', etc.


@dataclass
class GeometryAnalysis:
    """Complete analysis of STL geometry."""
    bounding_box: BoundingBox
    triangle_count: int
    vertex_count: int
    surface_area: float
    surfaces: List[SurfaceGroup]
    identified_components: Dict[str, List[SurfaceGroup]]


class STLAnalyzer:
    """Analyzer for STL files."""

    def __init__(self, stl_path: str):
        self.stl_path = Path(stl_path)
        self.triangles: List[Triangle] = []
        self.analysis: Optional[GeometryAnalysis] = None

    def load_stl(self) -> bool:
        """Load STL file (binary or ASCII format)."""
        try:
            # Try binary format first
            if self._load_binary_stl():
                return True
            # Fall back to ASCII format
            return self._load_ascii_stl()
        except Exception as e:
            print(f"Error loading STL file: {e}")
            return False

    def _load_binary_stl(self) -> bool:
        """Load binary STL file."""
        try:
            with open(self.stl_path, 'rb') as f:
                # Skip header (80 bytes)
                header = f.read(80)

                # Read number of triangles
                triangle_count = struct.unpack('I', f.read(4))[0]

                self.triangles = []
                for _ in range(triangle_count):
                    # Read normal (3 floats)
                    normal = np.array(struct.unpack('fff', f.read(12)))

                    # Read 3 vertices (9 floats)
                    vertices = np.array([
                        struct.unpack('fff', f.read(12)),
                        struct.unpack('fff', f.read(12)),
                        struct.unpack('fff', f.read(12))
                    ])

                    # Skip attribute byte count
                    f.read(2)

                    self.triangles.append(Triangle(normal=normal, vertices=vertices))

                return len(self.triangles) > 0
        except:
            return False

    def _load_ascii_stl(self) -> bool:
        """Load ASCII STL file."""
        try:
            with open(self.stl_path, 'r') as f:
                lines = f.readlines()

            self.triangles = []
            i = 0
            while i < len(lines):
                line = lines[i].strip()

                if line.startswith('facet normal'):
                    # Parse normal
                    parts = line.split()
                    normal = np.array([float(parts[2]), float(parts[3]), float(parts[4])])

                    # Skip 'outer loop'
                    i += 2

                    # Parse 3 vertices
                    vertices = []
                    for _ in range(3):
                        parts = lines[i].strip().split()
                        vertices.append([float(parts[1]), float(parts[2]), float(parts[3])])
                        i += 1

                    self.triangles.append(Triangle(normal=normal, vertices=np.array(vertices)))

                i += 1

            return len(self.triangles) > 0
        except Exception as e:
            print(f"Error loading ASCII STL: {e}")
            return False

    def analyze(self) -> GeometryAnalysis:
        """Perform complete geometric analysis."""
        if not self.triangles:
            raise ValueError("No triangles loaded. Call load_stl() first.")

        # Calculate bounding box
        all_vertices = np.vstack([t.vertices for t in self.triangles])
        bbox = BoundingBox(
            min_x=float(np.min(all_vertices[:, 0])),
            max_x=float(np.max(all_vertices[:, 0])),
            min_y=float(np.min(all_vertices[:, 1])),
            max_y=float(np.max(all_vertices[:, 1])),
            min_z=float(np.min(all_vertices[:, 2])),
            max_z=float(np.max(all_vertices[:, 2]))
        )

        # Calculate surface area
        total_area = sum(t.area for t in self.triangles)

        # Group surfaces by orientation and position
        surfaces = self._group_surfaces()

        # Identify components using heuristics
        components = self._identify_components(surfaces, bbox)

        self.analysis = GeometryAnalysis(
            bounding_box=bbox,
            triangle_count=len(self.triangles),
            vertex_count=len(all_vertices),
            surface_area=total_area,
            surfaces=surfaces,
            identified_components=components
        )

        return self.analysis

    def _group_surfaces(self, angle_threshold: float = 15.0) -> List[SurfaceGroup]:
        """
        Group triangles into surfaces based on normal orientation.

        Previous versions lumped all triangles with similar orientation into a
        single group (e.g., all horizontal triangles), which caused large,
        mixed groups that span from floor to ceiling. That made it impossible
        for the component classifier to reliably distinguish floor, ceiling,
        furniture, windows, and doors.

        This refined implementation instead creates a small surface group for
        each triangle, tagged with its dominant orientation. The component
        identification logic then works on these finer-grained surfaces, and
        aggregates them back into semantic categories (floor, wall, furniture,
        etc.) when building summaries. This is slightly more verbose but much
        more accurate for room-scale STL models.
        """
        surfaces: List[SurfaceGroup] = []

        for tri in self.triangles:
            # Normalized normal vector
            normal = tri.normal / (np.linalg.norm(tri.normal) + 1e-10)

            # Angle between normal and global vertical axis (Z)
            angle_from_vertical = np.degrees(np.arccos(np.abs(normal[2])))

            if angle_from_vertical < angle_threshold:
                direction = 'horizontal'
            elif angle_from_vertical > (90 - angle_threshold):
                # Vertical surface (normal mostly horizontal)
                if abs(normal[0]) > abs(normal[1]):
                    direction = 'vertical_x'
                else:
                    direction = 'vertical_y'
            else:
                direction = 'other'

            surfaces.append(self._create_surface_group([tri], direction))

        return surfaces

    def _create_surface_group(self, triangles: List[Triangle], direction: str) -> SurfaceGroup:
        """Create a surface group from triangles."""
        all_vertices = np.vstack([t.vertices for t in triangles])

        bbox = BoundingBox(
            min_x=float(np.min(all_vertices[:, 0])),
            max_x=float(np.max(all_vertices[:, 0])),
            min_y=float(np.min(all_vertices[:, 1])),
            max_y=float(np.max(all_vertices[:, 1])),
            min_z=float(np.min(all_vertices[:, 2])),
            max_z=float(np.max(all_vertices[:, 2]))
        )

        avg_normal = np.mean([t.normal for t in triangles], axis=0)
        total_area = sum(t.area for t in triangles)

        return SurfaceGroup(
            triangles=triangles,
            normal_direction=direction,
            average_normal=avg_normal,
            bounding_box=bbox,
            total_area=total_area
        )

    def _identify_components(self, surfaces: List[SurfaceGroup],
                           bbox: BoundingBox) -> Dict[str, List[SurfaceGroup]]:
        """Identify components (floor, walls, ceiling, etc.) using heuristics."""
        components = defaultdict(list)

        z_threshold = bbox.height * 0.1  # 10% tolerance for floor/ceiling detection

        for surface in surfaces:
            surf_bbox = surface.bounding_box

            # Classify based on position and orientation
            if surface.normal_direction == 'horizontal':
                # Check if it's near the bottom (floor) or top (ceiling)
                avg_z = (surf_bbox.min_z + surf_bbox.max_z) / 2

                if abs(avg_z - bbox.min_z) < z_threshold:
                    surface.classification = 'floor'
                    components['floor'].append(surface)
                elif abs(avg_z - bbox.max_z) < z_threshold:
                    surface.classification = 'ceiling'
                    components['ceiling'].append(surface)
                else:
                    # Horizontal surface in the middle - could be furniture
                    surface.classification = 'furniture'
                    components['furniture'].append(surface)

            elif surface.normal_direction in ['vertical_x', 'vertical_y']:
                # Vertical surfaces are likely walls
                # Check if full height (wall) or partial (door/window/furniture)
                if surf_bbox.height > bbox.height * 0.6:
                    surface.classification = 'wall'
                    components['wall'].append(surface)
                else:
                    # Could be door, window, or furniture
                    # Use position heuristics
                    z_pos = (surf_bbox.min_z + surf_bbox.max_z) / 2

                    if z_pos > bbox.min_z + bbox.height * 0.3 and z_pos < bbox.min_z + bbox.height * 0.8:
                        surface.classification = 'window'
                        components['window'].append(surface)
                    elif abs(surf_bbox.min_z - bbox.min_z) < z_threshold:
                        surface.classification = 'door'
                        components['door'].append(surface)
                    else:
                        surface.classification = 'furniture'
                        components['furniture'].append(surface)
            else:
                surface.classification = 'other'
                components['other'].append(surface)

        return dict(components)

    def get_summary(self) -> Dict:
        """Get a summary of the analysis suitable for LLM prompts."""
        if not self.analysis:
            raise ValueError("Analysis not performed. Call analyze() first.")

        summary = {
            'file_name': self.stl_path.name,
            'bounding_box': {
                'width': round(self.analysis.bounding_box.width, 2),
                'depth': round(self.analysis.bounding_box.depth, 2),
                'height': round(self.analysis.bounding_box.height, 2),
                'volume': round(self.analysis.bounding_box.volume, 2)
            },
            'mesh_statistics': {
                'triangle_count': self.analysis.triangle_count,
                'vertex_count': self.analysis.vertex_count,
                'surface_area': round(self.analysis.surface_area, 2)
            },
            'identified_components': {}
        }

        for comp_type, surfaces in self.analysis.identified_components.items():
            summary['identified_components'][comp_type] = {
                'count': len(surfaces),
                'total_area': round(sum(s.total_area for s in surfaces), 2),
                'surfaces': [
                    {
                        'bbox': str(s.bounding_box),
                        'area': round(s.total_area, 2),
                        'position': {
                            'x': round(s.bounding_box.center[0], 2),
                            'y': round(s.bounding_box.center[1], 2),
                            'z': round(s.bounding_box.center[2], 2)
                        }
                    }
                    for s in surfaces
                ]
            }

        return summary


if __name__ == '__main__':
    # Test the analyzer
    import sys
    import json

    if len(sys.argv) < 2:
        print("Usage: python stl_analyzer.py <stl_file>")
        sys.exit(1)

    analyzer = STLAnalyzer(sys.argv[1])

    print("Loading STL file...")
    if not analyzer.load_stl():
        print("Failed to load STL file")
        sys.exit(1)

    print("Analyzing geometry...")
    analysis = analyzer.analyze()

    print("\n" + "="*60)
    print("STL ANALYSIS RESULTS")
    print("="*60)

    summary = analyzer.get_summary()
    print(json.dumps(summary, indent=2))
