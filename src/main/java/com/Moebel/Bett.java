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

				gc.setStroke(Color.BLACK);
				gc.setLineWidth(lw);
				
				gc.moveTo(0,0);
				gc.beginPath();
				gc.lineTo(getWidth(),0);
				gc.lineTo(getWidth(),getHeight());
				gc.lineTo(0,getHeight());
				gc.lineTo(0,0);
				gc.setFill(color);
				gc.closePath();

				//color in the middle first
				gc.fill();
				//so the black strokes are colored above
				gc.stroke();

				break;
			case "Le Petit France":
				//TODO: Vector Draw
//				break;
			default:
				fallbackDraw(color);
				break;
		}
	}

	public double getCostMoebel() {
		return 180;
	}

	@Override
	public double cost() {
		return getCostMoebel() + hourlyCost * 1.5;
	}
}
