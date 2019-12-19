package com.Moebel;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static com.Moebel.Schrank.BILLY;

public class SchrankWand extends Moebel {

	public static final Supplier<SchrankWand> BIGBILLY = (Supplier<SchrankWand>) PRESETS.put("BigBilly",()->SchrankWandBuilder.SchrankWandBuilder("BigBilly"));

    private List<Schrank> schraenke = new LinkedList<>();
    private boolean inList = false;
    private boolean finished = false;

    public SchrankWand(String name, int no) {
        super(name, null, null);
        System.out.println(""+no);
        if (no != 0) {
            for (int i = no; i > 0; i--) {
                schraenke.add(BILLY.get());
            }
            setHeight(schraenke.get(0).getHeight());
            setWidth(schraenke.get(0).getWidth() * no);
        } else {
            inList = true;
        }
        finished = true;
    }

    public SchrankWand(String name, Schrank... schranks) {
        super(name, 0.0, 0.0);
        schraenke.addAll(Arrays.asList(schranks));
        setHeight(schraenke.get(0).getHeight());
        setWidth(schraenke.get(0).getWidth() * (schraenke.size() + 1));
    }

    @Override
    protected void draw(Color color) throws ConcurrentModificationException {
    	if(!finished) throw new ConcurrentModificationException("The constructor hasn't finished yet!"); //Halt execution, and wait for the constructor to finish
        if (!inList) {
	        System.out.println("Drawing BigBillyc");
            for (Schrank schrank : schraenke) {
                int no = schraenke.indexOf(schrank);
                schrank.gc=gc;//redirect the draw on us
                schrank.draw(color);
                gc.translate(schrank.getWidth(), 0.0);//make sure,that we
            }
        }else {
	        System.out.println("Drawing fallback");
            Schrank schrank=new Schrank("",0,0);
            schrank.draw(color);
//            throw new NullPointerException("test, not a real exeption");
        }

    }
}
