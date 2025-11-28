"""
Material Property Library for COMSOL Heat Transfer Simulations

This module defines thermal and radiative properties for common building materials,
furniture, and heat sources used in COMSOL Multiphysics simulations.
"""

from dataclasses import dataclass
from typing import Dict, List, Optional


@dataclass
class Material:
    """Represents a material with thermal and radiative properties."""
    name: str
    description: str
    thermal_conductivity: float  # W/(m·K)
    density: float  # kg/m³
    heat_capacity: float  # J/(kg·K)
    emissivity: float  # 0-1
    opacity: float  # 0 (transparent) - 1 (opaque)
    default_temperature: float  # K (Kelvin)
    category: str  # wall, floor, ceiling, furniture, window, door, heat_source

    def __str__(self):
        return f"{self.name} - {self.description}"


class MaterialLibrary:
    """Library of predefined materials for building simulations."""

    def __init__(self):
        self.materials = self._initialize_materials()

    def _initialize_materials(self) -> Dict[str, Material]:
        """Initialize the material library with common building materials."""
        materials = {}

        # Wall Materials
        materials['concrete_wall'] = Material(
            name='Concrete Wall',
            description='Standard concrete wall material',
            thermal_conductivity=1.4,
            density=2300,
            heat_capacity=880,
            emissivity=0.93,
            opacity=1.0,
            default_temperature=293.15,  # 20°C
            category='wall'
        )

        materials['drywall'] = Material(
            name='Drywall',
            description='Gypsum board interior wall',
            thermal_conductivity=0.17,
            density=800,
            heat_capacity=1090,
            emissivity=0.90,
            opacity=1.0,
            default_temperature=293.15,
            category='wall'
        )

        materials['brick'] = Material(
            name='Brick',
            description='Clay brick wall',
            thermal_conductivity=0.72,
            density=1920,
            heat_capacity=840,
            emissivity=0.93,
            opacity=1.0,
            default_temperature=293.15,
            category='wall'
        )

        # Floor Materials
        materials['wood_floor'] = Material(
            name='Wood Floor',
            description='Hardwood flooring',
            thermal_conductivity=0.14,
            density=700,
            heat_capacity=1380,
            emissivity=0.90,
            opacity=1.0,
            default_temperature=293.15,
            category='floor'
        )

        materials['tile_floor'] = Material(
            name='Ceramic Tile',
            description='Ceramic tile flooring',
            thermal_conductivity=1.3,
            density=2300,
            heat_capacity=840,
            emissivity=0.94,
            opacity=1.0,
            default_temperature=293.15,
            category='floor'
        )

        materials['concrete_floor'] = Material(
            name='Concrete Floor',
            description='Concrete slab flooring',
            thermal_conductivity=1.4,
            density=2300,
            heat_capacity=880,
            emissivity=0.93,
            opacity=1.0,
            default_temperature=293.15,
            category='floor'
        )

        # Ceiling Materials
        materials['ceiling_tile'] = Material(
            name='Ceiling Tile',
            description='Acoustic ceiling tile',
            thermal_conductivity=0.06,
            density=300,
            heat_capacity=840,
            emissivity=0.90,
            opacity=1.0,
            default_temperature=293.15,
            category='ceiling'
        )

        materials['concrete_ceiling'] = Material(
            name='Concrete Ceiling',
            description='Concrete ceiling',
            thermal_conductivity=1.4,
            density=2300,
            heat_capacity=880,
            emissivity=0.93,
            opacity=1.0,
            default_temperature=293.15,
            category='ceiling'
        )

        # Window Materials
        materials['glass_window'] = Material(
            name='Glass Window',
            description='Standard window glass',
            thermal_conductivity=0.96,
            density=2500,
            heat_capacity=840,
            emissivity=0.84,
            opacity=0.1,  # Semi-transparent
            default_temperature=293.15,
            category='window'
        )

        materials['double_pane_glass'] = Material(
            name='Double Pane Glass',
            description='Insulated double pane window',
            thermal_conductivity=0.48,
            density=2500,
            heat_capacity=840,
            emissivity=0.84,
            opacity=0.1,
            default_temperature=293.15,
            category='window'
        )

        # Door Materials
        materials['wood_door'] = Material(
            name='Wood Door',
            description='Solid wood door',
            thermal_conductivity=0.14,
            density=700,
            heat_capacity=1380,
            emissivity=0.90,
            opacity=1.0,
            default_temperature=293.15,
            category='door'
        )

        materials['metal_door'] = Material(
            name='Metal Door',
            description='Steel door',
            thermal_conductivity=45,
            density=7850,
            heat_capacity=460,
            emissivity=0.28,
            opacity=1.0,
            default_temperature=293.15,
            category='door'
        )

        # Furniture Materials
        materials['wood_furniture'] = Material(
            name='Wood Furniture',
            description='Wooden furniture',
            thermal_conductivity=0.14,
            density=700,
            heat_capacity=1380,
            emissivity=0.90,
            opacity=1.0,
            default_temperature=293.15,
            category='furniture'
        )

        materials['fabric_furniture'] = Material(
            name='Fabric Furniture',
            description='Upholstered furniture',
            thermal_conductivity=0.06,
            density=200,
            heat_capacity=1340,
            emissivity=0.92,
            opacity=1.0,
            default_temperature=293.15,
            category='furniture'
        )

        materials['metal_furniture'] = Material(
            name='Metal Furniture',
            description='Metal furniture (steel)',
            thermal_conductivity=45,
            density=7850,
            heat_capacity=460,
            emissivity=0.28,
            opacity=1.0,
            default_temperature=293.15,
            category='furniture'
        )

        # Heat Source Materials
        materials['stove_metal'] = Material(
            name='Stove (Metal)',
            description='Metal stove or heater',
            thermal_conductivity=45,
            density=7850,
            heat_capacity=460,
            emissivity=0.75,
            opacity=1.0,
            default_temperature=473.15,  # 200°C
            category='heat_source'
        )

        materials['radiator'] = Material(
            name='Radiator',
            description='Heating radiator',
            thermal_conductivity=45,
            density=7850,
            heat_capacity=460,
            emissivity=0.92,
            opacity=1.0,
            default_temperature=343.15,  # 70°C
            category='heat_source'
        )

        materials['heating_element'] = Material(
            name='Heating Element',
            description='Electric heating element',
            thermal_conductivity=16,
            density=8900,
            heat_capacity=385,
            emissivity=0.85,
            opacity=1.0,
            default_temperature=523.15,  # 250°C
            category='heat_source'
        )

        # Air (for room volume)
        materials['air'] = Material(
            name='Air',
            description='Room air',
            thermal_conductivity=0.026,
            density=1.2,
            heat_capacity=1005,
            emissivity=0.0,  # Air doesn't emit
            opacity=0.0,  # Transparent
            default_temperature=293.15,
            category='air'
        )

        return materials

    def get_material(self, key: str) -> Optional[Material]:
        """Get a material by its key."""
        return self.materials.get(key)

    def get_materials_by_category(self, category: str) -> List[Material]:
        """Get all materials in a specific category."""
        return [m for m in self.materials.values() if m.category == category]

    def get_all_materials(self) -> Dict[str, Material]:
        """Get all materials in the library."""
        return self.materials

    def list_materials_by_category(self, category: str) -> List[tuple]:
        """Return a list of (key, Material) tuples for a category."""
        return [(k, v) for k, v in self.materials.items() if v.category == category]

    def get_material_choices(self, category: str) -> List[tuple]:
        """Get material choices for interactive selection (index, key, name)."""
        materials = self.list_materials_by_category(category)
        return [(i+1, k, m.name) for i, (k, m) in enumerate(materials)]

    def suggest_material_for_component(self, component_type: str,
                                       position: str = None,
                                       has_heat: bool = False) -> str:
        """Suggest a default material based on component type and context."""
        if has_heat:
            return 'stove_metal'

        suggestions = {
            'wall': 'drywall',
            'floor': 'wood_floor',
            'ceiling': 'concrete_ceiling',
            'window': 'glass_window',
            'door': 'wood_door',
            'furniture': 'wood_furniture',
            'heat_source': 'stove_metal',
        }

        return suggestions.get(component_type.lower(), 'drywall')


# Global material library instance
material_library = MaterialLibrary()


if __name__ == '__main__':
    # Test the material library
    lib = MaterialLibrary()

    print("Available material categories:")
    categories = set(m.category for m in lib.get_all_materials().values())
    for cat in sorted(categories):
        print(f"\n{cat.upper()}:")
        for mat in lib.get_materials_by_category(cat):
            print(f"  - {mat}")
