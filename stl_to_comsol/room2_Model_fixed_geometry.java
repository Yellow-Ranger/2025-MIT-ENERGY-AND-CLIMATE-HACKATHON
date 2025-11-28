/*
 * room2_Model_fixed_geometry.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Nov 16 2025, 06:13 by COMSOL 6.3.0.420. */
public class room2_Model_fixed_geometry {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath(".");

    model.label("room2.mph");

    model.component().create("comp1", true);

    model.component("comp1").geom().create("geom1", 3);
    model.component("comp1").geom("geom1").create("imp1", "Import");
    model.component("comp1").geom("geom1").feature("imp1").set("type", "mesh");
    model.component("comp1").geom("geom1").feature("imp1")
         .set("filename", "/Users/anesu/Documents/comsol-physics/assets/room2.stl");
    model.component("comp1").geom("geom1").feature("imp1").set("importtol", 1.0E-6);
    model.component("comp1").geom("geom1").run("imp1");

    model.label("room2_Model.mph");

    model.component("comp1").geom("geom1").run("imp1");
    model.component("comp1").geom("geom1").run("imp1");
    model.component("comp1").geom("geom1").run("imp1");
    model.component("comp1").geom("geom1").feature("fin").set("repairtoltype", "relative");
    model.component("comp1").geom("geom1").run("imp1");

    model.component("comp1").mesh("mesh1").feature().clear();
    model.component("comp1").mesh("mesh1").geometricModel("");

    model.component("comp1").geometricModel("mesh/mesh1");

    model.component("comp1").mesh("mesh1").feature().create("imp1", "Import");
    model.component("comp1").mesh("mesh1").feature("imp1").set("source", "sequence");
    model.component("comp1").mesh("mesh1").feature("imp1").set("sequence", "mpart1");
    model.component("comp1").mesh("mesh1").feature("imp1").set("buildsource", true);
    model.component("comp1").mesh("mesh1").feature("imp1").set("domelemsequence", false);
    model.component("comp1").mesh("mesh1").feature("imp1").set("unmesheddom", true);

    model.component("comp1").geom("geom1").feature("imp1").active(false);

    model.component("comp1").mesh("mesh1").runCurrent();
    model.component("comp1").mesh("mesh1").stat().setQualityMeasure("skewness");
    model.component("comp1").mesh("mesh1").feature().create("imp2", "Import");
    model.component("comp1").mesh("mesh1").feature("imp2").set("source", "geom");
    model.component("comp1").mesh("mesh1").feature("imp2").set("geom", "geom1");
    model.component("comp1").mesh("mesh1").runCurrent();
    model.component("comp1").mesh("mesh1").feature().create("uni1", "Union");
    model.component("comp1").mesh("mesh1").feature().create("dom1", "CreateDomains");
    model.component("comp1").mesh().create("mesh2");
    model.component("comp1").mesh("mesh2").contribute("geom/detail", true);

    return model;
  }

  public static void main(String[] args) {
    run();
  }

}
