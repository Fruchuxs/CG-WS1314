package joglwrap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

/**
 *
 * @author Florian "Fruchuxs" Vogelpohl
 */
public class OpenGLJFrame extends JFrame implements GLEventListener {
    private Float[] color = new Float[] {1f, 1f, 1f, 0f};
    
    public OpenGLJFrame(Integer pWidth, Integer pHeight) {
        this(pWidth, pHeight, null, null);
    }
    
    public OpenGLJFrame(Integer pWidth, Integer pHeight, String pTitel) {
        this(pWidth, pHeight, pTitel, null);
    }
    
    public OpenGLJFrame(Integer pWidth, Integer pHeight, String pTitle, Float[] pColor) {
        if(pWidth == null || pHeight == null 
                || pWidth < 0 || pHeight < 0) {
            throw new IllegalArgumentException("One of the Parameters was null or less than zero.");
        }
        
        super.setSize(new Dimension(pWidth, pHeight));
             
        if(pTitle != null) {
            super.setTitle(pTitle);
        }
        
        if(pColor != null) {
            color = pColor;
        }
        
        initFrameWithJOGL();
    }
    
    private void initFrameWithJOGL() {
        // initJOGL() muss aufgerufen werden immer bevor irgendwas mit JOGL gemacht wird
        GLCanvas canvas = new GLCanvas(InitJOGL.init()); 
        // Fuege sich selber zum EventListener hinzu
        canvas.addGLEventListener(this);
        
        // Canvas hinzufuegen ohne groessenangaben - passt sich den Frame an
        super.add(canvas, BorderLayout.CENTER); 
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();	
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        new GLU().gluOrtho2D(0, super.getWidth(), 0, super.getHeight());
        gl.glClearColor(color[0], color[1], color[2], color[3]); 
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        
    }

    /*
     * new OpenGLJFrame(200, 300, "Hello World") {
     *      @Overwrite
     *      public void display(GLAutoDrawable drawable) {
     *          // Code here!
     *      }
     * }
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        throw new UnsupportedOperationException("Please overwrite this method in a child class!"); 
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        init(drawable);
    }
    
}
