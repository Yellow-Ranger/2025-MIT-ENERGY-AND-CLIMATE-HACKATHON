"""
LLM Interface Module

Provides an LLM-agnostic interface for interacting with various language models
to generate COMSOL Java code from STL analysis.
"""

import os
import time
import json
from abc import ABC, abstractmethod
from typing import Dict, Any, Optional, List
from dataclasses import dataclass


@dataclass
class LLMResponse:
    """Response from an LLM."""
    content: str
    success: bool
    error_message: Optional[str] = None
    tokens_used: Optional[int] = None


class LLMProvider(ABC):
    """Abstract base class for LLM providers."""

    def __init__(self, api_key: Optional[str] = None, model: Optional[str] = None):
        self.api_key = api_key
        self.model = model
        self.max_retries = 3
        self.retry_delay = 2  # seconds

    @abstractmethod
    def generate(self, prompt: str, system_prompt: Optional[str] = None) -> LLMResponse:
        """Generate a response from the LLM."""
        pass

    def generate_with_retry(self, prompt: str, system_prompt: Optional[str] = None) -> LLMResponse:
        """Generate with exponential backoff retry logic."""
        for attempt in range(self.max_retries):
            try:
                response = self.generate(prompt, system_prompt)
                if response.success:
                    return response

                # If not successful, retry
                if attempt < self.max_retries - 1:
                    wait_time = self.retry_delay * (2 ** attempt)
                    print(f"Attempt {attempt + 1} failed. Retrying in {wait_time}s...")
                    time.sleep(wait_time)
                else:
                    return response

            except Exception as e:
                if attempt < self.max_retries - 1:
                    wait_time = self.retry_delay * (2 ** attempt)
                    print(f"Error: {e}. Retrying in {wait_time}s...")
                    time.sleep(wait_time)
                else:
                    return LLMResponse(
                        content="",
                        success=False,
                        error_message=f"Max retries exceeded. Last error: {str(e)}"
                    )

        return LLMResponse(
            content="",
            success=False,
            error_message="Max retries exceeded"
        )


class ClaudeProvider(LLMProvider):
    """Anthropic Claude LLM provider."""

    def __init__(self, api_key: Optional[str] = None, model: Optional[str] = None):
        super().__init__(api_key, model or "claude-3-5-sonnet-20241022")
        self.api_key = api_key or os.getenv("ANTHROPIC_API_KEY")

        if not self.api_key:
            raise ValueError("Anthropic API key not provided. Set ANTHROPIC_API_KEY environment variable.")

    def generate(self, prompt: str, system_prompt: Optional[str] = None) -> LLMResponse:
        """Generate using Claude API."""
        try:
            import anthropic

            client = anthropic.Anthropic(api_key=self.api_key)

            messages = [
                {"role": "user", "content": prompt}
            ]

            kwargs = {
                "model": self.model,
                "max_tokens": 8000,
                "messages": messages
            }

            if system_prompt:
                kwargs["system"] = system_prompt

            response = client.messages.create(**kwargs)

            return LLMResponse(
                content=response.content[0].text,
                success=True,
                tokens_used=response.usage.input_tokens + response.usage.output_tokens
            )

        except Exception as e:
            return LLMResponse(
                content="",
                success=False,
                error_message=str(e)
            )


class OpenAIProvider(LLMProvider):
    """OpenAI GPT LLM provider."""

    def __init__(self, api_key: Optional[str] = None, model: Optional[str] = None):
        super().__init__(api_key, model or "gpt-4-turbo-preview")
        self.api_key = api_key or os.getenv("OPENAI_API_KEY")

        if not self.api_key:
            raise ValueError("OpenAI API key not provided. Set OPENAI_API_KEY environment variable.")

    def generate(self, prompt: str, system_prompt: Optional[str] = None) -> LLMResponse:
        """Generate using OpenAI API."""
        try:
            import openai

            client = openai.OpenAI(api_key=self.api_key)

            messages = []
            if system_prompt:
                messages.append({"role": "system", "content": system_prompt})
            messages.append({"role": "user", "content": prompt})

            response = client.chat.completions.create(
                model=self.model,
                messages=messages,
                max_tokens=8000
            )

            return LLMResponse(
                content=response.choices[0].message.content,
                success=True,
                tokens_used=response.usage.total_tokens
            )

        except Exception as e:
            return LLMResponse(
                content="",
                success=False,
                error_message=str(e)
            )


class LLMInterface:
    """High-level interface for LLM interactions."""

    def __init__(self, provider: str = "claude", api_key: Optional[str] = None, model: Optional[str] = None):
        """
        Initialize LLM interface.

        Args:
            provider: LLM provider name ("claude", "openai", etc.)
            api_key: API key for the provider
            model: Specific model to use (optional)
        """
        self.provider_name = provider.lower()
        self.llm = self._create_provider(provider, api_key, model)

    def _create_provider(self, provider: str, api_key: Optional[str], model: Optional[str]) -> LLMProvider:
        """Create the appropriate LLM provider."""
        providers = {
            "claude": ClaudeProvider,
            "openai": OpenAIProvider,
        }

        provider_class = providers.get(provider.lower())
        if not provider_class:
            raise ValueError(f"Unknown provider: {provider}. Available: {list(providers.keys())}")

        return provider_class(api_key=api_key, model=model)

    def analyze_stl_and_suggest_materials(self, stl_analysis: Dict[str, Any],
                                         material_library: Dict[str, Any]) -> Dict[str, Any]:
        """
        Analyze STL geometry and suggest material assignments.

        Args:
            stl_analysis: Dictionary containing STL analysis results
            material_library: Dictionary of available materials

        Returns:
            Dictionary with material suggestions and heat source identification
        """
        system_prompt = """You are an expert in building physics and COMSOL Multiphysics simulations.
Your task is to analyze 3D room geometry from STL files and suggest appropriate material assignments
for heat transfer and radiation simulations."""

        prompt = f"""I have analyzed an STL file representing a 3D room/building interior.

STL Analysis Results:
{json.dumps(stl_analysis, indent=2)}

Available Materials:
{json.dumps(material_library, indent=2)}

Please analyze this geometry and provide:

1. Material assignments for each identified component
   - For each component type (wall, floor, ceiling, furniture, etc.), suggest the most appropriate material
   - Consider the position and context when making suggestions
   - Explain your reasoning briefly

2. Heat source identification
   - Identify any components that could serve as heat sources (stoves, radiators, heaters, etc.)
   - If you find potential heat sources, specify which components and suggest temperatures
   - If no obvious heat sources are found, suggest a random location on a wall to place a small heating element

3. Return your analysis as a JSON object with this structure:
{{
  "material_assignments": {{
    "component_type": {{
      "suggested_material": "material_key",
      "reasoning": "brief explanation",
      "confidence": "high/medium/low"
    }}
  }},
  "heat_sources": [
    {{
      "component_type": "type",
      "component_index": 0,
      "suggested_material": "material_key",
      "temperature": 473.15,
      "reasoning": "explanation"
    }}
  ],
  "fallback_heat_source": {{
    "location": "wall",
    "position": {{"x": 0.5, "y": 1.5, "z": 1.0}},
    "size": {{"width": 0.3, "height": 0.3, "depth": 0.05}},
    "suggested_material": "heating_element",
    "temperature": 323.15
  }},
  "analysis_summary": "brief summary of the space and your material choices"
}}

Return ONLY the JSON object, no additional text."""

        response = self.llm.generate_with_retry(prompt, system_prompt)

        if not response.success:
            raise Exception(f"LLM failed: {response.error_message}")

        # Parse JSON response
        try:
            # Extract JSON from response (handle potential markdown code blocks)
            content = response.content.strip()
            if content.startswith("```"):
                # Remove markdown code block markers
                lines = content.split('\n')
                content = '\n'.join(lines[1:-1])

            result = json.loads(content)
            return result
        except json.JSONDecodeError as e:
            # If JSON parsing fails, return a structured error
            print(f"Failed to parse LLM response as JSON: {e}")
            print(f"Response content: {response.content}")
            raise Exception(f"LLM returned invalid JSON: {e}")

    def generate_comsol_java(self, stl_path: str, stl_analysis: Dict[str, Any],
                            material_assignments: Dict[str, Any],
                            reference_code: str) -> str:
        """
        Generate COMSOL Java code based on STL analysis and material assignments.

        Args:
            stl_path: Path to the STL file
            stl_analysis: STL geometry analysis
            material_assignments: Material assignment decisions
            reference_code: Reference Java code to use as template

        Returns:
            Generated COMSOL Java code as string
        """
        system_prompt = """You are an expert in COMSOL Multiphysics Java API programming.
Your task is to generate valid COMSOL Java code for heat transfer and radiation simulations."""

        prompt = f"""Generate a COMSOL Multiphysics Java file for analyzing radiative heat flux in a 3D room.

STL File: {stl_path}

Geometry Analysis:
{json.dumps(stl_analysis, indent=2)}

Material Assignments:
{json.dumps(material_assignments, indent=2)}

Reference Code Structure:
{reference_code[:3000]}  # First 3000 chars for reference

Requirements:
1. Import the STL file directly into COMSOL geometry
2. Apply the specified materials to appropriate domains/surfaces
3. Set up heat transfer physics with radiation (Surface-to-Surface Radiation)
4. Configure mesh with appropriate sizing (aim for 200,000-500,000 elements)
5. Add a stationary solver
6. Include result visualization
7. Save the model to a .mph file
8. Add proper exception handling (throws IOException)
9. Follow the same code structure as the reference

Important notes:
- Use ModelUtil.create() to create the model
- Import STL using: model.component("comp1").geom("geom1").create("imp1", "Import")
- Set STL path: .set("filename", "{stl_path}")
- Assign materials using model.component("comp1").material().create()
- Set up physics: model.component("comp1").physics().create("ht", "HeatTransfer", "geom1")
- Add radiation: model.component("comp1").physics("ht").create("rad", "SurfaceToSurfaceRadiation", 2)
- The main class should be named after the STL file

Generate complete, working Java code that can be compiled and run with COMSOL.
Return ONLY the Java code, no explanations or markdown."""

        response = self.llm.generate_with_retry(prompt, system_prompt)

        if not response.success:
            raise Exception(f"LLM failed to generate code: {response.error_message}")

        # Clean up response (remove markdown if present)
        code = response.content.strip()
        if code.startswith("```java"):
            lines = code.split('\n')
            code = '\n'.join(lines[1:-1])
        elif code.startswith("```"):
            lines = code.split('\n')
            code = '\n'.join(lines[1:-1])

        return code

    def fix_compilation_error(self, java_code: str, error_message: str) -> str:
        """
        Fix Java compilation errors using LLM.

        Args:
            java_code: The Java code that failed to compile
            error_message: Compilation error message

        Returns:
            Fixed Java code
        """
        system_prompt = """You are an expert in COMSOL Multiphysics Java API and debugging.
Your task is to fix compilation errors in COMSOL Java code."""

        prompt = f"""The following COMSOL Java code failed to compile with this error:

ERROR:
{error_message}

JAVA CODE:
{java_code}

Please fix the compilation error and return the corrected Java code.
Make minimal changes - only fix what's broken.
Return ONLY the complete corrected Java code, no explanations."""

        response = self.llm.generate_with_retry(prompt, system_prompt)

        if not response.success:
            raise Exception(f"LLM failed to fix error: {response.error_message}")

        # Clean up response
        code = response.content.strip()
        if code.startswith("```java"):
            lines = code.split('\n')
            code = '\n'.join(lines[1:-1])
        elif code.startswith("```"):
            lines = code.split('\n')
            code = '\n'.join(lines[1:-1])

        return code


if __name__ == '__main__':
    # Test the LLM interface
    import sys

    provider = sys.argv[1] if len(sys.argv) > 1 else "claude"

    try:
        llm = LLMInterface(provider=provider)
        print(f"Successfully initialized {provider} provider")

        # Simple test
        test_analysis = {
            "bounding_box": {"width": 5.0, "depth": 6.0, "height": 2.7},
            "identified_components": {
                "floor": {"count": 1},
                "ceiling": {"count": 1},
                "wall": {"count": 4}
            }
        }

        test_materials = {
            "drywall": {"category": "wall"},
            "wood_floor": {"category": "floor"}
        }

        print("\nTesting material suggestion...")
        result = llm.analyze_stl_and_suggest_materials(test_analysis, test_materials)
        print(json.dumps(result, indent=2))

    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)
