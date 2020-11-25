package edu.tpu.dar.ruban.view.paintpatterns;

import java.awt.*;

public class PaintLine extends PaintObject {
    public PaintLine(Color color, double x1, double y1, double x2, double y2) {
        super(color, x1, y1, x2, y2);
    }

    @Override
    public void paint(Graphics gr) {
        gr.setColor(color);
        gr.drawLine(getX(), getY() ,getDX(), getDY());
    }
}
