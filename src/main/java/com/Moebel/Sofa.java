package com.Moebel;


import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.function.Supplier;

public class Sofa extends Moebel {

	public static final Supplier<Sofa> KOMFORT = () -> new Sofa("Komfort", 2, 1);

	public Sofa(String name, double width, double height) {
		super(name, width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected HashMap<String, Supplier<? extends Moebel>> getPreset() {
		HashMap<String, Supplier<? extends Moebel>> presets = new HashMap<>();
		presets.put("Komfort", KOMFORT);
		return presets;
	}

	protected void draw(Color color){
		switch (name){
			case "Komfort":
				//TODO: Vector Draw
				break;
			default:
				fallbackDraw(color);
				break;
		}
	}

}
