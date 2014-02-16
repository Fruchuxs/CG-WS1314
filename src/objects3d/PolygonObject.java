/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects3d;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import constraints.Constraint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import joglwrap.GLPanel;
import transforms.Transforms;

/**
 *
 * @author Florian
 */
public abstract class PolygonObject implements OnlyDraw {

    protected static Integer objNumber = 0;

    protected Flaeche3D[] flaechen;
    protected Punkt[] punkte;
    protected List<Transforms> transforms = new ArrayList<>();
    protected boolean focus = false;
    protected final Integer objNr = objNumber++;

    // rotate and translate values
    protected Float rotate_x = 0f;
    protected Float rotate_y = 0f;

    protected Float trans_x = 0f;
    protected Float trans_y = 0f;
    protected Float prev_trans_x = 0f;
    protected Float prev_trans_y = 0f;

    protected float scaleFactor = 1f;

    protected Texture tex = null;
    protected File textureFile;

    protected Constraint rotateConstraint;
    protected Constraint translateConstraint;
    protected GLPanel parentPanel;
    
    protected float currentCamRotation = 0f;
    protected float inheritedAngle = 0f;

    public void loadTexture(Texture pTexToLoad) {
        if (pTexToLoad == null) {
            throw new IllegalArgumentException();
        }

        tex = pTexToLoad;
    }

    public void setRotateConstraint(Constraint c) {
        rotateConstraint = c;
    }

    public void setTranslateConstraint(Constraint c) {
        translateConstraint = c;
    }

    public void loadTexture() {
        try {
            loadTexture(TextureIO.newTexture(textureFile, true));
            System.out.println("Textur geladen!");
        } catch (IOException | GLException ex) {
            Logger.getLogger(PolygonObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean setTextureFile(File pFileTex) {
        if (pFileTex == null) {
            throw new IllegalArgumentException();
        }

        textureFile = pFileTex;

        return pFileTex.exists();
    }

    public boolean isTextured() {
        return tex != null;
    }

    public boolean isTextureFile() {
        return textureFile != null;
    }

    public void setColor(Float[] color) {
        for (Flaeche3D i : flaechen) {
            i.setColor(color);
        }
    }

    public void setScaleFactor(float scaleF) {
        scaleFactor = scaleF;
    }

    protected void translate(GL2 gl) {
        float local_trans_x = trans_x * 3f;
        float local_trans_y = trans_y * 3f;
        float local_trans_z = 0f;

        gl.glTranslatef(local_trans_x, local_trans_y, local_trans_z);
    }

    protected void rotate(GL2 gl) {
        gl.glRotatef(rotate_x, 1, 0, 0);
        gl.glRotatef(rotate_y, 0, 1, 0);
    }

    protected void beforeDraw(GL2 gl) {
        gl.glPushMatrix();
        
        for(Transforms i : transforms) {
            i.draw(gl);
        }
        translate(gl);
        rotate(gl);
        if (isTextured()) {
            tex.enable(gl);
            tex.bind(gl);
        }
    }

    protected void afterDraw(GL2 gl) {
        gl.glPopMatrix();

        if (isTextured()) {
            tex.disable(gl);
        }
    }

    @Override
    public void draw(GL2 gl) {
        draw(gl, GL2.GL_RENDER);
    }

    public void draw(GL2 gl, int mode) {
        beforeDraw(gl);

        for (Flaeche3D flaechen1 : flaechen) {
            if (mode == GL2.GL_SELECT) {
                gl.glPushName(getObjNr());
                flaechen1.draw(gl, isTextured());
                gl.glPopName();
            } else {
                flaechen1.draw(gl, isTextured());
            }
        }

        afterDraw(gl);
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
    
    protected float normalizeAngle(float angle) {
        if(Math.abs(angle) > 360) {
            angle = Math.abs(angle) - 360;
        }
        
        return angle;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.deepHashCode(this.flaechen);
        hash = 97 * hash + Arrays.deepHashCode(this.punkte);
        hash = 97 * hash + Objects.hashCode(this.objNr);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PolygonObject other = (PolygonObject) obj;
        if (!Arrays.deepEquals(this.flaechen, other.flaechen)) {
            return false;
        }
        if (!Arrays.deepEquals(this.punkte, other.punkte)) {
            return false;
        }
        if (!Objects.equals(this.objNr, other.objNr)) {
            return false;
        }
        return true;
    }
    
    public void addTransformation(Transforms toAdd) {
        transforms.add(toAdd);
    }

    public Float getRotate_x() {
        return rotate_x;
    }

    public void setRotate_x(Float rotate_x) {
        if (rotateConstraint != null) {
            rotate_x = rotateConstraint.moveX(rotate_x);
        }

        this.rotate_x = normalizeAngle(rotate_x);
    }

    public Float getRotate_y() {
        return rotate_y;
    }

    public void setRotate_y(Float rotate_y) {
        if (rotateConstraint != null) {
            rotate_y = rotateConstraint.moveY(rotate_y);
        }
        this.rotate_y = normalizeAngle(rotate_y);
    }

    public Integer getObjNr() {
        return objNr;
    }

    public Float getTrans_x() {
        return trans_x;
    }

    public void setTrans_x(Float trans_x) {
        if (translateConstraint != null) {
            trans_x = translateConstraint.moveX(trans_x);
        }

        this.trans_x = trans_x;
    }

    public Float getTrans_y() {
        return trans_y;
    }

    public void setTrans_y(Float trans_y) {
        if (translateConstraint != null) {
            trans_y = translateConstraint.moveY(trans_y);
        }
        this.trans_y = trans_y;
    }

    public Float getPrev_trans_x() {
        return prev_trans_x;
    }

    public void setPrev_trans_x(Float prev_trans_x) {
        this.prev_trans_x = prev_trans_x;
    }

    public Float getPrev_trans_y() {
        return prev_trans_y;
    }

    public void setPrev_trans_y(Float prev_trans_y) {
        this.prev_trans_y = prev_trans_y;
    }

    public float getCurrentViewRotation() {
        return currentCamRotation;
    }

    public void setCurrentViewRotation(float currentViewRotation) {
        this.currentCamRotation = currentViewRotation;
    }
}
