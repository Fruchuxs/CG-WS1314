package objects3d;

/**
 * Einfache Punktklasse fuer das zeichnen eines 3D Punktes
 */
public class Punkt {
    /**
     * x-Koordinate
     */
    private Float x;
    
    /**
     * y-Koordinate
     */
    private Float y;
    
    /**
     * z-Koordinate
     */
    private Float z;
    
    /**
     * Erzeugt einen Punkt mit den entsprechenden Werten.
     * 
     * @param pX x-Koordinate
     * @param pY yKoordinate
     * @param pZ zKoordinate
     */
    public Punkt(Float pX, Float pY, Float pZ) {
        if(pX == null || pY == null) {
            throw new IllegalArgumentException();
        }
        
        x = pX;
        y = pY;
        z = pZ;
    }

    /**
     * Gibt die x-Koordinate zurueck.
     * 
     * @return x-Koordinate
     */
    public Float getX() {
        return x;
    }

    /**
     * Setzt die x-Koordinate.
     * 
     * @param x x-Koordinate die gesetzt werden soll.
     */
    public void setX(Float x) {
        this.x = x;
    }

    /**
     * Gibt die y-Koordinate zurueck.
     * 
     * @return y-Koordinate
     */
    public Float getY() {
        return y;
    }

    /**
     * Setzt die y-Koordinate
     * 
     * @param y y-Koordinate
     */
    public void setY(Float y) {
        this.y = y;
    }

    /**
     * Gibt die z-Koordinate zurueck
     * @return z-Koordinate
     */
    public Float getZ() {
        return z;
    }

    /**
     * Setzt die z-Koordinate
     * @param z z-Koordinate
     */
    public void setZ(Float z) {
        this.z = z;
    }
}
