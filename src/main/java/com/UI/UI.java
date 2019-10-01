package com.UI;

import com.Moebel.Moebel;
import com.Repository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
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
    private List<moebelListNodeController> displayList = new LinkedList<>();

    @FXML
    public void initialize() throws IOException {
        populate();
        divider.setOnMouseDragExited(MouseEvent-> {
            System.out.println(divider.getLayoutX());
        });
    }



    //Populate the ListView and other stuff on startup
    private void populate(){
        displayList = new LinkedList<>();
        list.forEach(Moebel-> {
            String title = Moebel.getName().concat(":").concat(Moebel.getClass().getSimpleName());
            String desc = ""+Moebel.getBreite()+"x"+Moebel.getLaenge();
            displayList.add(new com.UI.moebelListNodeController(title,desc,Moebel.getDisplay(),Moebel.getBreite()));
        });
        displayList.forEach(this::dragNode);
        moebelList.setItems(FXCollections.observableList(displayList));
    }

    /** Handles all dragging EventHandlers for a list object
     *
     * @param node Node to apply the ability to drag to
     */
    private void dragNode(moebelListNodeController node) {
        node.setOnMouseReleased(mouseEvent -> node.setCursor(Cursor.HAND));
        node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));

        node.setOnMouseDragged(mouseEvent -> {
            if(mouseEvent.getSceneX()>divider.getScene().getX()){
                populate();
                ImageView img = new ImageView(node.display);

                img.setFitWidth(node.breite*50);
                img.setPreserveRatio(true);
                img.setSmooth(true);

                img.setX(mouseEvent.getX());
                img.setY(mouseEvent.getY());

                room.getChildren().add(img);
                dragNode(img);
            }
        });
    }

    /** Handles all dragging EventHandlers for any node object
     *
     * @param node Node to apply the ability to drag to
     */
    private void dragNode(Node node) {
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
        });

        node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));
    }

    class Delta { double x, y; }



}
