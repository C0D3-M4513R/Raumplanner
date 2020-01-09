package com.Moebel;


import com.Operators;
import com.UI.Menu.MoebelMenu;
import com.UI.moebelListNodeController;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
    public static final Color DEFAULT_COLOR = Color.BLACK;
    public Color currentColor = DEFAULT_COLOR;
    private final Double width,height;
    protected MoebelMenu menu = new MoebelMenu(this);
    protected GraphicsContext gc = getGraphicsContext2D();
    /** This defines how thick the strokes should be by standard */
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
