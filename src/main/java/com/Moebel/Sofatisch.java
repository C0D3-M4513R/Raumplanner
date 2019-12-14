package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Sofatisch extends Moebel {

	public static final Supplier<Sofatisch> ADMIRAL = (Supplier<Sofatisch>) PRESETS.put("Admiral",() -> new Sofatisch("Admiral", 1, 1));
	public static final Supplier<Sofatisch> MONTECARLO = (Supplier<Sofatisch>) PRESETS.put("Monte Carlo",() -> new Sofatisch("Monte Carlo", 1));
	public final boolean ROUND;

	public Sofatisch(String name, double width, double height) {
		super(name, width, height);
		ROUND = false;
	}

	public Sofatisch(String name, double radius) {
		super(name, radius * 2, radius * 2);
		ROUND = true;
	}


	protected void draw(Color color){
		switch (name){
			case "Admiral":
				//TODO: Vector Draw
//				break;
			case "Monte Carlo":
				//TODO: Vector Draw
//				break;
			default:
				fallbackDraw(color);
				break;
		}
	}


}
