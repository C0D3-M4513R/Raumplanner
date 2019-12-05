package com.Moebel;


import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.function.Supplier;

public class Bett extends Moebel {

	public final int PERSONS;
	public static final Supplier<Bett> LEFRANCE = () -> new Bett("Le France", 2, 2, 2);
	public static final Supplier<Bett> LEPETITFRANCE = () -> new Bett("Le Petit France", 1, 1, 2);

	public Bett(String name, int persons, double width, double height) {
		super(name, width, height);
		PERSONS = persons;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected HashMap<String, Supplier<? extends Moebel>> getPreset() {
		HashMap<String, Supplier<? extends Moebel>> presets = new HashMap<>();
		presets.put("Le France", LEFRANCE);
		presets.put("Le Petit France", LEPETITFRANCE);
		return presets;
	}


	protected void draw(Color color){
		switch (name){
			case "Le France":
				//TODO: Vector Draw
				break;
			case "Le Petit France":
				//TODO: Vector Draw
				break;
			default:
				fallbackDraw(color);
				break;
		}
	}
}
