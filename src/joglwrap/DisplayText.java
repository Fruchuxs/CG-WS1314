/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package joglwrap;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import objects3d.OnlyDraw;

/**
 *
 * @author FloH
 */
public class DisplayText implements OnlyDraw{
    private int xPos;
    private int yPos;
    private String text;
    private GLPanel pPanel;
    
    public DisplayText(String pText, int pXPos, int pYPos) {
        xPos = pXPos;
        yPos = pYPos;
        text = pText;
    }

    @Override
    public void draw(GL2 gl) {
        GLU glu = GLU.createGLU(gl);
        GLUT glut = new GLUT();
        gl.glWindowPos2i(xPos, pPanel.getHeight() - yPos);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, text);
    }

    @Override
    public void setParentPanel(GLPanel parentPanel) {
        pPanel = parentPanel;
    }
}
