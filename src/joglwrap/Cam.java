/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package joglwrap;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author FloH
 */
public class Cam {
    private float viewingAngle;
    private float whRatio;
    private float distance;
    private float xPos;
    private float yPos;
    private float rotateX;
    private float rotateY;
    
    public Cam(float pAngle, float pDistance) {
        viewingAngle = pAngle;
        distance = pDistance;
        
        rotateX = 0f;
        rotateY = 0f;
    }
    
    public Cam() {
        this(45f, 2f);
    }
    
    public void draw(GL2 gl) {
        GLU glu = GLU.createGLU(gl);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        glu.gluPerspective(viewingAngle, whRatio, 1, 1000);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        glu.gluLookAt(xPos, yPos, distance, 0, 0, 0, 0, 1, 0);
    }

    public float getWhRatio() {
        return whRatio;
    }

    public void setWhRatio(float whRatio) {
        this.whRatio = whRatio;
    }

    public float getViewingAngle() {
        return viewingAngle;
    }

    public void setViewingAngle(float viewingAngle) {
        this.viewingAngle = viewingAngle;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getX() {
        return xPos;
    }

    public void setX(float xPos) {
        this.xPos = xPos;
    }

    public float getY() {
        return yPos;
    }

    public void setY(float yPos) {
        this.yPos = yPos;
    }
}
