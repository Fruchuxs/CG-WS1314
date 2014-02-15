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
import javax.swing.JFrame;
import joglwrap.GLPanel;
import joglwrap.Sky;
import objects3d.PolygonGroup;
import objects3d.PolygonObject;

/**
 *
 * @author FloH
 */
public class Main {
    JFrame mainFrame;
    GLPanel openGLPanel;
    
    private void createAndAddCrane() {
        
    }
    public static void main(String[] args) {
        try {
            JFrame mainFrame = new JFrame();
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            Constraint fahrstuhlTranslateConstraint = new Translateable(new boolean[] { false, true, false });
            Constraint fahrstuhlRotateConstraint = new Rotateable(new boolean[] { false, false, false });
            fahrstuhlTranslateConstraint.setMaxY(0.33f);
            fahrstuhlTranslateConstraint.setMinY(0f);
            
            Constraint obenTranslateConstraint = new Translateable(new boolean[] { false, false, false });
            Constraint obenRotateConstraint = new Rotateable(new boolean[] { false, true, false });
            
            Constraint windeTranslateConstraint = new Translateable(new boolean[] { true, false, false });
            Constraint windeRotateConstraint = new Rotateable(new boolean[] { false, false, false });
            
            windeTranslateConstraint.setMaxX(0);
            windeTranslateConstraint.setMinX(-0.2f);
            
            PolygonObject unten = new ModelProxy("models/unten.obj");
            PolygonObject fahrstuhl = new ModelProxy("models/fahrstuhl.obj");
            PolygonObject oben = new ModelProxy("models/oben.obj");
            PolygonObject winde = new ModelProxy("models/winde.obj");
            
            PolygonObject karton = new ModelProxy("models/karton.obj");
            
            fahrstuhl.setRotateConstraint(fahrstuhlRotateConstraint);
            fahrstuhl.setTranslateConstraint(fahrstuhlTranslateConstraint);
            
            oben.setRotateConstraint(obenRotateConstraint);
            oben.setTranslateConstraint(obenTranslateConstraint);
            
            winde.setRotateConstraint(windeRotateConstraint);
            winde.setTranslateConstraint(windeTranslateConstraint);
            
            PolygonGroup cpo = new PolygonGroup(unten);
            cpo.addModel(fahrstuhl);
            
            PolygonGroup bla = new PolygonGroup(oben);
            bla.addModel(winde);
            
            cpo.addModel(bla);
            
            
            Sky sky = new Sky("models/sky.jpg");
            
            
            
            GLPanel panel = new GLPanel(600, 480);
            panel.addObjToDraw(cpo);
            panel.addOnlyDrawObj(sky);
            panel.addOnlyDrawObj(karton);
            
            mainFrame.add(panel);
            mainFrame.pack();
            mainFrame.setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
