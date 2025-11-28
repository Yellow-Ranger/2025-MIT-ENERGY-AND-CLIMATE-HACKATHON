"""
Report Generator Module

Creates comprehensive summary reports of COMSOL simulations including
geometry analysis, material assignments, execution statistics, and results.
"""

import json
from datetime import datetime
from pathlib import Path
from typing import Dict, Any, Optional
from comsol_executor import ExecutionStats


class ReportGenerator:
    """Generator for simulation summary reports."""

    def __init__(self, output_dir: str = "."):
        self.output_dir = Path(output_dir)
        self.output_dir.mkdir(parents=True, exist_ok=True)

    def generate_report(self,
                       stl_path: str,
                       stl_analysis: Dict[str, Any],
                       material_assignments: Dict[str, Any],
                       llm_suggestions: Dict[str, Any],
                       execution_stats: Optional[ExecutionStats],
                       java_file: Optional[Path] = None) -> Path:
        """
        Generate complete simulation report.

        Args:
            stl_path: Path to original STL file
            stl_analysis: STL geometry analysis results
            material_assignments: Final material assignments
            llm_suggestions: Original LLM suggestions
            execution_stats: Execution statistics (if simulation was run)
            java_file: Path to generated Java file

        Returns:
            Path to generated report file
        """
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        report_file = self.output_dir / f"simulation_report_{timestamp}.md"

        with open(report_file, 'w') as f:
            # Header
            f.write(self._generate_header(stl_path, timestamp))

            # Geometry analysis
            f.write(self._generate_geometry_section(stl_analysis))

            # Material assignments
            f.write(self._generate_materials_section(material_assignments, llm_suggestions))

            # Execution results
            if execution_stats:
                f.write(self._generate_execution_section(execution_stats))

            # Generated files
            f.write(self._generate_files_section(stl_path, java_file, execution_stats))

            # Footer
            f.write(self._generate_footer())

        print(f"\n✓ Report saved to: {report_file}")
        return report_file

    def _generate_header(self, stl_path: str, timestamp: str) -> str:
        """Generate report header."""
        return f"""# COMSOL Heat Transfer Simulation Report

**Generated:** {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}
**STL File:** `{Path(stl_path).name}`
**Full Path:** `{stl_path}`

---

"""

    def _generate_geometry_section(self, stl_analysis: Dict[str, Any]) -> str:
        """Generate geometry analysis section."""
        bbox = stl_analysis.get('bounding_box', {})
        mesh_stats = stl_analysis.get('mesh_statistics', {})
        components = stl_analysis.get('identified_components', {})

        section = """## 1. Geometry Analysis

### Room Structure

| Dimension | Value |
|-----------|-------|
"""

        section += f"| Width | {bbox.get('width', 0):.2f} m |\n"
        section += f"| Depth | {bbox.get('depth', 0):.2f} m |\n"
        section += f"| Height | {bbox.get('height', 0):.2f} m |\n"
        section += f"| Volume | {bbox.get('volume', 0):.2f} m³ |\n"

        section += "\n### Mesh Statistics\n\n"
        section += "| Property | Value |\n"
        section += "|----------|-------|\n"
        section += f"| Triangle Count | {mesh_stats.get('triangle_count', 0):,} |\n"
        section += f"| Vertex Count | {mesh_stats.get('vertex_count', 0):,} |\n"
        section += f"| Surface Area | {mesh_stats.get('surface_area', 0):.2f} m² |\n"

        section += "\n### Identified Components\n\n"

        if components:
            section += "| Component Type | Count | Total Area (m²) |\n"
            section += "|----------------|-------|-----------------|\n"

            for comp_type, info in components.items():
                count = info.get('count', 0)
                area = info.get('total_area', 0)
                section += f"| {comp_type.capitalize()} | {count} | {area:.2f} |\n"
        else:
            section += "*No components identified*\n"

        section += "\n---\n\n"
        return section

    def _generate_materials_section(self,
                                    material_assignments: Dict[str, Any],
                                    llm_suggestions: Dict[str, Any]) -> str:
        """Generate materials section."""
        section = """## 2. Material Assignments

### Applied Materials

"""

        for comp_type, assignment in material_assignments.items():
            if comp_type == 'heat_sources':
                continue

            material = assignment.get('material')
            count = assignment.get('component_count', 0)

            if material:
                section += f"#### {comp_type.capitalize()} ({count} components)\n\n"
                section += f"**Material:** {material.name}\n\n"
                section += "| Property | Value |\n"
                section += "|----------|-------|\n"
                section += f"| Thermal Conductivity | {material.thermal_conductivity} W/(m·K) |\n"
                section += f"| Density | {material.density} kg/m³ |\n"
                section += f"| Heat Capacity | {material.heat_capacity} J/(kg·K) |\n"
                section += f"| Emissivity | {material.emissivity} |\n"
                section += f"| Opacity | {material.opacity} |\n"
                section += f"| Default Temperature | {material.default_temperature} K ({material.default_temperature - 273.15:.1f}°C) |\n"
                section += "\n"

        # Heat sources
        section += "### Heat Sources\n\n"

        heat_sources = material_assignments.get('heat_sources', [])
        if heat_sources:
            for i, hs in enumerate(heat_sources, 1):
                section += f"**Heat Source {i}:**\n\n"
                section += f"- **Type:** {hs.get('component_type', 'Unknown')}\n"
                section += f"- **Material:** {hs.get('suggested_material', 'N/A')}\n"
                section += f"- **Temperature:** {hs.get('temperature', 0)} K ({hs.get('temperature', 273.15) - 273.15:.1f}°C)\n"
                section += f"- **Reasoning:** {hs.get('reasoning', 'N/A')}\n\n"
        else:
            section += "*No heat sources configured*\n\n"

        # LLM Analysis Summary
        section += "### AI Analysis Summary\n\n"
        summary = llm_suggestions.get('analysis_summary', 'No summary provided')
        section += f"{summary}\n\n"

        section += "---\n\n"
        return section

    def _generate_execution_section(self, stats: ExecutionStats) -> str:
        """Generate execution results section."""
        section = """## 3. Simulation Results

### Execution Summary

"""

        if stats.success:
            section += "✅ **Status:** Simulation completed successfully\n\n"
        else:
            section += f"❌ **Status:** Simulation failed\n\n"
            if stats.error_message:
                section += f"**Error:** {stats.error_message}\n\n"

        section += "| Metric | Value |\n"
        section += "|--------|-------|\n"

        if stats.compilation_time > 0:
            section += f"| Compilation Time | {stats.compilation_time:.2f} s |\n"

        if stats.mesh_elements > 0:
            section += f"| Mesh Elements | {stats.mesh_elements:,} |\n"

        if stats.solution_time > 0:
            section += f"| Solution Time | {stats.solution_time:.2f} s |\n"

        if stats.execution_time > 0:
            section += f"| Total Execution Time | {stats.execution_time:.2f} s |\n"

        if stats.memory_used_mb > 0:
            section += f"| Peak Memory Usage | {stats.memory_used_mb:.0f} MB ({stats.memory_used_mb/1024:.2f} GB) |\n"

        if stats.solver_iterations > 0:
            section += f"| Solver Iterations | {stats.solver_iterations} |\n"

        section += "\n"

        # Progress log summary
        if stats.progress_log:
            section += "### Key Progress Milestones\n\n"
            section += "```\n"

            # Extract key lines from progress log
            key_keywords = ['Mesh', 'Solving', 'Solution time', 'Done', 'complete', 'ERROR']
            for line in stats.progress_log:
                if any(keyword in line for keyword in key_keywords):
                    section += line + "\n"

            section += "```\n\n"

        section += "---\n\n"
        return section

    def _generate_files_section(self,
                                stl_path: str,
                                java_file: Optional[Path],
                                stats: Optional[ExecutionStats]) -> str:
        """Generate generated files section."""
        section = """## 4. Generated Files

### Input Files

"""

        section += f"- **STL File:** `{stl_path}`\n"

        section += "\n### Output Files\n\n"

        if java_file:
            section += f"- **Java Source:** `{java_file}`\n"

            class_file = java_file.with_suffix('.class')
            if class_file.exists():
                section += f"- **Compiled Class:** `{class_file}`\n"

        if stats and stats.output_file:
            section += f"- **COMSOL Model:** `{stats.output_file}`\n"

        section += "\n---\n\n"
        return section

    def _generate_footer(self) -> str:
        """Generate report footer."""
        return """## Notes

This report was automatically generated by the STL to COMSOL Heat Transfer Simulation tool.

### Next Steps

1. Review the material assignments and modify if needed
2. Open the generated `.mph` file in COMSOL Multiphysics GUI for detailed analysis
3. Examine temperature distribution and heat flux results
4. Export data or visualizations as needed

---

*Generated with STL to COMSOL automation tool*
"""

    def save_json_data(self,
                      stl_analysis: Dict[str, Any],
                      material_assignments: Dict[str, Any],
                      execution_stats: Optional[ExecutionStats]) -> Path:
        """
        Save all data in JSON format for programmatic access.

        Args:
            stl_analysis: STL geometry analysis
            material_assignments: Material assignments
            execution_stats: Execution statistics

        Returns:
            Path to JSON file
        """
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        json_file = self.output_dir / f"simulation_data_{timestamp}.json"

        data = {
            'timestamp': timestamp,
            'stl_analysis': stl_analysis,
            'material_assignments': self._serialize_materials(material_assignments),
            'execution_stats': self._serialize_execution_stats(execution_stats) if execution_stats else None
        }

        with open(json_file, 'w') as f:
            json.dump(data, f, indent=2)

        print(f"✓ Data saved to: {json_file}")
        return json_file

    def _serialize_materials(self, material_assignments: Dict[str, Any]) -> Dict:
        """Serialize material assignments to JSON-compatible format."""
        serialized = {}

        for comp_type, assignment in material_assignments.items():
            if comp_type == 'heat_sources':
                serialized[comp_type] = assignment
                continue

            material = assignment.get('material')
            if material:
                serialized[comp_type] = {
                    'material_key': assignment.get('material_key'),
                    'material_name': material.name,
                    'thermal_conductivity': material.thermal_conductivity,
                    'density': material.density,
                    'heat_capacity': material.heat_capacity,
                    'emissivity': material.emissivity,
                    'opacity': material.opacity,
                    'default_temperature': material.default_temperature,
                    'component_count': assignment.get('component_count', 0)
                }

        return serialized

    def _serialize_execution_stats(self, stats: ExecutionStats) -> Dict:
        """Serialize execution stats to JSON-compatible format."""
        return {
            'compilation_time': stats.compilation_time,
            'execution_time': stats.execution_time,
            'mesh_elements': stats.mesh_elements,
            'solution_time': stats.solution_time,
            'solver_iterations': stats.solver_iterations,
            'memory_used_mb': stats.memory_used_mb,
            'success': stats.success,
            'error_message': stats.error_message,
            'output_file': stats.output_file
        }


if __name__ == '__main__':
    # Test report generation
    from materials import material_library

    test_stl_analysis = {
        'bounding_box': {
            'width': 5.83,
            'depth': 6.51,
            'height': 2.7,
            'volume': 102.5
        },
        'mesh_statistics': {
            'triangle_count': 50000,
            'vertex_count': 25000,
            'surface_area': 185.3
        },
        'identified_components': {
            'wall': {'count': 4, 'total_area': 95.2},
            'floor': {'count': 1, 'total_area': 37.9},
            'ceiling': {'count': 1, 'total_area': 37.9}
        }
    }

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

    test_suggestions = {
        'analysis_summary': 'Standard residential room with typical dimensions.'
    }

    test_stats = ExecutionStats(
        compilation_time=2.5,
        execution_time=68.0,
        mesh_elements=254446,
        solution_time=43.0,
        memory_used_mb=1720,
        success=True
    )

    generator = ReportGenerator()
    report_path = generator.generate_report(
        "/path/to/test.stl",
        test_stl_analysis,
        test_assignments,
        test_suggestions,
        test_stats
    )

    print(f"\nTest report generated: {report_path}")
