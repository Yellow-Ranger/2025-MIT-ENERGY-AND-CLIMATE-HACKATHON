#!/usr/bin/env python3
"""
STL to COMSOL Heat Transfer Simulation Tool

Main entry point for converting STL files to COMSOL heat transfer simulations
using AI-assisted material assignment and geometry analysis.
"""

import argparse
import sys
import json
from pathlib import Path
from typing import Optional

# Import our modules
from stl_analyzer import STLAnalyzer
from materials import MaterialLibrary
from llm_interface import LLMInterface
from user_config import UserConfig
from comsol_generator import COMSOLJavaGenerator
from comsol_executor import COMSOLExecutor
from report_generator import ReportGenerator


class STLToCOMSOL:
    """Main orchestrator for STL to COMSOL conversion."""

    def __init__(self,
                 stl_path: str,
                 output_dir: str = ".",
                 llm_provider: str = "claude",
                 llm_api_key: Optional[str] = None,
                 llm_model: Optional[str] = None,
                 auto_accept: bool = False,
                 auto_run: bool = False,
                 comsol_root: Optional[str] = None):
        """
        Initialize the converter.

        Args:
            stl_path: Path to STL file
            output_dir: Output directory for generated files
            llm_provider: LLM provider ("claude", "openai", etc.)
            llm_api_key: API key for LLM provider
            llm_model: Specific model to use
            auto_accept: Automatically accept all AI suggestions
            auto_run: Automatically compile and run after generation
            comsol_root: Path to COMSOL installation
        """
        self.stl_path = Path(stl_path)
        self.output_dir = Path(output_dir)
        self.auto_accept = auto_accept
        self.auto_run = auto_run

        # Validate STL file exists
        if not self.stl_path.exists():
            raise FileNotFoundError(f"STL file not found: {stl_path}")

        # Create output directory
        self.output_dir.mkdir(parents=True, exist_ok=True)

        # Initialize components
        self.material_library = MaterialLibrary()
        self.llm = LLMInterface(provider=llm_provider, api_key=llm_api_key, model=llm_model)
        self.user_config = UserConfig(self.material_library)
        self.report_generator = ReportGenerator(output_dir=str(self.output_dir))

        # COMSOL executor (initialized only if needed)
        self.comsol_executor = None
        if auto_run:
            self.comsol_executor = COMSOLExecutor(comsol_root=comsol_root)

    def run(self):
        """Execute the complete workflow."""
        print("="*70)
        print("STL TO COMSOL HEAT TRANSFER SIMULATION")
        print("="*70)
        print(f"\nInput STL: {self.stl_path}")
        print(f"Output Directory: {self.output_dir}")
        print()

        try:
            # Step 1: Analyze STL geometry
            print("\n" + "="*70)
            print("STEP 1: ANALYZING STL GEOMETRY")
            print("="*70)

            stl_analysis = self._analyze_stl()

            # Step 2: Get AI suggestions for materials
            print("\n" + "="*70)
            print("STEP 2: AI MATERIAL ANALYSIS")
            print("="*70)

            llm_suggestions = self._get_ai_suggestions(stl_analysis)

            # Step 3: User configuration of materials
            print("\n" + "="*70)
            print("STEP 3: MATERIAL CONFIGURATION")
            print("="*70)

            material_assignments = self._configure_materials(llm_suggestions, stl_analysis)

            # Step 4: Generate COMSOL Java code
            print("\n" + "="*70)
            print("STEP 4: GENERATING COMSOL JAVA CODE")
            print("="*70)

            java_file = self._generate_java_code(stl_analysis, material_assignments)

            # Step 5: Optionally compile and execute
            execution_stats = None
            if self.auto_run or self._ask_to_run():
                print("\n" + "="*70)
                print("STEP 5: COMPILING AND EXECUTING")
                print("="*70)

                execution_stats = self._compile_and_execute(java_file)
            else:
                print("\n✓ Java code generated. You can compile and run it later.")

            # Step 6: Generate report
            print("\n" + "="*70)
            print("STEP 6: GENERATING REPORT")
            print("="*70)

            self._generate_report(stl_analysis, material_assignments,
                                llm_suggestions, execution_stats, java_file)

            # Success message
            print("\n" + "="*70)
            print("✓ WORKFLOW COMPLETED SUCCESSFULLY")
            print("="*70)
            print(f"\nGenerated files in: {self.output_dir}")
            print(f"  - Java source: {java_file.name}")
            if execution_stats:
                print(f"  - Simulation status: {'Success' if execution_stats.success else 'Failed'}")
            print()

        except KeyboardInterrupt:
            print("\n\n✗ Workflow interrupted by user")
            sys.exit(1)
        except Exception as e:
            print(f"\n\n✗ Error: {e}")
            import traceback
            traceback.print_exc()
            sys.exit(1)

    def _analyze_stl(self):
        """Analyze STL geometry."""
        print(f"Loading and analyzing: {self.stl_path.name}")

        analyzer = STLAnalyzer(str(self.stl_path))

        if not analyzer.load_stl():
            raise Exception("Failed to load STL file")

        print(f"  ✓ Loaded {len(analyzer.triangles):,} triangles")

        analysis = analyzer.analyze()

        print(f"  ✓ Bounding box: {analysis.bounding_box}")
        print(f"  ✓ Surface area: {analysis.surface_area:.2f} m²")
        print(f"  ✓ Identified components:")

        for comp_type, surfaces in analysis.identified_components.items():
            print(f"      - {comp_type}: {len(surfaces)}")

        return analyzer.get_summary()

    def _get_ai_suggestions(self, stl_analysis):
        """Get AI suggestions for material assignments."""
        print("Sending geometry data to AI for analysis...")

        # Prepare material library data
        material_data = {}
        for key, material in self.material_library.get_all_materials().items():
            material_data[key] = {
                'name': material.name,
                'category': material.category,
                'description': material.description,
                'thermal_conductivity': material.thermal_conductivity,
                'emissivity': material.emissivity
            }

        # Get suggestions from LLM
        suggestions = self.llm.analyze_stl_and_suggest_materials(
            stl_analysis,
            material_data
        )

        print("  ✓ AI analysis complete")
        print(f"  Summary: {suggestions.get('analysis_summary', 'N/A')}")

        return suggestions

    def _configure_materials(self, llm_suggestions, stl_analysis):
        """Configure materials (interactive or auto-accept)."""
        # Get component counts
        component_counts = {}
        for comp_type, info in stl_analysis.get('identified_components', {}).items():
            component_counts[comp_type] = info.get('count', 0)

        if self.auto_accept:
            print("Auto-accepting AI suggestions...")
            return self.user_config.get_auto_accept_config(
                llm_suggestions,
                component_counts
            )
        else:
            return self.user_config.review_and_configure(
                llm_suggestions,
                component_counts
            )

    def _generate_java_code(self, stl_analysis, material_assignments):
        """Generate COMSOL Java code."""
        print("Generating COMSOL Java code...")

        generator = COMSOLJavaGenerator(
            stl_path=str(self.stl_path),
            output_dir=str(self.output_dir)
        )

        code = generator.generate(stl_analysis, material_assignments)
        java_file = generator.save(code)

        print(f"  ✓ Generated: {java_file}")
        print(f"  ✓ Class name: {generator.class_name}")
        print(f"  ✓ Lines of code: {len(code.splitlines())}")

        return java_file

    def _ask_to_run(self) -> bool:
        """Ask user if they want to run the simulation now."""
        print("\nWould you like to compile and run the simulation now?")
        print("  (You can also run it later manually)")

        while True:
            try:
                answer = input("Run now? [y/N]: ").strip().lower()
                if answer == "" or answer == "n" or answer == "no":
                    return False
                elif answer == "y" or answer == "yes":
                    return True
                else:
                    print("Please answer 'y' or 'n'")
            except KeyboardInterrupt:
                print("\nSkipping execution")
                return False

    def _compile_and_execute(self, java_file):
        """Compile and execute the Java file."""
        if not self.comsol_executor:
            self.comsol_executor = COMSOLExecutor()

        # Define error fix callback
        def fix_error(code, error):
            return self.llm.fix_compilation_error(code, error)

        # Compile and execute with retry logic
        success, stats, final_code = self.comsol_executor.compile_and_execute(
            java_file,
            retry_on_error=True,
            max_retries=3,
            error_fix_callback=fix_error
        )

        if not success:
            print(f"\n✗ Compilation failed: {stats.error_message}")
            return stats

        return stats

    def _generate_report(self, stl_analysis, material_assignments,
                        llm_suggestions, execution_stats, java_file):
        """Generate summary report."""
        print("Generating summary report...")

        report_path = self.report_generator.generate_report(
            stl_path=str(self.stl_path),
            stl_analysis=stl_analysis,
            material_assignments=material_assignments,
            llm_suggestions=llm_suggestions,
            execution_stats=execution_stats,
            java_file=java_file
        )

        # Also save JSON data
        json_path = self.report_generator.save_json_data(
            stl_analysis,
            material_assignments,
            execution_stats
        )

        print(f"  ✓ Markdown report: {report_path.name}")
        print(f"  ✓ JSON data: {json_path.name}")


def main():
    """Main entry point."""
    parser = argparse.ArgumentParser(
        description="Convert STL files to COMSOL heat transfer simulations using AI",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Basic usage with Claude
  python stl_to_comsol.py room.stl

  # Use OpenAI with auto-run
  python stl_to_comsol.py room.stl --provider openai --auto-run

  # Auto-accept AI suggestions and run
  python stl_to_comsol.py room.stl --auto-accept --auto-run

  # Specify output directory
  python stl_to_comsol.py room.stl -o ./simulations/room1
        """
    )

    parser.add_argument('stl_file', help='Path to STL file')
    parser.add_argument('-o', '--output', default='.',
                       help='Output directory for generated files (default: current directory)')
    parser.add_argument('--provider', default='claude',
                       choices=['claude', 'openai'],
                       help='LLM provider (default: claude)')
    parser.add_argument('--api-key', help='API key for LLM provider (or use environment variable)')
    parser.add_argument('--model', help='Specific LLM model to use')
    parser.add_argument('--auto-accept', action='store_true',
                       help='Automatically accept all AI suggestions without user review')
    parser.add_argument('--auto-run', action='store_true',
                       help='Automatically compile and run the simulation')
    parser.add_argument('--comsol-root', help='Path to COMSOL installation directory')

    args = parser.parse_args()

    # Create and run converter
    converter = STLToCOMSOL(
        stl_path=args.stl_file,
        output_dir=args.output,
        llm_provider=args.provider,
        llm_api_key=args.api_key,
        llm_model=args.model,
        auto_accept=args.auto_accept,
        auto_run=args.auto_run,
        comsol_root=args.comsol_root
    )

    converter.run()


if __name__ == '__main__':
    main()
