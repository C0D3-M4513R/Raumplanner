package com.Moebel;

import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Schrank extends AbstractMoebel {
    public static final Supplier<Schrank> BILLY = (Supplier<Schrank>) PRESETS.put("Billy",()->new Schrank("Billy",1.5,2));

    public Schrank(String name, double width, double height) {
        super(name, width, height);
    }

    /**
     @{inheritDoc}
     */
    public void draw(Color color) {
        gc.setLineWidth(lw);
        drawRect(color);
        drawName(color);

        gc.beginPath();
        //"delete" the front section, where the door would be
        gc.setStroke(color);
        gc.setLineWidth(lw*2);
        gc.lineTo(getWidth()/8,0);
        gc.lineTo(getWidth()-getWidth()/8,0);
        gc.closePath();
        gc.stroke();

        gc.setLineWidth(lw);
    }

    /**
     @{inheritDoc}
     */
    public double getCostMoebel() {
        return 200;
    }

    /**
     @{inheritDoc}
     */
    @Override
    public double cost() {
        return getCostMoebel() + hourlyCost*1.5;
    }
}
