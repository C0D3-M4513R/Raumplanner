package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Bett extends Moebel {

	public final int PERSONS;
	public static final Supplier<Bett> LEFRANCE = (Supplier<Bett>) PRESETS.put("Le France",() -> new Bett("Le France", 2, 2, 2));
	public static final Supplier<Bett> LEPETITFRANCE = (Supplier<Bett>) PRESETS.put("Le Petit France",() -> new Bett("Le Petit France", 1, 1, 2));

	public Bett(String name, int persons, double width, double height) {
		super(name, width, height);
		PERSONS = persons;
	}

	protected void draw(Color color){
		switch (name){
			case "Le France":
				//TODO: Vector Draw
//				break;
			case "Le Petit France":
				//TODO: Vector Draw
//				break;
			default:
				fallbackDraw(color);
				break;
		}
	}
}
