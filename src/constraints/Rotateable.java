/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package constraints;

/**
 *
 * @author FloH
 */
public class Rotateable extends Constraint {
    public Rotateable(boolean[] pAllow) {
        super.allowed = pAllow;
        
    }
}
