package environment.light;

import javax.media.opengl.GL2;

/**
 *
 */
public abstract class GLLight {
    private float[] lightPos = {0f, 0f, 0f, 1};
    private int name;
    private GL2 glObj;
    private boolean enabled = false;
    public abstract void setupLight(GL2 gl);
    
    private void disable() {
        glObj.glDisable(name);
    }
    
    private void enable() {
        glObj.glEnable(name);
    }
    
    protected void init(GL2 gl, int pName) {
        name = pName;
        glObj = gl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if(enabled) {
            enable();
        } else {
            disable();
        }
        
        this.enabled = enabled;
    }

    public float[] getLightPos() {
        return lightPos;
    }

    public void setLightPos(float[] lightPos) {
        this.lightPos = lightPos;
        glObj.glLightfv(name, GL2.GL_POSITION, lightPos, 0);
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
