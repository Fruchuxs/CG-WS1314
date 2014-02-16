/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import constraints.Constraint;
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
    private float xPos = 0;
    private float yPos = 0f;
    private float rotateX;
    private float rotateY;
    
    private Constraint rotateConstraint;
    private Constraint translateConstraint;
    
    public Cam(float pAngle, float pDistance) {
        viewingAngle = pAngle;
        distance = pDistance;
        
        rotateX = 0f;
        rotateY = 0f;
    }
    
    public Cam() {
        this(45f, 2.5f);
    }
    
    public void draw(GL2 gl) {
        GLU glu = GLU.createGLU(gl);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        glu.gluPerspective(viewingAngle, whRatio, 1, 1000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        gl.glTranslatef(-xPos, -yPos, -distance);
        gl.glRotatef(rotateX, 1, 0, 0);
        gl.glRotatef(rotateY, 0, 1, 0);
        gl.glTranslatef(xPos, yPos, distance);
        
        double x = xPos, y = yPos, z = distance;
        x = Math.cos(Math.toRadians(rotateX)) * z;
        y = Math.sin(Math.toRadians(rotateX)) * y + Math.cos(Math.toRadians(rotateX)) * y;
        z = Math.sin(Math.toRadians(rotateX)) * z;

        //glu.gluLookAt(x, yPos, z, 0, 0, 0, 0, 1, 0);
        glu.gluLookAt(xPos, yPos, distance, 0, 0, 0, 0, 1, 0);
    }
    
    private float normalizeAngle(float angle) {
        if(Math.abs(angle) > 360) {
            angle = Math.abs(angle) - 360;
        }
        
        return angle;
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
        this.viewingAngle = normalizeAngle(viewingAngle);
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        if(translateConstraint != null) {
            distance = translateConstraint.moveZ(distance);
        }
        
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

    public float getRotateX() {
        return rotateX;
    }

    public void setRotateX(float rotateX) {
        if(rotateConstraint != null) {
            rotateX = rotateConstraint.moveX(rotateX);
        }
        
        this.rotateX = normalizeAngle(rotateX);
    }

    public float getRotateY() {
        return rotateY;
    }

    public void setRotateY(float rotateY) {
        if(rotateConstraint != null) {
            rotateY = rotateConstraint.moveY(rotateY);
        }
        
        this.rotateY = normalizeAngle(rotateY);
        System.out.println(rotateY);
    }

    public Constraint getRotateConstraint() {
        return rotateConstraint;
    }

    public void setRotateConstraint(Constraint rotateConstraint) {
        this.rotateConstraint = rotateConstraint;
    }

    public Constraint getTranslateConstraint() {
        return translateConstraint;
    }

    public void setTranslateConstraint(Constraint translateConstraint) {
        this.translateConstraint = translateConstraint;
    }
}
