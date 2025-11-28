#!/usr/bin/env python3
"""
Demo script to test COMSOL code generation without LLM calls.
This creates a simple example to verify the generator works.
"""

import sys
from pathlib import Path
from materials import material_library
from comsol_generator import COMSOLJavaGenerator

def main():
    if len(sys.argv) < 2:
        print("Usage: python demo_generator.py <stl_file>")
        sys.exit(1)

    stl_path = sys.argv[1]

    # Mock STL analysis
    stl_analysis = {
        'file_name': Path(stl_path).name,
        'bounding_box': {
            'width': 6.5,
            'depth': 7.13,
            'height': 2.7,
            'volume': 125.14
        },
        'mesh_statistics': {
            'triangle_count': 4558,
            'vertex_count': 13674,
            'surface_area': 206.14
        },
        'identified_components': {
            'wall': {'count': 2, 'total_area': 105.22},
            'furniture': {'count': 2, 'total_area': 94.54}
        }
    }

    # Mock material assignments
    material_assignments = {
        'wall': {
            'material_key': 'drywall',
            'material': material_library.get_material('drywall'),
            'component_count': 2
        },
        'floor': {
            'material_key': 'wood_floor',
            'material': material_library.get_material('wood_floor'),
            'component_count': 1
        },
        'ceiling': {
            'material_key': 'concrete_ceiling',
            'material': material_library.get_material('concrete_ceiling'),
            'component_count': 1
        },
        'heat_sources': [
            {
                'location': 'wall',
                'position': {'x': 2.5, 'y': 0.5, 'z': 1.0},
                'suggested_material': 'heating_element',
                'temperature': 323.15,
                'reasoning': 'Fallback heat source on wall'
            }
        ]
    }

    # Generate code
    print(f"Generating COMSOL Java code for: {stl_path}")
    generator = COMSOLJavaGenerator(stl_path, output_dir=".")

    code = generator.generate(stl_analysis, material_assignments, mesh_size="normal")
    java_file = generator.save(code)

    print(f"\n✓ Generated: {java_file}")
    print(f"  Class name: {generator.class_name}")
    print(f"  Lines: {len(code.splitlines())}")
    print(f"\nYou can compile it with:")
    print(f"  /Applications/COMSOL63/Multiphysics/bin/comsol compile {java_file}")

if __name__ == '__main__':
    main()
