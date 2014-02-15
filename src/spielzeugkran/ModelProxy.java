/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spielzeugkran;

import de.fruchuxs.wavefrontobjloader.ModelLoader;
import de.fruchuxs.wavefrontobjloader.data.ImportedModel;
import java.io.FileNotFoundException;
import javax.media.opengl.GL2;
import joglwrap.GLPanel;
import objects3d.PolygonObject;

/**
 *
 * @author FloH
 */
public class ModelProxy extends PolygonObject{
    private ImportedModel model;
    
    public ModelProxy(String pModelPathToLoad) throws FileNotFoundException {
        model = ModelLoader.modelFactory(pModelPathToLoad);
    }
    
    @Override 
    public void draw(GL2 gl) {
        beforeDraw(gl);
        model.draw(gl);
        afterDraw(gl);
    }
    /*
    public Float[] isInFace(float xPos, float yPos) {
        for(Face i : model.getFacesList()) {
            
        }
    }*/

    public Float getExtremPoint(String pPoint) {
        return model.getExtremPoint(pPoint);
    }
    
    @Override
    public void setParentPanel(GLPanel parentPanel) {
        super.parentPanel = parentPanel;
    }
}
