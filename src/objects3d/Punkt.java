/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects3d;

/**
 *
 * @author Florian
 */
public class Punkt {
    Float x;
    Float y;
    Float z;
    
    Punkt(Float pX, Float pY) {
        this(pX, pY, null);
    }
    
    Punkt(Float pX, Float pY, Float pZ) {
        if(pX == null || pY == null) {
            throw new IllegalArgumentException();
        }
        
        x = pX;
        y = pY;
        z = pZ;
    }
    
    public Boolean is3D() {
        return !(z == null);
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }
}
