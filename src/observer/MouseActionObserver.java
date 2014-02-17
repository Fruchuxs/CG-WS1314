package observer;

import java.awt.event.MouseEvent;

/**
 * Observerinterface (zur Benachrichtigung implementierender Klassen) fuer 
 * Mausinteraktionen
 */
public interface MouseActionObserver {
    /**
     * Wird getriggert, wenn die Maus gedrueckt wurde und gehalten wird,
     * wird fuer jede Mausbewegung neu aufgerufen, also wenn x, y sich veraendert
     * haben.
     * 
     * @param x x-Koordinate der Maus umgerechnet in das Koordinatensystem von OpenGL
     * @param y y-Koordinate der Maus umgerechnet in das Koordinatensystem von OpenGL
     * @param e MouseEvent Objekt um verschiedene Sachen untersuchen zu koennen
     */
    public void mouseDragged(float x, float y, MouseEvent e);
    
    /**
     * Wird getriggert, wenn OpenGL registriert, dass ein Objekt angeklickt wurde,
     * und es wird auch die Nummer des erkannten Objektes uebertragen.
     * Momentan wird naiv davon ausgegangen, dass nur ein Objekt angeklickt wurde
     * TODO: Evtl Array uebertragen oder nur das uebertragen, was z-technisch weiter vorne liegt
     * 
     * @param x x-Koordinate der Maus umgerechnet in das Koordinatensystem von OpenGL
     * @param y y-Koordinate der Maus umgerechnet in das Koordinatensystem von OpenGL
     * @param objNumber Die Nummer des geklickten Objektes
     * @param e 
     */
    public void mouseClicked(float x, float y, Integer objNumber, MouseEvent e);
    
    /**
     * Wird getriggert, wenn die Maus wieder losgelassen wird, wenn diese vorher
     * gedrueckt wurde
     */
    public void mouseReleased();
}
