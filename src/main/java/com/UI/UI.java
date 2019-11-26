package com.UI;

import com.Moebel.Moebel;
import com.Operators;
import com.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


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
	AnchorPane room;
	@FXML
	Region selection;
	static ObservableList<AnchorPane> groups = FXCollections.observableList(new LinkedList<>());


	private static final List<Moebel> list = Repository.getAll();
	private static ObservableList<moebelListNodeController> displayList = FXCollections.observableList(new LinkedList<>());

	static ContextMenu imgContext = new ContextMenu(); //for Furniture
	static ContextMenu selContext = new ContextMenu(); //for selections

	@FXML
	public void initialize() {
		populate();
		//Begin making context menu for furniture
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
				delete(room, pos, Optional.of(true));
				room.getChildren().forEach(Node -> System.out.printf("%10.1f,%10.1f%n", Node.getLayoutX(), Node.getLayoutY()));

			});
		}
		//Begin handling selections
		{
			//Make blue selection follow mouse
			{
				selection.setVisible(false);
				//array for storing initial, final, delta
				final Point2D[] pos = {null, null};

				//Blue selection box
				room.setOnMousePressed(MouseEvent -> {
					//if still null, we are just entering the drag, so setting the initial coordinates
					if (pos[0] == null && MouseEvent.getButton().equals(MouseButton.PRIMARY)) {
						pos[0] = room.sceneToLocal(MouseEvent.getSceneX(), MouseEvent.getSceneY());
						MouseEvent.consume();
					}
				});
				room.setOnMouseDragged(MouseEvent -> {

					//Prevent creating a region, if right clicking
					if (MouseEvent.getButton().equals(MouseButton.SECONDARY)) return;
					if (!room.getChildren().filtered(Predicate -> {
						try {
							if (Predicate.getLayoutX() < MouseEvent.getSceneX() &&
									((ImageView) Predicate).getX() + Predicate.getLayoutX() > MouseEvent.getSceneX()) {
								return true;
							}
						} catch (ClassCastException E) {
							return false;
						}
						return false;
					}).isEmpty()) {
						return;
					}
					//Move selection to the initial click point
					System.out.println("Creating Region");
					//Get the current mouse position, and calculate the delta
					pos[1] = room.sceneToLocal(MouseEvent.getSceneX(), MouseEvent.getSceneY());

					//Calculate starting x,y , aka min x and y
					final double xS = Math.min(pos[0].getX(), pos[1].getX());
					final double yS = Math.min(pos[0].getY(), pos[1].getY());
					selection.relocate(xS, yS);
					//Calculate delta x,y aka max x and y
					final double xE = Math.max(pos[0].getX(), pos[1].getX());
					final double yE = Math.max(pos[0].getY(), pos[1].getY());
					final double xD = xE - xS;
					final double yD = yE - yS;
					//Print debugger info
					System.out.printf("%10.1f , %10.1f %n", pos[0].getX(), pos[0].getY());
					System.out.printf("%10.1f , %10.1f %n", pos[1].getX(), pos[1].getY());
					//setting the width and height just right, to point to the mouse
					setMinMax(selection, xD, yD);

					selection.setVisible(true);
					System.out.println("Done");
				});

				//reset the drag to the initial state
				room.setOnMouseReleased(MouseEvent -> Arrays.fill(pos, null));
				room.setOnContextMenuRequested(ContextMenuEvent -> selection.setVisible(false));
			}

			//Configure context menu for the selection box
			{
				MenuItem group1 = new MenuItem("Group");
				group1.setOnAction(EventHandler -> {

					//setup everything for the drag
					Group group = new Group();
					room.getChildren().add(group);
					//prevent recursion
					group.getChildren().remove(group);
					dragNode(group, ()->group.getHeight(12345.6789), ()->group.getWidth(12345.6789));
				});


				//Configure the delete button
				MenuItem massDelete = new MenuItem("Delete");
				massDelete.setOnAction(EventHandler -> {
					//Remove all selected nodes
					room.getChildren().removeIf(
							(Node) -> Node.getLayoutX() > selection.getLayoutX()
									&& Node.getLayoutX() < selection.getLayoutX() + selection.getMinWidth()
									&& Node.getLayoutY() > selection.getLayoutY()
									&& Node.getLayoutY() < selection.getLayoutY() + selection.getMinHeight()
					);
					//selection is done
					selection.setVisible(false);
					selContext.hide();
				});

				//Add both actions
				selContext.getItems().addAll(group1, massDelete);


				selection.setOnContextMenuRequested(ContextMenuEvent -> {
					System.out.println("selection context requested");
					Point2D anchor = room.localToScreen(selection.getLayoutX(), selection.getLayoutY());
					selContext.show(selection, anchor.getX(), anchor.getY());
					ContextMenuEvent.consume();
				});

			}

			//TODO: Add a context menu to that group
		}
	}

	/**
	 * @param room
	 * 		Node with the Children, where you wanna delete the element from
	 * @param pos
	 * 		Position, where you would like someting deleted
	 *
	 * @return to be ignored, output is not reliable
	 */
	private boolean delete(Pane room, Point2D pos, Optional<Boolean> loopOp) {
		boolean loop = loopOp.orElse(true);

		boolean deleted = false;
		for (Node node : room.getChildrenUnmodifiable()) {
			ImageView ImageView;
			try {
				ImageView = (ImageView) node;
				if (pos.getX() >= node.getLayoutX() - 1 &&
						pos.getX() <= node.getLayoutX() + ImageView.getX() + 1 &&
						pos.getY() >= node.getLayoutY() - 1 &&
						pos.getY() <= node.getLayoutY() + ImageView.getY() + 1) {
					if (node instanceof Pane) {
						Alert sure = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure, that you want to delete this Group and EVERYTHING inside it?", ButtonType.NO, ButtonType.YES);
						sure.show();
						sure.setOnCloseRequest(DialogEvent -> {
							if (sure.getResult().equals(ButtonType.YES)) {
								room.getChildren().remove(node);
							}
						});
						return true;

					}
					room.getChildren().remove(node);
					System.out.println("Successfully deleted the Element");
				}
			} catch (ClassCastException t) {
				//probably not a moebel, might be a selection.
				if (node instanceof Pane && loop) {
					return delete(((Pane) node), pos, loopOp);
				} else if (node instanceof Region) {
					//just our selection... not throwing an error, because this is somewhat expected
				} else throw t;
			}
		}
		return false;
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
		moebelList
				.setItems(
						displayList);
	}

	/**
	 * Makes moebels be able to spawn on the room
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
				img.setFitHeight(node.width * 50);
				img.setPreserveRatio(true);
				img.setSmooth(true);
				room.getChildren().add(img);
				img.setVisible(true);
				img.relocate(mouseEvent.getX(), mouseEvent.getY());

				//applying all event handlers
				mouseHandlers(img);
				created = true;
			}
			mouseEvent.consume();
		});
	}


	private void mouseHandlers(ImageView node) {
		double imgHeight = node.getFitHeight() <= 0 ? node.getFitWidth() : node.getFitHeight();
		double imgWidth = node.getFitWidth() <= 0 ? node.getFitHeight() : node.getFitWidth();

		dragNode(node, imgHeight, imgWidth);
		//when right clicked, create context menu
		node.setOnContextMenuRequested(ContextMenuEvent -> {
			ContextMenuEvent.consume();
			System.out.println("Create Menu");
			System.out.printf("%10.1f,%10.1f%n", node.getLayoutX(), node.getLayoutY());

			Point2D pos = room.localToScreen(node.getLayoutX(), node.getLayoutY());

			imgContext.show(node, pos.getX(), pos.getY());
		});
	}


	/**
	 * Set min,preferred and max height to the specified {@code width} and {@code height}
	 *
	 * @param width
	 * 		width to be set
	 * @param height
	 * 		height to be set
	 */
	private Region setMinMax(Region node, double width, double height) {
		node.setMinWidth(width);
		node.setMinHeight(height);
		node.setMaxWidth(width);
		node.setMaxHeight(height);
		node.setPrefHeight(height);
		node.setPrefWidth(width);
		return node;
	}

	private void dragNode(Node node, double height, double width) {
		dragNode(node, () -> height, () -> width);
	}

	/**
	 * Handles all dragging EventHandlers for any node object
	 *
	 * @param node
	 * 		Node to apply the ability to drag to
	 */
	private void dragNode(Node node, Supplier<Double> height, Supplier<Double> width) {
		// Custom object to hold x and y positions
		final Delta dragDelta = new Delta();

		//Calculate the amount dragged
		node.setOnMousePressed(mouseEvent -> {
			imgContext.hide();
			selContext.hide();
			dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
			dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
			mouseEvent.consume();
		});

		node.setOnMouseEntered(mouseEvent -> node.setCursor(Cursor.HAND));
		node.setOnMouseReleased(mouseEvent -> node.setCursor(Cursor.HAND));


		node.setOnMouseDragged(mouseEvent -> {

			//Move node, like it was dragged
			node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
			node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);

			if (node.getLayoutY() < node.getParent().getLayoutY() || node.getLayoutX() < node.getParent().getLayoutX() && !node.getParent().equals(room)) {
				node.getParent().relocate(node.getLayoutX(), node.getLayoutY());
			} else if (!node.getParent().equals(room)) {

			}

			double localHeight = Operators.ifNullRet(height.get(),0.0);
			double localWidth = Operators.ifNullRet(width.get(),0.0);

			System.out.println("height = " + localHeight);
			System.out.println("width = " + localWidth);


			//move node appropriately after accounting for collisions
			node.setLayoutX(colX(node, localWidth, localHeight));
			node.setLayoutY(colY(node, localHeight, localWidth));
			mouseEvent.consume();
		});
	}

	/**
	 * Checks collision between program bounds in the y direction
	 * Also sets the Rotation variable
	 *
	 * @param node
	 * 		Node to be checked
	 * @param height
	 * 		height of the node
	 *
	 * @return Returns the appropriate y value
	 */
	private double colY(Node node, double height, double width) {

		if (node.getBoundsInParent().getMaxY() >= room.getHeight()) {
			System.out.println("bottom");
			node.setRotate(180);
			return room.getHeight() - 2 * height;
		} else if (node.getBoundsInParent().getMinY() < 0) {
			node.setRotate(0);
			return 0;
		}

		return node.getLayoutY();
	}

	/**
	 * Checks collision between program bounds in the x direction
	 * Also sets the Rotation variable
	 *
	 * @param node
	 * 		Node to be checked
	 * @param width
	 * 		width of the node
	 *
	 * @return Returns the appropriate x value
	 */
	private double colX(Node node, double width, double height) {

		if (node.getBoundsInParent().getMaxX() >= room.getWidth()) { //bottom
			node.setRotate(90);
			return room.getWidth() - width;
		} else if (node.getBoundsInParent().getMinX() < 0.0) {
			node.setRotate(270);
			return -width;
		}
		return node.getLayoutX();
	}

	private class Delta {
		double x, y;
	}
}
