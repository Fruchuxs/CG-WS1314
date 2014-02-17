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
 * einen MouseActionObserver, damit die Gruppe faehig ist auf Mausinteraktionen
 * zu reagieren.
 *
 * Da PolygonGroup selber PolygonObject implementiert, kann sie auch unter
 * Gruppen beinhalten, Untergruppen werden auch benachrichtigt.
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

    /**
     * Erstellt einen PolygonGruppe mit einem PolygonObject als Hauptobjekt,
     * welches die Gruppe repraesentiert.
     *
     * Momentan muss noch eine Hauptgruppe an ein Objekt gebunden sein, aber so
     * wird realisiert, dass wenn zB das Hauptobjekt gedreht wird, auch alle
     * anderen Objekte gedreht werden
     *
     * Die Gruppe wird auch mit den Transformationsparametern des Mainobjektes
     * initialisiert, da die PolygonGruppe selber auch nur ein zusammengesetztes
     * PolygonObject repraesentiert.
     *
     * @param pMainModel Das Mainmodel welches die Gruppe repraesentiert
     */
    public PolygonGroup(PolygonObject pMainModel) {
        models = new HashMap<>();
        subGroupsToInform = new ArrayList<>();

        mainModel = pMainModel;
        translateConstraint = pMainModel.translateConstraint;
        rotateConstraint = pMainModel.rotateConstraint;

        init(mainModel);
    }

    /**
     * Initialisiert mit dem mainmodel und prueft auch ob das mainmodel ggf.
     * selber auch eine Untergruppe ist
     */
    private void init(PolygonObject pToIni) {
        addModel(pToIni);

        checkIsSubGroup(pToIni);
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

            // vererbe Kameradistanz an Unterobjekte
            j.setCamDistance(camDistance);

            // Fuelle Namestack wenn im GL_SELECT Modus
            if (mode == GL2.GL_SELECT) {
                if (j instanceof PolygonGroup) {
                    // Wenn Polygongruppe ist, rufe das Zeichnen rekursiv in Untergruppen auf
                    j.draw(gl, mode);
                } else {
                    // Fuelle Namestack
                    gl.glPushName((Integer) i.getKey());
                    j.draw(gl);
                    gl.glPopName();
                }
            } else {

                // Zeichne einfach und veerbe die Viewrotation weiter
                j.setCurrentViewRotation(super.getCurrentViewRotation());
                j.draw(gl);
            }
        }

        afterDraw(gl);
    }

    /**
     * Fuegt ein model oder eine Subgrp zur Gruppe hinzu
     *
     * @param subM Gruppe oder Polygon-Objekt was hinzugefuegt werden soll
     */
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

    /**
     * Rotiert ein uebergebens Objekt anhand der x,y Koordinaten TODO: Pruefen,
     * rotationen generell zusammenzufassen, bzw. pruefe Aufloesung/MausDPI
     * Abhaengigkeiten
     *
     * @param toRotate Objekt welches rotiert werden soll
     * @param x x-Mauskoordinate, umgerechnet in Prozent (anhand von Fenster verhaeltnis) und in die Mitte geschoben
     * @param y y-Mauskoordinate, umgerechnet in Prozent (anhand von Fenster verhaeltnis) und in die Mitte geschoben
     */
    private void rotateObject(PolygonObject toRotate, float x, float y) {
        int yRotate = 10;

        // maus koordinatensystem transformieren fuer rotation
        // sprich: verschiebe nach unten rechts
        x = x + 0.5f;
        y = y + 0.5f;

        if (toRotate != null) {
            // x-Rotation bewusst kleiner gehalten
            if (Math.abs(toRotate.getPrev_trans_y() - y) > 0) {
                if (y < toRotate.getPrev_trans_y()) { // links
                    toRotate.setRotate_x(toRotate.getRotate_x() + 5);
                } else { // rechts
                    toRotate.setRotate_x(toRotate.getRotate_x() - 5);
                }
            }

            if (Math.abs(toRotate.getPrev_trans_x() - x) > 0) {
                if (x < toRotate.getPrev_trans_x()) { // unten
                    toRotate.setRotate_y(toRotate.getRotate_y() - yRotate);
                } else { // rechts
                    toRotate.setRotate_y(toRotate.getRotate_y() + yRotate);
                }
            }

            toRotate.setPrev_trans_x(x);
            toRotate.setPrev_trans_y(y);
        }
    }

    /**
     * Verschiebt ein Objekt anhand der umgewandelten Mauskoordinaten.
     * Die Rechnung ist hier etwas komplexer, bzw. hauefig modifiziert, damit die
     * Verschiebung auf der Sichtachse der Kamera ablaeuft
     * 
     * @param pToMove Objekt welches verschoben werden soll
     * @param x x-Mauskoordinate, umgerechnet in Prozent (anhand von Fenster verhaeltnis) und in die Mitte geschoben
     * @param y y-Mauskoordinate, umgerechnet in Prozent (anhand von Fenster verhaeltnis) und in die Mitte geschoben
     */
    private void moveObject(PolygonObject pToMove, float x, float y) {
        float nx, nz;

        float yAngle, xAngle;
        yAngle = pToMove.getRotate_y() + pToMove.getCurrentViewRotation()[1] + pToMove.getInheritedAngle() + getInheritedAngle();
        xAngle = pToMove.getRotate_x() + pToMove.getCurrentViewRotation()[0];

        double cosYAngle, sinYAngle; //, cosXAngle, sinXAngle;
        yAngle = -yAngle;
        cosYAngle = Math.cos(Math.toRadians(yAngle));
        sinYAngle = Math.sin(Math.toRadians(yAngle));/*
        cosXAngle = Math.cos(Math.toRadians(xAngle));
        sinXAngle = Math.sin(Math.toRadians(xAngle));*/

        /*
        Alte Drehtransformation
        Problem: Letzter Wert wird nicht beruecksichtigt
        */
        /*nx = (float)(cosYAngle * x);
         ny = (float)(sinYAngle * y);
         nz = (float)(sinYAngle * x);*/
        
        /* */
        double skalar = pToMove.getTrans_x() * cosYAngle + pToMove.getTrans_z() * (-sinYAngle);

        nx = (float) (pToMove.getTrans_x() + (x - skalar) * cosYAngle);
        nz = (float) (pToMove.getTrans_z() + (x - skalar) * (-sinYAngle));
        //ny = (float) (pToMove.getTrans_y() + (x - skalar) * 0);
        

            //pToMove.setTrans_x(pToMove.getTrans_x() + nx - pToMove.getPrev_trans_x());
        pToMove.setTrans_y(pToMove.getTrans_y() + (y - pToMove.getPrev_trans_y()));
        //pToMove.setTrans_z(pToMove.getTrans_z() - nz + pToMove.getPrev_trans_z());

        pToMove.setTrans_x(nx);
        //pToMove.setTrans_y(pToMove.getTrans_y() - ny + pToMove.getPrev_trans_y());
        pToMove.setTrans_z(nz);

        // Multipliziere die Camdistance dazu
        // nicht wissenschaftlich fundiert - eher durch ausprobieren um nachfolge Verschiebungen
        // etwas zu beschleunigen; hat hier aber momentan keine Auswirkungen
        pToMove.setPrev_trans_x(nx * camDistance);
        pToMove.setPrev_trans_y(y);
        pToMove.setPrev_trans_z(nz * camDistance);

    }

    @Override
    public void mouseClicked(float x, float y, Integer objNumber, MouseEvent e) {
        mouseClickButton = e.getButton();

        if (objNumber != null) {
            active = models.get(objNumber);
        }

        if (active == null) {
            // deligiere an moegliche untergruppen weiter
            // Die Gruppe weiss nicht, ob evtl unterobjekte angeklickt sind 
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
