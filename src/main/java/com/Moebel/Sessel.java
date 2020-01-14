package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Sessel extends AbstractMoebel {

	public static final Supplier<Sessel> DELUXE = (Supplier<Sessel>) PRESETS.put("deLuxe",() -> new Sessel("deLuxe", 1, 1));

	public Sessel(String name, double width, double height) {
		super(name, width, height);
	}

	/**
	 @{inheritDoc}
	 */
	public void draw(Color color){
		gc.setFill(color.brighter());
		gc.fillRoundRect(0,0,getWidth()/8,getHeight(),0,0);
		gc.strokeRect(0,0,getWidth()/8,getHeight());               //armrests

		gc.fillRoundRect(7*getWidth()/8,0,getWidth()/4,getHeight(),0,0);
		gc.strokeRect(7*getWidth()/8,0,getWidth()/4,getHeight());  //armrests

		gc.fillRoundRect(getWidth()/8,0,6*getWidth()/8,getHeight()/10,0,0);
		gc.strokeRect(getWidth()/8,0,6*getWidth()/8,getHeight()/10);               //backrest

		gc.setFill(color);
		gc.fillRoundRect(getWidth()/8,getHeight()/10,6*getWidth()/8,9*getHeight()/10,0,0);
		gc.strokeRect(getWidth()/8,getHeight()/10,6*getWidth()/8,9*getHeight()/10);               //body
		gc.fill();

		drawName(color);
	}

	/**
	 @{inheritDoc}
	 */
	public double getCostMoebel() {
		return 100;
	}

	/**
	 @{inheritDoc}
	 */
	@Override
	public double cost() {
		return getCostMoebel() + hourlyCost * 0.25;
	}
}
