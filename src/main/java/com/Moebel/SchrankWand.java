package com.Moebel;

import com.UI.ExeptionDialog;
import com.UI.RootPane;
import com.UI.UI;
import com.UI.moebelListNodeController;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.Moebel.Schrank.BILLY;

public class SchrankWand<T extends Moebel> extends HBox implements Moebel{

	public static final Supplier<SchrankWand<Schrank>> BIGBILLY = (Supplier<SchrankWand<Schrank>>) PRESETS.put("BigBilly",()->SchrankWand.SchrankWandBuilder("BigBilly"));

    private boolean inList = false;
    private boolean finished = false;
    private Color currentColor = DEFAULT_COLOR;
    private String name = "";

    private SchrankWand(String name){
        super();
        init();
        RootPane.add(this);
        setName(name);
        //prevent Children from giving any Menus
        addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED,(Event)->{
            getMenu().visible(getNode(),Event.getScreenX(),Event.getScreenY());
            Event.consume();
        });
    }

    public SchrankWand(String name, int no, Supplier<T> supplier) {
        this(name);
        System.out.println(""+no);
        if (no != 0) {
            for (int i = no; i > 0; i--) {
                getChildren().add(supplier.get().getNode());
            }
            setHeight(((Moebel)getChildren().get(0)).getHeight());
            setWidth(((Moebel)getChildren().get(0)).getWidth() * no);
        } else {
            inList = true;
        }
        finished = true;
    }

    @SafeVarargs
    public SchrankWand(String name, T... schranks) {
        this(name);
        getChildren().addAll((Collection<? extends Node>) Arrays.stream(schranks).map((Function<Moebel,Node>) Moebel::getNode));
        setHeight(((Moebel)getChildren().get(0)).getHeight());
        setWidth(schranks[0].getWidth() * (getChildren().size() + 1));
    }

    public static SchrankWand<Schrank> SchrankWandBuilder(String name){
        final SchrankWand<Schrank>[] sw = new SchrankWand[1];
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

    /**
     @{inheritDoc}
     */
    @Override
    public void remove(){
        RootPane.delete(this);
    }

    /**
     @{inheritDoc}
     */
    @Override
    public void draw(Color color) throws ConcurrentModificationException {
    	if(!finished) throw new ConcurrentModificationException("The constructor hasn't finished yet!"); //Halt execution, and wait for the constructor to finish
        if (!inList) {
	        System.out.println("Drawing BigBillyc");
            for (Node furniture : getChildren()) {
                ((Moebel)furniture).draw(currentColor);
            }
        } else {
	        System.out.println("Drawing fallback");
            Schrank schrank=new Schrank("",0,0);
            schrank.draw(color);
//            throw new NullPointerException("test, not a real exeption");
        }

    }

    /**
     @{inheritDoc}
     */
    @Override
    public Color getCurrentColor() {
        return currentColor;
    }

    /**
     @{inheritDoc}
     */
    @Override
    public void changeColor(Color color) {
        // TODO: Fix this! Doesn't work
        System.out.println("Testttttt");
        getChildren().forEach(
                moebel->((Moebel)moebel).changeColor(color)
        );
    }

    /**
     @{inheritDoc}
     */
    @Override
    public moebelListNodeController<SchrankWand<T>> getMoebelListNodeController(){
        return new moebelListNodeController<>(this, getWidth() / STRETCH, getHeight() / STRETCH);
    }

    @Override
    public void setWidth(double b) {
        UI.setWidth(this, b);
    }

    @Override
    public void setHeight(double b) {
        UI.setHeight(this,b);
    }

    /**
     @{inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     @{inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name=name;
    }

    /**
     @{inheritDoc}
     */
    @Override
    public Node getNode() {
        return this;
    }
}
