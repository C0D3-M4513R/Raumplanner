package com.UI;

import com.Moebel.Moebel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class moebelListNodeController extends SplitPane {
    @FXML
    Label title = new Label();
    @FXML
    Label description = new Label();
    @FXML
    ImageView img = new ImageView();
    Moebel moebel;

    public moebelListNodeController(Moebel moebel,double width, double height){
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("moebelListNode.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        this.moebel = moebel;
        moebel.getImage(true);  //sets fallback
        img.imageProperty().bindBidirectional(moebel.imageProperty());
        title.setText(moebel.getName());
        title.setTooltip(new Tooltip(moebel.getClass().getSimpleName()));
        description.setText(""+height+"x"+width);
    }

    public String getTitle() {
        return title.getText();
    }

    public Label getDescription() {
        return description;
    }

    public Class<? extends Moebel> getType() {
        return moebel.getClass();
    }

    public String getName() {
        return moebel.getName();
    }

    public Moebel getMoebel(){return moebel;}
}
