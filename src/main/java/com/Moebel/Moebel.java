package com.Moebel;


import com.Operators;
import com.UI.Menu.MoebelMenu;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;


/**
 This class handels most of the features related to Displaying Moebel instances
 */
public abstract class Moebel extends Canvas {

    /**
     * Indicates how many Objects of this class have already been created
     */
    private static int no = 0;
    /**
     * just a random unique id
     */
    public final int id = (++no); //Unique id for all members of this class, to make identifying of duplicates easier
    public final static double STRETCH = 50.0;
    /**
     * The name of the current Moebel
     */
    protected String name = "";

    /**
     * An image to use, if no other exists
     */
    public final static Image fallback = new Image(Objects.requireNonNull(Moebel.class.getClassLoader().getResource("chair.png")).toExternalForm(),true);

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

    protected MoebelMenu menu = new MoebelMenu();

    protected GraphicsContext gc = getGraphicsContext2D();

    protected abstract void draw(Color color);
    protected void fallbackDraw(Color color){
        System.out.println("Fallback draw");
        gc.drawImage(fallback,0,0,getWidth(),getHeight());
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Moebel)
            return ((Moebel)obj).id == id;
        else
            return super.equals(obj);
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

    public Moebel(String name,Double width,Double height) {
        this(width,height);
        setName(name);
    }

    private Moebel(Double width,Double height) {
        super(Operators.ifNullRet(width,0.0) *STRETCH,Operators.ifNullRet(height,0.0)*STRETCH);
        try {
        	draw(Color.BLACK);
        }catch (ConcurrentModificationException e){
	        System.out.println("Deferring execution to later, since object isn't fully set-up yet");
	        Platform.runLater(()->draw(Color.BLACK));
        }
        //generic Settings
        setVisible(true);
        //set height and width
        if(width!=null) setWidth(width * STRETCH);
        if(height!=null) setHeight(height * STRETCH);
        //attach menu
        setOnContextMenuRequested((Event)->{menu.visible(this,Event.getScreenX(),Event.getScreenY());});
    }


}
