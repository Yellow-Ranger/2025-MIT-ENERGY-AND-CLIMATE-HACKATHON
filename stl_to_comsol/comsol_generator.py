"""
COMSOL Java Code Generator

Generates COMSOL Multiphysics Java code for heat transfer and radiation simulations
based on STL geometry and material assignments.
"""

import os
from pathlib import Path
from typing import Dict, Any, List
from materials import Material


class COMSOLJavaGenerator:
    """Generator for COMSOL Java code."""

    def __init__(self, stl_path: str, output_dir: str = "."):
        self.stl_path = Path(stl_path).absolute()
        self.output_dir = Path(output_dir)
        self.class_name = self._generate_class_name()

    def _generate_class_name(self) -> str:
        """Generate Java class name from STL filename."""
        stem = self.stl_path.stem
        # Remove special characters and spaces
        clean_name = ''.join(c if c.isalnum() or c == '_' else '_' for c in stem)
        # Ensure it starts with a letter
        if clean_name and clean_name[0].isdigit():
            clean_name = 'Model_' + clean_name
        return clean_name or 'COMSOLModel'

    def generate(self, stl_analysis: Dict[str, Any],
                material_assignments: Dict[str, Any],
                mesh_size: str = "normal") -> str:
        """
        Generate complete COMSOL Java code.

        Args:
            stl_analysis: STL geometry analysis results
            material_assignments: Final material assignments
            mesh_size: Mesh sizing ('coarse', 'normal', 'fine', 'finer')

        Returns:
            Complete Java code as string
        """
        bbox = stl_analysis['bounding_box']

        code = f"""/*
 * {self.class_name}.java
 * Generated COMSOL model for heat transfer and radiation analysis
 * Source STL: {self.stl_path.name}
 */

import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.IOException;

public class {self.class_name} {{

  public static Model run() throws IOException {{
    Model model = ModelUtil.create("Model");

    model.modelPath("{self.output_dir}");
    model.label("{self.class_name}.mph");

    // Create component and geometry
    model.component().create("comp1", true);
    model.component("comp1").geom().create("geom1", 3);

    // Import STL geometry
    model.component("comp1").geom("geom1").create("imp1", "Import");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("type", "mesh");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("filename", "{str(self.stl_path)}");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("importtol", 1.0E-6);

    // Build geometry
    model.component("comp1").geom("geom1").run();

    return model;
  }}

  public static Model run2(Model model) {{
{self._generate_materials_code(material_assignments)}

    return model;
  }}

  public static Model run3(Model model) {{
{self._generate_physics_code(material_assignments)}

    return model;
  }}

  public static Model run4(Model model) {{
{self._generate_mesh_code(mesh_size)}

    return model;
  }}

  public static Model run5(Model model) {{
{self._generate_solver_code()}

    return model;
  }}

  public static Model run6(Model model) throws IOException {{
{self._generate_results_code()}

    // Save model
    model.save("{self.class_name}_results.mph");

    return model;
  }}

  public static void main(String[] args) throws IOException {{
    System.out.println("Starting COMSOL heat transfer simulation...");
    System.out.println("STL file: {self.stl_path.name}");
    System.out.println("Room dimensions: {bbox['width']:.2f}m x {bbox['depth']:.2f}m x {bbox['height']:.2f}m");
    System.out.println();

    Model model = run();
    System.out.println("Geometry imported successfully");

    model = run2(model);
    System.out.println("Materials assigned");

    model = run3(model);
    System.out.println("Physics configured");

    model = run4(model);
    System.out.println("Mesh generated");

    model = run5(model);
    System.out.println("Solution computed");

    run6(model);
    System.out.println("Results saved");

    System.out.println("\\nSimulation complete!");
  }}
}}
"""
        return code

    def _generate_materials_code(self, material_assignments: Dict[str, Any]) -> str:
        """Generate material assignment code."""
        code_lines = []
        code_lines.append("    // Define materials")

        material_index = 1
        material_map = {}  # Track which materials we've created

        # Create materials for each component type
        for comp_type, assignment in material_assignments.items():
            if comp_type == 'heat_sources':
                continue

            material_obj = assignment.get('material')
            if not material_obj:
                continue

            material_key = assignment['material_key']

            # Only create each unique material once
            if material_key not in material_map:
                mat_name = f"mat{material_index}"
                material_map[material_key] = mat_name

                code_lines.append(f"\n    // {material_obj.name} - for {comp_type}")
                code_lines.append(f"    model.component(\"comp1\").material().create(\"{mat_name}\", \"Common\");")
                code_lines.append(f"    model.component(\"comp1\").material(\"{mat_name}\").label(\"{material_obj.name}\");")

                # Set material properties
                code_lines.append(f"    model.component(\"comp1\").material(\"{mat_name}\").propertyGroup(\"def\")")
                code_lines.append(f"         .set(\"thermalconductivity\", \"{material_obj.thermal_conductivity}\");")
                code_lines.append(f"    model.component(\"comp1\").material(\"{mat_name}\").propertyGroup(\"def\")")
                code_lines.append(f"         .set(\"density\", \"{material_obj.density}\");")
                code_lines.append(f"    model.component(\"comp1\").material(\"{mat_name}\").propertyGroup(\"def\")")
                code_lines.append(f"         .set(\"heatcapacity\", \"{material_obj.heat_capacity}\");")

                # Selection - assign to all domains for now (can be refined)
                code_lines.append(f"    model.component(\"comp1\").material(\"{mat_name}\").selection().all();")

                material_index += 1

        # Handle heat sources
        heat_sources = material_assignments.get('heat_sources', [])
        if heat_sources:
            code_lines.append("\n    // Heat source materials")
            for i, hs in enumerate(heat_sources):
                material_key = hs.get('suggested_material', 'heating_element')
                # Note: Heat source assignment would require more specific geometry info
                code_lines.append(f"    // Heat source {i+1}: {hs.get('reasoning', 'N/A')}")

        return "\n".join(code_lines)

    def _generate_physics_code(self, material_assignments: Dict[str, Any]) -> str:
        """Generate physics setup code."""
        code_lines = []
        code_lines.append("    // Heat transfer physics")
        code_lines.append("    model.component(\"comp1\").physics().create(\"ht\", \"HeatTransfer\", \"geom1\");")

        # Set up initial temperature
        code_lines.append("    model.component(\"comp1\").physics(\"ht\").feature(\"init1\")")
        code_lines.append("         .set(\"Tinit\", \"293.15\");  // 20°C")

        # Add heat source if specified
        heat_sources = material_assignments.get('heat_sources', [])
        if heat_sources:
            for i, hs in enumerate(heat_sources, 1):
                temp = hs.get('temperature', 323.15)
                code_lines.append(f"\n    // Heat source {i}")
                code_lines.append(f"    model.component(\"comp1\").physics(\"ht\").create(\"temp{i}\", \"TemperatureBoundary\", 2);")
                code_lines.append(f"    model.component(\"comp1\").physics(\"ht\").feature(\"temp{i}\")")
                code_lines.append(f"         .set(\"T0\", \"{temp}\");")
                # Note: Selection would require specific boundary identification

        # Surface-to-surface radiation
        code_lines.append("\n    // Surface-to-surface radiation")
        code_lines.append("    model.component(\"comp1\").physics().create(\"rad\", \"SurfaceToSurfaceRadiation\", \"geom1\");")
        code_lines.append("    model.component(\"comp1\").physics(\"rad\").create(\"dsurf1\", \"DiffuseSurface\", 2);")
        code_lines.append("    model.component(\"comp1\").physics(\"rad\").feature(\"dsurf1\").selection().all();")

        # Set emissivity (use first material's emissivity as default)
        default_emissivity = 0.9
        for assignment in material_assignments.values():
            if isinstance(assignment, dict) and 'material' in assignment:
                material = assignment['material']
                if material:
                    default_emissivity = material.emissivity
                    break

        code_lines.append(f"    model.component(\"comp1\").physics(\"rad\").feature(\"dsurf1\")")
        code_lines.append(f"         .set(\"epsilon_rad\", \"{default_emissivity}\");")

        # Multiphysics coupling
        code_lines.append("\n    // Multiphysics coupling")
        code_lines.append("    model.component(\"comp1\").multiphysics().create(\"rhtcpl1\", \"RadiativeHeating\", -1);")
        code_lines.append("    model.component(\"comp1\").multiphysics(\"rhtcpl1\").selection().all();")

        return "\n".join(code_lines)

    def _generate_mesh_code(self, mesh_size: str = "normal") -> str:
        """Generate mesh configuration code."""
        mesh_params = {
            "coarse": {"hmax": 0.5, "hmin": 0.2},
            "normal": {"hmax": 0.3, "hmin": 0.1},
            "fine": {"hmax": 0.2, "hmin": 0.05},
            "finer": {"hmax": 0.15, "hmin": 0.03}
        }

        params = mesh_params.get(mesh_size, mesh_params["normal"])

        code_lines = []
        code_lines.append("    // Create mesh")
        code_lines.append("    model.component(\"comp1\").mesh().create(\"mesh1\");")
        code_lines.append("    model.component(\"comp1\").mesh(\"mesh1\").create(\"ftet1\", \"FreeTet\");")
        code_lines.append("    model.component(\"comp1\").mesh(\"mesh1\").feature(\"size\").set(\"custom\", \"on\");")
        code_lines.append(f"    model.component(\"comp1\").mesh(\"mesh1\").feature(\"size\").set(\"hmax\", {params['hmax']});")
        code_lines.append(f"    model.component(\"comp1\").mesh(\"mesh1\").feature(\"size\").set(\"hmin\", {params['hmin']});")
        code_lines.append("    model.component(\"comp1\").mesh(\"mesh1\").run();")
        code_lines.append("\n    System.out.println(\"Mesh statistics:\");")
        code_lines.append("    System.out.println(\"  Elements: \" + model.component(\"comp1\").mesh(\"mesh1\").getNumElem());")

        return "\n".join(code_lines)

    def _generate_solver_code(self) -> str:
        """Generate solver configuration code."""
        code_lines = []
        code_lines.append("    // Create study")
        code_lines.append("    model.study().create(\"std1\");")
        code_lines.append("    model.study(\"std1\").create(\"stat\", \"Stationary\");")

        code_lines.append("\n    // Create solver")
        code_lines.append("    model.sol().create(\"sol1\");")
        code_lines.append("    model.sol(\"sol1\").attach(\"std1\");")
        code_lines.append("    model.sol(\"sol1\").createAutoSequence(\"std1\");")

        code_lines.append("\n    System.out.println(\"\\nSolving...\");")
        code_lines.append("    long startTime = System.currentTimeMillis();")
        code_lines.append("    model.sol(\"sol1\").runAll();")
        code_lines.append("    long endTime = System.currentTimeMillis();")
        code_lines.append("    System.out.println(\"Solution time: \" + (endTime - startTime) / 1000.0 + \" seconds\");")

        return "\n".join(code_lines)

    def _generate_results_code(self) -> str:
        """Generate results and visualization code."""
        code_lines = []
        code_lines.append("    // Create result plots")
        code_lines.append("    model.result().create(\"pg1\", \"PlotGroup3D\");")
        code_lines.append("    model.result(\"pg1\").label(\"Temperature Distribution\");")
        code_lines.append("    model.result(\"pg1\").create(\"surf1\", \"Surface\");")
        code_lines.append("    model.result(\"pg1\").feature(\"surf1\").set(\"expr\", \"T\");")
        code_lines.append("    model.result(\"pg1\").run();")

        code_lines.append("\n    model.result().create(\"pg2\", \"PlotGroup3D\");")
        code_lines.append("    model.result(\"pg2\").label(\"Heat Flux\");")
        code_lines.append("    model.result(\"pg2\").create(\"surf1\", \"Surface\");")
        code_lines.append("    model.result(\"pg2\").feature(\"surf1\").set(\"expr\", \"ht.ntflux\");")
        code_lines.append("    model.result(\"pg2\").run();")

        code_lines.append("\n    System.out.println(\"\\nResults generated successfully\");")

        return "\n".join(code_lines)

    def save(self, code: str) -> Path:
        """Save generated code to file."""
        output_file = self.output_dir / f"{self.class_name}.java"
        output_file.parent.mkdir(parents=True, exist_ok=True)

        with open(output_file, 'w') as f:
            f.write(code)

        return output_file


if __name__ == '__main__':
    # Test the generator
    import sys

    if len(sys.argv) < 2:
        print("Usage: python comsol_generator.py <stl_file>")
        sys.exit(1)

    stl_path = sys.argv[1]

    # Test data
    test_analysis = {
        'bounding_box': {
            'width': 5.0,
            'depth': 6.0,
            'height': 2.7,
            'volume': 81.0
        }
    }

    from materials import material_library

    test_assignments = {
        'wall': {
            'material_key': 'drywall',
            'material': material_library.get_material('drywall'),
            'component_count': 4
        },
        'floor': {
            'material_key': 'wood_floor',
            'material': material_library.get_material('wood_floor'),
            'component_count': 1
        },
        'heat_sources': []
    }

    generator = COMSOLJavaGenerator(stl_path)
    code = generator.generate(test_analysis, test_assignments)

    output_file = generator.save(code)
    print(f"Generated: {output_file}")
    print(f"\nPreview:")
    print(code[:500] + "...")
