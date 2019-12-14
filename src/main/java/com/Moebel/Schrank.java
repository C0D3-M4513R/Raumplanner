package com.Moebel;

import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Schrank extends Moebel {

    public static final Supplier<Schrank> BILLY = (Supplier<Schrank>) PRESETS.put("Billy",()->new Schrank("Billy",1.5,2));

    public Schrank(String name, double width, double height) {
        super(name, width, height);
    }

    @Override
    protected void draw(Color color) {
        switch (name) {
            case "Billy":
                //TODO: Vector Draw
//                break;
            default:
                fallbackDraw(color);
                break;
        }
    }
}
