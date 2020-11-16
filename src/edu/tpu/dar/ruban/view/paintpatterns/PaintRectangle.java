package edu.tpu.dar.ruban.view.paintpatterns;

import java.awt.*;

public class PaintRectangle extends PaintObject {
    Color borderColor;
    public PaintRectangle(Color color, Color borderColor, double nominalX, double nominalY, double nominalDX, double nominalDY) {
        super(color, nominalX, nominalY, nominalDX, nominalDY);
        this.borderColor = borderColor;
    }

    @Override
    public void paint(Graphics gr) {
        gr.setColor(color);
        gr.fillRect(getX(), getY() ,getDX(), getDY());
        gr.setColor(borderColor);
        gr.drawRect(getX(), getY() ,getDX(), getDY());
    }
}
