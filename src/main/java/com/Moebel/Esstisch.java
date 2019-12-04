package com.Moebel;


import java.util.HashMap;
import java.util.function.Supplier;

public class Esstisch extends Moebel {


	public static final Supplier<Esstisch> WIKINGER = () -> new Esstisch("Wikinger", 1.2, 1.2);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected HashMap<String, Supplier<? extends Moebel>> getPreset() {
		HashMap<String, Supplier<? extends Moebel>> presets = new HashMap<>();
		presets.put("Wikinger", WIKINGER);
		return presets;
	}

	public Esstisch(String name, double width, double height) {
		super(name, width, height);
	}

}
