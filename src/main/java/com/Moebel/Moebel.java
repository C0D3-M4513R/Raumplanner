package com.Moebel;


import com.UI.moebelListNodeController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public abstract class Moebel extends ImageView {

//    This is all handled by our superclass. We don't need these anymore
//    private double Breite; //Breite = Höhe
//    private double Laenge; //Länge = Länge
//    private int x;
//    private int y;
//    private int rotation;

    private final double height,width;
    private String Name = "";
    boolean fallbackImg=false;
    moebelListNodeController listController;

    public Image getImage(boolean overridden){
        Image img = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResource("chair.png")).toExternalForm(),true);
        if(getImage()==null){
            fallbackImg=true;
            setImage(img);
        } else if(getImage().equals(img)) {
            fallbackImg=true; //still on fallback
        } else {
            fallbackImg=false; //not on fallback anymore
        }
        return getImage();
    }

    public moebelListNodeController getListController() {
        return listController;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
        listController = new moebelListNodeController(this,width,height); //Update the listController
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

    public Moebel(String name,double width,double height) {
        this(width,height);
        setName(name);
    }

    private Moebel(double width,double height) {
        super();
        this.height=height;
        this.width=width;
        listController = new moebelListNodeController(this,width,height);
        setFitHeight(height * 50);
        setFitWidth(width * 50);
        setPreserveRatio(true);
        setSmooth(true);
    }
}
