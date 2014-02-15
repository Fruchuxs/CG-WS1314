/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spielzeugkran;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.GL2;

/**
 *
 * @author FloH
 */
public class Wall {
    private float[] sPoint;
    private float[] ePoint;
    private float[] rotation;
    private float[] translation;
    private Texture textur;
    
    public Wall(float[] pStart, float[] pEnd, float[] pTranslation, float[] pRotation, File textureFile) throws IOException {
        sPoint = pStart;
        rotation = pRotation;
        ePoint = pEnd;
        translation = pTranslation;
        
        if(textureFile != null) {
            textur = TextureIO.newTexture(textureFile, true);
        }
    }
    
    public boolean hasTextur() {
        return textur != null;
    }
    
    public Wall(float[] pStart, float[] pEnd, float[] pTranslation, float[] pRotation) throws IOException {
        this(pStart, pEnd, pTranslation, pRotation, null);
    }
    
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glLoadIdentity();
        
        textur.enable(gl);
        textur.bind(gl);
        
        gl.glTranslatef(translation[0], translation[1], translation[2]);
        
        gl.glRotatef(rotation[0], 1, 0, 0);
        gl.glRotatef(rotation[1], 0, 1, 0);
        gl.glRotatef(rotation[2], 0, 0, 1);
        
        gl.glBegin(GL2.GL_QUADS);
            if(hasTextur()) {
                gl.glTexCoord2d(0, 0);
            }
            gl.glVertex3f(sPoint[0], sPoint[1], sPoint[2]); // Punkt unten links
            if(hasTextur()) {
                gl.glTexCoord2d(1, 0);
            }
            gl.glVertex3f(sPoint[0] + ePoint[0], sPoint[1], sPoint[2]);
            if(hasTextur()) {
                gl.glTexCoord2d(1, 1);
            }
            gl.glVertex3f(sPoint[0], sPoint[1] + ePoint[1], sPoint[2]);
            if(hasTextur()) {
                gl.glTexCoord2d(0, 1);
            }
            gl.glVertex3f(ePoint[0], ePoint[1], ePoint[2]); // Punkt oben rechts
        gl.glEnd();
        
        textur.disable(gl);
        gl.glPopMatrix();    
    }

    public float[] getStartPoint() {
        return sPoint;
    }

    public void setStartPoint(float[] sPoint) {
        this.sPoint = sPoint;
    }

    public float[] getEndPoint() {
        return ePoint;
    }

    public void setEndPoint(float[] ePoint) {
        this.ePoint = ePoint;
    }

}
