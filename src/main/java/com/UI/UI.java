package com.UI;

import com.Moebel.Moebel;
import com.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
	private ObservableList<moebelListNodeController> displayList = FXCollections.observableList(new LinkedList<>());

	private ContextMenu imgContext = new ContextMenu();

	@FXML
	public void initialize() {
		populate();
		{
			MenuItem delete = new MenuItem("Delete");
			imgContext.getItems().add(delete);
			delete.setOnAction(EventHandler -> {
				EventHandler.consume();
				System.out.println("Delete");
				final Point2D pos = room.screenToLocal(delete.getParentPopup().getAnchorX(), delete.getParentPopup().getAnchorY()).add(-25.0, 0.0);
				System.out.printf("%10.1f,%10.1f%n", pos.getX(), pos.getY());

				room.getChildren().removeIf(Predicate ->
					pos.getX() >= Predicate.getLayoutX()-1 &&
					pos.getX() <= Predicate.getLayoutX()+((ImageView) Predicate).getX()+1&&
					pos.getY() >= Predicate.getLayoutY()-1 &&
					pos.getY() <= Predicate.getLayoutY()+((ImageView) Predicate).getY()+1);

				room.getChildren().forEach(Node -> System.out.printf("%10.1f,%10.1f%n", Node.getLayoutX(), Node.getLayoutY()));
			});
		}
	}

	//Populate the ListView and other stuff on startup
	private void populate() {
		list.forEach(Moebel -> {
			String title = Moebel.getName().concat(":").concat(Moebel.getClass().getSimpleName());
			String desc = "" + Moebel.getBreite() + "x" + Moebel.getLaenge();
			displayList.add(new com.UI.moebelListNodeController(title, desc, Moebel.getDisplay(), Moebel.getBreite()));
		});
		displayList.forEach(this::dragNode);
		moebelList.setItems(displayList);
	}

	/**
	 * Handles all dragging EventHandlers for a list object
	 *
	 * @param node
	 * 		Node to apply the ability to drag to
	 */
	private void dragNode(moebelListNodeController node) {
		node.setOnMousePressed(mouseEvent -> {
			boolean created = false;
			if (mouseEvent.getScreenX() > divider.getScene().getWindow().getX() && !created) {
				//populate();
				ImageView img = new ImageView(node.display);

				img.setFitWidth(node.width * 50);
				img.setPreserveRatio(true);
				img.setSmooth(true);

				room.getChildren().add(img);
				img.relocate(mouseEvent.getX(), mouseEvent.getY());
				dragNode(img);
				created = true;
			}
			mouseEvent.consume();
		});
	}

	/**
	 * Handles all dragging EventHandlers for any node object
	 *
	 * @param node
	 * 		Node to apply the ability to drag to
	 */
	private void dragNode(ImageView node) {
		// Custom object to hold x and y positions
		final Delta dragDelta = new Delta();

		node.setOnMousePressed(mouseEvent -> {
			imgContext.hide();
			dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
			dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
		});

		node.setOnMouseReleased(mouseEvent -> node.setCursor(Cursor.HAND));

		node.setOnMouseDragged(mouseEvent -> {
			node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
			node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);


			double imgHeight = node.getFitHeight() <= 0 ? node.getFitWidth() : node.getFitHeight();
			double imgWidth = node.getFitWidth() <= 0 ? node.getFitHeight() : node.getFitWidth();

			double y;
			double x;


			if (node.getLayoutY() >= room.getHeight() - imgHeight) {
				node.setRotate(180);
				y = room.getHeight() - imgHeight;

			} else if (node.getLayoutY() <= 0) {
				node.setRotate(0);
				y = 0;
			} else {
				y = node.getLayoutY();
			}

			if (node.getLayoutX() >= room.getWidth() - imgWidth) {
				node.setRotate(90);
				x = room.getWidth() - imgWidth;
			} else if (node.getLayoutX() <= 0) {
				node.setRotate(270);
				x = 0;
			} else {
				x = node.getLayoutX();
			}
			node.relocate(x, y);
		});

		node.setOnContextMenuRequested(ContextMenuEvent -> {
			ContextMenuEvent.consume();
			System.out.println("Create Menu");
			System.out.printf("%10.1f,%10.1f%n", node.getLayoutX(), node.getLayoutY());

			imgContext.show(node, Side.RIGHT, 0, 0);
		});

		node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));
	}

	private class Delta {
		double x, y;
	}


}
