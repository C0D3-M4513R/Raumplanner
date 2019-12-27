package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Stuhl extends Moebel {

	public static final Supplier<Stuhl> NOMADE = (Supplier<Stuhl>) PRESETS.put("Nomade",() -> new Stuhl("Nomade", 0.5, 0.5));

	public Stuhl(String name, double width, double height) {
		super(name, width, height);
	}


    protected void draw(Color color){
        switch (name){
            case "Nomade":
                //TODO: Vector Draw
//                break;
            default:
                fallbackDraw(color);
                break;
        }
    }

	public double getCostMoebel() {
		return 50;
	}

	@Override
	public double cost() {
		return getCostMoebel();
	}
}
