package constraints;

/**
 * Konkrete Constraint Implementierung; soll die Beschraenkungen fuer
 * Drehungen darstellen. Existiert eigentlich nur zur Besserung Unterscheidung,
 * welche Beschraenkung gemeint ist.
 */
public class Rotateable extends Constraint {
    /**
     * Erzeugt ein Rotier-Beschraenkung.
     * 
     * @param pAllow 0 = x, 1 = y, 2 = z - Setzten, welche Achsen erlaubt sind zum rotieren
     */
    public Rotateable(boolean[] pAllow) {
        super.allowed = pAllow;
        
    }
}
