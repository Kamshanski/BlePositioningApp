package edu.tpu.dar.ruban.view.paintpatterns;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class PaintString extends PaintObject {
    private String str;

    public PaintString(String str, Color color, double nominalX, double nominalY, double nominalDX, double nominalDY) {
        super(color, nominalX, nominalY, nominalDX, nominalDY);
        this.str = str;
    }

    public void setText(String data) {
        if (data == null) {
            throwError();
        }
        str = data;
    }

    @Override
    public void paint(Graphics gr) {
        gr.setColor(color);
        gr.setFont(new Font("Monaco", Font.PLAIN, 12));
        gr.drawString(str, getX(), getY());
    }
}
