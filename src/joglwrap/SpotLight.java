/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package joglwrap;

import javax.media.opengl.GL2;

/**
 *
 * @author FloH
 */
public class SpotLight extends GLLight {
    
    public SpotLight(int name) {
        super.setName(name);
    }
    
    @Override
    public void setupLight(GL2 gl) {
        super.init(gl, super.getName());
        float[] light2_spot_dir = {0, 5, -5};
        float[] light2_color_am = {0, 0, 0.4f, 1};
        float[] light2_color_diff = {1, 0, 0, 0};
        float[] light2_color_spec = {1, 1, 1, 1};
        
        gl.glLightfv(super.getName(), GL2.GL_POSITION, new float[] {10f, 10f, -0f,1}, 0);
        gl.glLightfv(super.getName(), GL2.GL_AMBIENT, light2_color_am, 0);
        gl.glLightfv(super.getName(), GL2.GL_DIFFUSE, light2_color_diff, 0);
        gl.glLightfv(super.getName(), GL2.GL_SPECULAR, light2_color_spec, 0);
        gl.glLightfv(super.getName(), GL2.GL_SPOT_DIRECTION, light2_spot_dir, 0);
    }
    
}
