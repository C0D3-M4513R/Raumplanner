package com.Moebel;


public class Esstisch extends Moebel {


    public static final Esstisch WIKINGER = new Esstisch("Wikinger",1.2,1.2);

    public Esstisch(String name, double width, double height) {
        super(name,width,height);
    }

}
