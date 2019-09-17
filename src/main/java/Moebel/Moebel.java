package Moebel;

abstract class Moebel {

    private String Name = "";
    private int Breite;
    private int Laenge;
    private int x;
    private int y;
    private int rotation;

    public Moebel(String name, int laenge, int breite) {
        Name = name;
        Laenge = laenge;
        Breite = breite;
    }


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

}
