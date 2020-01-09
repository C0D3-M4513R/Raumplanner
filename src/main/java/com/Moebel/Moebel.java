package com.Moebel;


import com.Operators;
import com.UI.Menu.MoebelMenu;
import com.UI.moebelListNodeController;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.*;
import java.util.function.Supplier;


/**
 This class handels most of the features related to Displaying Moebel instances
 */
public abstract class Moebel extends Canvas implements Cost {
    /**
     * Indicates how many Objects of this class have already been created
     */
    private static int no = 0;
    /**
     * just a random unique id
     */
    public final int id = (++no); //Unique id for all members of this class, to make identifying of duplicates easier
    public final static double STRETCH = 50.0;
    public static final Color DEFAULT_COLOR = Color.GRAY;
    public Color currentColor = DEFAULT_COLOR;
    private final Double width,height;
    protected MoebelMenu menu = new MoebelMenu(this);
    protected GraphicsContext gc = getGraphicsContext2D();
    /**
     This defines how thick the strokes should be by standard

     */
    protected static final double lw = 10.0;
    private List<InvalidationListener> invalidationListener = new ArrayList<>();

    /**
     * The name of the current Moebel
     */
    protected String name = "";

    /**
     * An image to use, if no other exists
     */
    public final static Image fallback = new Image(Objects.requireNonNull(Moebel.class.getClassLoader().getResource("chair.png")).toExternalForm());

    /**
     * Holds all Furniture presets defined
     */
    protected static HashMap<String,Supplier<? extends Moebel>> PRESETS = new HashMap<String, Supplier<? extends Moebel>>(){
        @Override
        public Supplier<? extends Moebel> put(String key, Supplier<? extends Moebel> value) {
            super.put(key,value);
            return value;
        }
    };


    /**
     * @return Returns a list, that holds all Furniture Presets
     */
    public static HashMap<String, Supplier<? extends Moebel>> getPRESETS() {
        return PRESETS;
    }

    protected abstract void draw(Color color);
    public void draw(){
        draw(currentColor);
    }

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
//        gc.setStroke(Color.BLACK);
//        gc.beginPath();
//
//        gc.lineTo(startX, startY);             //effectively moves to 0,0
//        gc.lineTo(startX+width, startY);        //top
//        gc.lineTo(startX+width, startY+height);  //right
//        gc.lineTo(startX, startY+height);       //bottom
//        gc.lineTo(startX, startY);             //left
//        gc.closePath();
//
//        gc.setFill(color);
//        //color in the middle first
//        gc.fill();
//        //so the black strokes are colored above
//        gc.stroke();
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

    public void changeColor(Color color){
        currentColor=color;
        gc.clearRect(0,0,getWidth(),getHeight());
        draw(color);
    }




    public String getName() {
        return name;
    }

    public ObjectProperty<String> nameProperty(){
        return new ReadOnlyObjectWrapper<>(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public moebelListNodeController getMoebelListNodeController(){
        moebelListNodeController entry = new moebelListNodeController(this,getWidth()/STRETCH,getHeight()/STRETCH);
        final double ratio = width/height;


//        gc.setTransform(xScale,1,1,getWidth()*xScale,0,0);
        setHeight(entry.getMaxHeight());
        setWidth(entry.getMaxHeight()*ratio);
        draw(currentColor);

        return entry;
    }

    public Moebel(String name,Double width,Double height) {
        this(width,height);
        setName(name);
    }

    private Moebel(Double width,Double height) {
        super(Operators.ifNullRet(width,0.0) *STRETCH,Operators.ifNullRet(height,0.0)*STRETCH);
        try {
        	draw(DEFAULT_COLOR);
        }catch (ConcurrentModificationException e){
	        System.out.println("Deferring execution to later, since object isn't fully set-up yet");
	        Platform.runLater(()->draw(DEFAULT_COLOR));
        }
        //generic Settings
        setVisible(true);
        //set height and width
        if(width!=null) setWidth(width * STRETCH);
        if(height!=null) setHeight(height * STRETCH);
        this.width = width;
        this.height = height;
        //attach menu
        setOnContextMenuRequested((Event)->{
            menu.visible(this,Event.getScreenX(),Event.getScreenY());
            Event.consume();
        });
    }
}
