package com.Moebel;


public class Bett extends Moebel {

    public final int PERSONS;
    public static final Bett LEFRANCE = new Bett("Le France",2,2,2);
    public static final Bett LEPETITFRANCE = new Bett("Le Petit France",1,1,2);
    public Bett(String name,int persons, double width, double height) {
        super(name,width,height);
        PERSONS=persons;
    }

}
