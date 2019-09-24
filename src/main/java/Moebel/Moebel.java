package Moebel;

public abstract class Moebel {

    String Name = "";
    double Breite;
    double Laenge;
    int x;
    int y;
    int rotation;

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

    public Moebel(String name, double laenge, double breite) {
        Name = name;
        Laenge = laenge;
        Breite = breite;
    }

    public Moebel() {

    }
}
