package com.UI;

import com.Moebel.Moebel;
import com.Repository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.LinkedList;
import java.util.List;

/**
 * Handles all of the User-Interface interactions and Processes them
 *
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
	public void initialize() {
		populate();
	}


	//Populate the ListView and other stuff on startup
	private void populate() {
		displayList = new LinkedList<>();
		list.forEach(Moebel -> {
			String title = Moebel.getName().concat(":").concat(Moebel.getClass().getSimpleName());
			String desc = "" + Moebel.getBreite() + "x" + Moebel.getLaenge();
			displayList.add(new com.UI.moebelListNodeController(title, desc, Moebel.getDisplay(), Moebel.getBreite()));
		});
		displayList.forEach(this::dragNode);
		moebelList.setItems(FXCollections.observableList(displayList));
	}

	/**
	 * Handles all dragging EventHandlers for a list object
	 *
	 * @param node Node to apply the ability to drag to
	 */
	private void dragNode(moebelListNodeController node) {
		node.setOnMouseReleased(mouseEvent -> node.setCursor(Cursor.HAND));
		node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));

		node.setOnMouseDragged(mouseEvent -> {
			if (mouseEvent.getSceneX() > divider.getScene().getX()) {
				populate();
				ImageView img = new ImageView(node.display);

				img.setFitWidth(node.breite * 50);
				img.setPreserveRatio(true);
				img.setSmooth(true);

				room.getChildren().add(img);
				img.relocate(mouseEvent.getX(),mouseEvent.getY());
				dragNode(img);
			}
		});
	}

	/**
	 * Handles all dragging EventHandlers for any node object
	 *
	 * @param node Node to apply the ability to drag to
	 */
	private void dragNode(ImageView node) {
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


			double imgHeight=node.getFitHeight()<=0?node.getFitWidth():node.getFitHeight();
			double imgWidth=node.getFitWidth()<=0?node.getFitHeight():node.getFitWidth();


			if (node.getLayoutY() >= room.getHeight()-imgHeight) {
				node.setRotate(180);
				node.relocate(Math.min(mouseEvent.getSceneX() + dragDelta.x,room.getWidth()-imgWidth),room.getHeight()-imgHeight);

			}
			if (node.getLayoutX() >= room.getWidth()-imgWidth) {
				node.setRotate(90);
				node.relocate(room.getWidth()-imgWidth, Math.min(mouseEvent.getSceneY() + dragDelta.y, room.getHeight()-imgHeight));
			}
			if (node.getLayoutY() <= 0) {
				node.setRotate(0);
				node.relocate(Math.max(mouseEvent.getSceneX() + dragDelta.x ,0),0);
			}
			if (node.getLayoutX() <= 0) {
				node.setRotate(270);
				node.relocate(0, Math.max(mouseEvent.getSceneY() + dragDelta.y,0));
			}
		});
		node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));
	}

	private class Delta {
		double x, y;
	}


}
