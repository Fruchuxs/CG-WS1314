package objects3d;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import constraints.Constraint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import joglwrap.GLPanel;
import transforms.Transforms;

/**
 * Repraesentiert ein Polygon mit einer einzigartigen Objektnummer, Transformations-
 * informationen, Flaechen
 */
public abstract class PolygonObject implements OnlyDraw {

    /**
     * Statischer Objektnummern zaehler
     */
    protected static Integer objNumber = 0;

    /**
     * Flaechenliste - falls benoetigt
     */
    protected Flaeche[] flaechen;

    /**
     * Liste mit Transformationen die vor dem Zeichnen ausgefuehrt werden sollen
     */
    protected List<Transforms> transforms = new ArrayList<>();
    
    /**
     * Ob das Objekt im Fokus ist
     * @deprecated Wurde eingefuehrt fur ein Mausfokus System mit Tastaturwas nun nicht mehr genutzt wird
     */
    protected boolean focus = false;
    
    /**
     * Die einzigartige, nicht modifizierbare Objektnummer
     */
    protected final Integer objNr = objNumber++;

    /**
     * Rotation um die x-Achse
     */
    protected Float rotate_x = 0f;
    
    /**
     * Rotation um die y-achse
     */
    protected Float rotate_y = 0f;

    /**
     * Translation auf der x-Achse
     */
    protected Float trans_x = 0f;
    
    /**
     * Translation auf der y-Achse
     */
    protected Float trans_y = 0f;
    
    /**
     * Translation auf der z-Achse
     */
    protected Float trans_z = 0f;
    
    /**
     * Vorherige Translatioen, kann genutzt werden um Werte zwischenzuspeichern,
     * oder eben fuer die vorherige Translation fuer Berechnungen
     */
    protected Float prev_trans_x = 0f;
    
    /**
     * Vorherige Translatioen, kann genutzt werden um Werte zwischenzuspeichern,
     * oder eben fuer die vorherige Translation fuer Berechnungen
     */
    protected Float prev_trans_y = 0f;
    
    /**
     * Vorherige Translatioen, kann genutzt werden um Werte zwischenzuspeichern,
     * oder eben fuer die vorherige Translation fuer Berechnungen
     */
    protected Float prev_trans_z = 0f;

    /**
     * Textur die auf das Objekt aufgetragen wird
     * Ist eher deprecated, weil die Textur eigentlich zu den Flaechen sollte,
     * aber so kann ein Objekt komplett mit einer Textur ueberzogen werden
     */
    protected Texture tex = null;
    
    /**
     * Datei zur Texturefile
     */
    protected File textureFile;

    /**
     * Drehbeschraenkung um die x,y und z Achse
     */
    protected Constraint rotateConstraint;
    
    /**
     * Verschiebebeschraenkung auf der x, y und z Achse
     */
    protected Constraint translateConstraint;
    
    /**
     * Das Elternpanel, geerbt von DrawOnly
     */
    protected GLPanel parentPanel;
    
    /**
     * TODO: Berechne Distanz der Kamera zum Objekt
     * TODO: Fasse zusammen zu einer neuen Klasse oder nutze direkt das Cam Objekt!
     * Distanz der aktuellen Kamera zum 0 Punkt
     */
    protected float camDistance;
    
    /**
     * Rotation der aktuellen Kamera
     */
    protected float[] currentCamRotation = new float[] { 0f, 0f, 0f };
    protected float inheritedAngle = 0f;

    /**
     * Lade eine spezielle Textur. Kann vorher nicht automatisch geladen werden, da ein OpenGL
     * Kontext benoetigt wird
     * @param pTexToLoad Textur die geladen werden soll
     */
    public void loadTexture(Texture pTexToLoad) {
        if (pTexToLoad == null) {
            throw new IllegalArgumentException();
        }

        tex = pTexToLoad;
    }

    /**
     * Setzt das rotate-Constraint.
     * 
     * @param c Konkretes rotate Constraint, null unsettet es
     */
    public void setRotateConstraint(Constraint c) {
        rotateConstraint = c;
    }

    /**
     * Setzt das translate-Constraint.
     * 
     * @param c Konkretes Translate Constraint, null unsettet es
     */
    public void setTranslateConstraint(Constraint c) {
        translateConstraint = c;
    }

    /**
     * Lade die bereits gesetzte Textur.
     * Muss vvon aussen angestubst werden, da ein aktiver OpenGL Kontext
     * benoetigt wird
     */
    public void loadTexture() {
        try {
            loadTexture(TextureIO.newTexture(textureFile, true));
            System.out.println("Textur geladen!");
        } catch (IOException | GLException ex) {
            Logger.getLogger(PolygonObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Setzten einer Texturedatei.
     * 
     * @param pFileTex File Objekt zur Texture
     * @return True wenn existiert, ansonsten false
     */
    public boolean setTextureFile(File pFileTex) {
        if (pFileTex == null) {
            throw new IllegalArgumentException();
        }

        textureFile = pFileTex;

        return pFileTex.exists();
    }

    /**
     * Prueft ob dieses Objekt eine Textur hat.
     * 
     * @return true wenn es eine Textur hat, andernfalls false
     */
    public boolean isTextured() {
        return tex != null;
    }

    /**
     * Prueft ob eine Texture-File gesetzt ist.
     * 
     * @return true wenn eine Texturefile gesetzt ist ansonsten false
     */
    public boolean isTextureFile() {
        return textureFile != null;
    }

    /**
     * Setzt die Farbe fuer moegliche Flaechen
     * 
     * @param color Der Farbwert fuer die Flaechen
     */
    public void setColor(Float[] color) {
        for (Flaeche i : flaechen) {
            i.setColor(color);
        }
    }

    /**
     * Fuehre translate-Transformation aus
     * 
     * @param gl Momentaner OpenGL Context
     */
    protected void translate(GL2 gl) {
        float local_trans_x = trans_x ;
        float local_trans_y = trans_y;
        float local_trans_z = trans_z;

        gl.glTranslatef(local_trans_x, local_trans_y, local_trans_z);
    }

    /**
     * Fuehrt die rotations-Transformation aus
     * 
     * @param gl Der momentane OpenGL Context
     */
    protected void rotate(GL2 gl) {
        gl.glRotatef(rotate_x, 1, 0, 0);
        gl.glRotatef(rotate_y, 0, 1, 0);
    }

    /**
     * Sollte vor dem zeichnen aufgerufen werden
     * 
     * @param gl Der momentane OpenGL Context
     */
    protected void beforeDraw(GL2 gl) {
        gl.glPushMatrix();
        
        for(Transforms i : transforms) {
            i.draw(gl);
        }
        translate(gl);
        rotate(gl);
        if (isTextured()) {
            tex.enable(gl);
            tex.bind(gl);
        }
    }

    /**
     * Sollte nach dem Zeichnen aufgerufen werden.
     * 
     * @param gl Der momentane OpenGL Context
     */
    protected void afterDraw(GL2 gl) {
        gl.glPopMatrix();

        if (isTextured()) {
            tex.disable(gl);
        }
    }

    @Override
    public void draw(GL2 gl) {
        draw(gl, GL2.GL_RENDER);
    }

    /**
     * Beispielhafte draw Methode
     * @param gl
     * @param mode 
     */
    public void draw(GL2 gl, int mode) {
        beforeDraw(gl);

        for (Flaeche flaechen1 : flaechen) {
            if (mode == GL2.GL_SELECT) {
                gl.glPushName(getObjNr());
                flaechen1.draw(gl, isTextured());
                gl.glPopName();
            } else {
                flaechen1.draw(gl, isTextured());
            }
        }

        afterDraw(gl);
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
    
    /**
     * Normalisiert den Winkel.
     * Da OpenGL Gradmas verwendet, wird generell gradmas verwendet.
     * 
     * @param angle Der Winkel der Normalisiert werden soll
     * @return Normalisierter Winkel
     */
    protected float normalizeAngle(float angle) {
        if (angle < 0) {
            angle = 360 - Math.abs(angle);
        } else if(angle > 360) {
            angle = Math.abs(angle) - 360;
        }

        return angle;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.objNr);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PolygonObject other = (PolygonObject) obj;
        
        return Objects.equals(this.objNr, other.objNr);
    }
    
    /**
     * Fuegt eine Transformation hinzu
     * 
     * @param toAdd Transformation die hinzugefuegt werden soll
     */
    public void addTransformation(Transforms toAdd) {
        transforms.add(toAdd);
    }
    
    /**
     * Entfernt eine Transformation
     * 
     * @param toRemove Transformationsobjekt was entfernt werden soll
     */
    public void removeTransformation(Transforms toRemove) {
        transforms.remove(toRemove);
    }

    /**
     * Gibt den x-Rotationswert
     * 
     * @return x-Rotationswert 
     */
    public Float getRotate_x() {
        return rotate_x;
    }

    /**
     * Setzt den x-Rotationswert
     * 
     * @param rotate_x x-Rotationswert
     */
    public void setRotate_x(Float rotate_x) {
        if (rotateConstraint != null) {
            rotate_x = rotateConstraint.moveX(rotate_x);
        }

        this.rotate_x = normalizeAngle(rotate_x);
    }

    /**
     * Gibt den y-Rotationswert
     * 
     * @return y-Rotationswert
     */
    public Float getRotate_y() {
        return rotate_y;
    }

    /**
     * Setzt den y-Rotationswert
     * 
     * @param rotate_y y-Rotationswert
     */
    public void setRotate_y(Float rotate_y) {
        if (rotateConstraint != null) {
            rotate_y = rotateConstraint.moveY(rotate_y);
        }
        
        this.rotate_y = normalizeAngle(rotate_y);
    }

    /**
     * Gibt die einzigartige Objektnummer zurueck.
     * 
     * @return Die einzige Objektnummer
     */
    public Integer getObjNr() {
        return objNr;
    }

    /**
     * Gibt die x-Translation 
     * 
     * @return Die x-Translation
     */
    public Float getTrans_x() {
        return trans_x;
    }

    /**
     * Setzt die x-Translation
     * 
     * @param trans_x x-Translation
     */
    public void setTrans_x(Float trans_x) {
        if (translateConstraint != null) {
            trans_x = translateConstraint.moveX(trans_x);
        }

        this.trans_x = trans_x;
    }

    /**
     * Gibt die y-Translation
     * 
     * @return y-Translation
     */
    public Float getTrans_y() {
        return trans_y;
    }

    /**
     * Setzt die y-Translation
     * 
     * @param trans_y y-Translation
     */
    public void setTrans_y(Float trans_y) {
        if (translateConstraint != null) {
            trans_y = translateConstraint.moveY(trans_y);
        }
        this.trans_y = trans_y;
    }

    /**
     * Gibt die vorherige x-Translation zurueck
     * 
     * @return vorherige x-translation
     */
    public Float getPrev_trans_x() {
        return prev_trans_x;
    }

    /**
     * Setzt die vorherige x-Translation
     * 
     * @param prev_trans_x orhergehende x-Translation
     */
    public void setPrev_trans_x(Float prev_trans_x) {
        this.prev_trans_x = prev_trans_x;
    }

    /**
     * Gibt die vorherige y-Translation zurueck
     * 
     * @return vorherige y-translation
     */
    public Float getPrev_trans_y() {
        return prev_trans_y;
    }

    /**
     * Setzt die vorherige y-Translation
     * 
     * @param prev_trans_y vorhergehende y-Translation
     */
    public void setPrev_trans_y(Float prev_trans_y) {
        this.prev_trans_y = prev_trans_y;
    }

    /**
     * Gibt die momentane View-Rotation der Kamera zurueck
     * 
     * @return Momentane View-Rotation der Kamera
     */
    public float[] getCurrentViewRotation() {
        return currentCamRotation;
    }

    /**
     * Setzt die momentane View-Rotation
     * 
     * @param x x-Komponente
     * @param y y-Komponente
     * @param z z-Komponente
     */
    public void setCurrentViewRotation(float x, float y, float z) {
        this.currentCamRotation[0] = x;
        this.currentCamRotation[1] = y;
        this.currentCamRotation[2] = z;
    }
    
    /**
     * Setzt die momentane View-Rotation der Kamera
     * @param xyz float Array mit der x,y,z Rotation der Kamera
     */
    public void setCurrentViewRotation(float[] xyz) {
        this.currentCamRotation = xyz;
    }

    /**
     * Gibt die Verschiebung auf der z-Achse zurueck
     * 
     * @return z-Achsen Verschiebung
     */
    public Float getTrans_z() {
        return trans_z;
    }

    /**
     * Setzt die Verschiebung auf der z-Achse
     * 
     * @param trans_z Neuer z-Achsenverschiebungswert
     */
    public void setTrans_z(Float trans_z) {
        if(translateConstraint != null) {
            trans_z = translateConstraint.moveZ(trans_z);
        }
        this.trans_z = trans_z;
    }

    /**
     * Gibt die vorherige z-Translation zurueck
     * 
     * @return vorherige z-translation
     */
    public Float getPrev_trans_z() {
        return prev_trans_z;
    }

    /**
     * Setzt den vorherigen z-Verschiebungswert
     * 
     * @param prev_trans_z Vorheriger z-Verschiebungswert
     */
    public void setPrev_trans_z(Float prev_trans_z) {
        this.prev_trans_z = prev_trans_z;
    }

    /**
     * Gibt den veerbten Winkel zurueck
     * 
     * @return Der veerbte Winkel
     */
    public float getInheritedAngle() {
        return inheritedAngle;
    }

    /**
     * Setzt den veerbten Winkel
     * 
     * @param inheritedAngle Der Winkel der veerbt werden soll
     */
    public void setInheritedAngle(float inheritedAngle) {
        this.inheritedAngle = inheritedAngle;
    }

    /**
     * Gibt die Kameradistanz zurueck
     * 
     * @return Die Kameradistanz
     */
    public float getCamDistance() {
        return camDistance;
    }

    /**
     * Setzt die Kameradistanz.
     * 
     * @param camDistance Die Kamerdistanz
     */
    public void setCamDistance(float camDistance) {
        this.camDistance = camDistance;
    }
}
