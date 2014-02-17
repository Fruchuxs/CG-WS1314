package environment;

import constraints.Constraint;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Einfache Kamera, in der Beschraenkungen fuer Drehen und Verschieben gesetzt
 * werden konnen. Bietet momentan nur das rotieren um den Punkt 0, 0, 0!
 * Weitere Klassen koennen von dieser Klasse erben und Spezialisiert werden
 * und dann entsprechend im GLPanel registriert werden
 * 
 * @see joglwrap.GLPanel
 */
public class Cam {
    /**
     * Winkel aus den die Kamera immer schaut
     */
    protected float viewingAngle;
    
    /**
     * Groessen/Breiten Verhaeltnis - wird vom GLPanel injiziert per 
     * setter-dependency-injection
     */
    protected float whRatio;
    
    /**
     * Distanz oder auch Zoom Faktor / Entfernung der Kamera vom Betrachtungspunkt
     */
    protected float distance;
    
    /**
     * x Position der Kamera
     */
    protected float xPos = 0f;
    
    /**
     * y Position der Kamera
     */
    protected float yPos = 0f;
    
    /**
     * x Rotation der Kamera
     */
    protected float rotateX;
    
    /**
     * y Rotation der Kamera
     */
    protected float rotateY;
    
    /**
     * z Rotation der Kamera
     */
    protected float rotateZ;

    /**
     * Dreh Beschraenkung
     */
    private Constraint rotateConstraint;
    
    /**
     * Verschiebe Beschraenkung
     */
    private Constraint translateConstraint;

    /**
     * Konstruktor
     * 
     * @param pAngle Blinkwinkel
     * @param pDistance Entfernung
     */
    public Cam(float pAngle, float pDistance) {
        viewingAngle = pAngle;
        distance = pDistance;

        rotateX = 0f;
        rotateY = 0f;
        rotateZ = 0f;
    }

    /**
     * Erzeugt eine Cam mit einem Blickwinkel von 45° und der Distanz
     * 2,5x
     */
    public Cam() {
        this(45f, 2.5f);
    }

    /**
     * Zeichnet die Kamera
     * 
     * @param gl Momentane OpenGL Instanz 
     */
    public void draw(GL2 gl) {
        GLU glu = GLU.createGLU(gl);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(viewingAngle, whRatio, 1, 1000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        gl.glTranslatef(-xPos, -yPos, -distance);
        gl.glRotatef(rotateX, 1, 0, 0);
        gl.glRotatef(rotateY, 0, 1, 0);
        gl.glTranslatef(xPos, yPos, distance);


        //glu.gluLookAt(x, yPos, z, 0, 0, 0, 0, 1, 0);
        glu.gluLookAt(xPos, yPos, distance, 0, 0, 0, 0, 1, 0);
    }
    
    /**
     * Normalisiert den Winkel von 0 - 360
     * 
     * @param angle Winkel der normalisiert werden soll
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

    /**
     * Gibt das Breiten/Hoehen Verhaeltnis zurueck
     * 
     * @return Breiten/Hoehen Verhaeltnis 
     */
    public float getWhRatio() {
        return whRatio;
    }

    /**
     * Setzt das Breiten/Hoehen Verhaeltnis 
     * 
     * @param whRatio Breiten/Hoehen Verhaeltnis 
     */
    public void setWhRatio(float whRatio) {
        this.whRatio = whRatio;
    }

    /**
     * Gibt den Blickwinkel zrueuck.
     * 
     * @return Der Blickwinkel
     */
    public float getViewingAngle() {
        return viewingAngle;
    }

    /**
     * Setzt den Sichtwinkel.
     * 
     * @param viewingAngle Sichtwinkel.
     */
    public void setViewingAngle(float viewingAngle) {
        this.viewingAngle = normalizeAngle(viewingAngle);
    }

    /**
     * Gibt die Distanz zurueck.
     * 
     * @return Distanz.
     */
    public float getDistance() {
        return distance;
    }

    /**
     * Setzt die Distanz - wird zum zoomen genutzt von dem GLPanel.
     * 
     * @param distance Distanz.
     */
    public void setDistance(float distance) {
        if (translateConstraint != null) {
            distance = translateConstraint.moveZ(distance);
        }

        this.distance = distance;
    }

    /**
     * Gibt die x-Position zurueck.
     * 
     * @return x-Position.
     */
    public float getX() {
        return xPos;
    }

    /**
     * Setzt die x-Position.
     * 
     * @param xPos x-Position.
     */
    public void setX(float xPos) {
        this.xPos = xPos;
    }

    /**
     * Gibt den y-Position zurueck.
     * 
     * @return y-Position.
     */
    public float getY() {
        return yPos;
    }

    /**
     * Setzt die y-Position.
     * 
     * @param yPos Y-Position. 
     */
    public void setY(float yPos) {
        this.yPos = yPos;
    }

    /**
     * Gibt die x-Rotation zurueck.
     * 
     * @return x-Rotation.
     */
    public float getRotateX() {
        return rotateX;
    }
    
    /**
     * Setzt die x-Rotation.
     * 
     * @param rotateX x-Rotation.
     */
    public void setRotateX(float rotateX) {
        if (rotateConstraint != null) {
            rotateX = rotateConstraint.moveX(rotateX);
        }
        
        this.rotateX = normalizeAngle(rotateX);
    }

    /**
     * Gibt die y-Rotation zurueck.
     * 
     * @return y-Rotation.
     */
    public float getRotateY() {
        return rotateY;
    }

    /**
     * Setzt die y-Rotation.
     * 
     * @param rotateY y-Rotation.
     */
    public void setRotateY(float rotateY) {
        if (rotateConstraint != null) {
            rotateY = rotateConstraint.moveY(rotateY);
        }

        this.rotateY = normalizeAngle(rotateY);
    }

    /**
     * Gibt die Rotationsbeschraenkung zurueck.
     * 
     * @return Rotationsbeschraenkungsobjekt oder null wenn es nicht existiert.
     */
    public Constraint getRotateConstraint() {
        return rotateConstraint;
    }

    /**
     * Setzten des Rotationsbeschraenkungsobjekt.
     * 
     * @param rotateConstraint Konkretes Rotationsbeschraenkungsobjekt.
     */
    public void setRotateConstraint(Constraint rotateConstraint) {
        this.rotateConstraint = rotateConstraint;
    }

    /**
     * Gibt das Translationsbeschraenkungsobjekt zurueck.
     * 
     * @return Gibt das Translationsbeschraenkungsobjekt zurueck, oder null wenn es nicht existiert.
     */
    public Constraint getTranslateConstraint() {
        return translateConstraint;
    }

    /**
     * Setzten des Translationsbeschraenkungsobjekt.
     * 
     * @param translateConstraint Translationsbeschraenkungsobjekt
     */
    public void setTranslateConstraint(Constraint translateConstraint) {
        this.translateConstraint = translateConstraint;
    }

    /**
     * Gibt den z-Wert um den rotiert werden soll.
     * 
     * @return z-Wert
     */
    public float getRotateZ() {
        return rotateZ;
    }

    /**
     * Setzten des rotate-z Wertes.
     * 
     * @param rotateZ z-rotate Wert.
     */
    public void setRotateZ(float rotateZ) {
        this.rotateZ = rotateZ;
    }
}
