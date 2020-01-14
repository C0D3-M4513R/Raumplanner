package com.Moebel;


import javafx.scene.paint.Color;

import java.util.function.Supplier;

public class Stuhl extends AbstractMoebel {

	public static final Supplier<Stuhl> NOMADE = (Supplier<Stuhl>) PRESETS.put("Nomade",() -> new Stuhl("Nomade", 0.5, 0.5));

	public Stuhl(String name, double width, double height) {
		super(name, width, height);
	}


    public void draw(Color color){
	    gc.fillRoundRect(0,0,6*getWidth()/8,getHeight()/10,0,0);
	    gc.strokeRect(0,0,6*getWidth()/8,getHeight()/10);               //backrest

	    gc.setFill(color);
	    gc.fillRoundRect(0,getHeight()/10,6*getWidth()/8,9*getHeight()/10,0,0);
	    gc.strokeRect(0,getHeight()/10,6*getWidth()/8,9*getHeight()/10);               //body
	    gc.fill();

	    drawName(color);
    }

	public double getCostMoebel() {
		return 50;
	}

	@Override
	public double cost() {
		return getCostMoebel();
	}
}
