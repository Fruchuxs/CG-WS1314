package joglwrap;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

/**
 * Initialisiert JOGL; Singleton
 */
public class InitJOGL {
    private static GLCapabilities openGLCapabilities = null;
   
    private InitJOGL() {
        
    }
    
    /**
     * Initialisiert JOGL und gibt das GLCapabilities Objekt zurueck
     * TODO: Setzt momentan nur das GL2 Profil, evtl. dynamisch machen
     * 
     * @return Das GLCapabilities Objekt 
     */
    public static GLCapabilities init() {
        if(openGLCapabilities == null) {
            // Muss vor der Verwendung von JOGL aufgerufen werden
            GLProfile.initSingleton();
            // mit OpenGL 2.x Profil initialisieren
            openGLCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        }
        
        return openGLCapabilities;
    }
    
    public static void setCapabilities(GLCapabilities a) {
        if(a == null) {
            throw new IllegalArgumentException();
        }
        
        openGLCapabilities = a;
    }
}
