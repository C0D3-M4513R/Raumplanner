package com.Moebel;


public class Stuhl extends Moebel {
    public Stuhl(String name, double width, double height) {
        super(name,width,height);
    }
    public static final Stuhl NOMADE = new Stuhl("Nomade",0.5,0.5);
}
