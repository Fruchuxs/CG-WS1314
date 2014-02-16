/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objects3d;

import javax.media.opengl.GL2;import joglwrap.GLPanel;


/**
 *
 * @author FloH
 */
public interface OnlyDraw {
    public void draw(GL2 gl);
    public void setParentPanel(GLPanel parentPanel);
}
