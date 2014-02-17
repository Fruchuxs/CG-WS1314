package spielzeugkran;

import de.fruchuxs.wavefrontobjloader.ModelLoader;
import de.fruchuxs.wavefrontobjloader.data.ImportedModel;
import java.io.FileNotFoundException;
import javax.media.opengl.GL2;
import joglwrap.GLPanel;
import objects3d.PolygonObject;

/**
 * Proxy fuer den Modelloader; wird benoetigt, da der Loader aus einer
 * externen Library kommt und so ist es moeglich, dass geladene Models
 * als Polygonobjekte gezeichnet, bzw. im GLPanel geladen werden koennen
 */
public class ModelProxy extends PolygonObject{
    /**
     * Vom Modelloader geladenes Objekt
     */
    private ImportedModel model;
    
    /**
     * Laedt das angegebene Model vom uebergebenen Parameter, insofern
     * vorhanden.
     * 
     * @param pModelPathToLoad Pfad zur Datei die geladen werden soll
     * @throws FileNotFoundException Wenn die Modeldatei nicht gefunden wurde
     */
    public ModelProxy(String pModelPathToLoad) throws FileNotFoundException {
        model = ModelLoader.modelFactory(pModelPathToLoad);
    }
    
    @Override 
    public void draw(GL2 gl) {
        beforeDraw(gl);
        model.draw(gl);
        afterDraw(gl);
    }
    
    /**
     * Delegate method die die extrem Punkte zurueck gibt.
     * 
     * @param pPoint Punkt der gewuenscht ist, near, far, right left, top oder bottom moeglich
     * @return null wenn nicht gesetzt, ansonsten der entsprechende float Wert
     */
    public Float getExtremPoint(String pPoint) {
        return model.getExtremPoint(pPoint);
    }
    
    @Override
    public void setParentPanel(GLPanel parentPanel) {
        super.parentPanel = parentPanel;
    }
}
