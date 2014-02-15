/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package constraints;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author FloH
 */
public abstract class Constraint {
    protected enum MAX_MIN {
        MAX_X, MIN_X, MAX_Y, MIN_Y, MAX_Z, MIN_Z
    }
    protected CONSTRAINT_TYPE type;
    
    protected boolean[] allowed = new boolean[3];
    protected Map<MAX_MIN, Float> constraints = new EnumMap<>(MAX_MIN.class);
    
    protected Float[] currentStates = new Float[3];
    

    public CONSTRAINT_TYPE getType() {
        return type;
    }
    
    public Float increaseX(float p) {
        return changeValue(p, 0, MAX_MIN.MAX_X, MAX_MIN.MIN_X);
    }
    
    public Float increaseY(float p) {
        return changeValue(p, 1, MAX_MIN.MAX_Y, MAX_MIN.MIN_Y);
    }
    
    public Float increaseZ(float p) {
        return changeValue(p, 2, MAX_MIN.MAX_Z, MAX_MIN.MIN_Z);
    }
    
    public Float reduceX(float p) {
        return increaseX(-p);
    }
    
    public Float reduceY(float p) {
        return increaseY(-p);
    }
    
    public Float reduceZ(float p) {
        return increaseZ(-p);
    }

    private Float changeValue(float p, int fieldNumber, MAX_MIN max, MAX_MIN min) {
        if(allowed[fieldNumber]) {
            float tmp = currentStates[fieldNumber] + p;

            if(isInRange(p, max, min)) {
                currentStates[fieldNumber] = tmp;
            }
        } 
        
        return currentStates[fieldNumber];
    }
    
    public boolean isAllowed(float pX, float pY, float pZ) {
        return isAllowedX(pX) && isAllowedY(pY) && isAllowedZ(pZ);
    }
    
    public boolean isAllowedX(float p) {
        return p == 0f || (allowed[0] && isInRange(p, MAX_MIN.MAX_X, MAX_MIN.MIN_X));
    }
    
    public boolean isAllowedY(float p) {
        return p == 0f || (allowed[1] && isInRange(p, MAX_MIN.MAX_Y, MAX_MIN.MIN_Y));
    }
    
    public boolean isAllowedZ(float p) {
        return p == 0f || (allowed[2] && isInRange(p, MAX_MIN.MAX_Z, MAX_MIN.MIN_Z));
    }
    
    private float move(float p, int index, Float max, Float min) {
        float value = 0f;
        
        if((min != null && max != null) && allowed[index]) {
            if(isInRange(p, max, min)) {
                value = p;
            } else if(p > max) {
                value = max;
            } else {
                value = min;
            } 
        } else if(allowed[index]) {
            value = p;
        }
        
        return value;
    }
    
    public float moveX(float p) {
        return move(p, 0, constraints.get(MAX_MIN.MAX_X), constraints.get(MAX_MIN.MIN_X));
    }
    
    public float moveY(float p) {
        return move(p, 1, constraints.get(MAX_MIN.MAX_Y), constraints.get(MAX_MIN.MIN_Y));
    }
    
    public float moveZ(float p) {
        return move(p, 2, constraints.get(MAX_MIN.MAX_Z), constraints.get(MAX_MIN.MIN_Y));
    }
    
    private boolean isInRange(float p, MAX_MIN max, MAX_MIN min) {
        return isInRange(p, constraints.get(max), constraints.get(min));
    }
    
    private boolean isInRange(float p, float max, float min) {
        return p <= max && p >= min;
    }

    public void setType(CONSTRAINT_TYPE type) {
        this.type = type;
    }
    
    public boolean isType(CONSTRAINT_TYPE pTypeToCheck) {
        return type == pTypeToCheck;
    }

    public void setMaxX(float pMax) {
        constraints.put(MAX_MIN.MAX_X, pMax);
    }
    
    public void setMinX(float pMin) {
        constraints.put(MAX_MIN.MIN_X, pMin);
    }
    
    public void setMaxY(float pMax) {
        constraints.put(MAX_MIN.MAX_Y, pMax);
    }
    
    public void setMinY(float pMin) {
        constraints.put(MAX_MIN.MIN_Y, pMin);
    }
    
    public void setMaxZ(float pMax) {
        constraints.put(MAX_MIN.MAX_Z, pMax);
    }
    
    public void setMinZ(float pMin) {
        constraints.put(MAX_MIN.MIN_Z, pMin);
    }
    
    public float getMaxX() {
        return constraints.get(MAX_MIN.MAX_X);
    }
    
    public float getMinX() {
        return constraints.get(MAX_MIN.MIN_X);
    }
    
    public float getMaxY() {
        return constraints.get(MAX_MIN.MAX_Y);
    }
    
    public float getMinY() {
        return constraints.get(MAX_MIN.MIN_Y);
    }
    
    public float getMaxZ() {
        return constraints.get(MAX_MIN.MAX_Z);
    }
    
    public float getMinZ() {
        return constraints.get(MAX_MIN.MIN_Z);
    }
}
