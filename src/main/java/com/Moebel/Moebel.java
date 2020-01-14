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

    /**
     Updates, what is seen on the Screen
     @param color Background Color
     */
    void draw(Color color);
    /**
     Updates, what is seen on Screen
     Use the default color
     */
    default void draw(){
        draw(getCurrentColor());
    }
    /**
     Gets the Current Background color
     @return Returns the current Background Color
     */
    Color getCurrentColor();
    /**
     Change the Background color of what is being displayed
     @param color Color to switch to
     */
    void changeColor(Color color);

    /**
     A property, of what this Moebel is called
     @return Returns a Property, of this Moebel's name
     */
    default ObjectProperty<String> nameProperty(){
        return new ReadOnlyObjectWrapper<>(getName());
    }
    /**
     Gets the Name of this Moebel
     @return Returns the Name of this Moebel
     */
    String getName();
    /**
     Sets the Name of this Moebel
     */
    void setName(String name);

    /**
     Preps this node for being displayed in a confined Space
     @return Returns an {@link moebelListNodeController} to add to a List (such as {@link com.UI.UI#moebelList})
     */
    default moebelListNodeController getMoebelListNodeController(){
        moebelListNodeController entry = new moebelListNodeController(this,getWidth()/STRETCH,getHeight()/STRETCH);
        entry.getName();
        //rezise the with to match the new height
        setWidth(entry.getMaxHeight()/getHeight() *getWidth());
        setHeight(entry.getMaxHeight());
        draw();

        return entry;
    }

    /**
     Gets a new Menu, that will be used for providing a number of events to the User
     @return Returns a new Menu, that will be used for providing a number of events to the User
     */
    default MoebelMenu getMenu(){
        return new MoebelMenu(this);
    }

    /**
     Basically what a Constructor should do, however we lack those in interfaces, so we use a dedicated method, (that SHOULD get run in a Constructor) to accommodate for that
     */
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

    /**
     Properly delete this element
     */
    void remove();

    /**
     Sets wether this element is being displayed.
     Note: this will get Overridden by some javafx implementation
     @param b Boolean, that decides, if it is being displayed (true) or not (false)
     */
    void setVisible(boolean b);
    /**
     Set the current Width of this Element.
     Will sometimes be overridden by an implementation in javafx
     */
    void setWidth(double b);
    /**
     Get the current Width.
     Note: this will get Overridden by some javafx implementation
     @return Returns the current Width
     */
    double getWidth();
    /**
     Sets the current Height
     Will sometimes be overridden by an implementation in javafx
     @param b Height to be set
     */
    void setHeight(double b);
    /**
     Get the current Height.
     Note: this will get Overridden by some javafx implementation
     @return Returns the current Height
     */
    double getHeight();
    /**
     Get the javafx Node, that is displaying this
     @return Returns the javafx Node, that is displaying this
     */
    Node getNode();
    /**
     This sets the wanted behavior of what happens if a Menu is requested
     Note: this will get Overridden by some javafx implementation (Node)
     @param eventHandler Specifies what to do on the event
     */
    void setOnContextMenuRequested(EventHandler<? super ContextMenuEvent> eventHandler);

}
