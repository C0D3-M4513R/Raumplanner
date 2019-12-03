package com.Moebel;


public class Sofatisch  extends Moebel {

    public final boolean ROUND;
    public static final Sofatisch ADMIRAL = new Sofatisch("Admiral",1,1);
    public static final Sofatisch MONTECARLO = new Sofatisch("Monte Carlo",1);

    public Sofatisch(String name, double width, double height) {
        super(name,width,height);
        ROUND=false;
    }
    public Sofatisch(String name, double radius) {
        super(name,radius*2,radius*2);
        ROUND=true;
    }



}
