package edu.tpu.dar.ruban.view.paintpatterns;

import java.awt.*;

public abstract class PaintObject {
    public final double X, Y, DX, DY;
    protected double x, y, dx, dy;
    protected Color color;

    public PaintObject(Color color, double nominalX, double nominalY, double nominalDX, double nominalDY) {
        this.color = color;
        this.X = nominalX;
        this.Y = nominalY;
        this.DX = nominalDX;
        this.DY = nominalDY;
    }

    public void setPositionAndSize(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    protected void throwError() {
        throw new RuntimeException("Wrong data was passed to " + this.getClass().getName());
    }

    public abstract void paint(Graphics gr);

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getDX() {
        return (int) dx;
    }

    public int getDY() {
        return (int) dy;
    }
}
