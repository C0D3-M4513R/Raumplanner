import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class moebelListNodeController extends GridPane {
    @FXML
    Label title;
    @FXML
    Label description;
    String tit ="lol";
    String desc ="lol";


    public moebelListNodeController(String title,String description) {
        this.tit=title;
        this.desc=description;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("moebelListNode.fxml"));
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