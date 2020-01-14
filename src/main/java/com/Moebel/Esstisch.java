package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Esstisch extends AbstractMoebel {

	final double inset = getWidth()*0.25;
	public static final Supplier<Esstisch> WIKINGER = (Supplier<Esstisch>) PRESETS.put("Wikinger",() -> new Esstisch("Wikinger", 1.2, 1.2));

	public void draw(Color color){
		gc.setLineWidth(lw);
		drawRect(color);
		drawName(color);

		gc.setLineWidth(lw * 0.20);
		drawSqr(inset,inset/2,color);   //top left
		drawSqr(getWidth()-inset-inset/2,inset/2,color);    //bottom right
		drawSqr(getWidth()-inset-inset/2,inset,inset/2,color);        //top right
		drawSqr(inset,getHeight()-inset-inset/2,inset/2,color);        //bottom left

	}

	public Esstisch(String name, double width, double height) {
		super(name, width, height);
	}

	public double getCostMoebel() {
		return 250;
	}

	@Override
	public double cost() {
		return getCostMoebel()+ hourlyCost * 2;
	}
}
