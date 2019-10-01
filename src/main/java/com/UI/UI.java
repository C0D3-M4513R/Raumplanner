package com.UI;

import com.Moebel.Moebel;
import com.Repository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles all of the User-Interface interactions and Processes them
 * @author Timon Kayser
 */
public class UI {
    @FXML
    private SplitPane divider;
    @FXML
    private ListView<moebelListNodeController> moebelList;
    @FXML
    private AnchorPane room;

    private final List<Moebel> list = Repository.getAll();
    private List<com.UI.moebelListNodeController> display = new LinkedList<>();

    @FXML
    public void initialize() throws IOException {
        populate();
    }

    //Populate the ListView and other stuff on startup
    private void populate(){
        display = new LinkedList<>();
        list.forEach(Moebel-> {
            String title = Moebel.getName().concat(":").concat(Moebel.getClass().getSimpleName());
            String desc = ""+Moebel.getBreite()+"x"+Moebel.getLaenge();
            display.add(new com.UI.moebelListNodeController(title,desc));
        });
        display.forEach(this::dragNode);
        moebelList.setItems(FXCollections.observableList(display));
    }

    /** Handles all dragging EventHandlers for any node object
     *
     * @param node Node to apply the ability to drag to
     */
    public void dragNode(Node node) {
            // Custom object to hold x and y positions
            final Delta dragDelta = new Delta();

            node.setOnMousePressed(mouseEvent -> {
                dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
            });

            node.setOnMouseReleased(mouseEvent -> node.setCursor(Cursor.HAND));

            node.setOnMouseDragged(mouseEvent -> {
                node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                if(mouseEvent.getSceneX()>divider.getScene().getX() && !room.getChildren().contains(node)){
                    populate();
                    room.getChildren().add(node);
                }
            });

            node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));
        }

        class Delta { double x, y; }
}
