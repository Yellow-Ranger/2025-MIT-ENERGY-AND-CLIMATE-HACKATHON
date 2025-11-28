"""
User Configuration Module

Provides interactive configuration interface for reviewing and modifying
LLM-suggested material assignments.
"""

from typing import Dict, List, Any, Optional
from materials import MaterialLibrary, Material


class UserConfig:
    """Interactive configuration for material assignments."""

    def __init__(self, material_library: MaterialLibrary):
        self.material_library = material_library

    def review_and_configure(self, llm_suggestions: Dict[str, Any],
                            component_counts: Dict[str, int]) -> Dict[str, Any]:
        """
        Present LLM suggestions to user and allow modifications.

        Args:
            llm_suggestions: LLM's suggested material assignments
            component_counts: Number of each component type found

        Returns:
            Finalized material assignments
        """
        print("\n" + "="*70)
        print("MATERIAL ASSIGNMENT REVIEW")
        print("="*70)

        print("\nThe AI has analyzed your STL file and suggests the following materials:")
        print("\nYou can accept the suggestions or choose different materials.")

        final_assignments = {}

        # Process each component type
        material_assignments = llm_suggestions.get('material_assignments', {})

        for component_type, suggestion in material_assignments.items():
            count = component_counts.get(component_type, 0)
            if count == 0:
                continue

            print(f"\n{'-'*70}")
            print(f"Component: {component_type.upper()} ({count} found)")
            print(f"{'-'*70}")

            suggested_material_key = suggestion.get('suggested_material')
            reasoning = suggestion.get('reasoning', 'No reasoning provided')
            confidence = suggestion.get('confidence', 'unknown')

            suggested_material = self.material_library.get_material(suggested_material_key)

            if suggested_material:
                print(f"\nAI Suggestion: {suggested_material.name}")
                print(f"  Description: {suggested_material.description}")
                print(f"  Reasoning: {reasoning}")
                print(f"  Confidence: {confidence}")
                print(f"  Thermal Conductivity: {suggested_material.thermal_conductivity} W/(m·K)")
                print(f"  Emissivity: {suggested_material.emissivity}")

                # Ask user to accept or choose alternative
                choice = self._get_material_choice(component_type, suggested_material_key)

                final_assignments[component_type] = {
                    'material_key': choice,
                    'material': self.material_library.get_material(choice),
                    'component_count': count
                }
            else:
                print(f"\nWarning: Suggested material '{suggested_material_key}' not found in library")
                choice = self._get_material_choice(component_type, None)

                final_assignments[component_type] = {
                    'material_key': choice,
                    'material': self.material_library.get_material(choice),
                    'component_count': count
                }

        # Handle heat sources
        print(f"\n{'='*70}")
        print("HEAT SOURCE CONFIGURATION")
        print(f"{'='*70}")

        heat_sources = llm_suggestions.get('heat_sources', [])
        fallback_heat_source = llm_suggestions.get('fallback_heat_source')

        if heat_sources:
            print(f"\nThe AI identified {len(heat_sources)} potential heat source(s):")
            for i, hs in enumerate(heat_sources, 1):
                print(f"\n{i}. {hs.get('component_type', 'Unknown')}")
                print(f"   Reasoning: {hs.get('reasoning', 'N/A')}")

            use_identified = self._get_yes_no("Use the identified heat sources?", default=True)

            if use_identified:
                final_assignments['heat_sources'] = heat_sources
            else:
                final_assignments['heat_sources'] = [fallback_heat_source] if fallback_heat_source else []
        else:
            print("\nNo obvious heat sources found in the geometry.")
            if fallback_heat_source:
                print(f"AI suggests using a fallback heat source:")
                print(f"  Location: {fallback_heat_source.get('location', 'wall')}")
                print(f"  Position: {fallback_heat_source.get('position', {})}")
                print(f"  Temperature: {fallback_heat_source.get('temperature', 323.15)} K")

                use_fallback = self._get_yes_no("Use this fallback heat source?", default=True)

                if use_fallback:
                    final_assignments['heat_sources'] = [fallback_heat_source]
                else:
                    final_assignments['heat_sources'] = []

        print(f"\n{'='*70}")
        print("Configuration complete!")
        print(f"{'='*70}\n")

        return final_assignments

    def _get_material_choice(self, component_type: str,
                            suggested_key: Optional[str]) -> str:
        """
        Get material choice from user (accept suggestion or pick alternative).

        Args:
            component_type: Type of component (wall, floor, etc.)
            suggested_key: Suggested material key

        Returns:
            Selected material key
        """
        # Get appropriate materials for this component type
        category = self._map_component_to_category(component_type)
        available_materials = self.material_library.list_materials_by_category(category)

        if not available_materials:
            # Fallback to all materials
            available_materials = list(self.material_library.get_all_materials().items())

        print("\nOptions:")
        print("  0. Accept AI suggestion")

        for i, (key, material) in enumerate(available_materials, 1):
            marker = " (SUGGESTED)" if key == suggested_key else ""
            print(f"  {i}. {material.name}{marker}")

        while True:
            try:
                choice_input = input("\nYour choice (0-{}, or press Enter for 0): ".format(len(available_materials)))

                if choice_input.strip() == "":
                    choice_input = "0"

                choice = int(choice_input)

                if choice == 0:
                    return suggested_key if suggested_key else available_materials[0][0]
                elif 1 <= choice <= len(available_materials):
                    return available_materials[choice - 1][0]
                else:
                    print(f"Invalid choice. Please enter 0-{len(available_materials)}")
            except ValueError:
                print("Invalid input. Please enter a number.")
            except KeyboardInterrupt:
                print("\nUsing suggested material (interrupted)")
                return suggested_key if suggested_key else available_materials[0][0]

    def _get_yes_no(self, question: str, default: bool = True) -> bool:
        """Get yes/no answer from user."""
        default_str = "Y/n" if default else "y/N"
        while True:
            try:
                answer = input(f"{question} [{default_str}]: ").strip().lower()

                if answer == "":
                    return default
                elif answer in ['y', 'yes']:
                    return True
                elif answer in ['n', 'no']:
                    return False
                else:
                    print("Please answer 'y' or 'n'")
            except KeyboardInterrupt:
                print(f"\nUsing default: {'yes' if default else 'no'}")
                return default

    def _map_component_to_category(self, component_type: str) -> str:
        """Map component type to material category."""
        mapping = {
            'wall': 'wall',
            'walls': 'wall',
            'floor': 'floor',
            'floors': 'floor',
            'ceiling': 'ceiling',
            'ceilings': 'ceiling',
            'window': 'window',
            'windows': 'window',
            'door': 'door',
            'doors': 'door',
            'furniture': 'furniture',
            'heat_source': 'heat_source',
        }

        return mapping.get(component_type.lower(), component_type.lower())

    def get_auto_accept_config(self, llm_suggestions: Dict[str, Any],
                               component_counts: Dict[str, int]) -> Dict[str, Any]:
        """
        Automatically accept all LLM suggestions without user interaction.

        Args:
            llm_suggestions: LLM's suggested material assignments
            component_counts: Number of each component type found

        Returns:
            Material assignments (auto-accepted)
        """
        print("\n" + "="*70)
        print("AUTO-ACCEPTING AI MATERIAL SUGGESTIONS")
        print("="*70)

        final_assignments = {}
        material_assignments = llm_suggestions.get('material_assignments', {})

        for component_type, suggestion in material_assignments.items():
            count = component_counts.get(component_type, 0)
            if count == 0:
                continue

            material_key = suggestion.get('suggested_material')
            material = self.material_library.get_material(material_key)

            if material:
                print(f"  {component_type}: {material.name}")
                final_assignments[component_type] = {
                    'material_key': material_key,
                    'material': material,
                    'component_count': count
                }

        # Auto-accept heat sources
        heat_sources = llm_suggestions.get('heat_sources', [])
        if heat_sources:
            print(f"\n  Heat sources: {len(heat_sources)} identified")
            final_assignments['heat_sources'] = heat_sources
        else:
            fallback = llm_suggestions.get('fallback_heat_source')
            if fallback:
                print(f"\n  Using fallback heat source")
                final_assignments['heat_sources'] = [fallback]
            else:
                final_assignments['heat_sources'] = []

        print("="*70 + "\n")

        return final_assignments


if __name__ == '__main__':
    # Test the user configuration
    from materials import MaterialLibrary

    lib = MaterialLibrary()
    config = UserConfig(lib)

    # Test data
    test_suggestions = {
        'material_assignments': {
            'wall': {
                'suggested_material': 'drywall',
                'reasoning': 'Standard interior walls',
                'confidence': 'high'
            },
            'floor': {
                'suggested_material': 'wood_floor',
                'reasoning': 'Residential setting',
                'confidence': 'medium'
            }
        },
        'heat_sources': [],
        'fallback_heat_source': {
            'location': 'wall',
            'position': {'x': 2.5, 'y': 0.5, 'z': 1.0},
            'suggested_material': 'heating_element',
            'temperature': 323.15
        }
    }

    test_counts = {
        'wall': 4,
        'floor': 1,
        'ceiling': 1
    }

    result = config.review_and_configure(test_suggestions, test_counts)
    print("\nFinal configuration:")
    print(result)
