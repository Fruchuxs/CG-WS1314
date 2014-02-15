/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects3d;

import javax.media.opengl.GL2;

/**
 *
 * @author Florian
 */
public class Flaeche3D {
    private Punkt[] punkte;
    private Float[] color = new Float[] { 0f, 0f, 0f, 0f };
    
    public Flaeche3D(Punkt pA, Punkt pB, Punkt pC) {
        if(pA == null || pB == null || pC == null) {
            throw new IllegalArgumentException();
        }
        
        punkte = new Punkt[] { pA, pB, pC };
    }
    
    public Flaeche3D(Punkt pA, Punkt pB, Punkt pC, Punkt pD) {
        if(pA == null || pB == null || pC == null || pD == null) {
            throw new IllegalArgumentException();
        }
        
        punkte = new Punkt[] { pA, pB, pC, pD };
    }
    
    public void draw(GL2 gl) {
        draw(gl, false);
    }
    
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
    /* VIEL COOOOOOOOLERERER
        public void draw(GL2 gl) {
       Punkt gl.glColor3f(color[0], color[1], color[2]);
        
        if(punkte.length > 3) {     
           gl.glBegin(GL2.GL_QUADS));
              gl.glVertex3f(punkte[0].getX(), punkte[0].getY(), punkte[0].getZ());
              gl.glVertex3f(punkte[1].getX(), punkte[1].getY(), punkte[1].getZ());
              gl.glVertex3f(punkte[2].getX(), punkte[2].getY(), punkte[2].getZ());
              gl.glVertex3f(punkte[3].getX(), punkte[3].getY(), punkte[3].getZ());
           gl.glEnd();
        }else{
            gl.glBegin(GL2.GL_TRIANGLES);
                gl.glVertex3f(punkte[0].getX(), punkte[0].getY(), punkte[0].getZ());
                gl.glVertex3f(punkte[1].getX(), punkte[1].getY(), punkte[1].getZ());
                gl.glVertex3f(punkte[2].getX(), punkte[2].getY(), punkte[2].getZ());
            gl.glEnd();
        }
    }*/

    public Float[] getColor() {
        return color;
    }

    public void setColor(Float[] color) {
        this.color = color;
    }
}
