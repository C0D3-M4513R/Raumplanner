package Moebel;

public abstract class Moebel {

    String Name = "";
    int Breite;
    int Laenge;
    int x;
    int y;
    int rotation;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getLaenge() {
        return Laenge;
    }

    public void setLaenge(int laenge) {
        Laenge = laenge;
    }

    public int getBreite() {
        return Breite;
    }

    public void setBreite(int breite) {
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

    public Moebel(String name, int laenge, int breite) {
        Name = name;
        Laenge = laenge;
        Breite = breite;
    }

    public Moebel() {

    }
}
