/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package observer;

import java.awt.event.MouseEvent;

/**
 *
 * @author FloH
 */
public interface MouseActionObserver {
    public void mouseDragged(float x, float y, MouseEvent e);
    public void mouseClicked(float x, float y, Integer objNumber, MouseEvent e);
    public void mouseReleased();
}
