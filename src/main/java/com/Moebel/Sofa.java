package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Sofa extends AbstractMoebel {

	public static final Supplier<Sofa> KOMFORT = (Supplier<Sofa>) PRESETS.put("Komfort",() -> new Sofa("Komfort", 2, 1));

	public Sofa(String name, double width, double height) {
		super(name, width, height);
	}

	/**
	 @{inheritDoc}
	 */
	public void draw(Color color){
		gc.setFill(color.brighter());
		gc.fillRoundRect(0,0,getWidth()/4,getHeight(),0,0);
		gc.strokeRect(0,0,getWidth()/4,getHeight());               //armrests

		gc.fillRoundRect(3*getWidth()/4,0,getWidth()/4,getHeight(),0,0);
		gc.strokeRect(3*getWidth()/4,0,getWidth()/4,getHeight());  //armrests

		gc.fillRoundRect(getWidth()/4,0,getWidth()/2,getHeight()/10,0,0);
		gc.strokeRect(getWidth()/4,0,getWidth()/2,getHeight()/10);               //backrest

		gc.setFill(color);
		gc.fillRoundRect(getWidth()/4,getHeight()/10,getWidth()/2,9*getHeight()/10,0,0);
		gc.strokeRect(getWidth()/4,getHeight()/10,getWidth()/2,9*getHeight()/10);               //body
		gc.fill();

		drawName(color);
	}

	/**
	 @{inheritDoc}
	 */
	public double getCostMoebel() {
		return 120;
	}

	/**
	 @{inheritDoc}
	 */
	@Override
	public double cost() {
		return getCostMoebel() + hourlyCost;
	}
}
