"""
COMSOL Execution Module

Handles compilation and execution of generated COMSOL Java code,
with real-time progress monitoring and error handling.
"""

import os
import subprocess
import re
import time
from pathlib import Path
from typing import Optional, Tuple, Dict, List
from dataclasses import dataclass, field


@dataclass
class ExecutionStats:
    """Statistics from COMSOL execution."""
    compilation_time: float = 0.0
    execution_time: float = 0.0
    mesh_elements: int = 0
    solution_time: float = 0.0
    solver_iterations: int = 0
    memory_used_mb: float = 0.0
    success: bool = False
    error_message: Optional[str] = None
    output_file: Optional[str] = None
    progress_log: List[str] = field(default_factory=list)


class COMSOLExecutor:
    """Executor for COMSOL Java code."""

    def __init__(self, comsol_root: Optional[str] = None):
        """
        Initialize COMSOL executor.

        Args:
            comsol_root: Path to COMSOL installation (auto-detected if None)
        """
        self.comsol_root = self._find_comsol(comsol_root)
        self.comsol_bin = os.path.join(self.comsol_root, "Multiphysics", "bin", "comsol")

        if not os.path.exists(self.comsol_bin):
            raise FileNotFoundError(f"COMSOL executable not found at {self.comsol_bin}")

    def _find_comsol(self, comsol_root: Optional[str] = None) -> str:
        """Find COMSOL installation directory."""
        if comsol_root and os.path.exists(comsol_root):
            return comsol_root

        # Try common locations
        possible_paths = [
            "/Applications/COMSOL63",
            "/Applications/COMSOL62",
            "/Applications/COMSOL61",
            "/usr/local/comsol63/multiphysics",
            "/usr/local/comsol62/multiphysics",
            "C:\\Program Files\\COMSOL\\COMSOL63\\Multiphysics",
            "C:\\Program Files\\COMSOL\\COMSOL62\\Multiphysics",
        ]

        for path in possible_paths:
            if os.path.exists(path):
                return path

        # Try to find via environment
        comsol_env = os.getenv("COMSOL_ROOT")
        if comsol_env and os.path.exists(comsol_env):
            return comsol_env

        raise FileNotFoundError(
            "COMSOL installation not found. Please specify comsol_root parameter "
            "or set COMSOL_ROOT environment variable."
        )

    def compile(self, java_file: Path, output_dir: Optional[Path] = None) -> Tuple[bool, Optional[str]]:
        """
        Compile COMSOL Java file.

        Args:
            java_file: Path to Java file
            output_dir: Output directory for compiled class

        Returns:
            (success, error_message)
        """
        print(f"\nCompiling {java_file.name}...")

        start_time = time.time()

        # Change to directory containing the Java file
        original_dir = os.getcwd()
        work_dir = output_dir if output_dir else java_file.parent
        os.chdir(work_dir)

        try:
            cmd = [self.comsol_bin, "compile", str(java_file)]

            result = subprocess.run(
                cmd,
                capture_output=True,
                text=True,
                timeout=120
            )

            compilation_time = time.time() - start_time

            if result.returncode == 0:
                print(f"  ✓ Compilation successful ({compilation_time:.1f}s)")
                return True, None
            else:
                error_msg = result.stderr or result.stdout
                print(f"  ✗ Compilation failed")
                return False, error_msg

        except subprocess.TimeoutExpired:
            return False, "Compilation timed out after 120 seconds"
        except Exception as e:
            return False, str(e)
        finally:
            os.chdir(original_dir)

    def execute(self, java_file: Path, class_file: Optional[Path] = None) -> ExecutionStats:
        """
        Execute compiled COMSOL Java class with real-time progress monitoring.

        Args:
            java_file: Path to original Java file (for reference)
            class_file: Path to compiled class file (if None, derived from java_file)

        Returns:
            ExecutionStats object with execution results
        """
        stats = ExecutionStats()

        if class_file is None:
            class_file = java_file.with_suffix('.class')

        if not class_file.exists():
            stats.error_message = f"Compiled class not found: {class_file}"
            return stats

        print(f"\nExecuting {class_file.name}...")
        print("="*70)

        start_time = time.time()

        # Change to directory containing the class file
        original_dir = os.getcwd()
        os.chdir(class_file.parent)

        try:
            cmd = [self.comsol_bin, "batch", "-inputfile", str(class_file)]

            process = subprocess.Popen(
                cmd,
                stdout=subprocess.PIPE,
                stderr=subprocess.STDOUT,
                text=True,
                bufsize=1
            )

            # Monitor output in real-time
            progress_pattern = re.compile(r'Current Progress:\s+(\d+)\s+%\s+-\s+(.*)')
            mesh_pattern = re.compile(r'Number of elements:\s+(\d+)')
            solution_time_pattern = re.compile(r'Solution time:\s+([\d.]+)\s+s')
            memory_pattern = re.compile(r'Physical memory:\s+([\d.]+)\s+([GM]B)')

            for line in process.stdout:
                line = line.rstrip()

                # Store in log
                stats.progress_log.append(line)

                # Display progress
                if "Current Progress:" in line:
                    match = progress_pattern.search(line)
                    if match:
                        percent = match.group(1)
                        task = match.group(2)
                        print(f"  [{percent}%] {task}")

                elif "Number of elements:" in line:
                    match = mesh_pattern.search(line)
                    if match:
                        stats.mesh_elements = int(match.group(1))
                        print(f"  Mesh: {stats.mesh_elements:,} elements")

                elif "Solution time:" in line:
                    match = solution_time_pattern.search(line)
                    if match:
                        stats.solution_time = float(match.group(1))
                        print(f"  Solution time: {stats.solution_time:.1f}s")

                elif "Physical memory:" in line:
                    match = memory_pattern.search(line)
                    if match:
                        value = float(match.group(1))
                        unit = match.group(2)
                        if unit == "GB":
                            stats.memory_used_mb = value * 1024
                        else:
                            stats.memory_used_mb = value
                        print(f"  Memory: {stats.memory_used_mb:.0f} MB")

                elif "Mesh statistics:" in line or "Solving..." in line or "Results generated" in line:
                    print(f"  {line.strip()}")

                elif "ERROR" in line or "Error" in line:
                    print(f"  ✗ {line.strip()}")
                    if not stats.error_message:
                        stats.error_message = line.strip()

                elif "Done" in line or "complete" in line.lower():
                    print(f"  ✓ {line.strip()}")

            process.wait()

            stats.execution_time = time.time() - start_time

            if process.returncode == 0:
                stats.success = True
                print("\n" + "="*70)
                print(f"✓ Execution completed successfully ({stats.execution_time:.1f}s)")
            else:
                stats.success = False
                if not stats.error_message:
                    stats.error_message = f"Execution failed with return code {process.returncode}"
                print("\n" + "="*70)
                print(f"✗ Execution failed")

        except Exception as e:
            stats.error_message = str(e)
            stats.execution_time = time.time() - start_time
            print(f"\n✗ Execution error: {e}")

        finally:
            os.chdir(original_dir)

        return stats

    def compile_and_execute(self, java_file: Path,
                          retry_on_error: bool = True,
                          max_retries: int = 3,
                          error_fix_callback=None) -> Tuple[bool, ExecutionStats, Optional[str]]:
        """
        Compile and execute with automatic retry on errors.

        Args:
            java_file: Path to Java source file
            retry_on_error: Whether to retry on compilation errors
            max_retries: Maximum number of retry attempts
            error_fix_callback: Function(java_code, error) -> fixed_code for fixing errors

        Returns:
            (compilation_success, execution_stats, final_java_code)
        """
        current_java_file = java_file
        attempt = 0

        while attempt < max_retries:
            attempt += 1

            if attempt > 1:
                print(f"\n{'='*70}")
                print(f"Retry attempt {attempt}/{max_retries}")
                print(f"{'='*70}")

            # Compile
            compile_success, compile_error = self.compile(current_java_file)

            if not compile_success:
                print(f"\nCompilation failed: {compile_error}")

                if retry_on_error and error_fix_callback and attempt < max_retries:
                    print("\nAttempting to fix compilation error using LLM...")

                    try:
                        # Read current code
                        with open(current_java_file, 'r') as f:
                            java_code = f.read()

                        # Try to fix
                        fixed_code = error_fix_callback(java_code, compile_error)

                        # Save fixed code
                        fixed_file = current_java_file.with_stem(
                            current_java_file.stem + f"_fixed{attempt}"
                        )
                        with open(fixed_file, 'w') as f:
                            f.write(fixed_code)

                        current_java_file = fixed_file
                        print(f"  Saved fixed code to: {fixed_file}")

                        continue  # Try again with fixed code

                    except Exception as e:
                        print(f"  Failed to fix error: {e}")
                        break
                else:
                    break

            # Execute
            stats = self.execute(current_java_file)

            # Read final code
            with open(current_java_file, 'r') as f:
                final_code = f.read()

            return compile_success, stats, final_code

        # All retries failed
        stats = ExecutionStats()
        stats.error_message = "Compilation failed after all retries"

        with open(current_java_file, 'r') as f:
            final_code = f.read()

        return False, stats, final_code


if __name__ == '__main__':
    # Test the executor
    import sys

    if len(sys.argv) < 2:
        print("Usage: python comsol_executor.py <java_file>")
        sys.exit(1)

    java_file = Path(sys.argv[1])

    executor = COMSOLExecutor()
    success, stats, _ = executor.compile_and_execute(java_file, retry_on_error=False)

    print("\n" + "="*70)
    print("EXECUTION SUMMARY")
    print("="*70)
    print(f"Success: {stats.success}")
    print(f"Mesh elements: {stats.mesh_elements:,}")
    print(f"Solution time: {stats.solution_time:.1f}s")
    print(f"Total execution time: {stats.execution_time:.1f}s")
    print(f"Memory used: {stats.memory_used_mb:.0f} MB")

    if stats.error_message:
        print(f"\nError: {stats.error_message}")
