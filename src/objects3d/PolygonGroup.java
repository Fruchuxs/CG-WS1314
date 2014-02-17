package objects3d;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import observer.MouseActionObserver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.media.opengl.GL2;
import joglwrap.GLPanel;
import spielzeugkran.ModelProxy;

/**
 * Gruppiert Polygone die von der Klasse PolygonObject erben und implementiert
 * einen MouseActionObserver, da die Gruppe faehig ist auf Mausinteraktionen zu
 * reagieren.
 *
 * Da PolygonGroup selber PolygonObject implementiert, kann sie auch unter
 * Gruppen beinhalten.
 */
public class PolygonGroup extends PolygonObject implements MouseActionObserver {

    /**
     * Map der Models; die einzigartige Objektnummer wird als Key benutzt
     */
    private Map<Integer, PolygonObject> models;

    /**
     * Ermittelte PolygonGruppen die ueber Mausinteraktionen benachrichtigt
     * werden
     */
    private List<PolygonGroup> subGroupsToInform;

    /**
     * Referenz auf das mainmodel. Mit diesen Werten wird auch die
     * PolygonObject-erbende Komponente ini- tialisiert um Verschiebungen und
     * Rotierungen auf der Gruppe auszufuehren.
     */
    private PolygonObject mainModel;

    /**
     * Map mit den Extrempunkten der ganzen Gruppe. Wird momentan nicht
     * verwendet, evtl. fuer spaeteren Gebrauch gedacht
     */
    private Map<String, Float> extremPoints;

    /**
     * Ob extrempunkte berechnet werden sollen
     */
    private boolean extremPoints_calc = false;

    /**
     * Durch anklicken aktiviertes Objekt dieser Gruppe
     */
    private PolygonObject active;

    /**
     * Wert der besagt welcher Mousebutton geklickt wurde, wird bei mouseClick
     * gesetzt
     */
    private int mouseClickButton;

    public PolygonGroup(PolygonObject pMainModel) {
        models = new HashMap<>();
        subGroupsToInform = new ArrayList<>();

        mainModel = pMainModel;
        translateConstraint = pMainModel.translateConstraint;
        rotateConstraint = pMainModel.rotateConstraint;

        init();
    }

    private void init() {
        addModel(mainModel);

        checkIsSubGroup(mainModel);
        initExtrempoints();
    }

    /**
     * Initialisiert die Extrempunkte
     */
    private void initExtrempoints() {
        extremPoints = new HashMap<>();

        extremPoints.put("top", 0f);
        extremPoints.put("bottom", 0f);
        extremPoints.put("right", 0f);
        extremPoints.put("left", 0f);
        extremPoints.put("near", 0f);
        extremPoints.put("far", 0f);
    }

    /**
     * Pruefe ob pToCheck selbst auch eine PolygonGroup ist, und registriere
     * diese getrennt
     *
     * @param pToCheck PolygonObject welches geprueft werden soll.
     */
    private void checkIsSubGroup(PolygonObject pToCheck) {
        if (pToCheck instanceof PolygonGroup) {
            subGroupsToInform.add((PolygonGroup) pToCheck);
        }
    }

    /**
     * Berechnet die extrempunkte -- momentan nicht genutzt
     */
    private void calculateExtremPoints() {
        ModelProxy tmp;
        String[] lowest = new String[]{"bottom", "left", "far"};
        String[] highest = new String[]{"top", "right", "near"};

        for (Map.Entry i : models.entrySet()) {
            tmp = (ModelProxy) i.getValue();

            for (String k : highest) {
                if (tmp.getExtremPoint(k) > extremPoints.get(k)) {
                    extremPoints.put(k, tmp.getExtremPoint(k));
                }
            }

            for (String k : lowest) {
                if (tmp.getExtremPoint(k) < extremPoints.get(k)) {
                    extremPoints.put(k, tmp.getExtremPoint(k));
                }
            }

        }
    }

    @Override
    public void draw(GL2 gl, int mode) {
        beforeDraw(gl);
        PolygonObject j;

        for (Map.Entry i : models.entrySet()) {
            j = (PolygonObject) i.getValue();
            j.setCamDistance(camDistance);
            if (mode == GL2.GL_SELECT) {
                if (j instanceof PolygonGroup) {
                    j.draw(gl, mode);
                } else {
                    gl.glPushName((Integer) i.getKey());
                    j.draw(gl);
                    gl.glPopName();
                }
            } else {
                j.draw(gl);
                j.setCurrentViewRotation(super.getCurrentViewRotation());
            }
        }

        afterDraw(gl);
    }

    public void addModel(PolygonObject subM) {
        checkIsSubGroup(subM);
        models.put(subM.getObjNr(), subM);
    }

    @Override
    public void mouseDragged(float x, float y, MouseEvent e) {

        if (active == null) {
            // deligiere an moegliche untergruppen weiter
            for (PolygonGroup i : subGroupsToInform) {
                i.mouseDragged(x, y, e);
            }
        }
        if (active != null) {
            switch (mouseClickButton) {
                case 1: {
                    moveObject(active, x, y);
                    break;
                }
                case 3: {
                    rotateObject(active, x, y);
                    break;
                }
            }

        }
    }

    private void rotateObject(PolygonObject toRotate, float x, float y) {
        int rotate = 5;

        // maus koordinatensystem transformieren fuer rotation 
        x = x + 0.5f;
        y = y + 0.5f;

        if (toRotate != null) {
            if (Math.abs(toRotate.getPrev_trans_y() - y) > 0) {
                if (y < toRotate.getPrev_trans_y()) { // links
                    toRotate.setRotate_x(toRotate.getRotate_x() + 1);
                } else { // rechts
                    toRotate.setRotate_x(toRotate.getRotate_x() - 1);
                }
            }

            if (Math.abs(toRotate.getPrev_trans_x() - x) > 0) {
                if (x < toRotate.getPrev_trans_x()) { // unten
                    toRotate.setRotate_y(toRotate.getRotate_y() - rotate);
                } else { // rechts
                    toRotate.setRotate_y(toRotate.getRotate_y() + rotate);
                }
            }

            toRotate.setPrev_trans_x(x);
            toRotate.setPrev_trans_y(y);
        }
    }

    private void moveObject(PolygonObject pToMove, float x, float y) {
        float z = pToMove.getPrev_trans_x();
        float nx, ny, nz;

        float yAngle, xAngle;
        yAngle = pToMove.getRotate_y() + pToMove.getCurrentViewRotation()[1] + pToMove.getInheritedAngle() + getInheritedAngle();
        xAngle = pToMove.getRotate_x() + pToMove.getCurrentViewRotation()[0];

        double cosYAngle, sinYAngle, cosXAngle, sinXAngle;
        yAngle = -yAngle;
        cosYAngle = Math.cos(Math.toRadians(yAngle));
        cosXAngle = Math.cos(Math.toRadians(xAngle));
        sinYAngle = Math.sin(Math.toRadians(yAngle));
        sinXAngle = Math.sin(Math.toRadians(xAngle));

        /*nx = (float)(cosYAngle * x);
         ny = (float)(sinYAngle * y);
         nz = (float)(sinYAngle * x);*/
        double skalar = pToMove.getTrans_x() * cosYAngle + pToMove.getTrans_z() * (-sinYAngle);

        /*if(skalar < 0) {
         skalar = -skalar;
         cosYAngle = -cosYAngle;
         sinYAngle = -sinYAngle;
         }*/
        nx = (float) (pToMove.getTrans_x() + (x - skalar) * cosYAngle);
        ny = (float) (pToMove.getTrans_y() + (x - skalar) * 0);
        nz = (float) (pToMove.getTrans_z() + (x - skalar) * (-sinYAngle));

        if (pToMove != null) {
            //pToMove.setTrans_x(pToMove.getTrans_x() + nx - pToMove.getPrev_trans_x());

            pToMove.setTrans_y(pToMove.getTrans_y() + (y - pToMove.getPrev_trans_y()));
            //pToMove.setTrans_z(pToMove.getTrans_z() - nz + pToMove.getPrev_trans_z());

            pToMove.setTrans_x(nx);
            //pToMove.setTrans_y(pToMove.getTrans_y() - ny + pToMove.getPrev_trans_y());
            pToMove.setTrans_z(nz);

            pToMove.setPrev_trans_x(nx * camDistance);
            pToMove.setPrev_trans_y(y);
            pToMove.setPrev_trans_z(nz * camDistance);
        }
    }

    @Override
    public void mouseClicked(float x, float y, Integer objNumber, MouseEvent e) {
        mouseClickButton = e.getButton();

        if (objNumber != null) {
            active = models.get(objNumber);
        }

        if (active == null) {
            // deligiere an moegliche untergruppen weiter
            for (PolygonGroup i : subGroupsToInform) {
                i.mouseClicked(x, y, objNumber, e);
            }
        } else {
            if (active.getObjNr() == mainModel.getObjNr()) {
                active = this;
            }

            if (active != null) {
                active.setPrev_trans_x(x);
                active.setPrev_trans_y(y);
            }
        }
    }

    @Override
    public void mouseReleased() {
        active = null;

        for (PolygonGroup i : subGroupsToInform) {
            i.mouseReleased();
        }
    }

    @Override
    public void setParentPanel(GLPanel parentPanel) {
        super.parentPanel = parentPanel;
    }

    @Override
    public void setRotate_y(Float rotate_y) {
        super.setRotate_y(rotate_y);
        for (Map.Entry i : models.entrySet()) {
            if (((PolygonObject) i.getValue()).getObjNr() != mainModel.getObjNr()) {
                ((PolygonObject) i.getValue()).setInheritedAngle(normalizeAngle(rotate_y + inheritedAngle));
            }
        }
    }
}
