/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import joglwrap.GLPanel;
import objects3d.OnlyDraw;

/**
 * Zeichnet einen Himmel bzw. effektiv eine Spehre die ab der Haelfte
 * aufgeschnitten ist. Soll nur gezeichnet werden, daher das OnlyDraw Interface
 */
public class Sky implements OnlyDraw {
    /**
     * Elternpanel
     */
    private GLPanel parentPanel;
    
    /**
     * Die Himmelstexture
     */
    private Texture skyTexture;
    
    /**
     * Texture die geladen werden soll
     */
    private String textureToLoad;
    
    /**
     * Erzeugt einen Himmel mit der Texture.
     * TODO: Parameter setzten fuer bessere Himmelkonfiguration - magic-Numbers entfernen
     * @param pTextureUrl 
     */
    public Sky(String pTextureUrl) {     
        textureToLoad = pTextureUrl;
    }
    
    /**
     * Laedt die Texture; ist ausgelagert, da zum laden ein aktiver OpenGL Kontext
     * benoetigt wird.
     * TODO: Texture lade Methode evtl. in eine abstracte Klasse auslagern!
     * 
     * @param pTextureUrl Pfad zur Texture
     */
    private void loadTexture(String pTextureUrl) {
        if(pTextureUrl != null) {
            Path texture = FileSystems.getDefault().getPath(pTextureUrl);
            
            if(texture.toFile().exists()) {
                try {
                    skyTexture = TextureIO.newTexture(texture.toFile(), true);
                } catch (IOException | GLException ex) {
                    Logger.getLogger(Sky.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Zeichnet den Himmel.
     * 
     * @param gl Momentaner OpenGL Kontext.
     */
    @Override
    public void draw(GL2 gl) {
        if(skyTexture == null) {
            loadTexture(textureToLoad);
        }
        if(parentPanel != null) {
            GLU glu = GLU.createGLU(gl);
            
            if(skyTexture != null) {
                skyTexture.enable(gl);
                skyTexture.bind(gl);
            }
            
            GLUquadric quadric = glu.gluNewQuadric();
            
            // Erzeugen damit Texture gerundet gemapt werden kann
            glu.gluQuadricTexture(quadric, true);
            glu.gluQuadricOrientation(quadric, GLU.GLU_INSIDE);

            /* auskommentiert, da dies leider Fehler verusacht*//*gl.glDisable(GL2.GL_DEPTH_TEST);
            gl.glDepthMask(false);*/

            gl.glPushMatrix();

            // Rotieren, damit der Begin der Kugel oben ist
            gl.glRotatef(90f, 1, 0, 0);
            
            // Schneide ab der Mitte durch
            double[] clipPlane2 = {0.0f, 0.0f, -1.0f, 0.5f};
            gl.glClipPlane(GL2.GL_CLIP_PLANE2, clipPlane2, 0);
            gl.glEnable(GL2.GL_CLIP_PLANE2);
            
            
            // Zeichnen der Spehere, ausschalten vom clipping
            glu.gluSphere(quadric, 20, 200, 15);
            gl.glDisable(GL2.GL_CLIP_PLANE2);

            if(skyTexture != null) {
                skyTexture.disable(gl);
            }
            gl.glPopMatrix();

            // s.o. 
            //gl.glEnable(GL2.GL_DEPTH_TEST);
            //gl.glDepthMask(true);
        }
    }

    @Override
    public void setParentPanel(GLPanel parentPanel) {
        this.parentPanel = parentPanel;
    }
}
