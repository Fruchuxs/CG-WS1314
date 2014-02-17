package constraints;

import java.util.EnumMap;
import java.util.Map;

/**
 * Implementiert alles noetige um Beschraenkungen zu definieren
 */
public abstract class Constraint {
    /**
     * Enum zum identifizieren der Werte
     */
    protected enum MAX_MIN {
        MAX_X, MIN_X, MAX_Y, MIN_Y, MAX_Z, MIN_Z
    }

    /**
     * Beschreibt, welche aktionen erlaubtt sind
     * x, y, z
     */
    protected boolean[] allowed = new boolean[3];
    
    /**
     * Mapt die Beschraenkungen 
     */
    protected Map<MAX_MIN, Float> constraints = new EnumMap<>(MAX_MIN.class);
    
    /**
     * Prueft, ob die drei Werte zulaessig sind.
     * TODO: Es muessen momentan noch fuer alle min und max gesetzt werden!
     * 
     * @param pX x-Transformation
     * @param pY y-Transformation
     * @param pZ t-Transformation
     * @return true wenn erlaubt, ansonsten false
     */
    public boolean isAllowed(float pX, float pY, float pZ) {
        return isAllowedX(pX) && isAllowedY(pY) && isAllowedZ(pZ);
    }
    
    /**
     * Prueft ob die Transformation um den Wert p fuer die x-Achse erlaubt ist
     * 
     * @param p Wert der geprueft werden soll
     * @return true wenn ja, ansonsten false
     */
    public boolean isAllowedX(float p) {
        return p == 0f || (allowed[0] && isInRange(p, MAX_MIN.MAX_X, MAX_MIN.MIN_X));
    }
    
    /**
     * Prueft ob die Transformation um den Wert p fuer die y-Achse erlaubt ist
     * 
     * @param p Wert der geprueft werden soll
     * @return true wenn ja, ansonsten false
     */
    public boolean isAllowedY(float p) {
        return p == 0f || (allowed[1] && isInRange(p, MAX_MIN.MAX_Y, MAX_MIN.MIN_Y));
    }
    
    /**
     * Prueft ob die Transformation um den Wert p fuer die z-Achse erlaubt ist
     * 
     * @param p Wert der geprueft werden soll
     * @return true wenn ja, ansonsten false
     */
    public boolean isAllowedZ(float p) {
        return p == 0f || (allowed[2] && isInRange(p, MAX_MIN.MAX_Z, MAX_MIN.MIN_Z));
    }
    
    /**
     * Prueft fuer den Wert p, ob die Operation fuer die Achse des index index 
     * erlaubt ist fuer die max und min Werte - HelperMethode fuer die public
     * Versionen.
     * 
     * @param p Wert der geprueft werden soll.
     * @param index Index x = 0, y = 1, z = 2
     * @param max maximalwert
     * @param min minimalwert
     * @return Wenn p > max dann max, wenn p < min dann min, wenn max <= p <= min dann p
     */
    private float move(float p, int index, Float max, Float min) {
        float value = 0f;
        
        if((min != null && max != null) && allowed[index]) {
            if(isInRange(p, max, min)) {
                value = p;
            } else if(p > max) {
                value = max;
            } else {
                value = min;
            } 
        } else if(allowed[index]) {
            value = p;
        }
        
        return value;
    }
    
    /**
     * Prueft ob der Wert p fuer die x-Transformation erlaubt ist.
     * 
     * @param p Wert auf den gesetzt werden soll
     * @return Wenn p > max dann max, wenn p < min dann min, wenn min <= p <= max dann p
     */
    public float moveX(float p) {
        return move(p, 0, constraints.get(MAX_MIN.MAX_X), constraints.get(MAX_MIN.MIN_X));
    }
    
    /**
     * Prueft ob der Wert p fuer die y-Transformation erlaubt ist.
     * 
     * @param p Wert auf den gesetzt werden soll
     * @return Wenn p > max dann max, wenn p < min dann min, wenn min <= p <= max dann p
     */
    public float moveY(float p) {
        return move(p, 1, constraints.get(MAX_MIN.MAX_Y), constraints.get(MAX_MIN.MIN_Y));
    }
    
    /**
     * Prueft ob der Wert p fuer die z-Transformation erlaubt ist.
     * 
     * @param p Wert auf den gesetzt werden soll
     * @return Wenn p > max dann max, wenn p < min dann min, wenn min <= p <= max dann p
     */
    public float moveZ(float p) {
        return move(p, 2, constraints.get(MAX_MIN.MAX_Z), constraints.get(MAX_MIN.MIN_Z));
    }
    
    /**
     * Prueft ob der Wert p in Range von max und min ist
     * 
     * @param p Wert der geprueft werden soll.
     * @param max Maximalwert
     * @param min Minimalwert
     * @return true, wenn min <= p <= max
     */
    private boolean isInRange(float p, MAX_MIN max, MAX_MIN min) {
        return isInRange(p, constraints.get(max), constraints.get(min));
    }
    
    /**
     * Prueft ob der Wert p in Range von max und min ist
     * 
     * @param p Wert der geprueft werden soll.
     * @param max Maximalwert
     * @param min Minimalwert
     * @return true, wenn min <= p <= max
     */
    private boolean isInRange(float p, float max, float min) {
        return p <= max && p >= min;
    }

    /**
     * Setzt den maximalen x-Wert.
     * 
     * @param pMax Maximaler x-Wert
     */
    public void setMaxX(float pMax) {
        constraints.put(MAX_MIN.MAX_X, pMax);
    }
    
    /**
     * Setzt den minimalen x-Wert
     * 
     * @param pMin Minimaler x-Wert
     */
    public void setMinX(float pMin) {
        constraints.put(MAX_MIN.MIN_X, pMin);
    }
    
    /**
     * Setzt den maximalen y-Wert.
     * 
     * @param pMax Maximaler y-Wert. 
     */
    public void setMaxY(float pMax) {
        constraints.put(MAX_MIN.MAX_Y, pMax);
    }
    
    /**
     * Setzt den minimalen y-Wert.
     * 
     * @param pMin Minimaler y-Wert.
     */
    public void setMinY(float pMin) {
        constraints.put(MAX_MIN.MIN_Y, pMin);
    }
    
    /**
     * Setzt den maximalen z-Wert.
     * 
     * @param pMax Maximaler z-Wert.
     */
    public void setMaxZ(float pMax) {
        constraints.put(MAX_MIN.MAX_Z, pMax);
    }
    
    /**
     * Setzt den minimalen z-Wert.
     * 
     * @param pMin Minimaler z-Wert.
     */
    public void setMinZ(float pMin) {
        constraints.put(MAX_MIN.MIN_Z, pMin);
    }
    
    /**
     * Gibt den maximalen x-Wert zurueck.
     * 
     * @return Maximaler x-Wert
     */
    public float getMaxX() {
        return constraints.get(MAX_MIN.MAX_X);
    }
    
    /**
     * Gibt den minmalen x-Wert zurueck.
     * 
     * @return Minimaler x-Wert.
     */
    public float getMinX() {
        return constraints.get(MAX_MIN.MIN_X);
    }
    
    /**
     * Gibt den maximalen y-Wert zurueck.
     * 
     * @return Maximaler y-Wert
     */
    public float getMaxY() {
        return constraints.get(MAX_MIN.MAX_Y);
    }
    
    /**
     * Gibt den minmalen y-Wert zurueck.
     * 
     * @return Minimaler y-Wert.
     */
    public float getMinY() {
        return constraints.get(MAX_MIN.MIN_Y);
    }
    
    /**
     * Gibt den maximalen z-Wert zurueck.
     * 
     * @return Maximaler z-Wert
     */
    public float getMaxZ() {
        return constraints.get(MAX_MIN.MAX_Z);
    }
    
    /**
     * Gibt den minmalen z-Wert zurueck.
     * 
     * @return Minimaler z-Wert.
     */
    public float getMinZ() {
        return constraints.get(MAX_MIN.MIN_Z);
    }
}
