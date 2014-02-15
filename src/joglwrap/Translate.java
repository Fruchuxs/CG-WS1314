/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package joglwrap;

import javax.media.opengl.GL2;

/**
 *
 * @author FloH
 */
public class Translate implements Transforms{
    private float x;
    private float y;
    private float z;

    public Translate(float px, float py, float pz) {
        x = px;
        y = py;
        z = pz;
    }

    @Override
    public void draw(GL2 gl) {
        gl.glTranslatef(x, y, z);
    }
}
