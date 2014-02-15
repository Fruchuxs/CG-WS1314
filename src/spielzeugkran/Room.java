/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spielzeugkran;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import joglwrap.GLPanel;
import joglwrap.OnlyDraw;

/**
 *
 * @author FloH
 */
public class Room implements OnlyDraw {
    private List<Wall> walls;
    private int depth;
    private File textur;
    
    public Room(int pDepth, Path texture) {

        depth = pDepth;
        
        walls = new ArrayList<>();
        
        File tmpTexture = texture.toFile();
        if(tmpTexture.exists()) {
            textur = tmpTexture;
        }
    }
    
    private void createWalls() {
        try {
            walls.add(new Wall(
                    new float[] {1,1,0},
                    new float[] {1,-1,depth},
                    new float[] {0, 0, 0},
                    new float[] {0, 0, 0}
            ));
        } catch (IOException ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void draw(GL2 gl) {
        for(Wall i : walls) {
            i.draw(gl);
        }
    }

    @Override
    public void setParentPanel(GLPanel parentPanel) {
        
    }
    
    
}
