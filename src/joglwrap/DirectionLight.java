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
public class DirectionLight extends GLLight {
    
    public DirectionLight(int name) {
        super.setName(name);
    }
    
    @Override
    public void setupLight(GL2 gl) {
        super.init(gl, super.getName());
        super.setLightPos(new float[] {0, -4, 5, 0});
        float[] light0_color_am = {0, 0, 1, 1};
        float[] light0_color_diff = {1, 0, 0, 1};
        float[] light0_color_spec = {1, 1, 1, 1};

        gl.glLightfv(super.getName(), GL2.GL_AMBIENT, light0_color_am, 0);
        gl.glLightfv(super.getName(), GL2.GL_DIFFUSE, light0_color_diff, 0);
        gl.glLightfv(super.getName(), GL2.GL_SPECULAR, light0_color_spec, 0);
    }
}
