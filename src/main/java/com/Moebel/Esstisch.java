package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Esstisch extends Moebel {

	public static final Supplier<Esstisch> WIKINGER = (Supplier<Esstisch>) PRESETS.put("Wikinger",() -> new Esstisch("Wikinger", 1.2, 1.2));

	protected void draw(Color color){
		switch (name){
			case "Wikinger":
				//TODO: Vector Draw
//				break;
			default:
				fallbackDraw(color);
				break;
		}
	}

	public Esstisch(String name, double width, double height) {
		super(name, width, height);
	}

	public double getCostMoebel() {
		return 250;
	}

	@Override
	public double cost() {
		return getCostMoebel()+ hourlyCost * 2;
	}
}
