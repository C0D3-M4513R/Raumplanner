package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Sofatisch extends AbstractMoebel {

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


	/**
	 @{inheritDoc}
	 */
	public void draw(Color color){
		gc.clearRect(0,0,getWidth(),getHeight());
		gc.setStroke(Color.BLACK);
		gc.setFill(color);
		gc.setLineWidth(lw);
		switch (name){
			case "Admiral":
				drawRect(color);
				drawName(color);
				break;
			case "Monte Carlo":
				//we have all those lineWith constants there to prevent clipping
				gc.strokeRoundRect(lw/2,lw/2,getWidth()-lw,getHeight()-lw,getWidth()-lw,getHeight()-lw);
				gc.fillRoundRect(lw/2,lw/2,getWidth()-lw,getHeight()-lw,getWidth()-lw,getHeight()-lw);

				drawName(color);
				break;
			default:
				fallbackDraw(color);
				break;
		}
	}

	/**
	 @{inheritDoc}
	 */
	public double getCostMoebel() {
		return 80;
	}

	/**
	 @{inheritDoc}
	 */
	@Override
	public double cost() {
		return getCostMoebel() + hourlyCost *0.5;
	}
}
