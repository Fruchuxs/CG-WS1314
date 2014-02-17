package objects3d;

import javax.media.opengl.GL2;

/**
 * Eine Flaeche die Farbe haben kann und aus Punkten besteht
 * TODO: Um Material und Textur ergaenzen
 */
public class Flaeche {
    /**
     * Pinkteliste
     */
    private Punkt[] punkte;
    
    /**
     * Farbe
     */
    private Float[] color = new Float[] { 0f, 0f, 0f, 0f };
    
    /**
     * Erzeugt eine Flaeche aus drei Punkten
     * TODO: Dynamische Punktliste
     * 
     * @param pA Punkt 
     * @param pB Punkt
     * @param pC Punkt
     */
    public Flaeche(Punkt pA, Punkt pB, Punkt pC) {
        if(pA == null || pB == null || pC == null) {
            throw new IllegalArgumentException();
        }
        
        punkte = new Punkt[] { pA, pB, pC };
    }
    
    /**
     * Flaeche bestehend aus 4 Punkten.
     * 
     * @param pA Punkt
     * @param pB Punkt
     * @param pC Punkt
     * @param pD Punkt
     */
    public Flaeche(Punkt pA, Punkt pB, Punkt pC, Punkt pD) {
        if(pA == null || pB == null || pC == null || pD == null) {
            throw new IllegalArgumentException();
        }
        
        punkte = new Punkt[] { pA, pB, pC, pD };
    }
    
    /**
     * Zeichne Flaeche
     * 
     * @param gl Momentaner OpenGL Kontext
     */
    public void draw(GL2 gl) {
        draw(gl, false);
    }
    
    /**
     * Zeichne mit Textur, ja nein
     * 
     * @param gl Momentaner OpenGL Kontext
     * @param withTex Gibt an ob eine Textur verwendet werden soll
     */
    public void draw(GL2 gl, boolean withTex) {
        if(!withTex) {
            gl.glColor3f(color[0], color[1], color[2]);
        }
        
        double specialCoord = 0.5;
        
        if(withTex && punkte.length > 3) { 
            specialCoord = 1;
        }
        
        gl.glBegin(GL2.GL_TRIANGLES);
            if(withTex) {
                gl.glTexCoord2d(0, 0);
            }
            gl.glVertex3f(punkte[0].getX(), punkte[0].getY(), punkte[0].getZ());
            
            if(withTex) {
                gl.glTexCoord2d(1, 0);
            }
            gl.glVertex3f(punkte[1].getX(), punkte[1].getY(), punkte[1].getZ());
            
            if(withTex) {
                gl.glTexCoord2d(specialCoord, 1);
            }
            gl.glVertex3f(punkte[2].getX(), punkte[2].getY(), punkte[2].getZ());
        gl.glEnd();
        
        if(punkte.length > 3) {     
           gl.glBegin(GL2.GL_TRIANGLES);
              if(withTex) {
                gl.glTexCoord2d(0, 0);
              }
              gl.glVertex3f(punkte[0].getX(), punkte[0].getY(), punkte[0].getZ());
              
              if(withTex) {
                gl.glTexCoord2d(0, 1);
              }
              gl.glVertex3f(punkte[3].getX(), punkte[3].getY(), punkte[3].getZ());
              
              if(withTex) {
                gl.glTexCoord2d(specialCoord, 1);
              }
              gl.glVertex3f(punkte[2].getX(), punkte[2].getY(), punkte[2].getZ());
           gl.glEnd();
        }      
    }

    /**
     * Gibt die Farbe zurueck
     * 
     * @return Array mit den Farbwerten
     */
    public Float[] getColor() {
        return color;
    }

    /**
     * Setzt eine Farbe
     * 
     * @param color Farbwertarray
     */
    public void setColor(Float[] color) {
        this.color = color;
    }
}
