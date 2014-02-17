/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spielzeugkran;

import constraints.Constraint;
import constraints.Rotateable;
import constraints.Translateable;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.swing.JFrame;
import environment.Cam;
import environment.light.DirectionLight;
import joglwrap.GLPanel;
import environment.Plane;
import environment.Sky;
import environment.light.SpotLight;
import joglwrap.DisplayText;
import transforms.Translate;
import objects3d.PolygonGroup;
import objects3d.PolygonObject;

/**
 *
 * @author FloH
 */
public class Main {

    private JFrame mainFrame;
    private GLPanel openGLPanel;

    public Main(int pWidth, int pHeight) {
        setupMainFrame();
        openGLPanel = new GLPanel(pWidth, pHeight);
    }
    
    public void run() throws FileNotFoundException {
        createAndAddCrane();
        mainFrame.add(openGLPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void setupMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createAndAddCrane() throws FileNotFoundException {
        Constraint fahrstuhlTranslateConstraint = new Translateable(new boolean[]{false, true, false});
        Constraint fahrstuhlRotateConstraint = new Rotateable(new boolean[]{false, false, false});
        fahrstuhlTranslateConstraint.setMaxY(1f);
        fahrstuhlTranslateConstraint.setMinY(0f);

        Constraint obenTranslateConstraint = new Translateable(new boolean[]{false, false, false});
        Constraint obenRotateConstraint = new Rotateable(new boolean[]{false, true, false});

        Constraint windeTranslateConstraint = new Translateable(new boolean[]{true, false, false});
        Constraint windeRotateConstraint = new Rotateable(new boolean[]{false, false, false});

        windeTranslateConstraint.setMaxX(0);
        windeTranslateConstraint.setMinX(-0.75f);

        PolygonObject unten = new ModelProxy("models/unten4.obj");
        PolygonObject fahrstuhl = new ModelProxy("models/fahrstuhl4.obj");
        PolygonObject oben = new ModelProxy("models/oben5.obj");
        PolygonObject winde = new ModelProxy("models/windeFinal.obj");

        PolygonObject baum = new ModelProxy("models/baum3.obj");

        fahrstuhl.setRotateConstraint(fahrstuhlRotateConstraint);
        fahrstuhl.setTranslateConstraint(fahrstuhlTranslateConstraint);

        oben.setRotateConstraint(obenRotateConstraint);
        oben.setTranslateConstraint(obenTranslateConstraint);

        winde.setRotateConstraint(windeRotateConstraint);
        winde.setTranslateConstraint(windeTranslateConstraint);
        
        Constraint kranConstraint = new Translateable(new boolean[] { true, true, true });
        kranConstraint.setMinY(0);
        kranConstraint.setMaxY(5);
        
        PolygonGroup cpo = new PolygonGroup(unten);
        
        cpo.addModel(fahrstuhl);
        cpo.setTranslateConstraint(kranConstraint);
        
        Translate transk0 = new Translate(-1.2f, 0f, -0.7f);
        baum.addTransformation(transk0);
        PolygonGroup bla = new PolygonGroup(oben);
        bla.addModel(winde);

        cpo.addModel(bla);
        
        Sky sky = new Sky("models/sky.jpg");
        Plane plane = new Plane("models/holztextur.jpg", 500f, 500f);
        
        openGLPanel.addOnlyDrawObj(baum);
        Cam cam = new Cam();
        Rotateable camRotateConst = new Rotateable(new boolean[] { true, true, true });
        Translateable camDistance = new Translateable(new boolean[] { true, true, true });
        camRotateConst.setMinX(0);
        camRotateConst.setMaxX(135);
        camDistance.setMinZ(0.1f);
        camDistance.setMaxZ(10f);
        
        cam.setRotateConstraint(camRotateConst);
        cam.setTranslateConstraint(camDistance);
        cam.setRotateX(20f);
        cam.setRotateY(320f);
        
        DisplayText text0 = new DisplayText("Linke Maustaste druecken um Objekte zu bewegen", 5, 15);
        DisplayText text1 = new DisplayText("Rechte um sie zu drehen", 5, 30);
        DisplayText text2 = new DisplayText("Mittlere fuer Kamera Bewegung", 5, 45);
        DisplayText text3 = new DisplayText("Es kann nur mit manchen Objekten interagiert werden!", 5, 60);

        
        openGLPanel.addCam(cam);
        openGLPanel.addObjToDraw(cpo);
        openGLPanel.addOnlyDrawObj(sky);
        openGLPanel.addOnlyDrawObj(plane);
        
        openGLPanel.addOnlyDrawObj(text0);
        openGLPanel.addOnlyDrawObj(text1);
        openGLPanel.addOnlyDrawObj(text2);
        openGLPanel.addOnlyDrawObj(text3);
        
        
        openGLPanel.addLight(new SpotLight(GL2.GL_LIGHT0));
        openGLPanel.addLight(new DirectionLight(GL2.GL_LIGHT1));
        //openGLPanel.addOnlyDrawObj(karton);
    }

    public static void main(String[] args) {
        try {
            new Main(600, 480).run();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
