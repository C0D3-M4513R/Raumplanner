package com.Moebel;

import com.UI.ExeptionDialog;
import com.UI.RootPane;
import com.UI.UI;
import com.UI.moebelListNodeController;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.function.Supplier;

import static com.Moebel.Schrank.BILLY;

public class SchrankWand<T extends AbstractMoebel> extends HBox implements Moebel{

	public static final Supplier<SchrankWand<Schrank>> BIGBILLY = (Supplier<SchrankWand<Schrank>>) PRESETS.put("BigBilly",()->SchrankWand.SchrankWandBuilder("BigBilly"));

    private boolean inList = false;
    private boolean finished = false;
    private Color currentColor = DEFAULT_COLOR;
    private String name = "";

    private SchrankWand(){
        super();
        init();
        RootPane.add(this);
    }

    public SchrankWand(String name, int no, Supplier<T> supplier) {
        this();
        System.out.println(""+no);
        if (no != 0) {
            for (int i = no; i > 0; i--) {
                getChildren().add(supplier.get());
            }
            setHeight(((T)getChildren().get(0)).getHeight());
            setWidth(((T)getChildren().get(0)).getWidth() * no);
        } else {
            inList = true;
        }
        finished = true;
    }

    @SafeVarargs
    public SchrankWand(String name, T... schranks) {
        this();
        getChildren().addAll(Arrays.asList(schranks));
        setHeight(((T)getChildren().get(0)).getHeight());
        setWidth(schranks[0].getWidth() * (getChildren().size() + 1));
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
    public void remove(){
        RootPane.delete(this);
    }

    @Override
    public void draw(Color color) throws ConcurrentModificationException {
    	if(!finished) throw new ConcurrentModificationException("The constructor hasn't finished yet!"); //Halt execution, and wait for the constructor to finish
        if (!inList) {
	        System.out.println("Drawing BigBillyc");
            for (Node furniture : getChildren()) {
                ((T)furniture).draw(currentColor);
            }
        } else {
	        System.out.println("Drawing fallback");
            Schrank schrank=new Schrank("",0,0);
            schrank.draw(color);
//            throw new NullPointerException("test, not a real exeption");
        }

    }

    @Override
    public void draw() {
        draw(getCurrentColor());
    }

    @Override
    public Color getCurrentColor() {
        return currentColor;
    }

    @Override
    public void changeColor(Color color) {
        // TODO: Fix this! Doesn't work
        System.out.println("Testttttt");
        getChildren().forEach(
                moebel->((AbstractMoebel)moebel).changeColor(color)
        );
    }

    @Override
    public moebelListNodeController getMoebelListNodeController(){
        return new moebelListNodeController(this,getWidth()/STRETCH,getHeight()/STRETCH);
    }

    @Override
    public void setWidth(double b) {
        UI.setWidth(this, b);
    }

    @Override
    public void setHeight(double b) {
        UI.setHeight(this,b);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public Node getNode() {
        return this;
    }
}
