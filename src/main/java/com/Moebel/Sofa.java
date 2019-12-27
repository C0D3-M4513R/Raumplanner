package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Sofa extends Moebel {

	public static final Supplier<Sofa> KOMFORT = (Supplier<Sofa>) PRESETS.put("Komfort",() -> new Sofa("Komfort", 2, 1));

	public Sofa(String name, double width, double height) {
		super(name, width, height);
	}

	protected void draw(Color color){
		switch (name){
			case "Komfort":
				//TODO: Vector Draw
//				break;
			default:
				fallbackDraw(color);
				break;
		}
	}

	public double getCostMoebel() {
		return 120;
	}

	@Override
	public double cost() {
		return getCostMoebel() + hourlyCost;
	}
}
