# STL to COMSOL Heat Transfer Simulation Tool

Automatically convert STL files to COMSOL Multiphysics heat transfer simulations using AI-assisted geometry analysis and material assignment.

## Features

- **Automated STL Analysis**: Analyzes 3D geometry and identifies components (walls, floors, ceilings, furniture, etc.)
- **AI-Powered Material Assignment**: Uses LLMs (Claude, OpenAI) to suggest appropriate materials based on geometry
- **Interactive Configuration**: Review and modify AI suggestions with simple multiple-choice interface
- **Heat Source Detection**: Automatically identifies potential heat sources or suggests fallback locations
- **COMSOL Java Generation**: Creates complete, compilable COMSOL Java code
- **Automatic Execution**: Optionally compile and run simulations with real-time progress monitoring
- **Error Handling**: Automatic retry with AI-powered error fixes for compilation issues
- **Comprehensive Reports**: Generate markdown and JSON reports with all simulation details

## Requirements

- Python 3.8 or higher
- COMSOL Multiphysics 6.x installed
- API key for Claude (Anthropic) or OpenAI

## Installation

1. Clone or download this repository

2. Install Python dependencies:
```bash
pip install -r requirements.txt
```

3. Set up your API key (choose one):

For Claude:
```bash
export ANTHROPIC_API_KEY="your-api-key-here"
```

For OpenAI:
```bash
export OPENAI_API_KEY="your-api-key-here"
```

4. Ensure COMSOL is installed at a standard location or set `COMSOL_ROOT` environment variable:
```bash
export COMSOL_ROOT="/Applications/COMSOL63"  # or your installation path
```

## Usage

### Basic Usage

```bash
python stl_to_comsol.py room.stl
```

This will:
1. Analyze the STL geometry
2. Get AI suggestions for materials
3. Let you review and configure materials interactively
4. Generate COMSOL Java code
5. Ask if you want to run the simulation

### Auto-Accept AI Suggestions

```bash
python stl_to_comsol.py room.stl --auto-accept
```

Skip the interactive review and automatically accept all AI-suggested materials.

### Automatically Run Simulation

```bash
python stl_to_comsol.py room.stl --auto-run
```

Automatically compile and execute the generated code without prompting.

### Full Automation

```bash
python stl_to_comsol.py room.stl --auto-accept --auto-run
```

Complete hands-off workflow from STL to simulation results.

### Specify Output Directory

```bash
python stl_to_comsol.py room.stl -o ./simulations/room1
```

### Use OpenAI Instead of Claude

```bash
python stl_to_comsol.py room.stl --provider openai
```

### Specify COMSOL Installation

```bash
python stl_to_comsol.py room.stl --comsol-root /Applications/COMSOL63
```

## Complete Options

```
usage: stl_to_comsol.py [-h] [-o OUTPUT] [--provider {claude,openai}]
                        [--api-key API_KEY] [--model MODEL] [--auto-accept]
                        [--auto-run] [--comsol-root COMSOL_ROOT] stl_file

positional arguments:
  stl_file              Path to STL file

optional arguments:
  -h, --help            Show this help message and exit
  -o OUTPUT, --output OUTPUT
                        Output directory for generated files
  --provider {claude,openai}
                        LLM provider (default: claude)
  --api-key API_KEY     API key for LLM provider
  --model MODEL         Specific LLM model to use
  --auto-accept         Automatically accept all AI suggestions
  --auto-run            Automatically compile and run the simulation
  --comsol-root COMSOL_ROOT
                        Path to COMSOL installation directory
```

## Output Files

The tool generates several files:

1. **`<model_name>.java`** - Generated COMSOL Java source code
2. **`<model_name>.class`** - Compiled Java class (if executed)
3. **`<model_name>_results.mph`** - COMSOL model file with results (if executed)
4. **`simulation_report_<timestamp>.md`** - Comprehensive markdown report
5. **`simulation_data_<timestamp>.json`** - Machine-readable JSON data

## Workflow Details

### 1. STL Analysis

The tool analyzes your STL file to extract:
- Bounding box dimensions (width, depth, height)
- Surface area and mesh statistics
- Component identification using heuristics:
  - **Floor**: Horizontal surfaces at bottom
  - **Ceiling**: Horizontal surfaces at top
  - **Walls**: Vertical surfaces spanning full height
  - **Windows**: Partial vertical surfaces in mid-upper regions
  - **Doors**: Partial vertical surfaces near floor
  - **Furniture**: Other enclosed volumes

### 2. AI Material Analysis

The LLM analyzes the geometry and suggests:
- Appropriate materials for each component type
- Reasoning for material choices
- Confidence levels for suggestions
- Heat source identification
- Fallback heat source if none detected

### 3. User Configuration

Review and modify materials through simple multiple-choice interface:
- Accept AI suggestion (press 0 or Enter)
- Choose alternative material (press 1-N)
- Heat source confirmation

### 4. Code Generation

Generates complete COMSOL Java code including:
- STL geometry import
- Material property assignments
- Heat transfer physics setup
- Surface-to-surface radiation
- Mesh configuration
- Stationary solver
- Result visualization

### 5. Compilation & Execution

If enabled:
- Compiles Java code using COMSOL compiler
- Retries up to 3 times with AI-powered fixes on errors
- Executes simulation with real-time progress output
- Captures statistics (mesh elements, solution time, memory usage)

### 6. Report Generation

Creates comprehensive reports with:
- Geometry analysis summary
- Material assignments and properties
- Simulation execution statistics
- Generated file locations
- Key progress milestones

## Material Library

The tool includes predefined materials:

**Walls:** Concrete, Drywall, Brick
**Floors:** Wood, Ceramic Tile, Concrete
**Ceilings:** Ceiling Tile, Concrete
**Windows:** Glass, Double Pane Glass
**Doors:** Wood, Metal
**Furniture:** Wood, Fabric, Metal
**Heat Sources:** Stove (Metal), Radiator, Heating Element

Each material includes:
- Thermal conductivity
- Density
- Heat capacity
- Emissivity
- Opacity
- Default temperature

## Troubleshooting

### "COMSOL installation not found"

Set the COMSOL_ROOT environment variable or use `--comsol-root`:
```bash
export COMSOL_ROOT="/path/to/COMSOL"
```

### "API key not provided"

Set your API key as an environment variable:
```bash
export ANTHROPIC_API_KEY="your-key"  # For Claude
# or
export OPENAI_API_KEY="your-key"     # For OpenAI
```

### "Failed to load STL file"

Ensure your STL file is valid (binary or ASCII format). Try opening it in a 3D viewer first.

### Compilation errors persist after retries

The generated Java code may need manual fixes:
1. Review the generated `.java` file
2. Check COMSOL API compatibility
3. Manually compile with: `/path/to/comsol compile file.java`

## Examples

### Example 1: Residential Room

```bash
python stl_to_comsol.py living_room.stl --auto-accept --auto-run -o ./results/living_room
```

Output:
- Identifies walls, floor, ceiling
- Assigns drywall, wood floor, concrete ceiling
- Places heating element on wall
- Generates ~250k element mesh
- Solves in ~45 seconds

### Example 2: Commercial Space

```bash
python stl_to_comsol.py office.stl -o ./results/office
```

Interactive review:
- AI suggests concrete walls, tile floor
- User selects drywall instead for walls
- AI identifies HVAC system as heat source
- User confirms configuration
- Manual execution decision

## Advanced Usage

### Custom Material Properties

Edit `materials.py` to add new materials:

```python
materials['custom_wall'] = Material(
    name='Custom Wall',
    description='My custom material',
    thermal_conductivity=1.2,
    density=2000,
    heat_capacity=900,
    emissivity=0.85,
    opacity=1.0,
    default_temperature=293.15,
    category='wall'
)
```

### Direct Module Usage

Import modules for custom workflows:

```python
from stl_analyzer import STLAnalyzer
from materials import MaterialLibrary
from llm_interface import LLMInterface

# Custom analysis pipeline
analyzer = STLAnalyzer("room.stl")
analyzer.load_stl()
analysis = analyzer.analyze()

# Use LLM for suggestions
llm = LLMInterface(provider="claude")
suggestions = llm.analyze_stl_and_suggest_materials(
    analyzer.get_summary(),
    material_library.get_all_materials()
)
```

## Known Limitations

- STL must represent a single enclosed space (not multiple separate rooms)
- Heat source placement requires some geometry assumptions
- Material assignment is heuristic-based and may need manual review
- Large STL files (>1M triangles) may slow analysis
- Requires COMSOL license to execute generated code

## Contributing

Suggestions and improvements welcome! Key areas for contribution:
- Additional material types
- Improved geometry classification heuristics
- Support for more LLM providers
- Enhanced heat source detection
- Multi-room support

## License

This tool is provided as-is for educational and research purposes.

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review generated reports for error details
3. Examine COMSOL log files in `~/Library/Preferences/COMSOL/v63/logs/` (macOS)
4. Verify COMSOL API compatibility with your version

---

**Note**: This tool generates COMSOL Java code but does not include COMSOL Multiphysics software itself. You must have a valid COMSOL license to execute the generated simulations.
