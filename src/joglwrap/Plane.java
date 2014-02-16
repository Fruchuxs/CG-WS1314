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

/**
 *
 * @author FloH
 */
public class Plane implements OnlyDraw {

    private Texture planeTexture;
    private String textureToLoad;
    private float repeats;
    private float size;
    private float[] normal;

    public Plane(String pTextureUrl, float pRepeats, float pSize) {
        textureToLoad = pTextureUrl;
        repeats = pRepeats;
        size = pSize;
        normal = new float[] { 0, 1, 0};
    }

    private void loadTexture(String pTextureUrl, GL2 gl) {
        if (pTextureUrl != null) {
            Path texture = FileSystems.getDefault().getPath(pTextureUrl);

            if (texture.toFile().exists()) {
                try {
                    planeTexture = TextureIO.newTexture(texture.toFile(), true);
                    planeTexture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
                    planeTexture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
                    planeTexture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_MIRRORED_REPEAT);
                    planeTexture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_MIRRORED_REPEAT);
                    planeTexture.setTexParameteri(gl, GL2.GL_GENERATE_MIPMAP, GL2.GL_TRUE);
                } catch (IOException | GLException ex) {
                    Logger.getLogger(Sky.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void draw(GL2 gl) {
        if (planeTexture == null) {
            loadTexture(textureToLoad, gl);
        }

        planeTexture.enable(gl);

        planeTexture.bind(gl);

        gl.glBegin(GL2.GL_QUADS);
        gl.glNormal3f(normal[0], normal[1], normal[2]);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(size, 0, size);

        gl.glTexCoord2f(1 * repeats, 0);
        gl.glVertex3f(-size, 0, size);

        gl.glTexCoord2f(1 * repeats, 1 * repeats);
        gl.glVertex3f(-size, 0, -size);

        gl.glTexCoord2f(0, 1 * repeats);
        gl.glVertex3f(size, 0, -size);
        gl.glEnd();

        if (planeTexture != null) {
            planeTexture.disable(gl);
        }
    }

    @Override
    public void setParentPanel(GLPanel parentPanel) {

    }

    public float[] getNormal() {
        return normal;
    }

    public void setNormal(float[] normal) {
        this.normal = normal;
    }

}
