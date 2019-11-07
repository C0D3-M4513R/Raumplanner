package com.Moebel;


import javafx.scene.image.Image;

public abstract class Moebel {

    private String Name = "";
    private double Breite; //Breite = Höhe
    private double Laenge; //Länge = Länge
    private int x;
    private int y;
    private int rotation;

    public Image getDisplay() {
        return display;
    }

    public void setDisplay(Image display) {
        this.display = display;
    }

    private Image display;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLaenge() {
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

    public Moebel(String name, double laenge, double breite, Image display ) {
        Name = name;
        Laenge = laenge;
        Breite = breite;
        this.display=display;
    }

    public Moebel() {

    }
}
