package com.Moebel;

import com.UI.moebelListNodeController;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SchrankWand<T extends Moebel> extends Moebel {

	public static final Supplier<SchrankWand<Schrank>> BIGBILLY = (Supplier<SchrankWand<Schrank>>) PRESETS.put("BigBilly",()->SchrankWandBuilder.SchrankWandBuilder("BigBilly"));

    private List<T> furnitureList = new LinkedList<>();
    private boolean inList = false;
    private boolean finished = false;

    public SchrankWand(String name, int no, Supplier<T> supplier) {
        super(name, null, null);
        System.out.println(""+no);
        if (no != 0) {
            for (int i = no; i > 0; i--) {
                furnitureList.add(supplier.get());
            }
            setHeight(furnitureList.get(0).getHeight());
            setWidth(furnitureList.get(0).getWidth() * no);
        } else {
            inList = true;
        }
        finished = true;
    }

    @SafeVarargs
    public SchrankWand(String name, T... schranks) {
        super(name, 0.0, 0.0);
        furnitureList.addAll(Arrays.asList(schranks));
        setHeight(furnitureList.get(0).getHeight());
        setWidth(furnitureList.get(0).getWidth() * (furnitureList.size() + 1));
    }

    @Override
    protected void draw(Color color) throws ConcurrentModificationException {
    	if(!finished) throw new ConcurrentModificationException("The constructor hasn't finished yet!"); //Halt execution, and wait for the constructor to finish
        if (!inList) {
	        System.out.println("Drawing BigBillyc");
            for (T furniture : furnitureList) {
                int no = furnitureList.indexOf(furniture);
                furniture.gc=gc;//redirect the draw on us
                furniture.draw(color);
                gc.translate(furniture.getWidth(), 0.0);//make sure,that we don't draw on the same spot no times
            }
        }else {
	        System.out.println("Drawing fallback");
            Schrank schrank=new Schrank("",0,0);
            schrank.draw(color);
//            throw new NullPointerException("test, not a real exeption");
        }

    }
}
