package com.Moebel;


import com.UI.Menu.MoebelMenu;
import com.UI.moebelListNodeController;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.paint.Color;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;


/**
 This class handels most of the features related to Displaying Moebel instances
 */
public interface Moebel {
    /**
     * just a random unique id
     */
    double STRETCH = 50.0;
    Color DEFAULT_COLOR = Color.GRAY;
    /**
     This defines how thick the strokes should be by standard
     */
    double lw = 10.0;

    /**
     * An image to use, if no other exists
     */
    Image fallback = new Image(Objects.requireNonNull(Moebel.class.getClassLoader().getResource("chair.png")).toExternalForm());

    /**
     * Holds all Furniture presets defined
     */
     HashMap<String,Supplier<? extends Node>> PRESETS = new HashMap<String, Supplier<? extends Node>>(){
        @Override
        public Supplier<? extends Node> put(String key, Supplier<? extends Node> value) {
            super.put(key,value);
            return value;
        }
    };

    void draw(Color color);
    void draw();

    Color getCurrentColor();
    void changeColor(Color color);

    default ObjectProperty<String> nameProperty(){
        return new ReadOnlyObjectWrapper<>(getName());
    }

    String getName();
    void setName(String name);

    default moebelListNodeController getMoebelListNodeController(){
        moebelListNodeController entry = new moebelListNodeController(this,getWidth()/STRETCH,getHeight()/STRETCH);
        entry.getName();
        //rezise the with to match the new height
        setWidth(entry.getMaxHeight()/getHeight() *getWidth());
        setHeight(entry.getMaxHeight());
        draw();

        return entry;
    }

    default MoebelMenu getMenu(){
        return new MoebelMenu(this);
    }

    default void init() {
        try {
            draw();
        }catch (ConcurrentModificationException e){
            System.out.println("Deferring execution to later, since object isn't fully set-up yet");
            Platform.runLater(this::draw);
        }
        //generic Settings
        setVisible(true);

        setOnContextMenuRequested((Event)->{
            getMenu().visible(getNode(),Event.getScreenX(),Event.getScreenY());
            Event.consume();
        });
    }

    void remove();
    void setVisible(boolean b);
    void setWidth(double b);
    double getWidth();
    void setHeight(double b);
    double getHeight();
    Node getNode();
    void setOnContextMenuRequested(EventHandler<? super ContextMenuEvent> eventHandler);

}
