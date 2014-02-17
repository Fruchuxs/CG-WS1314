package joglwrap;

import objects3d.OnlyDraw;
import environment.light.GLLight;
import environment.Cam;
import static com.jogamp.common.nio.Buffers.newDirectIntBuffer;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import observer.MouseActionObserver;
import objects3d.PolygonObject;

/**
 * Ein Panel welches einem JFrame hinzugefuegt werden kann. Es handelt sich um
 * eine Spezifizierung der GLJPanel Klasse. Das Panel kann auf verschiedene
 * Interaktionen reagieren und benachrichtigt die registrierte Objekte, insofern
 * diese die entsprechenden Interfaces implementieren.
 *
 * In dem Panel kann eine Kamera separat registriert werden, diese reagier auch
 * auf Mausrad Interaktionen: - Mausrad runter => Zoom out - Mausrad hoch =>
 * Zoom in - Mausrad klick => Kamera bewegen Alle anderen Mausinteraktionen
 * werden an registrierte Unterobjekte weiter gegeben die das entsprechende
 * Interface implementieren.
 *
 * Dem Panel kann ebenfalls Licht hinzugefuegt werden, die das GLight Interface
 * implementieren.
 *
 * DrawOnly Objects sind Objekte, die einfach nur gezeichnet werden, d.h. es
 * wird keine Interaktionen mit Ihnen ausgefuehrt oder Aehnliches.
 *
 * @author Florian
 */
public class GLPanel extends GLJPanel implements
        GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener, MouseWheelListener {

    /**
     * Dimension des Fensters
     */
    protected Dimension frameDim;

    /**
     * Hintergrundfarbe, standard weiss
     */
    protected Float[] color = new Float[]{1f, 1f, 1f, 0f};

    /**
     * Liste mit interaktiven PolygonObjekten
     */
    protected List<PolygonObject> interactiveObjects;

    /**
     * Einfachere Objekte die einfach nur gezeichnet werden sollen
     */
    protected List<OnlyDraw> onlyDrawObjs;

    /**
     * Filter Liste, bzw. in die Liste werden automatisch Objekte sortiert, die
     * das MouseActionObserver Interface implementieren - diese Objekte sollen
     * informiert werden über mausinteraktionen
     */
    protected List<MouseActionObserver> informByMouseActions;

    /**
     * Liste mit Licht-Objekten
     */
    protected List<GLLight> lights;

    /**
     * Referenz auf das momentan aktive Licht TODO: evtl. durch index
     * austauschen
     */
    protected GLLight currentLight = null;

    /**
     * Licht aktiv?
     */
    protected boolean light = true;

    /**
     * Referenz auf die GL Utilitie
     */
    protected GLU glut;

    /**
     * Momentaner Klickpunkt der Maus - untransformierte Koordinaten
     */
    protected Integer[] currentClickPoint;

    /**
     * Vorheriger, untransformierter, Klickpunkt
     */
    protected Integer[] prevClickPoint;

    /**
     * Momentaner Mouseeventhandler
     */
    private MouseEvent currentMouseEvent;

    /**
     * List der Kameras TODO Implementierung von Kameraswitch
     */
    protected List<Cam> cams;

    /**
     * Referenz auf die momentan aktive Kamera
     */
    protected Cam currentCamera;

    /**
     * Befindet man sich im Maus-Kamera Modus?
     */
    protected boolean enterCamMode = false;

    /**
     * Prueft, ob Licht aktiviert sein soll
     *
     * @return true, wenn Licht aktiviert ist, ansonsten false
     */
    public boolean isLight() {
        return light;
    }

    /**
     * Schaltet Licht an oder aus
     *
     * @param light true fuer licht an, false fuer licht aus
     */
    public void setLight(boolean light) {
        this.light = light;
    }

    /**
     * Erzeugt ein GLPanel, welches effektiv OpenGL Inhalte rendern und
     * darstellen soll. TODO: Gleichmaessige Skalierung implementieren
     *
     * @param pWidth Breite des Panels
     * @param pHeight Hoehe des Panels
     */
    public GLPanel(Integer pWidth, Integer pHeight) {
        // Init JOGL and Size
        super(InitJOGL.init());

        cams = new ArrayList<>();
        interactiveObjects = new ArrayList<>();
        lights = new ArrayList<>();
        onlyDrawObjs = new ArrayList<>();
        informByMouseActions = new ArrayList<>();

        glut = new GLU();
        frameDim = new Dimension(pWidth, pHeight);
        super.setPreferredSize(frameDim);

        initListener();
    }

    /**
     * Initialisiere Listener
     */
    private void initListener() {
        super.addGLEventListener(this);
        super.addKeyListener(this);
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
        super.addMouseWheelListener(this);
    }

    /**
     * Initialisiere Licht, also registriere in GL die Lichter
     *
     * @param gl Momentaner OpenGL Kontext
     */
    protected void initLight(GL2 gl) {
        for (GLLight i : lights) {
            i.setupLight(gl);
        }
    }

    /**
     * Fuege ein Objekt hinzu, was gezeichnet werden soll.
     *
     * @param a Objekt was gezeichnet werden soll
     */
    public void addObjToDraw(PolygonObject a) {
        interactiveObjects.add(a);

        if (a instanceof MouseActionObserver) {
            informByMouseActions.add((MouseActionObserver) a);
        }
    }

    /**
     * Fuege eine Kamera hin.
     *
     * @param pCam Kamer die hinzugefuegt werden soll.
     */
    public void addCam(Cam pCam) {
        pCam.setWhRatio((float) frameDim.getWidth() / (float) frameDim.getHeight());
        cams.add(pCam);
        currentCamera = pCam;
    }

    /**
     * Fuege ein einfaches Objekt hinzu, was nur gezeichnet werden soll
     *
     * @param toAdd Objekt was hinzugefuegt werden soll.
     */
    public void addOnlyDrawObj(OnlyDraw toAdd) {
        if (toAdd != null) {
            toAdd.setParentPanel(this);
            onlyDrawObjs.add(toAdd);
        }
    }

    /**
     * Fuege ein Licht hinzu
     *
     * @param l Licht welches hinzugefuegt werden soll.
     */
    public void addLight(GLLight l) {
        lights.add(l);
        currentLight = l;
    }

    /**
     * Aktiviere naechstes Licht in der Liste
     */
    public void setNextLight() {
        int index = 0;

        if (currentLight != null) {

            if (lights.indexOf(currentLight) + 1 < lights.size()) {
                index = lights.indexOf(currentLight) + 1;
            }

            currentLight = lights.get(index);
        }
    }

    /**
     * Zeichne alles, was gezeichnet werden muss:
     * - Aktiviere Licht falls noetig
     * - Rendere Kamera
     * - Pruefe ob Objekte angeklickt wurden
     * - Zeichne OnlyDraw Objekte
     * - Zeichne interaktive Objekte
     * 
     * @param gl Momentaner OpenGL Kontext
     */
    public void drawObjects(GL2 gl) {

        if (isLight() && currentLight != null) {
            gl.glEnable(GL2.GL_LIGHTING);
            currentLight.setEnabled(true);

            // deaktiviere vorheriges licht
            if (lights.size() > 1) {
                int index;
                if (lights.indexOf(currentLight) - 1 < 0) {
                    index = lights.size() - 1;
                } else {
                    index = lights.indexOf(currentLight) - 1;
                }

                lights.get(index).setEnabled(false);
            }
        } else {
            gl.glDisable(GL2.GL_LIGHTING);
        }

        currentCamera.draw(gl);

        if (!enterCamMode) {
            picking(gl);
        }

        drawAlltodraw(gl);
    }

    /**
     * Zeichne OnlyDraw und interaktive Objekte
     * 
     * @param gl Momentaner OpenGL Kontext
     */
    private void drawAlltodraw(GL2 gl) {
        drawOnlyDraw(gl);

        // objekte die interaktionen haben zeichnen
        drawObjects(gl, GL2.GL_RENDER);
    }

    /**
     * Zeichne OnlyDraw Objekte
     * 
     * @param gl Momentaner OpenGL Kontext
     */
    private void drawOnlyDraw(GL2 gl) {
        // objekte ohne interaktionen zeichnen
        for (OnlyDraw i : onlyDrawObjs) {
            i.draw(gl);
            i.setParentPanel(this);
        }
    }

    /**
     * Zeichne Interaktive Objekte
     * 
     * @param gl Momentaner OpenGL Kontext
     * @param mode Pruefe ob im SELECT oder RENDER Mode gerendert werden soll
     */
    private void drawObjects(GL2 gl, int mode) {
        for (PolygonObject i : interactiveObjects) {
            i.draw(gl, mode);
            i.setParentPanel(this);
            i.setCurrentViewRotation(currentCamera.getRotateX(), currentCamera.getRotateY(), currentCamera.getRotateZ());
            i.setCamDistance(currentCamera.getDistance());
        }
    }

    /**
     * Fuehre picking aus - erkennt Mausklicks im GL_SELECT Mode.
     * Ist von OpenGL deprecated, es gibt bessere Verfahren -- allerdings zu 
     * spaet herausgefunden, daher hier noch die alte Methode verwendet
     * 
     * @param gl Momentaner OpenGL Kontext
     */
    private void picking(GL2 gl) {
        if (currentClickPoint != null) {
            int buffSize = 512;
            int[] select = new int[buffSize];
            int viewport[] = new int[4];
            IntBuffer selectBuffer = newDirectIntBuffer(buffSize);

            gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);

            gl.glSelectBuffer(buffSize, selectBuffer);
            //gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glRenderMode(GL2.GL_SELECT);
            gl.glInitNames();
            //gl.glPushName(-1);

            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glPushMatrix();
            gl.glLoadIdentity();

            glut.gluPickMatrix((double) currentClickPoint[0],
                    (double) (viewport[3] - currentClickPoint[1]),
                    10.0, 10.0, viewport, 0);

            glut.gluPerspective(currentCamera.getViewingAngle(), currentCamera.getWhRatio(), 1, 1000);
            gl.glMatrixMode(GL2.GL_MODELVIEW);

            drawObjects(gl, GL2.GL_SELECT);

            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glFlush();

            selectBuffer.get(select);

            interpretClicks(select, gl.glRenderMode(GL2.GL_RENDER));
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glPopMatrix();
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            resetClickPoint();
        }
    }

    /**
     * Werte die Klicks und den Buffer aus
     * 
     * @param pBuffer Buffer mit den erkannten Klicks und den Objekten
     * @param pHits Wie viele Treffer es gab
     */
    private void interpretClicks(int[] pBuffer, int pHits) {
        if (currentMouseEvent != null) {
            int names, ptr = 0;
            Float[] convertedClickPoints = transformMouseCoords(currentClickPoint[0], currentClickPoint[1]);

            for (int i = 0; i < pHits; i++) {
                names = pBuffer[ptr];
                //System.out.println("number of names for hit : " + names);
                ++ptr;
                //System.out.println("z1: " + pBuffer[ptr]);
                ++ptr;
                //System.out.println("z2: " + pBuffer[ptr]);
                ++ptr;

                for (int j = 0; j < names; j++) {
                    for (MouseActionObserver oberserver : informByMouseActions) {
                        oberserver.mouseClicked(convertedClickPoints[0], convertedClickPoints[1], pBuffer[ptr], currentMouseEvent);
                    }
                    ++ptr;
                }
            }
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        //gl.glEnable(GL2.GL_CULL_FACE);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        gl.glClearColor(color[0], color[1], color[2], color[3]);

        for (PolygonObject i : interactiveObjects) {
            if (i.isTextureFile()) {
                i.loadTexture();
            }
        }
        initLight(gl);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_PROJECTION);  // TODO: Set up a better projection?
        gl.glLoadIdentity();
        gl.glOrtho(-1, 1, -1, 1, -2, 2);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, -1f, -1f);

        drawObjects(gl);

        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        init(drawable);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {/*
             case KeyEvent.VK_LEFT:
             currentFocus.setRotate_y(currentFocus.getRotate_y() - 15);
             break;
             case KeyEvent.VK_RIGHT:
             currentFocus.setRotate_y(currentFocus.getRotate_y() + 15);
             break;
             case KeyEvent.VK_DOWN:
             currentFocus.setRotate_x(currentFocus.getRotate_x() + 15);
             break;
             case KeyEvent.VK_UP:
             currentFocus.setRotate_x(currentFocus.getRotate_x() - 15);
             break;
             case KeyEvent.VK_HOME:
             currentFocus.setRotate_x(0f);
             currentFocus.setRotate_y(0f);
             break;
             case 78: // N-Taste
             setNextInFocus();
             break;*/

            case 76: // L-Taste
                setNextLight();
                break;
            case 32: //Leer-Taste
                setLight(!isLight());
                break;
        }

        super.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentMouseEvent = e;
        super.requestFocus();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // mittlere maustaste gedrueckt
        // abfangen fuer kamera steuerung      
        if (e.getButton() == 2) {
            enterCamMode = true;
        }

        currentMouseEvent = e;
        newCurrentClickPoint(e.getX(), e.getY());

        super.repaint();
    }

    /**
     * Rechnet die Mauskoordinaten in Prozente um und verschiebt sie in das
     * Koordinatensystem von OpenGL
     *
     * @param x
     * @param y
     * @return
     */
    private Float[] transformMouseCoords(int x, int y) {
        return new Float[]{((float) x / super.getWidth()) - 0.5f, 1f - (((float) y / super.getHeight()) - 0.5f)};
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentMouseEvent = null;
        enterCamMode = false;

        // sag allen anderen bescheid, dass die maus los gelassen wurde
        for (MouseActionObserver i : informByMouseActions) {
            i.mouseReleased();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mouse_x = e.getX();
        int mouse_y = e.getY();

        if (enterCamMode) {
            int newX = currentClickPoint[0] - mouse_x;
            int newY = currentClickPoint[1] - mouse_y;
            int rotateby = 2;

            // rotieren um y
            if (newX < 0) { // rechts
                currentCamera.setRotateY(currentCamera.getRotateY() + rotateby);
            } else if (newX > 0) { // links
                currentCamera.setRotateY(currentCamera.getRotateY() - rotateby);
            }

            // rotieren um x
            if (newY < 0) {
                currentCamera.setRotateX(currentCamera.getRotateX() + rotateby);
            } else if (newY > 0) {
                currentCamera.setRotateX(currentCamera.getRotateX() - rotateby);
            }

            currentClickPoint[0] = mouse_x;
            currentClickPoint[1] = mouse_y;
        } else {
            Float[] convertedClickPoints = transformMouseCoords(mouse_x, mouse_y);
            for (MouseActionObserver i : informByMouseActions) {
                i.mouseDragged(convertedClickPoints[0], convertedClickPoints[1], e);
            }
        }

        super.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * Gibt die Hintergrund Farbe zurueck
     * 
     * @return Hintergrundfarbe des Panels
     */
    public Float[] getColor() {
        return color;
    }

    /**
     * Setzt die Hintergrundfarbe
     * 
     * @param color Hintergrundfarbe die gesetzt werden soll
     */
    public void setColor(Float[] color) {
        this.color = color;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();

        if (notches < 0) {
            currentCamera.setDistance(currentCamera.getDistance() - 0.1f);
        } else {
            currentCamera.setDistance(currentCamera.getDistance() + 0.1f);
        }

        super.repaint();
    }

    /**
     * Erzeugt einen neuen Clickpoint als Membervariable
     * 
     * @param x x-Mauskoordinate
     * @param y y-Mauskoordinate
     */
    private void newCurrentClickPoint(int x, int y) {
        currentClickPoint = new Integer[]{x, y};
    }

    /**
     * Setze den Clickpoint zurueck
     */
    private void resetClickPoint() {
        prevClickPoint = currentClickPoint;
        currentClickPoint = null;
    }

    /**
     * Gint das momentane Kameraobjekt zurueck
     * 
     * @return Das momentane Kameraobjekt 
     */
    public Cam getCurrentCamera() {
        return currentCamera;
    }

    /**
     * Setze die momentane Kamera um
     * 
     * @param currentCamera Momentane Kamer die gesetzt werden soll
     */
    public void setCurrentCamera(Cam currentCamera) {
        this.currentCamera = currentCamera;
    }
}
