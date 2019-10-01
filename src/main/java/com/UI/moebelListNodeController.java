package com.UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class moebelListNodeController extends GridPane {
    @FXML
    Label title;
    @FXML
    Label description;
    Image display;
    double breite;
    String tit ="lol";
    String desc ="lol";


    public moebelListNodeController(String title, String description, Image display,double breite) {
        this.tit=title;
        this.desc=description;
        this.display=display;
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("moebelListNode.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        title.setText(tit);
        description.setText(desc);
    }

}
