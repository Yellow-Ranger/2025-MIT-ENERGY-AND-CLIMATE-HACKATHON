/*
 * living_room_with_stove.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.IOException;

/** Model exported on Nov 15 2025, 21:08 by COMSOL 6.3.0.420. */
public class living_room_with_stove {

  public static Model run() throws IOException {
    Model model = ModelUtil.create("Model");

    model.modelPath("\\\\Mac\\Home\\Downloads\\room_models");

    model.label("living_room_with_stove.mph");

    model.component().create("comp1", true);

    model.component("comp1").geom().create("geom1", 3);

    model.component("comp1").mesh().create("mesh1");

    model.component("comp1").geom("geom1").geomRep("cadps");
    model.component("comp1").geom("geom1").create("blk1", "Block");
    model.component("comp1").geom("geom1").feature("blk1").set("pos", new int[]{0, 0, 0});
    model.component("comp1").geom("geom1").feature("blk1")
         .set("size", new String[]{"5.83 [m]", "6.51 [m]", "2.7 [m]"});
    model.component("comp1").geom("geom1").create("blk2", "Block");
    model.component("comp1").geom("geom1").feature("blk2").label("Block 2 - door");
    model.component("comp1").geom("geom1").feature("blk2").set("pos", new String[]{"-0.3[m]", "0.9[m]", "0"});
    model.component("comp1").geom("geom1").feature("blk2").set("size", new String[]{"0.3[m]", "1.7[m]", "2.1[m]"});
    model.component("comp1").geom("geom1").create("blk3", "Block");
    model.component("comp1").geom("geom1").feature("blk3").label("Block 3 - door");
    model.component("comp1").geom("geom1").feature("blk3").set("pos", new String[]{"2.32[m]", "-0.24[m]", "0"});
    model.component("comp1").geom("geom1").feature("blk3").set("size", new String[]{"2.51[m]", "0.24[m]", "2.1[m]"});
    model.component("comp1").geom("geom1").create("blk4", "Block");
    model.component("comp1").geom("geom1").feature("blk4").label("Block 4 - window");
    model.component("comp1").geom("geom1").feature("blk4")
         .set("pos", new String[]{"1.6025[m]", "6.51[m]", "0.625[m]"});
    model.component("comp1").geom("geom1").feature("blk4")
         .set("size", new String[]{"2.511[m]", "0.375[m]", "1.635[m]"});
    model.component("comp1").geom("geom1").create("blk5", "Block");
    model.component("comp1").geom("geom1").feature("blk5").label("Block 5 - window");
    model.component("comp1").geom("geom1").feature("blk5").set("pos", new String[]{"5.83[m]", "2[m]", "0.625[m]"});
    model.component("comp1").geom("geom1").feature("blk5")
         .set("size", new String[]{"0.375[m]", "2.51[m]", "1.635[m]"});
    model.component("comp1").geom("geom1").create("wp5", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp5").set("quickplane", "yz");
    model.component("comp1").geom("geom1").feature("wp5").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp5").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp5").geom().constrDimBuild("uptotarget");
    model.component("comp1").geom("geom1").feature("wp5").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("proj1").selection("input").set("blk2");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("proj1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp5").geom().create("r1", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("r1").set("pos", new double[]{2.85, 0.65});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("r1").set("rotconstr", true);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("r1")
         .set("size", new double[]{1.5, 0.6897832993049318});
    model.component("comp1").geom("geom1").feature("wp5").geom().create("dist1", "Distance");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("dist1").set("distance", "65[in]");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("dist1")
         .set("helppoint1", new double[]{2.2239089012145996, 0.7316340208053589});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("dist1")
         .set("helppoint2", new double[]{4.267566204071045, 1.6589055061340332});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("dist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("dist1").selection("entity1")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("dist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("dist1").selection("entity2")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp5").geom().create("xdist1", "XDistance");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("xdist1").set("distance", 1.5);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("xdist1")
         .set("helppoint1", new double[]{2.486229181289673, 1.56129789352417});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("xdist1")
         .set("helppoint2", new double[]{4.005246162414551, 1.56129789352417});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("xdist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("xdist1").selection("entity1")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("xdist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("xdist1").selection("entity2")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp5").geom().create("r2", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("r2").set("pos", new double[]{2.6, 0});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("r2").set("rotconstr", true);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("r2")
         .set("sizeconstr", new String[]{"on", "on"});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("r2").set("size", new double[]{2, 0.45});
    model.component("comp1").geom("geom1").feature("wp5").geom().create("coi2", "Coincident");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("coi2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("coi2").selection("entity1")
         .set("r2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("coi2").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("coi2").selection("entity2")
         .set("proj1.blk2", 3);
    model.component("comp1").geom("geom1").feature("wp5").geom().create("eqdist1", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("eqdist1")
         .set("helppoint1", new double[]{2.236109733581543, 0.13378798961639404});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("eqdist1")
         .set("helppoint2", new double[]{2.4740281105041504, 1.353882074356079});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("eqdist1")
         .set("helppoint3", new double[]{3.9991455078125, 1.305078387260437});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("eqdist1")
         .set("helppoint4", new double[]{4.590891361236572, 0.17649126052856445});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("eqdist1").selection("entity1")
         .set("r2(1)", 4);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("eqdist1").selection("entity2")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("eqdist1").selection("entity3")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("eqdist1").selection("entity4")
         .set("r2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp5").geom().create("ydist1", "YDistance");
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("ydist1").set("distance", 0.65);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("ydist1")
         .set("helppoint1", new double[]{4.273666858673096, -0.012623310089111328});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("ydist1")
         .set("helppoint2", new double[]{4.285867691040039, 0.3656059503555298});
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("ydist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("ydist1").selection("entity1")
         .set("r2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("ydist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp5").geom().feature("ydist1").selection("entity2")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").create("ext4", "Extrude");
    model.component("comp1").geom("geom1").feature("ext4").set("extrudefrom", "faces");
    model.component("comp1").geom("geom1").feature("ext4").setIndex("distance", "10[cm]", 0);
    model.component("comp1").geom("geom1").feature("ext4").selection("inputface").set("wp5.uni", 2);
    model.component("comp1").geom("geom1").create("ext5", "Extrude");
    model.component("comp1").geom("geom1").feature("ext5").set("extrudefrom", "faces");
    model.component("comp1").geom("geom1").feature("ext5").setIndex("distance", "30[cm]", 0);
    model.component("comp1").geom("geom1").feature("ext5").selection("inputface").set("ext4(1)", 1);
    model.component("comp1").geom("geom1").create("wp1", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp1").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp1").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp1").geom().constrDimBuild("uptotarget");
    model.component("comp1").geom("geom1").feature("wp1").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("proj1").selection("input")
         .set("blk1", "ext5");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("proj1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp1").geom().create("pol1", "Polygon");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("pol1").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("pol1")
         .set("table", new double[][]{{3.53, 5.5}, {5.03, 5.5}, {5.03, 1.7}, {2.93, 1.7}, {2.93, 2.6}, {4.13, 2.6}, {4.13, 4.6}, {3.53, 4.6}, {3.53, 5.5}});
    model.component("comp1").geom("geom1").feature("wp1").geom().create("pol2", "Polygon");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("pol2").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("pol2").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("pol2")
         .set("table", new double[][]{{0, 3.6}, {4.13, 3.6}});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("pol2").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp1").geom().create("coi1", "Coincident");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi1").selection("entity1")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi1").selection("entity2")
         .set("proj1.blk1", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("coi2", "Coincident");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi2").selection("entity1")
         .set("pol2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi2").selection("entity2")
         .set("pol1(1)", 6);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("hor1", "Horizontal");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("hor1").selection("edge")
         .set("pol1(1)", 1, 3, 5, 7);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("hor1").selection("edge").set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("ver1", "Vertical");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("ver1").selection("edge")
         .set("pol1(1)", 2, 4, 6, 8);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("dist1", "Distance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist1").set("distance", "0.90");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist1")
         .set("helppoint1", new double[]{2.83, 4.255});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist1")
         .set("helppoint2", new double[]{2.83, 5.255});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist1").selection("entity1")
         .set("pol1(1)", 8);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist1").selection("entity2")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("eqdist1", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist1")
         .set("helppoint1", new double[]{3.379183292388916, 4.669921398162842});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist1")
         .set("helppoint2", new double[]{3.7609524726867676, 5.736629486083984});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist1")
         .set("helppoint3", new double[]{2.5931878089904785, 2.457904815673828});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist1")
         .set("helppoint4", new double[]{2.3798460960388184, 1.3575111627578735});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist1").selection("entity1")
         .set("pol1(1)", 7);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist1").selection("entity2")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist1").selection("entity3")
         .set("pol1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist1").selection("entity4")
         .set("pol1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("eqdist2", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist2")
         .set("helppoint1", new double[]{3.4802393913269043, 4.636236190795898});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist2")
         .set("helppoint2", new double[]{3.446554183959961, 5.714172840118408});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist2")
         .set("helppoint3", new double[]{4.198864459991455, 4.232009410858154});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist2")
         .set("helppoint4", new double[]{5.321715354919434, 4.265695095062256});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist2").selection("entity1")
         .set("pol1(1)", 7);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist2").selection("entity2")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist2").selection("entity3")
         .set("pol1(1)", 6);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist2").selection("entity4")
         .set("pol1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("dist2", "Distance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist2").set("distance", 0.6);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist2")
         .set("helppoint1", new double[]{4.213, 4.6});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist2")
         .set("helppoint2", new double[]{3.113, 4.6});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist2").selection("entity1")
         .set("pol1(1)", 7);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist2").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist2").selection("entity2")
         .set("pol1(1)", 8);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("dist3", "Distance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist3").set("distance", 2);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist3")
         .set("helppoint1", new double[]{3.713, 2.4805});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist3")
         .set("helppoint2", new double[]{3.713, 4.6});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist3").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist3").selection("entity1")
         .set("pol1(1)", 6);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist3").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist3").selection("entity2")
         .set("pol1(1)", 7);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("eqdist3", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist3")
         .set("helppoint1", new double[]{1.964390754699707, 1.963850736618042});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist3")
         .set("helppoint2", new double[]{3.1321558952331543, 4.50149393081665});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist3")
         .set("helppoint3", new double[]{3.1321558952331543, 4.50149393081665});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist3")
         .set("helppoint4", new double[]{3.7721810340881348, 2.7610747814178467});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist3").selection("entity1")
         .set("pol1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist3").selection("entity2")
         .set("pol1(1)", 8);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist3").selection("entity3")
         .set("pol1(1)", 8);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist3").selection("entity4")
         .set("pol1(1)", 6);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("eqdist4", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist4")
         .set("helppoint1", new double[]{0.1394924521446228, 2.5951714515686035});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist4")
         .set("helppoint2", new double[]{0.17390882968902588, 3.489997386932373});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist4")
         .set("helppoint3", new double[]{0.17390882968902588, 3.489997386932373});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist4")
         .set("helppoint4", new double[]{0.18251293897628784, 4.582717418670654});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist4").selection("entity1")
         .set("proj1.ext5", 2);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist4").selection("entity2")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist4").selection("entity3")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist4").selection("entity4")
         .set("proj1.ext5", 5);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("eqdist5", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist5")
         .set("helppoint1", new double[]{3.202550172805786, 2.646796226501465});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist5")
         .set("helppoint2", new double[]{2.772345542907715, 3.5502262115478516});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist5")
         .set("helppoint3", new double[]{2.772345542907715, 3.5502262115478516});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist5")
         .set("helppoint4", new double[]{3.2455708980560303, 4.186929225921631});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist5").selection("entity1")
         .set("pol1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist5").selection("entity2")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist5").selection("entity3")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist5").selection("entity4")
         .set("pol1(1)", 7);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("dist4", "Distance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist4").set("distance", 0.8);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist4")
         .set("helppoint1", new double[]{4.764989852905273, 4.845006465911865});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist4")
         .set("helppoint2", new double[]{5.851680755615234, 5.239369869232178});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist4").selection("entity1")
         .set("pol1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist4").selection("entity2")
         .set("proj1.blk1", 4);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("r1", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("r1").set("pos", new double[]{2.93, 3.3});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("r1").set("size", new double[]{0.87, 0.6});
    model.component("comp1").geom("geom1").feature("wp1").geom().create("eqdist6", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist6")
         .set("helppoint1", new double[]{3.570533275604248, 4.320318222045898});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist6")
         .set("helppoint2", new double[]{3.512336492538452, 3.9711360931396484});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist6")
         .set("helppoint3", new double[]{2.8527705669403076, 3.227508068084717});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist6")
         .set("helppoint4", new double[]{2.891568660736084, 2.82012939453125});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist6").selection("entity1")
         .set("pol1(1)", 7);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist6").selection("entity2")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist6").selection("entity3")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("eqdist6").selection("entity4")
         .set("pol1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("dist5", "Distance");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist5").set("distance", "0.60");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist5")
         .set("helppoint1", new double[]{2.563, 3.213833333333333});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist5")
         .set("helppoint2", new double[]{2.563, 3.9861666666666684});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist5").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist5").selection("entity1")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist5").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("dist5").selection("entity2")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp1").geom().create("coi3", "Coincident");
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi3")
         .set("helppoint1", new double[]{2.5617856979370117, 3.266305923461914});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi3")
         .set("helppoint2", new double[]{2.74284291267395, 2.671403646469116});
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi3").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi3").selection("entity1")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp1").geom().feature("coi3").selection("entity2")
         .set("pol1(1)", 4);
    model.component("comp1").geom("geom1").create("ext1", "Extrude");
    model.component("comp1").geom("geom1").feature("ext1").setIndex("distance", "40[cm]", 0);
    model.component("comp1").geom("geom1").feature("ext1").selection("input").set("wp1");
    model.component("comp1").geom("geom1").create("wp2", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp2").set("planetype", "faceparallel");
    model.component("comp1").geom("geom1").feature("wp2").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp2").selection("face").set("ext1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp2").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("proj1").set("project", "dom");
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("proj1").selection("input")
         .set("ext1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp2").geom().create("off1", "Offset");
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("off1").set("keep", false);
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("off1").set("distance", "20[cm]");
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("off1").selection("input").set("proj1");
    model.component("comp1").geom("geom1").feature("wp2").geom().create("fil1", "Fillet");
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("fil1").set("radiusconstr", false);
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("fil1").set("radius", 0.20000000000002616);
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("fil1").selection("point")
         .set("off1(1)", 13, 14);
    model.component("comp1").geom("geom1").feature("wp2").geom().create("eqrad1", "EqualRadius");
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("eqrad1").selection("edge1")
         .set("fil1(1)", 11);
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("eqrad1").selection("edge2")
         .set("fil1(1)", 14);
    model.component("comp1").geom("geom1").feature("wp2").geom().create("csol1", "ConvertToSolid");
    model.component("comp1").geom("geom1").feature("wp2").geom().feature("csol1").selection("input").set("fil1");
    model.component("comp1").geom("geom1").create("ext2", "Extrude");
    model.component("comp1").geom("geom1").feature("ext2").setIndex("distance", "20[cm]", 0);
    model.component("comp1").geom("geom1").feature("ext2").selection("input").set("wp2");
    model.component("comp1").geom("geom1").create("wp3", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp3").set("quickplane", "zx");
    model.component("comp1").geom("geom1").feature("wp3").set("quickoffsettype", "vertex");
    model.component("comp1").geom("geom1").feature("wp3").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp3").selection("offsetvertex").set("ext2(1)", 32);
    model.component("comp1").geom("geom1").feature("wp3").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp3").geom().constrDimBuild("uptotarget");
    model.component("comp1").geom("geom1").feature("wp3").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("proj1").set("project", "vtx");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("proj1").selection("input")
         .set("ext2(1)", 32);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("proj1").selection("input")
         .set("ext1(1)", 23);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("proj1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp3").geom().create("pol1", "Polygon");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("pol1").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("pol1")
         .set("table", new double[][]{{0.6, 5.23}, {0.6, 4.811617859440279}, {1.2, 5.03}, {1.2, 5.4828980942125405}, {1, 5.4828980942125405}, {1, 5.28289809421254}, {0.7, 5.23}, {0.6, 5.23}});
    model.component("comp1").geom("geom1").feature("wp3").geom().create("coi1", "Coincident");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("coi1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("coi1").selection("entity1")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("coi1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("coi1").selection("entity2")
         .set("proj1.ext2", 1);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("hor1", "Horizontal");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("hor1").selection("edge")
         .set("pol1(1)", 4, 7);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("ver1", "Vertical");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ver1").selection("edge")
         .set("pol1(1)", 1, 3, 5);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("eqdist1", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1")
         .set("helppoint1", new double[]{1.3792681694030762, 5.341310024261475});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1")
         .set("helppoint2", new double[]{0.6210494637489319, 4.85527229309082});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1")
         .set("helppoint3", new double[]{0.6210494637489319, 4.85527229309082});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1")
         .set("helppoint4", new double[]{-0.0010785460472106934, 4.803428649902344});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1").selection("entity1")
         .set("pol1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1").selection("entity2")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1").selection("entity3")
         .set("pol1(1)", 1);

    return model;
  }

  public static Model run2(Model model) {
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1").selection("entity4").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist1").selection("entity4")
         .set("proj1.ext1", 1);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("ydist1", "YDistance");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ydist1")
         .set("helppoint1", new double[]{-0.002935051918029785, 4.8108391761779785});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ydist1")
         .set("helppoint2", new double[]{1.2098461389541626, 4.826337814331055});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ydist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ydist1").selection("entity1")
         .set("proj1.ext1", 1);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ydist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ydist1").selection("entity2")
         .set("pol1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("ang1", "Angle");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang1").set("reverse2", true);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang1").set("angle", 110);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang1")
         .set("helppoint1", new double[]{1.2, 4.83});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang1")
         .set("helppoint2", new double[]{1.2, 4.83});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang1").selection("edge1")
         .set("pol1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang1").selection("edge2")
         .set("pol1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("ang2", "Angle");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang2").set("reverse1", true);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang2").set("angle", 100);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang2")
         .set("helppoint1", new double[]{1, 5.1});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang2")
         .set("helppoint2", new double[]{1, 5.1});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang2").selection("edge1")
         .set("pol1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("ang2").selection("edge2")
         .set("pol1(1)", 6);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("eqdist2", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2")
         .set("helppoint1", new double[]{0.9928628206253052, 5.1905598640441895});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2")
         .set("helppoint2", new double[]{1.2175955772399902, 5.1363139152526855});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2")
         .set("helppoint3", new double[]{1.1168532371520996, 5.256429672241211});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2")
         .set("helppoint4", new double[]{1.0122363567352295, 5.085943222045898});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2").selection("entity1")
         .set("pol1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2").selection("entity2")
         .set("pol1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2").selection("entity3")
         .set("pol1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2").selection("entity4").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("eqdist2").selection("entity4")
         .set("pol1(1)", 6);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("dist1", "Distance");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist1").set("distance", 0.2);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist1")
         .set("helppoint1", new double[]{1.2, 5.300530792283386});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist1")
         .set("helppoint2", new double[]{1, 5.300530792283386});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist1").selection("entity1")
         .set("pol1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist1").selection("entity2")
         .set("pol1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("dist2", "Distance");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist2").set("distance", 0.1);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist2")
         .set("helppoint1", new double[]{0.6965024575511494, 5.03});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist2")
         .set("helppoint2", new double[]{0.6, 5.03});
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist2").selection("entity1")
         .set("pol1(1)", 7);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist2").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("dist2").selection("entity2")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("fil1", "Fillet");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("fil1").set("radius", "5[cm]");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("fil1").selection("point")
         .set("pol1(1)", 5, 7);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("fil2", "Fillet");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("fil2").set("radius", "30[cm]");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("fil2").selection("point")
         .set("fil1(1)", 3, 8);
    model.component("comp1").geom("geom1").feature("wp3").geom().create("fil3", "Fillet");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("fil3").set("radius", "10[cm]");
    model.component("comp1").geom("geom1").feature("wp3").geom().feature("fil3").selection("point")
         .set("fil2(1)", 5);
    model.component("comp1").geom("geom1").create("swe1", "Sweep");
    model.component("comp1").geom("geom1").feature("swe1").set("crossfaces", true);
    model.component("comp1").geom("geom1").feature("swe1").set("includefinal", false);
    model.component("comp1").geom("geom1").feature("swe1").selection("enttosweep").set("wp3.fil3", 1);
    model.component("comp1").geom("geom1").feature("swe1").selection("edge").set("ext2(1)", 47);
    model.component("comp1").geom("geom1").feature("swe1").selection("diredge").set("ext2(1)", 47);
    model.component("comp1").geom("geom1").create("uni1", "Union");
    model.component("comp1").geom("geom1").feature("uni1").set("intbnd", false);
    model.component("comp1").geom("geom1").feature("uni1").selection("input").set("ext1", "ext2", "swe1", "wp3");
    model.component("comp1").geom("geom1").create("fil1", "Fillet3D");
    model.component("comp1").geom("geom1").feature("fil1").set("radius", "5[cm]");
    model.component("comp1").geom("geom1").feature("fil1").selection("edge").set("uni1(1)", 73, 75);
    model.component("comp1").geom("geom1").create("fil2", "Fillet3D");
    model.component("comp1").geom("geom1").feature("fil2").set("radius", "5[cm]");
    model.component("comp1").geom("geom1").feature("fil2").selection("edge")
         .set("fil1(1)", 2, 3, 4, 5, 7, 8, 10, 11, 15, 16, 19, 21, 22, 32, 33, 34, 35, 37, 38, 40, 41, 45, 46, 49, 51, 52, 54, 55, 57, 58, 64, 65, 69, 71, 72, 74, 76, 78, 81, 82, 84, 85, 88, 91, 92, 96, 99, 101, 104, 105, 107, 108, 110, 111, 113, 114, 116, 117, 119, 121, 122, 123, 124, 127);
    model.component("comp1").geom("geom1").create("wp4", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp4").set("planetype", "faceparallel");
    model.component("comp1").geom("geom1").feature("wp4").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp4").selection("face").set("blk1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp4").geom().useConstrDim(false);
    model.component("comp1").geom("geom1").feature("wp4").geom().create("r1", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("r1").set("pos", new double[]{-1.35, 2.4});
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("r1")
         .set("size", new double[]{1.35, 0.515});
    model.component("comp1").geom("geom1").feature("wp4").geom().create("r2", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("r2").set("pos", new double[]{-1.35, 2});
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("r2").set("size", new double[]{0.95, 0.4});
    model.component("comp1").geom("geom1").feature("wp4").geom().create("r3", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("r3").set("pos", new double[]{-1.35, 1.6});
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("r3").set("size", new double[]{0.475, 0.4});
    model.component("comp1").geom("geom1").feature("wp4").geom().create("off1", "Offset");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("off1").set("distance", "5[cm]");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("off1").set("reverse", true);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("off1").selection("input").init(1);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("off1").selection("input")
         .set("r3(1)", 1, 2, 3, 4);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("off1").selection("input")
         .set("r2(1)", 1, 2, 3, 4);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("off1").selection("input")
         .set("r1(1)", 1, 2, 3, 4);
    model.component("comp1").geom("geom1").feature("wp4").geom().create("pard1", "PartitionDomains");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("pard1")
         .set("partitionwith", "extendededges");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("pard1").selection("domain")
         .set("r2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("pard1").selection("domain")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("pard1").selection("domain")
         .set("r3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("pard1").selection("extendededge")
         .set(new String[]{"off1(1)", "off1(2)", "r3(1)", "r2(1)", "r1(1)", "off1(3)"}, new int[][]{{1, 2, 3, 4}, {1, 2, 3, 4}, {1, 2, 3, 4}, {1, 2, 3, 4}, {1, 2, 3, 4}, {1, 2, 3, 4}});
    model.component("comp1").geom("geom1").feature("wp4").geom().create("del1", "Delete");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("del1").selection("input").init(2);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("del1").selection("input")
         .set("pard1(2)", 5, 11, 17);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("del1").selection("input")
         .set("pard1(1)", 5, 11);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("del1").selection("input")
         .set("pard1(3)", 5);
    model.component("comp1").geom("geom1").feature("wp4").geom().create("uni1", "Union");
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("uni1").set("intbnd", false);
    model.component("comp1").geom("geom1").feature("wp4").geom().feature("uni1").selection("input")
         .set("off1", "del1(2)", "del1(1)", "del1(3)");
    model.component("comp1").geom("geom1").create("ext3", "Extrude");
    model.component("comp1").geom("geom1").feature("ext3").setIndex("distance", "30[cm]", 0);
    model.component("comp1").geom("geom1").feature("ext3").set("reverse", true);
    model.component("comp1").geom("geom1").feature("ext3").selection("input").set("wp4");
    model.component("comp1").geom("geom1").create("wp12", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp12").label("Work Plane 12 - glass plate bottom");
    model.component("comp1").geom("geom1").feature("wp12").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp12").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("proj1").selection("input")
         .set("blk1", "ext5");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("proj1")
         .setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp12").geom().create("pol2", "Polygon");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("pol2").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("pol2").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("pol2")
         .set("table", new double[][]{{0, 5.31}, {0.6, 5.31}});
    model.component("comp1").geom("geom1").feature("wp12").geom().create("coi2", "Coincident");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi2").selection("entity1")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi2").selection("entity2")
         .set("proj1.blk1", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("ca1", "CircularArc");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("ca1")
         .set("center", new double[]{0.6, 5.91});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("ca1").set("r", 0.6);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("ca1").set("angle1", -90);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("ca1").set("angle2", 0);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("coi3", "Coincident");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi3").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi3").selection("entity1")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi3").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi3").selection("entity2")
         .set("pol2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("pol3", "Polygon");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("pol3").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("pol3").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("pol3")
         .set("table", new double[][]{{1.2, 5.91}, {1.2, 6.51}});
    model.component("comp1").geom("geom1").feature("wp12").geom().create("coi4", "Coincident");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi4").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi4").selection("entity1")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi4").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi4").selection("entity2")
         .set("ca1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("coi5", "Coincident");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi5").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi5").selection("entity1")
         .set("pol3(1)", 2);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("coi5").selection("entity2")
         .set("proj1.blk1", 3);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("eqdist1", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1")
         .set("helppoint1", new double[]{-0.022353529930114746, 5.595677375793457});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1")
         .set("helppoint2", new double[]{0.7101470232009888, 5.579216957092285});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1")
         .set("helppoint3", new double[]{1.0146697759628296, 5.891970157623291});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1")
         .set("helppoint4", new double[]{0.9982091188430786, 6.509245872497559});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1").selection("entity1")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1").selection("entity2")
         .set("pol2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1").selection("entity3").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1").selection("entity3")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1").selection("entity4").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("eqdist1").selection("entity4")
         .set("pol3(1)", 2);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("rad1", "Radius");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("rad1").set("radius", 0.6);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("rad1").selection("edge").set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("dist3", "Distance");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("dist3").set("distance", 1.2);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("dist3")
         .set("helppoint1", new double[]{-0.021123111248016357, 5.510324478149414});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("dist3")
         .set("helppoint2", new double[]{-0.007050812244415283, 6.512974262237549});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("dist3").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("dist3").selection("entity1")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("dist3").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("dist3").selection("entity2")
         .set("proj1.blk1", 2);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("hor2", "Horizontal");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("hor2").selection("edge")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("ver2", "Vertical");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("ver2").selection("edge")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("tanc4", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc4").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc4").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc4")
         .set("helppoint1", new double[]{1.2332587242126465, 6.019565582275391});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc4")
         .set("helppoint2", new double[]{1.2, 5.91});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc4").selection("edge1")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc4").selection("vertex1")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc4").selection("edge2")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc4").selection("vertex2")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().create("tanc3", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc3").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc3").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc3")
         .set("helppoint1", new double[]{0.6432648301124573, 5.550596237182617});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc3")
         .set("helppoint2", new double[]{0.6, 5.31});
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc3").selection("edge1")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc3").selection("vertex1")
         .set("pol2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc3").selection("edge2")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp12").geom().feature("tanc3").selection("vertex2")
         .set("pol2(1)", 2);
    model.component("comp1").geom("geom1").create("wp15", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp15").label("Work Plane 15 - glass plate TV wall");
    model.component("comp1").geom("geom1").feature("wp15").set("quickplane", "yz");
    model.component("comp1").geom("geom1").feature("wp15").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp15").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("proj1").selection("input")
         .set("blk1", "wp12");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("proj1")
         .setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp15").geom().create("pol1", "Polygon");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("pol1").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("pol1").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("pol1")
         .set("table", new double[][]{{5.31, 0}, {5.31, 0.7}});
    model.component("comp1").geom("geom1").feature("wp15").geom().create("ca1", "CircularArc");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("ca1")
         .set("center", new double[]{5.91, 0.7});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("ca1").set("r", 0.6);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("ca1").set("angle1", -180);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("ca1").set("clockwise", true);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("coi3", "Coincident");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi3").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi3").selection("entity1")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi3").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi3").selection("entity2")
         .set("pol1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("pol2", "Polygon");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("pol2").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("pol2").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("pol2")
         .set("table", new double[][]{{5.91, 1.3}, {6.51, 1.3}});
    model.component("comp1").geom("geom1").feature("wp15").geom().create("coi4", "Coincident");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi4").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi4").selection("entity1")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi4").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi4").selection("entity2")
         .set("ca1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("coi5", "Coincident");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi5").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi5").selection("entity1")
         .set("pol2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi5").selection("entity2")
         .set("proj1.blk1", 4);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("rad1", "Radius");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("rad1").set("radius", 0.6);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("rad1").selection("edge").set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("tanc3", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc3").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc3").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc3")
         .set("helppoint1", new double[]{5.31, 0.7});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc3")
         .set("helppoint2", new double[]{5.309852123260498, 1.4793696403503418});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc3").selection("edge1")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc3").selection("vertex1")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc3").selection("edge2")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc3").selection("vertex2")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("tanc4", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc4").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc4").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc4")
         .set("helppoint1", new double[]{6.032012939453125, 2.15033221244812});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc4")
         .set("helppoint2", new double[]{5.91, 1.3});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc4").selection("edge1")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc4").selection("vertex1")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc4").selection("edge2")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("tanc4").selection("vertex2")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("dist1", "Distance");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("dist1").set("distance", 1.3);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("dist1")
         .set("helppoint1", new double[]{6.520671844482422, 2.1469759941101074});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("dist1")
         .set("helppoint2", new double[]{6.526354789733887, -0.001277327537536621});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("dist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("dist1").selection("entity1")
         .set("pol2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("dist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("dist1").selection("entity2")
         .set("proj1.wp12", 3);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("coi6", "Coincident");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi6")
         .set("helppoint1", new double[]{5.31, 0});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi6")
         .set("helppoint2", new double[]{5.1684651374816895, -0.002347707748413086});
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi6").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi6").selection("entity1")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi6").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("coi6").selection("entity2")
         .set("proj1.wp12", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("perp1", "Perpendicular");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("perp1").selection("entity1")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("perp1").selection("entity2")
         .set("proj1.wp12", 1);
    model.component("comp1").geom("geom1").feature("wp15").geom().create("perp2", "Perpendicular");
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("perp2").selection("entity1")
         .set("proj1.blk1", 4);
    model.component("comp1").geom("geom1").feature("wp15").geom().feature("perp2").selection("entity2")
         .set("pol2(1)", 1);
    model.component("comp1").geom("geom1").create("wp16", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp16").label("Work Plane 16 - glass plate window wall");
    model.component("comp1").geom("geom1").feature("wp16").set("quickplane", "xz");
    model.component("comp1").geom("geom1").feature("wp16").set("quickoffsettype", "vertex");

    return model;
  }

  public static Model run3(Model model) {
    model.component("comp1").geom("geom1").feature("wp16").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp16").selection("offsetvertex").set("blk1(1)", 7);
    model.component("comp1").geom("geom1").feature("wp16").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("proj1").selection("input")
         .set("blk1", "wp12", "wp15");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("proj1")
         .setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp16").geom().create("pol3", "Polygon");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("pol3").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("pol3").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("pol3")
         .set("table", new double[][]{{1.2, 0}, {1.2, 0.7}});
    model.component("comp1").geom("geom1").feature("wp16").geom().create("coi7", "Coincident");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi7").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi7").selection("entity1")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi7").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi7").selection("entity2")
         .set("proj1.wp12", 3);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("ca2", "CircularArc");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("ca2")
         .set("center", new double[]{0.6, 0.7});
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("ca2").set("r", 0.6);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("coi8", "Coincident");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi8").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi8").selection("entity1")
         .set("ca2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi8").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi8").selection("entity2")
         .set("pol3(1)", 2);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("pol4", "Polygon");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("pol4").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("pol4").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("pol4")
         .set("table", new double[][]{{0.6, 1.3}, {0, 1.3}});
    model.component("comp1").geom("geom1").feature("wp16").geom().create("coi9", "Coincident");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi9").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi9").selection("entity1")
         .set("pol4(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi9").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi9").selection("entity2")
         .set("ca2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("coi10", "Coincident");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi10").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi10").selection("entity1")
         .set("pol4(1)", 2);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi10").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("coi10").selection("entity2")
         .set("proj1.wp15", 3);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("tanc5", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc5").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc5").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc5")
         .set("helppoint1", new double[]{1.2, 0.7});
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc5")
         .set("helppoint2", new double[]{1.2008235454559326, 0.7498188018798828});
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc5").selection("edge1")
         .set("ca2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc5").selection("vertex1")
         .set("ca2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc5").selection("edge2")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc5").selection("vertex2")
         .set("ca2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("tanc6", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc6").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc6").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc6")
         .set("helppoint1", new double[]{0.46501123905181885, 1.504619836807251});
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc6")
         .set("helppoint2", new double[]{0.6, 1.3});
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc6").selection("edge1")
         .set("pol4(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc6").selection("vertex1")
         .set("pol4(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc6").selection("edge2")
         .set("ca2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("tanc6").selection("vertex2")
         .set("pol4(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("perp1", "Perpendicular");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("perp1").selection("entity1")
         .set("pol3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("perp1").selection("entity2")
         .set("proj1.blk1", 2);
    model.component("comp1").geom("geom1").feature("wp16").geom().create("perp2", "Perpendicular");
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("perp2").selection("entity1")
         .set("pol4(1)", 1);
    model.component("comp1").geom("geom1").feature("wp16").geom().feature("perp2").selection("entity2")
         .set("proj1.blk1", 1);
    model.component("comp1").geom("geom1").create("wp6", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp6").label("Work Plane 6 - Stove");
    model.component("comp1").geom("geom1").feature("wp6").set("quickz", "5[cm]");
    model.component("comp1").geom("geom1").feature("wp6").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp6").geom().constrDimBuild("uptotarget");
    model.component("comp1").geom("geom1").feature("wp6").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("proj1").selection("input").set("wp12");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("proj1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp6").geom().create("r1", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("r1")
         .set("pos", new double[]{0.48109090243288216, 5.968063433444131});
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("r1").set("rot", -38.3776896886549);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("r1").set("base", "center");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("r1")
         .set("sizeconstr", new String[]{"on", "on"});
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("r1")
         .set("size", new String[]{"584[mm]", "225.5[mm]*2"});
    model.component("comp1").geom("geom1").feature("wp6").geom().create("ydist1", "YDistance");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("ydist1").set("distance", "30[cm]");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("ydist1")
         .set("helppoint1", new double[]{0.6920405626296997, 4.594729900360107});
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("ydist1")
         .set("helppoint2", new double[]{0.6226009130477905, 4.959288120269775});
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("ydist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("ydist1").selection("entity1")
         .set("proj1.wp12", 2);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("ydist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("ydist1").selection("entity2")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp6").geom().create("xdist1", "XDistance");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist1").set("distance", "57[cm]");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist1")
         .set("helppoint1", new double[]{-0.015375852584838867, 4.586050033569336});
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist1")
         .set("helppoint2", new double[]{0.605241060256958, 4.872488498687744});
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist1").selection("entity1")
         .set("proj1.wp12", 1);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist1").selection("entity2")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp6").geom().create("xdist2", "XDistance");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist2").set("distance", "85[cm]");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist2")
         .set("helppoint1", new double[]{-0.011322788894176483, 5.794771194458008});
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist2")
         .set("helppoint2", new double[]{0.8787558078765869, 5.230729103088379});
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist2").selection("entity1")
         .set("proj1.wp12", 1);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist2").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("xdist2").selection("entity2")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp6").geom().create("off1", "Offset");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("off1").set("distance", "5[cm]");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("off1").set("reverse", true);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("off1").set("trim", false);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("off1").selection("input").init(1);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("off1").selection("input")
         .set("r1(1)", 1, 2, 3, 4);
    model.component("comp1").geom("geom1").feature("wp6").geom().create("par1", "Partition");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("par1").set("keepinput", true);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("par1").selection("input").set("r1");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("par1").selection("tool").set("off1");
    model.component("comp1").geom("geom1").feature("wp6").geom().create("del1", "Delete");
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("del1").selection("input").init(2);
    model.component("comp1").geom("geom1").feature("wp6").geom().feature("del1").selection("input")
         .set("par1(1)", 2, 3, 4, 6, 8);
    model.component("comp1").geom("geom1").create("ext6", "Extrude");
    model.component("comp1").geom("geom1").feature("ext6").label("Extrude 6 - Stove");
    model.component("comp1").geom("geom1").feature("ext6").setIndex("distance", "0.800", 0);
    model.component("comp1").geom("geom1").feature("ext6").selection("input").set("wp6.r1");
    model.component("comp1").geom("geom1").create("ext7", "Extrude");
    model.component("comp1").geom("geom1").feature("ext7").set("specify", "vertices");
    model.component("comp1").geom("geom1").feature("ext7").selection("input").set("wp6.del1");
    model.component("comp1").geom("geom1").feature("ext7").selection("vertex").set("blk1(1)", 3);
    model.component("comp1").geom("geom1").create("wp13", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp13").set("planetype", "faceparallel");
    model.component("comp1").geom("geom1").feature("wp13").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp13").selection("face").set("ext6(1)", 6);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("proj1").selection("input").set("ext6");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("proj1")
         .setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp13").geom().create("r1", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("r1")
         .set("pos", new double[]{-0.15, -0.3245});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("r1").set("size", new double[]{0.3, 0.35});
    model.component("comp1").geom("geom1").feature("wp13").geom().create("r2", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("r2")
         .set("pos", new double[]{-0.15, 0.1995});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("r2")
         .set("size", new double[]{0.3, 0.125});
    model.component("comp1").geom("geom1").feature("wp13").geom().create("r3", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("r3")
         .set("pos", new double[]{-0.15, 0.05});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("r3")
         .set("size", new double[]{0.3, 0.125});
    model.component("comp1").geom("geom1").feature("wp13").geom().create("dist1", "Distance");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist1").set("distance", 0.35);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist1")
         .set("helppoint1", new double[]{0.15, 0.1});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist1")
         .set("helppoint2", new double[]{0.15, -0.35});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist1").selection("entity1")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist1").selection("entity2")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("dist2", "Distance");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist2").set("distance", 0.3);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist2")
         .set("helppoint1", new double[]{0.11666666666666664, -0.27222222222222214});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist2")
         .set("helppoint2", new double[]{-0.11666666666666664, -0.27222222222222214});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist2").selection("entity1")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist2").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("dist2").selection("entity2")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("eqdist1", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist1")
         .set("helppoint1", new double[]{-0.22368329763412476, 0.011886000633239746});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist1")
         .set("helppoint2", new double[]{-0.1768285632133484, 0.00781172513961792});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist1")
         .set("helppoint3", new double[]{0.11244851350784302, -0.02885723114013672});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist1")
         .set("helppoint4", new double[]{0.2346782684326172, -0.022745728492736816});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist1").selection("entity1")
         .set("proj1.ext6", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist1").selection("entity2")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist1").selection("entity3")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist1").selection("entity4")
         .set("proj1.ext6", 4);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("eqdist2", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist2")
         .set("helppoint1", new double[]{-0.15441972017288208, 0.02614617347717285});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist2")
         .set("helppoint2", new double[]{-0.23386910557746887, -0.02070862054824829});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist2")
         .set("helppoint3", new double[]{-0.005706906318664551, -0.27331674098968506});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist2")
         .set("helppoint4", new double[]{4.0459632873535156E-4, -0.42610394954681396});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist2").selection("entity1")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist2").selection("entity2")
         .set("proj1.ext6", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist2").selection("entity3")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist2").selection("entity4")
         .set("proj1.ext6", 2);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("eqdist3", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist3")
         .set("helppoint1", new double[]{-0.08923053741455078, -0.35480326414108276});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist3")
         .set("helppoint2", new double[]{-0.09330487251281738, -0.42610394954681396});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist3")
         .set("helppoint3", new double[]{0.014664709568023682, 0.2644941210746765});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist3")
         .set("helppoint4", new double[]{0.0065160393714904785, 0.4213557243347168});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist3").selection("entity1")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist3").selection("entity2")
         .set("proj1.ext6", 2);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist3").selection("entity3")
         .set("r2(1)", 3);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist3").selection("entity4")
         .set("proj1.ext6", 3);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("coi1", "Coincident");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi1")
         .set("helppoint1", new double[]{-0.15645688772201538, -0.05330318212509155});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi1")
         .set("helppoint2", new double[]{-0.12589949369430542, 0.07096374034881592});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi1").selection("entity1")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi1").selection("entity2")
         .set("r3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("coi2", "Coincident");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi2")
         .set("helppoint1", new double[]{-0.15034544467926025, 0.07096374034881592});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi2")
         .set("helppoint2", new double[]{-0.11163932085037231, 0.30523741245269775});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi2").selection("entity1")
         .set("r3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi2").selection("entity2")
         .set("r2(1)", 4);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("coi3", "Coincident");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi3")
         .set("helppoint1", new double[]{0.09207689762115479, 0.31542325019836426});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi3")
         .set("helppoint2", new double[]{0.11448568105697632, 0.11578124761581421});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi3").selection("entity1")
         .set("r2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi3").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi3").selection("entity2")
         .set("r3(1)", 3);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("coi4", "Coincident");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi4")
         .set("helppoint1", new double[]{0.08392822742462158, 0.07911235094070435});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi4")
         .set("helppoint2", new double[]{0.15115463733673096, -0.10626941919326782});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi4").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi4").selection("entity1")
         .set("r3(1)", 2);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("coi4").selection("entity2")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("eqdist5", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5")
         .set("helppoint1", new double[]{-0.06885892152786255, 0.07300084829330444});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5")
         .set("helppoint2", new double[]{-0.07904475927352905, 0.009848833084106445});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5")
         .set("helppoint3", new double[]{-0.048487305641174316, 0.24004822969436646});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5")
         .set("helppoint4", new double[]{-0.07089608907699585, 0.1952306032180786});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5").set("label", "E");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5").selection("entity1")
         .set("r3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5").selection("entity2")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5").selection("entity3")
         .set("r2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist5").selection("entity4")
         .set("r3(1)", 3);
    model.component("comp1").geom("geom1").feature("wp13").geom().create("eqdist6", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6")
         .set("helppoint1", new double[]{-0.09941637516021729, 0.30523741245269775});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6")
         .set("helppoint2", new double[]{-0.09737920761108398, 0.3459806442260742});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6")
         .set("helppoint3", new double[]{-0.042375802993774414, 0.22171372175216675});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6")
         .set("helppoint4", new double[]{-0.034227192401885986, 0.07911235094070435});
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6").set("label", "F");
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6").selection("entity1")
         .set("r2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6").selection("entity2")
         .set("r2(1)", 3);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6").selection("entity3")
         .set("r3(1)", 3);
    model.component("comp1").geom("geom1").feature("wp13").geom().feature("eqdist6").selection("entity4")
         .set("r3(1)", 1);
    model.component("comp1").geom("geom1").create("wp14", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp14").set("planetype", "faceparallel");
    model.component("comp1").geom("geom1").feature("wp14").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp14").selection("face").set("ext6(1)", 2);
    model.component("comp1").geom("geom1").feature("wp14").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp14").geom().constrDimBuild("uptotarget");
    model.component("comp1").geom("geom1").feature("wp14").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("proj1").set("project", "edg");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("proj1").selection("input")
         .set("wp13.uni", 10, 11, 12);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("proj1").selection("input")
         .set("ext6(1)", 1, 3, 5, 9);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("proj1").selection("input")
         .set("ext7(1)", 9, 25);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("proj1")
         .setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp14").geom().create("r1", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r1")
         .set("pos", new double[]{-0.242, 0.15});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r1").set("rotconstr", true);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r1")
         .set("size", new double[]{0.484, 0.2});
    model.component("comp1").geom("geom1").feature("wp14").geom().create("r2", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r2")
         .set("pos", new double[]{-0.242, -0.1});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r2").set("rotconstr", true);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r2")
         .set("size", new double[]{0.484, 0.2});
    model.component("comp1").geom("geom1").feature("wp14").geom().create("r3", "Rectangle");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r3")
         .set("pos", new double[]{-0.242, -0.35});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r3").set("rotconstr", true);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("r3")
         .set("size", new double[]{0.484, 0.2});
    model.component("comp1").geom("geom1").feature("wp14").geom().create("eqdist1", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist1")
         .set("helppoint1", new double[]{-0.2584476172924042, 0.25418615341186523});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist1")
         .set("helppoint2", new double[]{0.2571365237236023, 0.24423915147781372});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist1")
         .set("helppoint3", new double[]{-0.24850067496299744, 0.11161303520202637});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist1")
         .set("helppoint4", new double[]{0.2488473653793335, 0.08508777618408203});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist1").selection("entity1")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist1").selection("entity2")
         .set("r1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist1").selection("entity3")
         .set("r2(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist1").selection("entity4")
         .set("r2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("eqdist2", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist2")
         .set("helppoint1", new double[]{-0.2584476172924042, 0.06353604793548584});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist2")
         .set("helppoint2", new double[]{0.24718952178955078, 0.04695779085159302});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist2")
         .set("helppoint3", new double[]{-0.2534741461277008, -0.18845364451408386});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist2")
         .set("helppoint4", new double[]{0.2488473653793335, -0.19508495926856995});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist2").selection("entity1")
         .set("r2(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist2").selection("entity2")
         .set("r2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist2").selection("entity3")
         .set("r3(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist2").selection("entity4")
         .set("r3(1)", 2);

    return model;
  }

  public static Model run4(Model model) {
    model.component("comp1").geom("geom1").feature("wp14").geom().create("eqdist3", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist3")
         .set("helppoint1", new double[]{-0.2451850175857544, 0.2525283098220825});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist3")
         .set("helppoint2", new double[]{-0.30155113339424133, 0.2525283098220825});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist3")
         .set("helppoint3", new double[]{-0.10426974296569824, 0.39344358444213867});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist3")
         .set("helppoint4", new double[]{-0.10758540034294128, 0.3138679265975952});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist3").selection("entity1")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist3").selection("entity2")
         .set("proj1.wp13", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist3").selection("entity3")
         .set("proj1.ext6", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist3").selection("entity4")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("eqdist4", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist4")
         .set("helppoint1", new double[]{-0.177214115858078, -0.4023132920265198});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist4")
         .set("helppoint2", new double[]{-0.16726717352867126, -0.3194219470024109});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist4")
         .set("helppoint3", new double[]{-0.2551319897174835, -0.2597402036190033});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist4")
         .set("helppoint4", new double[]{-0.2982354760169983, -0.27134495973587036});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist4").selection("entity1")
         .set("proj1.ext6", 2);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist4").selection("entity2")
         .set("r3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist4").selection("entity3")
         .set("r3(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist4").selection("entity4")
         .set("proj1.wp13", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("eqdist5", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist5")
         .set("helppoint1", new double[]{-0.2534741461277008, -0.23321497440338135});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist5")
         .set("helppoint2", new double[]{-0.28828853368759155, -0.1834801733493805});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist5")
         .set("helppoint3", new double[]{-0.03795665502548218, -0.15032362937927246});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist5")
         .set("helppoint4", new double[]{-0.0628240704536438, 0.0021964311599731445});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist5").selection("entity1")
         .set("r3(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist5").selection("entity2")
         .set("proj1.wp13", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist5").selection("entity3")
         .set("r3(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist5").selection("entity4")
         .set("r2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("eqdist6", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist6")
         .set("helppoint1", new double[]{-0.14405760169029236, 0.3967592716217041});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist6")
         .set("helppoint2", new double[]{-0.14571541547775269, 0.35531359910964966});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist6")
         .set("helppoint3", new double[]{-0.12582150101661682, 0.15471655130386353});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist6")
         .set("helppoint4", new double[]{-0.16560935974121094, 0.204451322555542});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist6").selection("entity1")
         .set("proj1.ext6", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist6").selection("entity2")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist6").selection("entity3")
         .set("r2(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist6").selection("entity4")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("coi1", "Coincident");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi1")
         .set("helppoint1", new double[]{-0.24186939001083374, -0.1602706015110016});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi1")
         .set("helppoint2", new double[]{-0.24850067496299744, -0.07903707027435303});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi1").selection("entity1")
         .set("r3(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi1").selection("entity2")
         .set("r2(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("coi2", "Coincident");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi2")
         .set("helppoint1", new double[]{-0.2468428611755371, 0.13979607820510864});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi2")
         .set("helppoint2", new double[]{-0.24850067496299744, 0.1978200078010559});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi2").selection("entity1")
         .set("r2(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi2").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi2").selection("entity2")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("eqdist7", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist7")
         .set("helppoint1", new double[]{-0.19213458895683289, 0.35365575551986694});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist7")
         .set("helppoint2", new double[]{-0.14074194431304932, 0.16300565004348755});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist7")
         .set("helppoint3", new double[]{-0.1622936725616455, 0.11161303520202637});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist7")
         .set("helppoint4", new double[]{-0.1689249873161316, -0.1221405565738678});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist7").selection("entity1")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist7").selection("entity2")
         .set("r1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist7").selection("entity3")
         .set("r2(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist7").selection("entity4")
         .set("r2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("eqdist8", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist8")
         .set("helppoint1", new double[]{-0.17887195944786072, -0.08235272765159607});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist8")
         .set("helppoint2", new double[]{-0.1540045440196991, 0.12321782112121582});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist8")
         .set("helppoint3", new double[]{0.18750780820846558, -0.12048271298408508});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist8")
         .set("helppoint4", new double[]{0.16761386394500732, -0.359209805727005});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist8").selection("entity1")
         .set("r2(1)", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist8").selection("entity2")
         .set("r2(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist8").selection("entity3")
         .set("r3(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("eqdist8").selection("entity4")
         .set("r3(1)", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("coi3", "Coincident");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi3")
         .set("helppoint1", new double[]{-0.24916981132075497, 0.35716981132075476});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi3")
         .set("helppoint2", new double[]{-0.2435271143913269, 0.4133375287055969});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi3").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi3").selection("entity1")
         .set("r1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi3").selection("entity2")
         .set("proj1.ext7", 1);
    model.component("comp1").geom("geom1").feature("wp14").geom().create("coi4", "Coincident");
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi4")
         .set("helppoint1", new double[]{0.250505268573761, 0.34702450037002563});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi4")
         .set("helppoint2", new double[]{0.24553179740905762, 0.42328453063964844});
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi4").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi4").selection("entity1")
         .set("r1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp14").geom().feature("coi4").selection("entity2")
         .set("proj1.ext7", 2);
    model.component("comp1").geom("geom1").create("ext8", "Extrude");
    model.component("comp1").geom("geom1").feature("ext8").set("specify", "vertices");
    model.component("comp1").geom("geom1").feature("ext8").selection("input").set("wp14");
    model.component("comp1").geom("geom1").feature("ext8").selection("vertex").set("ext6(1)", 8);
    model.component("comp1").geom("geom1").create("uni2", "Union");
    model.component("comp1").geom("geom1").feature("uni2").set("intbnd", false);
    model.component("comp1").geom("geom1").feature("uni2").selection("input").set("ext6", "ext7", "ext8");
    model.component("comp1").geom("geom1").create("wp7", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp7").label("Work Plane 7 - Stove pipe cross section");
    model.component("comp1").geom("geom1").feature("wp7").set("planetype", "faceparallel");
    model.component("comp1").geom("geom1").feature("wp7").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp7").selection("face").set("uni2(1)", 4);
    model.component("comp1").geom("geom1").feature("wp7").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("proj1").set("project", "bnd");
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("proj1").selection("input")
         .set("uni2(1)", 4);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("proj1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp7").geom().create("c1", "Circle");
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("c1").set("pos", new double[]{0, -0.255});
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("c1").set("r", 0.06);
    model.component("comp1").geom("geom1").feature("wp7").geom().create("eqdist1", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1")
         .set("helppoint1", new double[]{-0.23767834901809692, 0.25428521633148193});
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1")
         .set("helppoint2", new double[]{0.014290690422058105, -0.12707316875457764});
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1")
         .set("helppoint3", new double[]{0.014290690422058105, -0.12707316875457764});
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1")
         .set("helppoint4", new double[]{0.20837485790252686, -0.014708638191223145});
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1").selection("entity1")
         .set("proj1.uni2", 1);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1").selection("entity2")
         .set("c1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1").selection("entity3").init(0);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1").selection("entity3")
         .set("c1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("eqdist1").selection("entity4")
         .set("proj1.uni2", 6);
    model.component("comp1").geom("geom1").feature("wp7").geom().create("rad1", "Radius");
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("rad1").set("radius", "60[mm]");
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("rad1").selection("edge").set("c1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp7").geom().create("ydist1", "YDistance");
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("ydist1").set("distance", 0.65);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("ydist1")
         .set("helppoint1", new double[]{-0.006139278411865234, -0.1406930685043335});
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("ydist1")
         .set("helppoint2", new double[]{-0.23086833953857422, 0.42112958431243896});
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("ydist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("ydist1").selection("entity1")
         .set("c1(1)", 5);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("ydist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp7").geom().feature("ydist1").selection("entity2")
         .set("proj1.uni2", 2);
    model.component("comp1").geom("geom1").create("wp8", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp8").label("Work Plane 8 - sweep path stove pipe 1");
    model.component("comp1").geom("geom1").feature("wp8").set("planetype", "faceparallel");
    model.component("comp1").geom("geom1").feature("wp8").set("offsettype", "vertex");
    model.component("comp1").geom("geom1").feature("wp8").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp8").set("showcoincident", false);
    model.component("comp1").geom("geom1").feature("wp8").set("showintersection", false);
    model.component("comp1").geom("geom1").feature("wp8").set("showprojection", false);
    model.component("comp1").geom("geom1").feature("wp8").selection("offsetvertex").set("wp7.c1", 2);
    model.component("comp1").geom("geom1").feature("wp8").selection("face").set("uni2(1)", 17);
    model.component("comp1").geom("geom1").feature("wp8").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp8").geom().constrDimBuild("uptotarget");
    model.component("comp1").geom("geom1").feature("wp8").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("proj1").set("project", "vtx");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("proj1").selection("input")
         .set("wp7.c1", 2, 3, 4);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("proj1").selection("input")
         .set("uni2(1)", 2);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("proj1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp8").geom().create("cro1", "CrossSection");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cro1").set("intersect", "selected");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cro1").selection("input").set("blk1");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cro1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp8").geom().create("cc1", "CompositeCurve");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").set("type", "solid");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").create("ca1", "CircularArc");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").feature("ca1")
         .set("center", new double[]{-0.292, -0.38});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").feature("ca1").set("r", 0.125);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").feature("ca1").set("angle1", 90);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").feature("ca1").set("angle2", 180);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").create("pol2", "Polygon");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").feature("pol2")
         .set("source", "table");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("cc1").feature("pol2")
         .set("table", new double[][]{{-0.417, -0.38}, {-0.417, -1.555}});
    model.component("comp1").geom("geom1").feature("wp8").geom().create("ver1", "Vertical");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("ver1").selection("edge")
         .set("cc1(1)", 2, 3);
    model.component("comp1").geom("geom1").feature("wp8").geom().create("tanc2", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("tanc2").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("tanc2").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("tanc2")
         .set("helppoint1", new double[]{-1.0184452533721924, -1.175253987312317});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("tanc2")
         .set("helppoint2", new double[]{-0.417, -0.38});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("tanc2").selection("edge1")
         .set("cc1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("tanc2").selection("vertex1")
         .set("cc1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("tanc2").selection("edge2")
         .set("cc1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("tanc2").selection("vertex2")
         .set("cc1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp8").geom().create("xdist1", "XDistance");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("xdist1").set("distance", "125[mm]");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("xdist1")
         .set("helppoint1", new double[]{-1.0315797328948975, -1.2942079305648804});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("xdist1")
         .set("helppoint2", new double[]{-0.24827659130096436, -0.8837067484855652});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("xdist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("xdist1").selection("entity1")
         .set("cc1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("xdist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("xdist1").selection("entity2")
         .set("cc1(1)", 4);
    model.component("comp1").geom("geom1").feature("wp8").geom().create("coi1", "Coincident");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("coi1")
         .set("helppoint1", new double[]{-0.2775980234146118, -0.26376616954803467});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("coi1")
         .set("helppoint2", new double[]{-0.1016688346862793, -0.3642970323562622});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("coi1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("coi1").selection("entity1")
         .set("proj1.wp7", 2);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("coi1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("coi1").selection("entity2")
         .set("cc1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp8").geom().create("ydist1", "YDistance");
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("ydist1").set("distance", 1.3);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("ydist1")
         .set("helppoint1", new double[]{-0.6100132465362549, -1.0833746194839478});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("ydist1")
         .set("helppoint2", new double[]{-0.292, -0.255});
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("ydist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("ydist1").selection("entity1")
         .set("cc1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp8").geom().feature("ydist1").selection("entity2")
         .set("cc1(1)", 1);
    model.component("comp1").geom("geom1").create("wp9", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp9").label("Work Plane 9 - sweep path stove pipe 2");
    model.component("comp1").geom("geom1").feature("wp9").set("quickz", 2.4);
    model.component("comp1").geom("geom1").feature("wp9").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp9").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("proj1").selection("input")
         .set("ext5", "wp8");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("proj1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp9").geom().create("pol1", "Polygon");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("pol1").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("pol1").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("pol1")
         .set("table", new double[][]{{0, 3.6}, {0.15418989666066257, 3.6}});
    model.component("comp1").geom("geom1").feature("wp9").geom().create("hor1", "Horizontal");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("hor1").selection("edge").set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp9").geom().create("coi1", "Coincident");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("coi1")
         .set("helppoint1", new double[]{0.4689173698425293, 3.29413104057312});
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("coi1")
         .set("helppoint2", new double[]{-0.00426030158996582, 3.20428729057312});
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("coi1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("coi1").selection("entity1")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("coi1").selection("entity2")
         .set("proj1.ext5", 3);
    model.component("comp1").geom("geom1").feature("wp9").geom().create("eqdist1", "EqualDistance");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1")
         .set("helppoint1", new double[]{-0.009832918643951416, 4.007316589355469});
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1")
         .set("helppoint2", new double[]{0.05144137144088745, 3.096194267272949});
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1")
         .set("helppoint3", new double[]{0.05144137144088745, 3.096194267272949});
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1")
         .set("helppoint4", new double[]{8.234977722167969E-4, 2.5047640800476074});
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1").selection("entity1")
         .set("proj1.ext5", 3);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1").selection("entity2")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1").selection("entity3")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1").selection("entity4").init(0);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("eqdist1").selection("entity4")
         .set("proj1.ext5", 2);
    model.component("comp1").geom("geom1").feature("wp9").geom().create("xdist1", "XDistance");
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("xdist1")
         .set("helppoint1", new double[]{0.04866749048233032, 3.595959186553955});
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("xdist1")
         .set("helppoint2", new double[]{0.15888619422912598, 5.508814334869385});
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("xdist1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("xdist1").selection("entity1")
         .set("pol1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("xdist1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp9").geom().feature("xdist1").selection("entity2")
         .set("proj1.wp8", 1);
    model.component("comp1").geom("geom1").feature("wp9").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").create("wp10", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp10").label("Work Plane 10 - sweep path stove pipe 3");
    model.component("comp1").geom("geom1").feature("wp10").set("planetype", "vertices");
    model.component("comp1").geom("geom1").feature("wp10").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp10").set("showcoincident", false);
    model.component("comp1").geom("geom1").feature("wp10").set("showintersection", false);
    model.component("comp1").geom("geom1").feature("wp10").set("showprojection", false);
    model.component("comp1").geom("geom1").feature("wp10").selection("vertex1").set("wp8.cc1", 1);
    model.component("comp1").geom("geom1").feature("wp10").selection("vertex2").set("wp8.cc1", 2);
    model.component("comp1").geom("geom1").feature("wp10").selection("vertex3").set("wp9.pol1", 2);
    model.component("comp1").geom("geom1").feature("wp10").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("proj1").selection("input")
         .set("wp8", "wp9");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("proj1")
         .setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp10").geom().create("cro1", "CrossSection");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("cro1").set("intersect", "selected");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("cro1").selection("input").set("wp9");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("cro1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp10").geom().create("pol1", "Polygon");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("pol1").set("type", "open");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("pol1").set("source", "table");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("pol1")
         .set("table", new double[][]{{-0.295, 1.9169547859940228}, {-0.19969650154290972, 0.18898604196836188}});
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("pol1").setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp10").geom().create("coi1", "Coincident");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi1").selection("entity1")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi1").selection("entity2")
         .set("cro1.wp9", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().create("ca1", "CircularArc");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("ca1").set("center", new double[]{0, 0.2});
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("ca1").set("r", 0.2);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("ca1").set("angle1", -176.8431364449632);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("ca1").set("angle2", -90);
    model.component("comp1").geom("geom1").feature("wp10").geom().create("rad1", "Radius");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("rad1").set("radius", 0.2);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("rad1").selection("edge").set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().create("coi2", "Coincident");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi2").selection("entity1")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi2").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi2").selection("entity2")
         .set("pol1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp10").geom().create("coi3", "Coincident");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi3")
         .set("helppoint1", new double[]{0.06563907861709595, 0.07633350044488907});
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi3")
         .set("helppoint2", new double[]{-0.017281949520111084, 0.001704581081867218});

    return model;
  }

  public static Model run5(Model model) {
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi3").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi3").selection("entity1")
         .set("ca1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi3").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("coi3").selection("entity2")
         .set("proj1.wp8", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().create("tanc1", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc1").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc1").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc1")
         .set("helppoint1", new double[]{1.2246467991473533E-17, 0});
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc1")
         .set("helppoint2", new double[]{0.3226943612098694, 0.009996682405471802});
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc1").selection("edge1")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc1").selection("vertex1")
         .set("ca1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc1").selection("edge2")
         .set("proj1.wp8", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc1").selection("vertex2")
         .set("ca1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp10").geom().create("tanc2", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc2").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc2").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc2")
         .set("helppoint1", new double[]{-0.3033595085144043, 0.8474990725517273});
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc2")
         .set("helppoint2", new double[]{-0.19969650154290966, 0.1889860419683619});
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc2").selection("edge1")
         .set("pol1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc2").selection("vertex1")
         .set("pol1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc2").selection("edge2")
         .set("ca1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp10").geom().feature("tanc2").selection("vertex2")
         .set("pol1(1)", 2);
    model.component("comp1").geom("geom1").create("wp11", "WorkPlane");
    model.component("comp1").geom("geom1").feature("wp11").label("Work Plane 11 - sweep path stove pipe 4");
    model.component("comp1").geom("geom1").feature("wp11").set("planetype", "vertices");
    model.component("comp1").geom("geom1").feature("wp11").set("unite", true);
    model.component("comp1").geom("geom1").feature("wp11").selection("vertex1").set("wp9.pol1", 1);
    model.component("comp1").geom("geom1").feature("wp11").selection("vertex2").set("wp9.pol1", 2);
    model.component("comp1").geom("geom1").feature("wp11").selection("vertex3").set("wp10.ca1", 1);
    model.component("comp1").geom("geom1").feature("wp11").geom().useConstrDim(true);
    model.component("comp1").geom("geom1").feature("wp11").geom().constrDimBuild("uptotarget");
    model.component("comp1").geom("geom1").feature("wp11").geom().create("proj1", "Projection");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("proj1").set("project", "obj");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("proj1").selection("input")
         .set("wp10", "wp9");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("proj1")
         .setAttribute("construction", "on");
    model.component("comp1").geom("geom1").feature("wp11").geom().create("cc1", "CompositeCurve");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").set("type", "solid");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").create("ca1", "CircularArc");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").feature("ca1")
         .set("center", new double[]{0, 0.154189896660662});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").feature("ca1")
         .set("r", 0.15418989666066243);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").feature("ca1").set("angle1", -90);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").feature("ca1").set("angle2", 0);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").create("pol1", "Polygon");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").feature("pol1")
         .set("source", "table");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("cc1").feature("pol1")
         .set("table", new double[][]{{0.15418989666066243, 0.154189896660662}, {0.15418989666066246, 1.7305949084484737}});
    model.component("comp1").geom("geom1").feature("wp11").geom().create("coi1", "Coincident");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi1")
         .set("helppoint1", new double[]{0.3678239583969116, 2.1511974334716797});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi1")
         .set("helppoint2", new double[]{0.3169099688529968, 2.053006172180176});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi1").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi1").selection("entity1")
         .set("cc1(1)", 3);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi1").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi1").selection("entity2")
         .set("proj1.wp10", 1);
    model.component("comp1").geom("geom1").feature("wp11").geom().create("coi2", "Coincident");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi2")
         .set("helppoint1", new double[]{0.1524747908115387, -0.005842685699462891});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi2")
         .set("helppoint2", new double[]{0.12722620368003845, -0.08547276258468628});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi2").selection("entity1").init(0);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi2").selection("entity1")
         .set("proj1.wp9", 1);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi2").selection("entity2").init(0);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("coi2").selection("entity2")
         .set("cc1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp11").geom().create("tanc1", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc1").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc1").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc1")
         .set("helppoint1", new double[]{0.15418989666066246, 1.7305949084484737});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc1")
         .set("helppoint2", new double[]{0.14547091722488403, 1.6902472972869873});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc1").selection("edge1")
         .set("proj1.wp10", 1);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc1").selection("vertex1")
         .set("proj1.wp10", 1);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc1").selection("edge2")
         .set("cc1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc1").selection("vertex2")
         .set("proj1.wp10", 1);
    model.component("comp1").geom("geom1").feature("wp11").geom().create("tanc2", "TangentConstraint");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc2").set("point1", "vertex");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc2").set("point2", "vertex");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc2")
         .set("helppoint1", new double[]{0.15418989666066243, 0.154189896660662});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc2")
         .set("helppoint2", new double[]{0.056179314851760864, 0.15164388716220856});
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc2").selection("edge1")
         .set("cc1(1)", 1);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc2").selection("vertex1")
         .set("cc1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc2").selection("edge2")
         .set("cc1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("tanc2").selection("vertex2")
         .set("cc1(1)", 2);
    model.component("comp1").geom("geom1").feature("wp11").geom().create("ver1", "Vertical");
    model.component("comp1").geom("geom1").feature("wp11").geom().feature("ver1").selection("edge").set("cc1(1)", 3);
    model.component("comp1").geom("geom1").create("swe2", "Sweep");
    model.component("comp1").geom("geom1").feature("swe2").set("crossfaces", true);
    model.component("comp1").geom("geom1").feature("swe2").set("keep", false);
    model.component("comp1").geom("geom1").feature("swe2").set("includefinal", false);
    model.component("comp1").geom("geom1").feature("swe2").selection("enttosweep").set("wp7.c1", 1);
    model.component("comp1").geom("geom1").feature("swe2").selection("edge").set("wp8.cc1", 1, 2);
    model.component("comp1").geom("geom1").feature("swe2").selection("edge").set("wp10.ca1", 1);
    model.component("comp1").geom("geom1").feature("swe2").selection("edge").set("wp11.cc1", 1, 2);
    model.component("comp1").geom("geom1").feature("swe2").selection("diredge").set("wp8.cc1", 1);
    model.component("comp1").geom("geom1").nodeGroup().create("grp3");
    model.component("comp1").geom("geom1").nodeGroup("grp3").label("TV and TV table");
    model.component("comp1").geom("geom1").nodeGroup("grp3").placeAfter("blk5");
    model.component("comp1").geom("geom1").nodeGroup("grp3").add("wp5");
    model.component("comp1").geom("geom1").nodeGroup("grp3").add("ext4");
    model.component("comp1").geom("geom1").nodeGroup("grp3").add("ext5");
    model.component("comp1").geom("geom1").nodeGroup().create("grp1");
    model.component("comp1").geom("geom1").nodeGroup("grp1").label("Couch and coffee table");
    model.component("comp1").geom("geom1").nodeGroup("grp1").placeAfter("blk5");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("wp1");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("ext1");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("wp2");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("ext2");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("wp3");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("swe1");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("uni1");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("fil1");
    model.component("comp1").geom("geom1").nodeGroup("grp1").add("fil2");
    model.component("comp1").geom("geom1").nodeGroup().create("grp2");
    model.component("comp1").geom("geom1").nodeGroup("grp2").label("Shelf");
    model.component("comp1").geom("geom1").nodeGroup("grp2").placeAfter("blk5");
    model.component("comp1").geom("geom1").nodeGroup("grp2").add("wp4");
    model.component("comp1").geom("geom1").nodeGroup("grp2").add("ext3");
    model.component("comp1").geom("geom1").nodeGroup().create("grp4");
    model.component("comp1").geom("geom1").nodeGroup("grp4").label("Stovepipe");
    model.component("comp1").geom("geom1").nodeGroup("grp4").placeAfter("uni2");
    model.component("comp1").geom("geom1").nodeGroup("grp4").add("wp7");
    model.component("comp1").geom("geom1").nodeGroup("grp4").add("wp8");
    model.component("comp1").geom("geom1").nodeGroup("grp4").add("wp9");
    model.component("comp1").geom("geom1").nodeGroup("grp4").add("wp10");
    model.component("comp1").geom("geom1").nodeGroup("grp4").add("wp11");
    model.component("comp1").geom("geom1").nodeGroup("grp4").add("swe2");
    model.component("comp1").geom("geom1").run();
    model.component("comp1").geom("geom1").run("fin");

    model.component("comp1").selection().create("sel1", "Explicit");
    model.component("comp1").selection("sel1").set(0, 2, 4, 5, 6, 7, 8, 9, 10, 11, 14, 15);
    model.component("comp1").selection().create("adj1", "Adjacent");
    model.component("comp1").selection().create("sel2", "Explicit");
    model.component("comp1").selection("sel2").geom("geom1", 2);
    model.component("comp1").selection("sel2").set(1, 156, 158, 288);
    model.component("comp1").selection().create("dif1", "Difference");
    model.component("comp1").selection("dif1").set("entitydim", 2);
    model.component("comp1").selection().create("sel3", "Explicit");
    model.component("comp1").selection("sel3").set(11);
    model.component("comp1").selection().create("adj2", "Adjacent");
    model.component("comp1").selection().create("sel4", "Explicit");
    model.component("comp1").selection("sel4").set(6, 7, 8, 9, 10);
    model.component("comp1").selection().create("adj3", "Adjacent");
    model.component("comp1").selection("sel1").label("Opaque domains");
    model.component("comp1").selection("adj1").label("Adjacent to opaque");
    model.component("comp1").selection("adj1").set("input", new String[]{"sel1"});
    model.component("comp1").selection("sel2").label("Door and windows");
    model.component("comp1").selection("dif1").label("Radiating surfaces");
    model.component("comp1").selection("dif1").set("add", new String[]{"adj1"});
    model.component("comp1").selection("dif1").set("subtract", new String[]{"sel2"});
    model.component("comp1").selection("sel3").label("Stove");
    model.component("comp1").selection("adj2").label("Adjacent to Stove");
    model.component("comp1").selection("adj2").set("input", new String[]{"sel3"});
    model.component("comp1").selection("sel4").label("Stove Chimney");
    model.component("comp1").selection("adj3").label("Adjacent to Stove Chimney");
    model.component("comp1").selection("adj3").set("input", new String[]{"sel4"});
    model.component("comp1").selection().create("box1", "Box");
    model.component("comp1").selection("box1").set("entitydim", 2);
    model.component("comp1").selection("box1").set("xmin", 2);
    model.component("comp1").selection("box1").set("ymin", 1);
    model.component("comp1").selection("box1").set("xmax", 5.5);
    model.component("comp1").selection("box1").set("ymax", 6);
    model.component("comp1").selection("box1").set("zmin", 0.2);
    model.component("comp1").selection("box1").set("zmax", 2);
    model.component("comp1").selection("box1").set("zmin", 0.41);
    model.component("comp1").selection("box1").set("xmin", 2.5);
    model.component("comp1").selection("box1").label("Couch coating");

    model.component("comp1").view("view2").axis().set("xmin", -0.13963612914085388);
    model.component("comp1").view("view2").axis().set("xmax", 7.231224536895752);
    model.component("comp1").view("view2").axis().set("ymin", 0.4386848211288452);
    model.component("comp1").view("view2").axis().set("ymax", 6.219427585601807);
    model.component("comp1").view("view3").axis().set("xmin", -5.521582126617432);
    model.component("comp1").view("view3").axis().set("xmax", 4.766582012176514);
    model.component("comp1").view("view3").axis().set("ymin", -3.9668474197387695);
    model.component("comp1").view("view3").axis().set("ymax", 4.1018476486206055);
    model.component("comp1").view("view4").axis().set("xmin", 0.26527807116508484);
    model.component("comp1").view("view4").axis().set("xmax", 2.3716652393341064);
    model.component("comp1").view("view4").axis().set("ymin", 3.868081569671631);
    model.component("comp1").view("view4").axis().set("ymax", 5.520057201385498);
    model.component("comp1").view("view5").axis().set("xmin", -1.8375705480575562);
    model.component("comp1").view("view5").axis().set("xmax", 0.7155340909957886);
    model.component("comp1").view("view5").axis().set("ymin", 1.1439279317855835);
    model.component("comp1").view("view5").axis().set("ymax", 3.1462507247924805);
    model.component("comp1").view("view6").axis().set("xmin", 0.8513031601905823);
    model.component("comp1").view("view6").axis().set("xmax", 6.280721187591553);
    model.component("comp1").view("view6").axis().set("ymin", -0.6897754669189453);
    model.component("comp1").view("view6").axis().set("ymax", 3.568352699279785);
    model.component("comp1").view("view7").axis().set("xmin", -2.0916547775268555);
    model.component("comp1").view("view7").axis().set("xmax", 6.961301803588867);
    model.component("comp1").view("view7").axis().set("ymin", 0.09034215658903122);
    model.component("comp1").view("view7").axis().set("ymax", 7.190301418304443);
    model.component("comp1").view("view8").axis().set("xmin", -1.0514698028564453);
    model.component("comp1").view("view8").axis().set("xmax", 1.9789670705795288);
    model.component("comp1").view("view8").axis().set("ymin", -1.5129022598266602);
    model.component("comp1").view("view8").axis().set("ymax", 0.8637776970863342);
    model.component("comp1").view("view9").axis().set("xmin", -2.7692253589630127);
    model.component("comp1").view("view9").axis().set("xmax", 6.064722061157227);
    model.component("comp1").view("view9").axis().set("ymin", -3.923779249191284);
    model.component("comp1").view("view9").axis().set("ymax", 3.004417657852173);
    model.component("comp1").view("view10").axis().set("xmin", -1.2341623306274414);
    model.component("comp1").view("view10").axis().set("xmax", 7.345833778381348);
    model.component("comp1").view("view10").axis().set("ymin", 0.2746419608592987);
    model.component("comp1").view("view10").axis().set("ymax", 7.003673076629639);
    model.component("comp1").view("view11").axis().set("xmin", -0.5529927015304565);
    model.component("comp1").view("view11").axis().set("xmax", -0.17570284008979797);
    model.component("comp1").view("view11").axis().set("ymin", 2.0591540336608887);
    model.component("comp1").view("view11").axis().set("ymax", 2.355051040649414);
    model.component("comp1").view("view12").axis().set("xmin", -1.0517468452453613);
    model.component("comp1").view("view12").axis().set("xmax", 1.6859328746795654);
    model.component("comp1").view("view12").axis().set("ymin", 0.5763362050056458);
    model.component("comp1").view("view12").axis().set("ymax", 2.7234153747558594);

    model.component("comp1").material().create("mat1", "Common");
    model.component("comp1").material("mat1").label("Varnished wood");
    model.component("comp1").material("mat1").selection().named("dif1");
    model.component("comp1").material("mat1").propertyGroup("def").set("emissivity", new String[]{"0.93"});
    model.component("comp1").material("mat1").set("family", "custom");
    model.component("comp1").material("mat1")
         .set("customspecular", new double[]{0.5137255191802979, 0.4627451002597809, 0.26274511218070984});
    model.component("comp1").material("mat1")
         .set("customdiffuse", new double[]{0.572549045085907, 0.4745098054409027, 0.2549019753932953});
    model.component("comp1").material("mat1")
         .set("customambient", new double[]{0.9058823529411765, 0.6784313725490196, 0.47843137254901963});
    model.component("comp1").material("mat1").set("noise", true);
    model.component("comp1").material("mat1").set("normalnoisetype", "1");
    model.component("comp1").material("mat1").set("noisescale", 1);
    model.component("comp1").material("mat1").set("noisefreq", 4);
    model.component("comp1").material("mat1").set("roughness", 1);
    model.component("comp1").material("mat1").set("metallic", 0);
    model.component("comp1").material("mat1").set("pearl", 0);
    model.component("comp1").material("mat1").set("diffusewrap", 0.15);
    model.component("comp1").material("mat1").set("clearcoat", 0);
    model.component("comp1").material("mat1").set("reflectance", 0);
    model.component("comp1").material().create("mat2", "Common");
    model.component("comp1").material("mat2").label("Painted cast iron");
    model.component("comp1").material("mat2").selection().named("adj2");
    model.component("comp1").material("mat2").propertyGroup("def").set("emissivity", new String[]{"0.95"});
    model.component("comp1").material("mat2").set("family", "iron");
    model.component("comp1").material().create("mat3", "Common");
    model.component("comp1").material("mat3").label("Glass");
    model.component("comp1").material("mat3").selection().geom("geom1", 2);
    model.component("comp1").material("mat3").selection().set(126);
    model.component("comp1").material("mat3").propertyGroup("def").set("emissivity", new String[]{"0.9"});
    model.component("comp1").material("mat3").set("family", "custom");
    model.component("comp1").material("mat3")
         .set("customspecular", new double[]{0.8588235378265381, 0.8588235378265381, 0.8588235378265381});
    model.component("comp1").material("mat3")
         .set("customdiffuse", new double[]{1, 0.3921568691730499, 0.3921568691730499});
    model.component("comp1").material("mat3").set("customambient", new double[]{1, 1, 0.5176470875740051});
    model.component("comp1").material("mat3").set("fresnel", 0.35);
    model.component("comp1").material("mat3").set("roughness", 0.85);
    model.component("comp1").material("mat3").set("metallic", 0);
    model.component("comp1").material("mat3").set("pearl", 0.05);
    model.component("comp1").material("mat3").set("diffusewrap", 0.45);
    model.component("comp1").material("mat3").set("clearcoat", 0.3);
    model.component("comp1").material("mat3").set("reflectance", 0.35);
    model.component("comp1").material().create("mat4", "Common");
    model.component("comp1").material("mat4").label("AISI steel 304");
    model.component("comp1").material("mat4").selection().named("adj3");
    model.component("comp1").material("mat4").propertyGroup("def").set("emissivity", new String[]{"0.15"});
    model.component("comp1").material("mat4").set("family", "custom");
    model.component("comp1").material("mat4")
         .set("customambient", new double[]{0.4156862795352936, 0.4156862795352936, 0.4156862795352936});
    model.component("comp1").material().create("mat5", "Common");
    model.component("comp1").material("mat5").label("Reflective plate");
    model.component("comp1").material("mat5").selection().geom("geom1", 2);
    model.component("comp1").material("mat5").selection().set(37, 38, 39);
    model.component("comp1").material("mat5").propertyGroup("def").set("emissivity", new String[]{"0.3"});
    model.component("comp1").material("mat5").set("family", "steelbrushed");
    model.component("comp1").material().create("mat6", "Common");
    model.component("comp1").material("mat6").label("Painted walls");
    model.component("comp1").material("mat6").selection().geom("geom1", 2);
    model.component("comp1").material("mat6").selection()
         .set(2, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 18, 22, 26, 40, 43, 47, 51, 114, 118, 142, 152, 154, 155, 157, 160, 223, 231, 282, 284, 285, 286, 287);
    model.component("comp1").material("mat6").propertyGroup("def").set("emissivity", new String[]{"0.77"});
    model.component("comp1").material("mat6").set("family", "custom");
    model.component("comp1").material("mat6")
         .set("customspecular", new double[]{0.9803921568627451, 0.9803921568627451, 0.9803921568627451});
    model.component("comp1").material("mat6")
         .set("customdiffuse", new double[]{0.9137254953384399, 0.9176470637321472, 0.929411768913269});
    model.component("comp1").material("mat6")
         .set("customambient", new double[]{0.8392156958580017, 0.8392156958580017, 0.8392156958580017});
    model.component("comp1").material("mat6").set("noise", true);
    model.component("comp1").material("mat6").set("noisescale", 0.3);
    model.component("comp1").material("mat6").set("noisefreq", 3);
    model.component("comp1").material("mat6").set("roughness", 0.62);
    model.component("comp1").material("mat6").set("metallic", 0);
    model.component("comp1").material("mat6").set("pearl", 0);
    model.component("comp1").material("mat6").set("diffusewrap", 0);
    model.component("comp1").material("mat6").set("clearcoat", 0);
    model.component("comp1").material("mat6").set("reflectance", 0);
    model.component("comp1").material().create("mat7", "Common");
    model.component("comp1").material("mat7").label("Parquet flooring");
    model.component("comp1").material("mat7").selection().geom("geom1", 2);
    model.component("comp1").material("mat7").selection()
         .set(3, 8, 20, 24, 41, 99, 104, 112, 132, 136, 140, 146, 159, 181, 188);
    model.component("comp1").material("mat7").propertyGroup("def").set("emissivity", new String[]{"0.9"});
    model.component("comp1").material("mat7").set("family", "custom");
    model.component("comp1").material("mat7").set("customspecular", new double[]{1, 1, 0.7254901960784313});
    model.component("comp1").material("mat7")
         .set("customdiffuse", new double[]{0.9882352941176471, 0.8823529411764706, 0.6627450980392157});
    model.component("comp1").material("mat7")
         .set("customambient", new double[]{0.9058823529411765, 0.6784313725490196, 0.47843137254901963});
    model.component("comp1").material("mat7").set("noise", true);
    model.component("comp1").material("mat7").set("noisescale", 1);
    model.component("comp1").material("mat7").set("noisefreq", 4);
    model.component("comp1").material("mat7").set("normalnoisebrush", "2");
    model.component("comp1").material("mat7").set("colornoise", true);
    model.component("comp1").material("mat7").set("colornoisescale", 1.5);
    model.component("comp1").material("mat7").set("colornoisefrequency", 4);
    model.component("comp1").material("mat7").set("colornoisebrush", "2");
    model.component("comp1").material("mat7")
         .set("customnoisecolor", new double[]{0.8549019607843137, 0.7607843137254902, 0.7686274509803922});
    model.component("comp1").material("mat7").set("noisecolorblend", 1);
    model.component("comp1").material("mat7").set("roughness", 1);
    model.component("comp1").material("mat7").set("metallic", 0);
    model.component("comp1").material("mat7").set("pearl", 0);
    model.component("comp1").material("mat7").set("diffusewrap", 0.15);
    model.component("comp1").material("mat7").set("clearcoat", 0);
    model.component("comp1").material("mat7").set("reflectance", 0);
    model.component("comp1").material().create("mat8", "Common");
    model.component("comp1").material("mat8").label("Plastic");
    model.component("comp1").material("mat8").selection().geom("geom1", 2);
    model.component("comp1").material("mat8").selection().set(27, 28, 29, 35, 55);
    model.component("comp1").material("mat8").propertyGroup("def").set("emissivity", new String[]{"0.9"});
    model.component("comp1").material("mat8").set("color", "black");
    model.component("comp1").material().create("mat9", "Common");
    model.component("comp1").material("mat9").label("Leather");
    model.component("comp1").material("mat9").selection().named("box1");
    model.component("comp1").material("mat9").propertyGroup("def").set("emissivity", new String[]{"0.9"});
    model.component("comp1").material("mat9").set("family", "custom");
    model.component("comp1").material("mat9")
         .set("customspecular", new double[]{0.9803921568627451, 0.9803921568627451, 0.9803921568627451});
    model.component("comp1").material("mat9")
         .set("customdiffuse", new double[]{1, 0.6274510025978088, 0.47843137383461});
    model.component("comp1").material("mat9")
         .set("customambient", new double[]{1, 0.6274510025978088, 0.47843137383461});
    model.component("comp1").material("mat9").set("noise", true);
    model.component("comp1").material("mat9").set("normalnoisetype", "1");
    model.component("comp1").material("mat9").set("noisescale", 0.3);
    model.component("comp1").material("mat9").set("noisefreq", 4);
    model.component("comp1").material("mat9").set("colornoise", true);
    model.component("comp1").material("mat9").set("colornoisetype", "1");
    model.component("comp1").material("mat9").set("colornoisescale", 0.2);
    model.component("comp1").material("mat9").set("colornoisefrequency", 2);
    model.component("comp1").material("mat9")
         .set("customnoisecolor", new double[]{1, 0.9333333373069763, 0.8666666746139526});
    model.component("comp1").material("mat9").set("noisecolorblend", 0.2);
    model.component("comp1").material("mat9").set("fresnel", 0.45);
    model.component("comp1").material("mat9").set("roughness", 0.62);
    model.component("comp1").material("mat9").set("metallic", 0);
    model.component("comp1").material("mat9").set("pearl", 0);
    model.component("comp1").material("mat9").set("diffusewrap", 0);
    model.component("comp1").material("mat9").set("clearcoat", 0);
    model.component("comp1").material("mat9").set("reflectance", 0);

    model.component("comp1").common().create("minpt1", "CommonInputDef");
    model.component("comp1").common().create("minpt2", "CommonInputDef");
    model.component("comp1").common().create("minpt3", "CommonInputDef");
    model.component("comp1").common("minpt1").selection().named("adj2");
    model.component("comp1").common("minpt2").selection().named("adj3");
    model.component("comp1").common("minpt3").selection().geom("geom1", 2);
    model.component("comp1").common("minpt3").selection().set(126);
    model.component("comp1").common("minpt1").label("Stove, cast iron");
    model.component("comp1").common("minpt1").set("value", "160[degC]");
    model.component("comp1").common("minpt2").label("Chimney");
    model.component("comp1").common("minpt2").set("value", "90[degC]");
    model.component("comp1").common("minpt3").label("Stove, glass ");
    model.component("comp1").common("minpt3").set("value", "240[degC]");

    model.component("comp1").physics().create("rad", "SurfaceToSurfaceRadiation", "geom1");
    model.component("comp1").physics("rad").selection().named("dif1");
    model.component("comp1").physics("rad").prop("RadiationSettings").set("radiationMethod", "rayshooting");
    model.component("comp1").physics("rad").prop("RadiationSettings").set("functionResolution", 0);
    model.component("comp1").physics("rad").prop("RadiationSettings").set("storeViewFactors", true);
    model.component("comp1").physics("rad").prop("RadiationSettings").set("angularDependentProperties", "full");
    model.component("comp1").physics("rad").prop("RadiationSettings").set("selfIrradiation", false);
    model.component("comp1").physics("rad").prop("RadiationSettings").set("radiationResolutionRayShooting", 32);
    model.component("comp1").physics("rad").prop("ShapeProperty").set("order_surfaceradiosity_disc", 2);
    model.component("comp1").physics("rad").create("opac1", "Opacity", 3);
    model.component("comp1").physics("rad").feature("opac1").selection().named("sel1");
    model.component("comp1").physics("rad").create("opac2", "Opacity", 3);

    return model;
  }

  public static Model run6(Model model) throws IOException {
    model.component("comp1").physics("rad").feature("opac2").selection().set(1, 3, 12, 13, 16);
    model.component("comp1").physics("rad").feature("opac2").set("opaque", 0);
    model.component("comp1").physics("rad").feature("dsurf1").set("minput_temperature_src", "fromCommonDef");

    model.component("comp1").mesh("mesh1").create("ftet1", "FreeTet");
    model.component("comp1").mesh("mesh1").feature("size").set("custom", "on");
    model.component("comp1").mesh("mesh1").feature("size").set("hmax", 0.2);
    model.component("comp1").mesh("mesh1").feature("size").set("hmin", 0.1);
    model.component("comp1").mesh("mesh1").run();

    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");
    model.study("std1").feature("stat").setEntry("activate", "rad", false);
    model.study("std1").feature("stat").setEntry("activate", "rad", true);

    model.sol().create("sol1");
    model.sol("sol1").attach("std1");
    model.sol("sol1").createAutoSequence("std1");
    model.sol("sol1").runAll();

    model.result().create("pg1", "PlotGroup3D");
    model.result("pg1").label("Surface Radiosity (rad)");
    model.result("pg1").set("data", "dset1");
    model.result("pg1").feature().create("slit1", "SurfaceSlit");
    model.result("pg1").feature("slit1").set("showsolutionparams", "on");
    model.result("pg1").feature("slit1").set("upexpr", "rad.Ju");
    model.result("pg1").feature("slit1").set("downexpr", "rad.Jd");
    model.result("pg1").feature("slit1").set("smooth", "internal");
    model.result("pg1").feature("slit1").set("showsolutionparams", "on");
    model.result("pg1").feature("slit1").set("data", "parent");
    model.result().create("pg2", "PlotGroup3D");
    model.result("pg2").label("Radiative Heat Flux (rad)");
    model.result("pg2").set("edges", false);
    model.result("pg2").create("surf1", "Surface");
    model.result("pg2").create("surf2", "Surface");
    model.result("pg2").create("surf3", "Surface");
    model.result("pg2").create("surf4", "Surface");
    model.result("pg2").create("surf5", "Surface");
    model.result("pg2").create("surf6", "Surface");
    model.result("pg2").create("surf7", "Surface");
    model.result("pg2").create("surf8", "Surface");
    model.result("pg2").create("surf9", "Surface");
    model.result("pg2").create("surf10", "Surface");
    model.result("pg2").create("surf11", "Surface");
    model.result("pg2").feature("surf1").set("expr", "rad.rflux");
    model.result("pg2").feature("surf1").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf2").set("expr", "rad.rflux");
    model.result("pg2").feature("surf2").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf2").create("sel1", "Selection");
    model.result("pg2").feature("surf2").feature("sel1").selection()
         .set(68, 69, 70, 71, 72, 73, 78, 79, 80, 88, 90, 91, 92, 93, 94, 95, 108, 109, 110, 111, 122, 123, 127, 128, 129, 130, 131);
    model.result("pg2").feature("surf3").set("expr", "z");
    model.result("pg2").feature("surf3").create("sel1", "Selection");
    model.result("pg2").feature("surf3").feature("sel1").selection().set(126);
    model.result("pg2").feature("surf4").set("expr", "rad.rflux");
    model.result("pg2").feature("surf4").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf5").set("expr", "rad.rflux");
    model.result("pg2").feature("surf5").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf6").set("expr", "rad.rflux");
    model.result("pg2").feature("surf6").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf6").create("tran1", "Transparency");
    model.result("pg2").feature("surf6").create("sel1", "Selection");
    model.result("pg2").feature("surf6").feature("sel1").selection()
         .set(16, 157, 159, 160, 231, 282, 284, 285, 286, 287);
    model.result("pg2").feature("surf7").set("expr", "rad.rflux");
    model.result("pg2").feature("surf7").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf8").set("expr", "rad.rflux");
    model.result("pg2").feature("surf8").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf9").set("expr", "rad.rflux");
    model.result("pg2").feature("surf9").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf10").set("expr", "rad.rflux");
    model.result("pg2").feature("surf10").create("mtrl1", "MaterialAppearance");
    model.result("pg2").feature("surf10").create("sel1", "Selection");
    model.result("pg2").feature("surf10").feature("sel1").selection().set(2, 3, 4, 5, 15, 40, 152, 154, 155, 223);
    model.result("pg2").feature("surf11").set("expr", "rad.rflux");
    model.result("pg2").feature("surf11").create("sel1", "Selection");
    model.result("pg2").feature("surf11").feature("sel1").selection().set(81, 82, 83, 96, 97, 98, 124, 125);
    model.result("pg2").set("titletype", "manual");
    model.result("pg2").set("title", "Surface: Radiative heat flux (W/m<sup>2</sup>)");
    model.result("pg2").set("edges", false);
    model.result("pg2").feature("surf1").set("rangecoloractive", true);
    model.result("pg2").feature("surf1").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf1").set("colortable", "HeatCameraLight");
    model.result("pg2").feature("surf1").set("colortabletrans", "nonlinear");
    model.result("pg2").feature("surf1").set("colorcalibration", -1.5);
    model.result("pg2").feature("surf1").set("resolution", "normal");
    model.result("pg2").feature("surf1").feature("mtrl1").set("useplotcolors", true);
    model.result("pg2").feature("surf2").set("rangecoloractive", true);
    model.result("pg2").feature("surf2").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf2").set("inheritplot", "surf1");
    model.result("pg2").feature("surf2").set("resolution", "normal");
    model.result("pg2").feature("surf2").feature("mtrl1").set("material", "mat2");
    model.result("pg2").feature("surf3").set("coloring", "gradient");
    model.result("pg2").feature("surf3").set("colorlegend", false);
    model.result("pg2").feature("surf3").set("colortabletrans", "nonlinear");
    model.result("pg2").feature("surf3").set("topcolor", "custom");
    model.result("pg2").feature("surf3").set("customtopcolor", new double[]{1, 0.501960813999176, 0});
    model.result("pg2").feature("surf3").set("bottomcolor", "custom");
    model.result("pg2").feature("surf3")
         .set("custombottomcolor", new double[]{0.9764705896377563, 0.9019607901573181, 0});
    model.result("pg2").feature("surf3").set("resolution", "normal");
    model.result("pg2").feature("surf4").set("rangecoloractive", true);
    model.result("pg2").feature("surf4").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf4").set("inheritplot", "surf1");
    model.result("pg2").feature("surf4").set("resolution", "normal");
    model.result("pg2").feature("surf4").feature("mtrl1").set("material", "mat4");
    model.result("pg2").feature("surf5").set("rangecoloractive", true);
    model.result("pg2").feature("surf5").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf5").set("colortable", "HeatCameraLight");
    model.result("pg2").feature("surf5").set("colortabletrans", "nonlinear");
    model.result("pg2").feature("surf5").set("colorcalibration", -1.5);
    model.result("pg2").feature("surf5").set("inheritplot", "surf1");
    model.result("pg2").feature("surf5").set("resolution", "normal");
    model.result("pg2").feature("surf5").feature("mtrl1").set("material", "mat5");
    model.result("pg2").feature("surf5").feature("mtrl1").set("useplotcolors", true);
    model.result("pg2").feature("surf6").set("rangecoloractive", true);
    model.result("pg2").feature("surf6").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf6").set("colortable", "HeatCameraLight");
    model.result("pg2").feature("surf6").set("colortabletrans", "nonlinear");
    model.result("pg2").feature("surf6").set("colorcalibration", -1.5);
    model.result("pg2").feature("surf6").set("inheritplot", "surf1");
    model.result("pg2").feature("surf6").set("resolution", "normal");
    model.result("pg2").feature("surf6").feature("mtrl1").set("material", "mat6");
    model.result("pg2").feature("surf6").feature("mtrl1").set("useplotcolors", true);
    model.result("pg2").feature("surf7").set("rangecoloractive", true);
    model.result("pg2").feature("surf7").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf7").set("colortable", "HeatCameraLight");
    model.result("pg2").feature("surf7").set("colortabletrans", "nonlinear");
    model.result("pg2").feature("surf7").set("colorcalibration", -1.5);
    model.result("pg2").feature("surf7").set("inheritplot", "surf1");
    model.result("pg2").feature("surf7").set("resolution", "normal");
    model.result("pg2").feature("surf7").feature("mtrl1").set("material", "mat7");
    model.result("pg2").feature("surf7").feature("mtrl1").set("useplotcolors", true);
    model.result("pg2").feature("surf8").set("rangecoloractive", true);
    model.result("pg2").feature("surf8").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf8").set("inheritplot", "surf1");
    model.result("pg2").feature("surf8").set("resolution", "normal");
    model.result("pg2").feature("surf8").feature("mtrl1").set("material", "mat8");
    model.result("pg2").feature("surf9").set("rangecoloractive", true);
    model.result("pg2").feature("surf9").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf9").set("colortable", "HeatCameraLight");
    model.result("pg2").feature("surf9").set("colortabletrans", "nonlinear");
    model.result("pg2").feature("surf9").set("colorcalibration", -1.5);
    model.result("pg2").feature("surf9").set("inheritplot", "surf1");
    model.result("pg2").feature("surf9").set("resolution", "normal");
    model.result("pg2").feature("surf9").feature("mtrl1").set("material", "mat9");
    model.result("pg2").feature("surf9").feature("mtrl1").set("useplotcolors", true);
    model.result("pg2").feature("surf10").set("rangecoloractive", true);
    model.result("pg2").feature("surf10").set("rangecolormax", 249.49539302714197);
    model.result("pg2").feature("surf10").set("colortable", "HeatCameraLight");
    model.result("pg2").feature("surf10").set("colortabletrans", "nonlinear");
    model.result("pg2").feature("surf10").set("colorcalibration", -1.5);
    model.result("pg2").feature("surf10").set("inheritplot", "surf1");
    model.result("pg2").feature("surf10").set("resolution", "normal");
    model.result("pg2").feature("surf10").feature("mtrl1").set("material", "mat6");
    model.result("pg2").feature("surf10").feature("mtrl1").set("useplotcolors", true);
    model.result("pg2").feature("surf11").set("coloring", "uniform");
    model.result("pg2").feature("surf11").set("color", "custom");
    model.result("pg2").feature("surf11")
         .set("customcolor", new double[]{0.2549019753932953, 0.2549019753932953, 0.2549019753932953});
    model.result("pg2").feature("surf11").set("resolution", "normal");
    model.result("pg2").run();
    model.result().create("pg3", "PlotGroup3D");
    model.result("pg3").create("surf1", "Surface");
    model.result("pg3").feature("surf1").set("expr", "rad.Grad");
    model.result("pg3").label("Surface Irradiation (rad)");
    model.result("pg3").feature("surf1").set("colortable", "HeatCameraLight");
    model.result("pg3").feature("surf1").set("colortabletrans", "nonlinear");
    model.result("pg3").feature("surf1").set("colorcalibration", -1.5);
    model.result("pg3").feature("surf1").set("resolution", "normal");

    model.component("comp1").view().create("view18", "geom1");

    model.result("pg1").set("view", "view18");
    model.result("pg2").set("view", "view18");
    model.result("pg1").run();
    model.result("pg2").run();

    model.component("comp1").view("view1").camera().set("zoomanglefull", 10);
    model.component("comp1").view("view1").camera().set("position", new double[]{49.89, -9.67, 17.03});
    model.component("comp1").view("view1").camera().set("target", new double[]{2.77, 3.32, 1.35});
    model.component("comp1").view("view1").camera().set("up", new double[]{-0.3, 0.06, 0.95});
    model.component("comp1").view("view1").camera().set("rotationpoint", new double[]{2.77, 3.32, 1.35});
    model.component("comp1").view("view1").camera().set("viewoffset", new int[]{0, 0});
    model.component("comp1").view("view18").camera().set("zoomanglefull", 11.49);
    model.component("comp1").view("view18").camera().set("position", new double[]{45.12, -9.67, 27.29});
    model.component("comp1").view("view18").camera().set("target", new double[]{2.77, 3.32, 1.35});
    model.component("comp1").view("view18").camera().set("up", new double[]{-0.47, 0.17, 0.86});
    model.component("comp1").view("view18").camera().set("rotationpoint", new double[]{2.77, 3.32, 1.35});
    model.component("comp1").view("view18").camera().set("viewoffset", new double[]{-0.056, 0});

    model.title("Heat Transfer in a Room With a Stove");

    model
         .description("This is a model of a living room at ambient temperature heated by a stove. A radiation study is performed with the Surface-to-Surface Radiation physics interface.");

    model.label("living_room_with_stove.mph");

    model.result("pg2").run();
    model.result("pg2").run();

    model.study("std1").createAutoSequences("all");

    model.sol("sol1").runAll();

    model.result("pg1").run();

    model.component("comp1").geom("geom1").export().setType("stlascii");
 
     model.save("room_with_stove_test.mph");

    return model;
  }

  public static void main(String[] args) throws IOException {
    Model model = run();
    model = run2(model);
    model = run3(model);
    model = run4(model);
    model = run5(model);
    run6(model);
  }

}
