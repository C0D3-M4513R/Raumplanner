package com.Moebel;

import com.UI.ExeptionDialog;
import com.UI.moebelListNodeController;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.function.Supplier;

import static com.Moebel.Schrank.BILLY;

public class SchrankWand<T extends Moebel> extends Moebel {

	public static final Supplier<SchrankWand<Schrank>> BIGBILLY = (Supplier<SchrankWand<Schrank>>) PRESETS.put("BigBilly",()->SchrankWand.SchrankWandBuilder("BigBilly"));

    private ArrayList<T> furnitureList = new ArrayList<>();
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

    public static SchrankWand SchrankWandBuilder(String name){
        final SchrankWand[] sw = new SchrankWand[1];
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input the number of Billies you want");
        dialog.setHeaderText("Input the number of Billies you want");
        dialog.setContentText("Please input the number of Billies you would like in BigBilly:");
        dialog.showAndWait().ifPresent(input->{
            try {
                int no = Integer.parseInt(input);
                sw[0] = builder(name,no);
            }catch (Throwable throwable){
                ExeptionDialog ex = new ExeptionDialog(Alert.AlertType.ERROR,throwable);
                ex.show();
            }
        });
        return sw[0];
    }

    public static SchrankWand<Schrank> builder(String name,int no){
        return new SchrankWand<>(name,no,BILLY);
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
        } else {
	        System.out.println("Drawing fallback");
            Schrank schrank=new Schrank("",0,0);
            schrank.draw(color);
//            throw new NullPointerException("test, not a real exeption");
        }

    }


    @Override
    public moebelListNodeController getMoebelListNodeController(){
        return new moebelListNodeController(this,getWidth()/STRETCH,getHeight()/STRETCH);
    }

    @Override
    public double cost() {
        if(furnitureList!=null && furnitureList.size()>0) {
            double cost = furnitureList.stream().mapToDouble(Cost::cost).sum();
            return cost + hourlyCost * furnitureList.size() * 0.5;
        }
    return 0;
    }

    @Override
    public void changeColor(Color color) {
        // TODO: Fix this! Doesn't work
        System.out.println("Testttttt");
        furnitureList.forEach(
                moebel->moebel.changeColor(color)
        );
        super.changeColor(color);
    }
}
