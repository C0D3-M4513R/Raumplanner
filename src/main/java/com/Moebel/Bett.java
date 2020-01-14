package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Bett extends AbstractMoebel {
	public final int PERSONS;
	public static final Supplier<Bett> LEFRANCE = (Supplier<Bett>) PRESETS.put("Le France", () -> new Bett("Le France", 2, 2, 2));
	public static final Supplier<Bett> LEPETITFRANCE = (Supplier<Bett>) PRESETS.put("Le Petit France", () -> new Bett("Le Petit France", 1, 1, 2));

	public Bett(String name, int persons, double width, double height) {
		super(name, width, height);
		PERSONS = persons;
	}

	/**
	 @{inheritDoc}
	 */
	public void draw(Color color) {
		final double extraRatio = 0.20;
		gc.clearRect(0, 0, getWidth(), getHeight());
		gc.setLineWidth(lw);
		drawRect(color);
		drawName(color);

		//paint extras

		if (name.equals("Le France")) {
			//seperation line, if needed
			gc.beginPath();
			gc.setLineWidth(lw / 2);
			gc.moveTo(getWidth() / 2, 0);
			gc.lineTo(getWidth() / 2, getHeight());
			gc.closePath();

			gc.stroke();
		}

		//bed cushions
		gc.beginPath();
		gc.setLineWidth(lw * extraRatio);
		gc.moveTo(0, getHeight() * extraRatio);
		gc.lineTo(getWidth(), getHeight() * extraRatio);
		gc.closePath();

		gc.stroke();


	}

	/**
	 @{inheritDoc}
	 */
	public double getCostMoebel() {
		return 180;
	}

	/**
	 @{inheritDoc}
	 */
	@Override
	public double cost() {
		return getCostMoebel() + hourlyCost * 1.5;
	}
}
