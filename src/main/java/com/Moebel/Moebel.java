package com.Moebel;


import com.Operators;
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


//TODO: maybe make this a canvas to draw line elements?
public abstract class Moebel extends Canvas {

//    This is all handled by our superclass. We don't need these anymore
//    private double Breite; //Breite = Höhe
//    private double Laenge; //Länge = Länge
//    private int x;
//    private int y;
//    private int rotation;
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

//    /**
//     * Indicates if we are on a fallback image
//     */
//    boolean fallbackImg=false;

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

    protected GraphicsContext gc = getGraphicsContext2D();

    protected abstract void draw(Color color);
    protected void fallbackDraw(Color color){
        System.out.println("Fallback draw");
//        int arcCurve = 10;
//        gc.setStroke(color);
//        gc.setLineWidth(10);
//        gc.beginPath();
//        gc.moveTo(0,0);
//        gc.lineTo(0,height*STRETCH-arcCurve);
//        gc.bezierCurveTo(0,height*STRETCH-arcCurve,arcCurve/2.0,height*STRETCH-(arcCurve/2.0),arcCurve,height*STRETCH);
//        gc.lineTo(width*STRETCH,height*STRETCH);
//        gc.closePath();
//
//        gc.fillRect(0,0,width*STRETCH,height*STRETCH);

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

/*    public Image getImage(boolean overridden){
        if(getImage()==null){
            fallbackImg=true;
            setImage(fallback);
        } else if(getImage().equals(fallback)) {
            fallbackImg=true; //still on fallback
        } else {
            fallbackImg=false; //not on fallback anymore
        }
        return getImage();
    }*/

    public String getName() {
        return name;
    }

    public ObjectProperty<String> nameProperty(){
        return new ReadOnlyObjectWrapper<>(name);
    }

    public void setName(String name) {
        this.name = name;
    }

/*    public double getLaenge() {
        return Laenge;
    }

    public void setLaenge(double laenge) {
        Laenge = laenge;
    }

    public double getBreite() {
        return Breite;
    }

    public void setBreite(double breite) {
        Breite = breite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
    */

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
//        setPreserveRatio(true);
//        setSmooth(true);
        setVisible(true);
        //set height and width
        if(width!=null) setWidth(width * STRETCH);
        if(height!=null) setHeight(height * STRETCH);
//        setFitHeight(height * 50);
//        setFitWidth(width * 50);
        //actually have an image to display
//        getImage(true);
    }


}
