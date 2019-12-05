package com.Moebel;


import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.function.Supplier;

public class Sessel extends Moebel {

	public static final Supplier<Sessel> DELUXE = () -> new Sessel("deLuxe", 1, 1);

	public Sessel(String name, double width, double height) {
		super(name, width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected HashMap<String, Supplier<? extends Moebel>> getPreset() {
		HashMap<String, Supplier<? extends Moebel>> presets = new HashMap<>();
		presets.put("deLuxe", DELUXE);
		return presets;
	}

	protected void draw(Color color){
		switch (name){
			case "deLuxe":
				//TODO: Vector Draw
				break;
			default:
				fallbackDraw(color);
				break;
		}
	}

}
