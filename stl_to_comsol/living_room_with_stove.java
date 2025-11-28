/*
 * living_room_with_stove.java
 * Generated COMSOL model for heat transfer and radiation analysis
 * Source STL: living_room_with_stove.stl
 */

import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.IOException;

public class living_room_with_stove {

  public static Model run() throws IOException {
    Model model = ModelUtil.create("Model");

    model.modelPath(".");
    model.label("living_room_with_stove.mph");

    // Create component and geometry
    model.component().create("comp1", true);
    model.component("comp1").geom().create("geom1", 3);

    // Import STL geometry
    model.component("comp1").geom("geom1").create("imp1", "Import");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("type", "mesh");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("filename", "/Users/anesu/Downloads/room_models/living_room_with_stove.stl");
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

    // Save model
    model.save("living_room_with_stove_results.mph");

    return model;
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Starting COMSOL heat transfer simulation...");
    System.out.println("STL file: living_room_with_stove.stl");
    System.out.println("Room dimensions: 6.50m x 7.13m x 2.70m");
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
