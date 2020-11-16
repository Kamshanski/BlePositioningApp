package edu.tpu.dar.ruban.view.paintpatterns;

import java.awt.*;

public class PaintCircle extends PaintObject {
    protected Color borderColor;

    public PaintCircle(Color color, Color borderColor, double nominalX, double nominalY, double nominalDX, double nominalDY) {
        super(color, nominalX, nominalY, nominalDX, nominalDY);
        this.borderColor = borderColor;
    }

    @Override
    public void setPositionAndSize(double x, double y, double dx, double dy) {
        super.setPositionAndSize(x, y, dx, dy);
    }

    public void setCircle(double xCenter, double yCenter, double radius) {
        this.x = (xCenter - radius);
        this.y = (yCenter - radius);
        dx = dy = radius * 2;  // radius * 2
    }

    @Override
    public void paint(Graphics gr) {
        gr.setColor(color);
        gr.drawOval(getX(), getY() ,getDX(), getDY());
    }
}
