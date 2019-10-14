package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

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
        MoebelList*/


		Parent root = new FXMLLoader(this.getClass().getClassLoader().getResource("app.fxml")).load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.maximizedProperty().addListener(ChangeListener->{
			if(primaryStage.isMaximized()) primaryStage.setFullScreen(true);
		});
		primaryStage.fullScreenProperty().addListener(ChangeListener->{
			if(!primaryStage.isFullScreen()) primaryStage.setMaximized(false);
		});
		primaryStage.setTitle("Raumplanner");
		primaryStage.show();
	}

}
