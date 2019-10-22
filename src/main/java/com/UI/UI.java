package com.UI;

import com.Moebel.Moebel;
import com.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

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
	@FXML
	private Region selection;

	private final List<Moebel> list = Repository.getAll();
	private ObservableList<moebelListNodeController> displayList = FXCollections.observableList(new LinkedList<>());

	private ContextMenu imgContext = new ContextMenu();

	@FXML
	public void initialize() {
		populate();
		{
			MenuItem delete = new MenuItem("Delete"); //creating delete menu item
			imgContext.getItems().add(delete); //adding delete Menu item
			//Defining delete handler
			delete.setOnAction(EventHandler -> {
				EventHandler.consume();
				System.out.println("Delete");
				//Getting coordinates
				final Point2D pos = room.screenToLocal(delete.getParentPopup().getAnchorX(), delete.getParentPopup().getAnchorY());
				System.out.printf("%10.1f,%10.1f%n", pos.getX(), pos.getY());

				//Deleting all nodes, intersecting that point
				// +1 and -1 for compensating the conversion from double to int
				room.getChildren().removeIf(Predicate -> {
					try {
						System.out.println("Deleting");
						return pos.getX() >= Predicate.getLayoutX() - 1 &&
								pos.getX() <= Predicate.getLayoutX() + ((ImageView) Predicate).getX() + 1 &&
								pos.getY() >= Predicate.getLayoutY() - 1 &&
								pos.getY() <= Predicate.getLayoutY() + ((ImageView) Predicate).getY() + 1;
					} catch (Throwable t) {
						//probably not a moebel, might be a selection.
						//TODO: add a more concrete handler
						t.printStackTrace();
						return false;
					}
				});
				room.getChildren().forEach(Node -> System.out.printf("%10.1f,%10.1f%n", Node.getLayoutX(), Node.getLayoutY()));
			});
		}
	}

	//Populate the ListView and other stuff on startup
	private void populate() {
		list.forEach(Moebel -> {
			String title = Moebel.getName();
			String desc = "" + Moebel.getBreite() + "x" + Moebel.getLaenge();
			String type = Moebel.getClass().getSimpleName();
			displayList.add(new com.UI.moebelListNodeController(title, desc, type, Moebel.getDisplay(), Moebel.getBreite()));
		});
		displayList.forEach(this::moebelSpawn);
		moebelList.setItems(displayList);
	}

	/**
	 * Handles all dragging EventHandlers for a list object
	 *
	 * @param node
	 * 		Node to apply the ability to drag to
	 */
	private void moebelSpawn(moebelListNodeController node) {
		node.setOnMousePressed(mouseEvent -> {
			boolean created = false;
			if (mouseEvent.getScreenX() > divider.getScene().getWindow().getX() && !created) {
				//Creating Moebel
				System.out.println("Creating moebel");
				ImageView img = new ImageView(node.display);

				//Set width, and make it visible
				img.setFitWidth(node.width * 50);
				img.setPreserveRatio(true);
				img.setSmooth(true);
				room.getChildren().add(img);
				img.setVisible(true);
				img.relocate(mouseEvent.getX(), mouseEvent.getY());

				//applying all event handlers
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

		//Calculate the amount dragged
		node.setOnMousePressed(mouseEvent -> {
			imgContext.hide();
			dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
			dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
		});

		node.setOnMouseEntered(mouseEvent -> node.setCursor(Cursor.HAND));
		node.setOnMouseReleased(mouseEvent -> node.setCursor(Cursor.HAND));


		node.setOnMouseDragged(mouseEvent -> {
			//Move node, like it was dragged
			node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
			node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);


			double imgHeight = node.getFitHeight() <= 0 ? node.getFitWidth() : node.getFitHeight();
			double imgWidth = node.getFitWidth() <= 0 ? node.getFitHeight() : node.getFitWidth();

			double y;
			double x;

			//Collision detection with programm bounds
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

			//move node appropriately after accounting for collisions
			node.relocate(x, y);
			mouseEvent.consume();
		});

		//when right clicked, create context menu
		node.setOnContextMenuRequested(ContextMenuEvent -> {
			ContextMenuEvent.consume();
			System.out.println("Create Menu");
			System.out.printf("%10.1f,%10.1f%n", node.getLayoutX(), node.getLayoutY());

			Point2D pos = room.localToScreen(node.getLayoutX(), node.getLayoutY());

			imgContext.show(node, pos.getX(), pos.getY());
		});

		node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));
	}

	private class Delta {
		double x, y;
	}
}
