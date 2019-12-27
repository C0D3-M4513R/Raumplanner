package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Sessel extends Moebel {

	public static final Supplier<Sessel> DELUXE = (Supplier<Sessel>) PRESETS.put("deLuxe",() -> new Sessel("deLuxe", 1, 1));

	public Sessel(String name, double width, double height) {
		super(name, width, height);
	}

	protected void draw(Color color){
		switch (name){
			case "deLuxe":
				//TODO: Vector Draw
//				break;
			default:
				fallbackDraw(color);
				break;
		}
	}

	public double getCostMoebel() {
		return 100;
	}

	@Override
	public double cost() {
		return getCostMoebel() + hourlyCost * 0.25;
	}
}
