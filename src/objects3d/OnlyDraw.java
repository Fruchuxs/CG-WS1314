package objects3d;

import javax.media.opengl.GL2;import joglwrap.GLPanel;


/**
 * Interface fuer Objekte die einfach nur gezeichnet
 * werden sollen
 */
public interface OnlyDraw {
    /**
     * Zeichnet das Objekt.
     * 
     * @param gl Momentaner OpenGL KOntext
     */
    public void draw(GL2 gl);
    
    /**
     * Setzt das Elternpanel, was ggf. mehr Informationen hat wie Groese usw.
     * 
     * @param parentPanel Das Elternpanel in dem gezeichnet wird
     */
    public void setParentPanel(GLPanel parentPanel);
}
