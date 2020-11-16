package edu.tpu.dar.ruban.view;

import edu.tpu.dar.ruban.view.paintpatterns.*;

import javax.swing.*;
import java.awt.*;

public class PaintPanel extends JPanel {
    private double W = 2200;
    private double H = 2000;
    double wr;
    double hr;
    PaintObject[] staticPaintings = new PaintObject[7];
    PaintCircle target;
    public static final int MAIN_FIELD = 0;
    public static final int BEACON = 0;     // BEACON + 1 -> 1st beacon out of 4
    public static final int FIELD_W = 5;
    public static final int FIELD_H = 6;
    public static final int TARGET = 7;
    public PaintPanel() {
        super();
        setBackground(Color.darkGray);

        initGraphicsList();
    }
    public void initGraphicsList() {
        staticPaintings[MAIN_FIELD] = new PaintRectangle(Color.white, Color.black, 133.33, 133.33, 1933.334, 1733.334);
        staticPaintings[BEACON+1] = new PaintString("1", Color.white, 127, 130, 0,0);
        staticPaintings[BEACON+2] = new PaintString("2", Color.white, 2066.7, 130, 0,0);
        staticPaintings[BEACON+3] = new PaintString("3", Color.white, 2066.7, 1950, 0,0);
        staticPaintings[BEACON+4] = new PaintString("4", Color.white, 127, 1950, 0,0);
        staticPaintings[FIELD_W] = new PaintString("2200мм", Color.white, 1100, 130, 0,0);
        staticPaintings[FIELD_H] = new PaintString("2000мм", Color.white, 2, 1000, 0,0);
        target = new PaintCircle(Color.red, null, 1000,1000,50,50);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (PaintObject po : staticPaintings) {
            po.paint(g);
        }

        target.paint(g);
    }


    public void mydraw() {
        repaint();
    }

    @Override
    public void reshape(int x, int y, int w, int h) {
        super.reshape(x, y, w, h);
        updatePaintingsSize(w, h);
    }

    public void updatePaintingsSize(int w, int h) {
        wr = ((double) w)/ W;
        hr = ((double) h)/ H;

        for (PaintObject po : staticPaintings) {
            po.setPositionAndSize(wr*po.X, hr*po.Y, wr*po.DX, hr*po.DY);
        }

        setTarget(1100, 1000, 50);

        target.setCircle(1100*wr, 1000*hr, 50 * Math.min(wr, hr));
    }

    public void updateTargetState(double x, double y, double r) {
        setTarget(x, y, r);
        repaint();
    }

    public void setTarget(double x, double y, double r) {
        target.setCircle(x*wr, y*hr, r * Math.min(wr, hr));
    }
}
