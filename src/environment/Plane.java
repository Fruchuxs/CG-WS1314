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
import joglwrap.GLPanel;
import objects3d.OnlyDraw;

/**
 * Erzeugt eine Ebene und implementiert das OnlyDraw-Interface, da mit der Ebene
 * keine Interaktion stattfinden soll.
 */
public class Plane implements OnlyDraw {

    /**
     * Die Textur fuer die Ebene
     */
    private Texture planeTexture;

    /**
     * Dateiname der Textur die geladen werden soll
     */
    private String textureToLoad;

    /**
     * Wie oft sie auf der Flaeche wiederholt werden soll
     */
    private float repeats;

    /**
     * Groese, wird quadratisch aufgespannt
     */
    private float size;

    /**
     * Die normale der Flaeche
     */
    private float[] normal;

    /**
     * Erzeugt eine Ebene
     *
     * @param pTextureUrl Pfad zur Textur
     * @param pRepeats Wiederholungswert
     * @param pSize Groesse der Ebene
     */
    public Plane(String pTextureUrl, float pRepeats, float pSize) {
        textureToLoad = pTextureUrl;
        repeats = pRepeats;
        size = pSize;
        normal = new float[]{0, 1, 0};
    }

    /**
     * Laedt die Texture. Ausgelagert, da Texturen erst geladen werden koennen,
     * wenn ein OpenGL Kontext existiert. Textur wird spiegelnd wiederholt und
     * linear gefiltert
     *
     * @param pTextureUrl Pfad zur Texture
     * @param gl OpenGL Kontext
     */
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
                } catch (IOException | GLException ex) {
                    Logger.getLogger(Sky.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Zeichnet die Ebene
     *
     * @param gl Momentaner OpenGL Kontext
     */
    @Override
    public void draw(GL2 gl) {
        if (planeTexture == null) {
            loadTexture(textureToLoad, gl);
        }

        if (planeTexture != null) {
            planeTexture.enable(gl);
            planeTexture.bind(gl);
        }

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

    /**
     * Setzt das Elternpanel, da hier nicht gebraucht, ohne Implementierung.
     * 
     * @param parentPanel null
     */
    @Override
    public void setParentPanel(GLPanel parentPanel) {

    }

    /**
     * Gibt die Normale der Ebene zurueck, wird evtl. fuer spaetere Schatten-
     * berechnung benoetigt.
     * 
     * @return Gibt die Normale zurueck.
     */
    public float[] getNormal() {
        return normal;
    }

    /**
     * Setzt die Normale.
     * 
     * @param normal Die Normale die gesetzt werden soll
     */
    public void setNormal(float[] normal) {
        this.normal = normal;
    }

}
