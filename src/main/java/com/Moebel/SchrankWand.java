package com.Moebel;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static com.Moebel.Schrank.BILLY;

public class SchrankWand extends Moebel {

    private List<Schrank> schraenke = new LinkedList<>();
    private boolean inList = false;

    public SchrankWand(String name, int no) {
        super(name, null, null);
        if (no != 0) {
            for (int i = no; i > 0; i--) {
                schraenke.add(BILLY.get());
            }
            setHeight(schraenke.get(0).getHeight());
            setWidth(schraenke.get(0).getWidth() * no);
        } else {
            inList = true;
        }
    }

    public SchrankWand(String name, Schrank... schranks) {
        super(name, 0.0, 0.0);
        schraenke.addAll(Arrays.asList(schranks));
        setHeight(schraenke.get(0).getHeight());
        setWidth(schraenke.get(0).getWidth() * (schraenke.size() + 1));
    }

    @Override
    protected HashMap<String, Supplier<? extends Moebel>> getPreset() {
        return null;
    }

    @Override
    protected void draw(Color color) {
        if (inList) {
            for (Schrank schrank : schraenke) {
                int no = schraenke.indexOf(schrank);
                schrank.draw(color);
                gc.translate(schrank.getWidth(), 0.0);
            }
        }else {
            Schrank schrank=new Schrank("",0,0);
            schrank.draw(color);
        }

    }
}
