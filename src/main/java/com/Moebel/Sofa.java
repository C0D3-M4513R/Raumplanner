package com.Moebel;


public class Sofa extends Moebel {

	public Sofa(String name, double width, double height) {
        super(name,width,height);
    }
	public static final Sofa KOMFORT = new Sofa("Komfort",2,1);
}
