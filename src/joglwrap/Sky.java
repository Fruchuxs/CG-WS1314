/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package joglwrap;

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

/**
 *
 * @author FloH
 */
public class Sky implements OnlyDraw {
    private GLPanel parentPanel;
    private Texture skyTexture;
    private String textureToLoad;
    
    public Sky(String pTextureUrl) {     
        textureToLoad = pTextureUrl;
    }
    
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
            glu.gluQuadricTexture(quadric, true);
            glu.gluQuadricOrientation(quadric, GLU.GLU_INSIDE);

            /*gl.glDisable(GL2.GL_DEPTH_TEST);
            gl.glDepthMask(false);*/

            gl.glPushMatrix();

            //gl.glTranslatef(currentCam.getX(), currentCam.getY(), currentCam.getDistance());
            gl.glRotatef(90f, 1, 0, 0);
            double[] clipPlane2 = {0.0f, 0.0f, -1.0f, 0.5f};
            gl.glClipPlane(GL2.GL_CLIP_PLANE2, clipPlane2, 0);
            gl.glEnable(GL2.GL_CLIP_PLANE2);
            glu.gluSphere(quadric, 20, 200, 15);
            gl.glDisable(GL2.GL_CLIP_PLANE2);

            if(skyTexture != null) {
                skyTexture.disable(gl);
            }
            gl.glPopMatrix();

            //gl.glEnable(GL2.GL_DEPTH_TEST);
            //gl.glDepthMask(true);
        }
    }

    @Override
    public void setParentPanel(GLPanel parentPanel) {
        this.parentPanel = parentPanel;
    }
}
