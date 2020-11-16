package edu.tpu.dar.ruban.view.paintpatterns;

import java.awt.*;

public class PaintLine extends PaintObject {
    public PaintLine(Color color, double nominalX, double nominalY, double nominalDX, double nominalDY) {
        super(color, nominalX, nominalY, nominalDX, nominalDY);
    }

    @Override
    public void paint(Graphics gr) {
        gr.drawLine(getX(), getY() ,getDX(), getDY());
    }
}
