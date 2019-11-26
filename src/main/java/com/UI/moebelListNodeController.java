package com.UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class moebelListNodeController extends SplitPane {
    @FXML
    Label title;
    @FXML
    Label description;
    @FXML
	ImageView img;
    Image display;
    double width;
    String tit ="lol";
    String desc ="lol";
    String type = null;
    //TODO: remove all temporary labels


    public moebelListNodeController(String title, String description, String type, Image display,double width) {
        this.tit=title;
        this.desc=description;
        this.display=display;
        this.width =width;
        this.type=type;
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("moebelListNode.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void recalculate(double width){
        title.resize(width,title.getHeight());
        description.resize(width,description.getHeight());
    }

    @FXML
    public void initialize(){
        title.setText(tit);
        description.setText(desc);
        img.setImage(display);
        if(type!=null){
        	Tooltip tip = new Tooltip(type);
        	tip.setOpacity(0.5);
        	title.setTooltip(tip);
        }
    }

}
