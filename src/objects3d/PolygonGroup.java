/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author FloH
 */
public class PolygonGroup extends PolygonObject implements MouseActionObserver {

    private Map<Integer, PolygonObject> models;
    private List<PolygonGroup> subGroupsToInform;
    private PolygonObject mainModel;
    private Map<String, Float> extremPoints;
    private boolean extremPoints_calc = false;
    private PolygonObject active;
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
    
    private void initExtrempoints() {
        extremPoints = new HashMap<>();

        extremPoints.put("top", 0f);
        extremPoints.put("bottom", 0f);
        extremPoints.put("right", 0f);
        extremPoints.put("left", 0f);
        extremPoints.put("near", 0f);
        extremPoints.put("far", 0f);
    }
    
    private void checkIsSubGroup(PolygonObject pToCheck) {
        if(pToCheck instanceof PolygonGroup) {
            subGroupsToInform.add((PolygonGroup)pToCheck);
        }
    }

    private void calculateExtremPoints() {
        ModelProxy tmp;
        String[] lowest = new String[]{"bottom", "left", "far"};
        String[] highest = new String[]{"top", "right", "near"};

        for (Map.Entry i : models.entrySet()) {
            tmp = (ModelProxy)i.getValue();

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
            j = (PolygonObject)i.getValue();
            
            if (mode == GL2.GL_SELECT) {
                if(j instanceof PolygonGroup) {
                    j.draw(gl, mode);
                } else {
                    gl.glPushName((Integer) i.getKey());
                    j.draw(gl);
                    gl.glPopName();
                }
            } else {
                j.draw(gl);
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
        // TODO code hier aufrauemen :D
        if(active == null) {
            // deligiere an moegliche untergruppen weiter
            for(PolygonGroup i :subGroupsToInform) {
                i.mouseDragged(x, y, e);
            }
        }
        if (active != null) {
            switch(mouseClickButton) {
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
        int rotate = 1000;
        
        if(toRotate != null) {
            if(x < toRotate.getPrev_trans_x()) { // links
                toRotate.setRotate_x(toRotate.getRotate_x() + (toRotate.getPrev_trans_y() - y) * rotate);
            } else { // rechts
                toRotate.setRotate_x(toRotate.getRotate_x() - (toRotate.getPrev_trans_y() - y) * rotate);
            }
            
            if(y < toRotate.getPrev_trans_y()) { // unten
                toRotate.setRotate_y(toRotate.getRotate_y() - (toRotate.getPrev_trans_x() - x) * rotate);
            } else { // rechts
                toRotate.setRotate_y(toRotate.getRotate_y() + (toRotate.getPrev_trans_x() - x) * rotate);
            }
            
            toRotate.setPrev_trans_x(x);
            toRotate.setPrev_trans_y(y);
        }
    }

    private void moveObject(PolygonObject pToMove, float x, float y) {
        if (pToMove != null) {

            pToMove.setTrans_x(pToMove.getTrans_x() + x - pToMove.getPrev_trans_x());
            pToMove.setTrans_y(pToMove.getTrans_y() - y + pToMove.getPrev_trans_y());
            
            pToMove.setPrev_trans_x(x);
            pToMove.setPrev_trans_y(y);
        }
    }

    @Override
    public void mouseClicked(float x, float y, Integer objNumber, MouseEvent e) {
        mouseClickButton = e.getButton();
        
        if (objNumber != null) {
            active = models.get(objNumber);
        }
        
        if(active == null) {
            // deligiere an moegliche untergruppen weiter
            for(PolygonGroup i : subGroupsToInform) {
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
        
        for(PolygonGroup i : subGroupsToInform) {
            i.mouseReleased();
        }
    }

    @Override
    public void setParentPanel(GLPanel parentPanel) {
        super.parentPanel = parentPanel;
    }
}
