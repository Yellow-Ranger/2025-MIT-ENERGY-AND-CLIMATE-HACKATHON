/*
 * room2.java
 * Generated COMSOL model for heat transfer and radiation analysis.
 *
 * Source STL: room2.stl
 *
 * Geometry analysis (from stl_to_comsol/stl_analyzer.py):
 *   - Bounding box: 4.68m (width) x 4.37m (depth) x 4.52m (height)
 *   - Volume: 92.46 m^3
 *   - Mesh: 1746 triangles, 5238 vertices, surface area 228.85 m^2
 *
 * Identified component groups in the STL:
 *   - Floor: 96 surface patches, total area 41.93 m^2
 *   - Walls: 120 surface patches, total area 142.85 m^2
 *   - Ceiling: 28 surface patches, total area 3.45 m^2
 *   - Doors: 417 surface patches, total area 12.58 m^2
 *   - Windows: 20 surface patches, total area 0.32 m^2
 *   - Furniture (tables, chairs, cabinets, etc.): 385 patches, total area 25.53 m^2
 *   - Other interior object: 680 patches, total area 2.20 m^2
 *
 * Potential categories (not all present in this STL):
 *   - wall, floor, ceiling, window, door, furniture (tables, chairs, etc.)
 *   These refined heuristics are used upstream to map each category to
 *   appropriate COMSOL materials (e.g., drywall for walls, wood for floor,
 *   glass for windows, wood/metal for furniture, etc.).
 */

import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.IOException;

public class room2 {

  public static Model run() throws IOException {
    Model model = ModelUtil.create("Model");

    model.modelPath(".");
    model.label("room2.mph");

    // Create component and geometry
    model.component().create("comp1", true);
    model.component("comp1").geom().create("geom1", 3);

    // Import STL geometry
    model.component("comp1").geom("geom1").create("imp1", "Import");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("type", "mesh");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("filename", "/Users/anesu/Documents/comsol-physics/assets/room2.stl");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("importtol", 1.0E-6);

    // Build geometry
    model.component("comp1").geom("geom1").run();

    return model;
  }

  public static Model run2(Model model) {
    // Define materials

    // Drywall - for wall
    model.component("comp1").material().create("mat1", "Common");
    model.component("comp1").material("mat1").label("Drywall");
    model.component("comp1").material("mat1").propertyGroup("def")
         .set("thermalconductivity", "0.17");
    model.component("comp1").material("mat1").propertyGroup("def")
         .set("density", "800");
    model.component("comp1").material("mat1").propertyGroup("def")
         .set("heatcapacity", "1090");
    model.component("comp1").material("mat1").selection().all();

    // Wood Floor - for floor
    model.component("comp1").material().create("mat2", "Common");
    model.component("comp1").material("mat2").label("Wood Floor");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("thermalconductivity", "0.14");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("density", "700");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("heatcapacity", "1380");
    model.component("comp1").material("mat2").selection().all();

    // Concrete Ceiling - for ceiling
    model.component("comp1").material().create("mat3", "Common");
    model.component("comp1").material("mat3").label("Concrete Ceiling");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("thermalconductivity", "1.4");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("density", "2300");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("heatcapacity", "880");
    model.component("comp1").material("mat3").selection().all();

    // Heat source materials
    // Heat source 1: Fallback heat source on wall

    return model;
  }

  public static Model run3(Model model) {
    // Heat transfer physics
    model.component("comp1").physics().create("ht", "HeatTransfer", "geom1");
    model.component("comp1").physics("ht").feature("init1")
         .set("Tinit", "293.15");  // 20°C

    // Heat source 1
    model.component("comp1").physics("ht").create("temp1", "TemperatureBoundary", 2);
    model.component("comp1").physics("ht").feature("temp1")
         .set("T0", "323.15");

    // Surface-to-surface radiation
    model.component("comp1").physics().create("rad", "SurfaceToSurfaceRadiation", "geom1");
    model.component("comp1").physics("rad").create("dsurf1", "DiffuseSurface", 2);
    model.component("comp1").physics("rad").feature("dsurf1").selection().all();
    model.component("comp1").physics("rad").feature("dsurf1")
         .set("epsilon_rad", "0.9");

    // Multiphysics coupling
    model.component("comp1").multiphysics().create("rhtcpl1", "RadiativeHeating", -1);
    model.component("comp1").multiphysics("rhtcpl1").selection().all();

    return model;
  }

  public static Model run4(Model model) {
    // Create mesh
    model.component("comp1").mesh().create("mesh1");
    model.component("comp1").mesh("mesh1").create("ftet1", "FreeTet");
    model.component("comp1").mesh("mesh1").feature("size").set("custom", "on");
    model.component("comp1").mesh("mesh1").feature("size").set("hmax", 0.3);
    model.component("comp1").mesh("mesh1").feature("size").set("hmin", 0.1);
    model.component("comp1").mesh("mesh1").run();

    System.out.println("Mesh statistics:");
    System.out.println("  Elements: " + model.component("comp1").mesh("mesh1").getNumElem());

    return model;
  }

  public static Model run5(Model model) {
    // Create study
    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");

    // Create solver
    model.sol().create("sol1");
    model.sol("sol1").attach("std1");
    model.sol("sol1").createAutoSequence("std1");

    System.out.println("\nSolving...");
    long startTime = System.currentTimeMillis();
    model.sol("sol1").runAll();
    long endTime = System.currentTimeMillis();
    System.out.println("Solution time: " + (endTime - startTime) / 1000.0 + " seconds");

    return model;
  }

  public static Model run6(Model model) throws IOException {
    // Create result plots
    model.result().create("pg1", "PlotGroup3D");
    model.result("pg1").label("Temperature Distribution");
    model.result("pg1").create("surf1", "Surface");
    model.result("pg1").feature("surf1").set("expr", "T");
    model.result("pg1").run();

    model.result().create("pg2", "PlotGroup3D");
    model.result("pg2").label("Heat Flux");
    model.result("pg2").create("surf1", "Surface");
    model.result("pg2").feature("surf1").set("expr", "ht.ntflux");
    model.result("pg2").run();

    System.out.println("\nResults generated successfully");

    // Export plots as images (requires COMSOL with graphics capabilities)
    try {
      model.result().export().create("imgT", "Image3D");
      model.result().export("imgT").set("plotgroup", "pg1");
      model.result().export("imgT").set("pngfilename", "room2_temperature.png");
      model.result().export("imgT").run();

      model.result().export().create("imgQ", "Image3D");
      model.result().export("imgQ").set("plotgroup", "pg2");
      model.result().export("imgQ").set("pngfilename", "room2_heatflux.png");
      model.result().export("imgQ").run();

      System.out.println("Image exports: room2_temperature.png, room2_heatflux.png");
    } catch (Exception e) {
      System.out.println("Warning: Failed to export images: " + e.getMessage());
    }

    // Save model
    model.save("room2_results.mph");

    return model;
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Starting COMSOL heat transfer simulation...");
    System.out.println("STL file: room2.stl");
    System.out.println("Room: room2");
    System.out.println();

    // Static summary of objects identified in assets/room2.stl
    System.out.println("Geometry analysis (from STL preprocessing):");
    System.out.println("  Bounding box: 4.68m x 4.37m x 4.52m");
    System.out.println("  Triangles: 1746, vertices: 5238, surface area: 228.85 m^2");
    System.out.println("  Detected component groups:");
    System.out.println("    Floor: 96 patches, total area 41.93 m^2");
    System.out.println("    Walls: 120 patches, total area 142.85 m^2");
    System.out.println("    Ceiling: 28 patches, total area 3.45 m^2");
    System.out.println("    Doors: 417 patches, total area 12.58 m^2");
    System.out.println("    Windows: 20 patches, total area 0.32 m^2");
    System.out.println("    Furniture (tables, chairs, cabinets, etc.): 385 patches, total area 25.53 m^2");
    System.out.println("    Other interior object: 680 patches, total area 2.20 m^2");
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

    System.out.println("\nSimulation complete!");
  }
}
