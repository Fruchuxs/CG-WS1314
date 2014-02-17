package constraints;

/**
 * Konkrete Constraint Implementierung; soll die Beschraenkungen fuer
 * Verschiebung darstellen. Existiert eigentlich nur zur Besserung Unterscheidung,
 * welche Beschraenkung gemeint ist.
 */
public class Translateable extends Constraint{
    /**
     * Erzeugt eine Translate-Beschraenkung.
     * 
     * @param pAllow 0 = x, 1 = y, 2 = z - Setzten, welche Achsen erlaubt sind zum verschieben
     */
    public Translateable(boolean[] pAllow) {
        super.allowed = pAllow;
    }
}
