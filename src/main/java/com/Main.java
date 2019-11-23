package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static final FXMLLoader fxml = new FXMLLoader(Main.class.getClassLoader().getResource("app.fxml"));


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        //TODO: Fix the Group to allow right and left clicks
        //TODO: Add BigBilly

        //TODO: Migrate moebelView to Moebel
        //TODO: Adapt Moebel to allow for multipile Colors
        //TODO: Add Preferences popup

        //TODO: Make click priorities in groups right
        //TODO: Make Furniture move with the group

        //TODO: MINOR Walls


        /*
        DataReader dr = new DataReader("test.txt");
        ArrayList<String> st = dr.getContent();
        st.forEach(System.out::println);
        System.out.println("-------------------------------------------------------");
        st.add("" + (Math.random() * 1000));
        dr.setContent(st);
        dr.getContent().forEach(System.out::println);
        */


       /* MoebelList.setCellFactory(param -> new ListCell<? extends com.Moebel>() {
            @Override
            protected void updateItem( ?<com.Moebel> extends com.Moebel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getWord() == null) {
                    setText(null);
                } else {
                    setText(item.getWord());
                }
            }
        });
        MoebelList
        */

       Parent root=fxml.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.maximizedProperty().addListener(ChangeListener -> {
            if (primaryStage.isMaximized()) primaryStage.setFullScreen(true);
        });
        primaryStage.fullScreenProperty().addListener(ChangeListener -> {
            if (!primaryStage.isFullScreen()) primaryStage.setMaximized(false);
        });
        primaryStage.setTitle("Raumplanner");
        primaryStage.show();
    }

}
