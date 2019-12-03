package com.Moebel;


public class Sessel extends Moebel {

    public Sessel(String name, double width, double height) {
        super(name,width,height);
    }
    public static final Sessel DELUXE = new Sessel("deLuxe",1,1);
}
