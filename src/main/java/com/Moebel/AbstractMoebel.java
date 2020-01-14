package com.Moebel;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public abstract class AbstractMoebel extends Canvas implements Moebel,Cost {

	protected Color currentColor = DEFAULT_COLOR;
	protected GraphicsContext gc = getGraphicsContext2D();
	protected String name = "";

	public AbstractMoebel(String name, double width, double height) {
		init();
		setName(name);
		setWidth(width*STRETCH);
		setHeight(height*STRETCH);
	}

	@Override
	public void draw() {
		draw(getCurrentColor());
	}

	@Override
	public Color getCurrentColor(){
		return currentColor;
	}

	@Override
	public void changeColor(Color color) {
		currentColor=color;
		gc.clearRect(0,0,getWidth(),getHeight());
		draw(color);
	}

	@Override
	public Node getNode() {
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void remove() {
		Cost.super.remove();
	}

	//-------------------------Begin Draw helpers---------------------------------
	/**
	 Draws a square with the dimensions wh and at the position start
	 @param start start x/y coordinate
	 @param wh width + height
	 @param color Color of the infill
	 */
	protected void drawSqr(double start, double wh, Color color){
		drawRect(start,start,wh,wh,color);
	}
	/**
	 Draws a square with the dimensions wh and at the position start
	 @param startX start x coordinate
	 @param startY start y coordinate
	 @param wh width + height
	 @param color Color of the infill
	 */
	protected void drawSqr(double startX, double startY, double wh, Color color){
		drawRect(startX, startY,wh,wh,color);
	}

	/**
	 Draws a black line around the edges.
	 Also colors in the middle
	 @param color Color to be used
	 */
	protected void drawRect(Color color){
		drawRect(0,0,getWidth(),getHeight(),color);
	}

	/**
	 Draws a Rectangle;
	 @param startX this defines the X starting coordinate
	 @param startY this defines the Y starting coordinate
	 @param width this defines how wide the rectangle should be
	 @param height this defines how high the rectangle should be
	 @param color this defines the color of the infill
	 */
	protected void drawRect(double startX, double startY, double width, double height,Color color)
	{
		gc.setStroke(Color.BLACK);
		gc.setFill(color);
		gc.fillRoundRect(startX,startY,width,height,STRETCH/2,STRETCH/2);
		gc.strokeRoundRect(startX,startY,width,height,STRETCH/2,STRETCH/2);
	}
	protected void drawName(Color color){
		//draw Name on the Moebel
		gc.setFill(Color.BLACK);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.fillText(
				getName(),
				Math.round(getWidth() / 2),
				Math.round(getHeight() / 2),
				Math.round(getWidth()-5)
		);
		gc.setFill(color);
	}

	protected final void fallbackDraw(Color color){
		System.out.println("Fallback draw");
		gc.setFill(color);
		gc.fillRect(0,0,getWidth(),getHeight());
		gc.drawImage(fallback,0,0,getWidth(),getHeight());
	}

}
