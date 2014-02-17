package observer;

import java.awt.event.KeyEvent;

/**
 * Observerinterface (zur Benachrichtigung implementierender Klassen) fuer 
 * Tastaturinteraktione
 */
public interface KeyboardActionObserver {
    /**
     * Wird getriggert, wenn eine Taste gedrueckt wurde
     * 
     * @param e KeyEvent
     */
    public void keyPressed(KeyEvent e);
}
