package com.UI;

import com.Moebel.Moebel;
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
import javafx.scene.layout.Region;

import java.util.Arrays;
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
	private ObservableList<AnchorPane> groups = FXCollections.observableList(new LinkedList<>());


	private final List<Moebel> list = Repository.getAll();
	private ObservableList<moebelListNodeController> displayList = FXCollections.observableList(new LinkedList<>());

	private ContextMenu imgContext = new ContextMenu(); //for Furniture
	private ContextMenu selContext = new ContextMenu(); //for selections

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
				room.getChildren().removeIf(Predicate -> {
					ImageView ImageView;
					try {
						ImageView = (ImageView) Predicate;
					} catch (ClassCastException t) {
						ImageView = new ImageView();
						ImageView.setX(0.0);
						ImageView.setY(0.0);
						//probably not a moebel, might be a selection.
					}
					System.out.println("Deleting");
					return pos.getX() >= Predicate.getLayoutX() - 1 &&
							pos.getX() <= Predicate.getLayoutX() + ImageView.getX() + 1 &&
							pos.getY() >= Predicate.getLayoutY() - 1 &&
							pos.getY() <= Predicate.getLayoutY() + ImageView.getY() + 1;
				});
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
					AnchorPane region = new AnchorPane();
					room.getChildren().add(region);

					//prevent recursion
					region.getChildren().remove(region);
					//Adds all selected Nodes to be in the Group
					region.getChildren().addAll(
							//Get all nodes in the selection
							room.getChildren().filtered(
									(Node) -> Node.getLayoutX() > selection.getLayoutX()
											&& Node.getLayoutX() < selection.getLayoutX() + selection.getMinWidth()
											&& Node.getLayoutY() > selection.getLayoutY()
											&& Node.getLayoutY() < selection.getLayoutY() + selection.getMinHeight()
											&& !Node.getClass().isInstance(Region.class)
							)
					);

					//Check if we selected something
					if (region.getChildren().size() <= 0) {
						//if we selected nothing display an error and abort
						Alert noSelected = new Alert(Alert.AlertType.ERROR, "Keine Möbel ausgewählt. \n " +
								"Ein Möebel in kann nicht in mehreren Gruppen sein!", ButtonType.CANCEL);
						noSelected.show();
						//selection is done
						selection.setVisible(false);
						selContext.hide();
						return;
					}

					//get the min locations
					final Double[] pos = {null, null};
					region.getChildrenUnmodifiable().forEach(Node -> {
						pos[0] = Math.min(pos[0] != null ? pos[0] : Node.getLayoutX(), Node.getLayoutX());
						pos[1] = Math.min(pos[1] != null ? pos[1] : Node.getLayoutY(), Node.getLayoutY());
					});

					//set all edge-points
					region.relocate(pos[0], pos[1]);
					dragNode(region, region.getWidth(), region.getHeight());

					//make nodes location relative to the new Pane/Scene for them to stay in the same place
					//Only now do the transform, because we needed the coordinates for the pos array
					region.getChildren().forEach(Node -> {
						Point2D poi = region.parentToLocal(Node.getLayoutX(),Node.getLayoutY());
						Node.relocate(poi.getX(), poi.getY());
					});

					region.setStyle("-fx-background-color: rgba(0,255,255,0.25); " +
							"-fx-border-style: solid; " +
							"-fx-border-radius: 2px ;" +
							"-fx-border-color: black;" +
							"-fx-border-width: 2px;");

					//groupDrag(group);

					groups.add(region);

					region.setOnContextMenuRequested(ContextMenuEvent -> {
						System.out.println("Region Context menu event fired");
						ContextMenuEvent.consume();
					});


					//move all children to the right parent
					room.getChildren().removeAll(region.getChildren());

					//selection is done
					selection.setVisible(false);
					selContext.hide();
				});


				//Configure the delete button
				MenuItem delete = new MenuItem("Delete");
				delete.setOnAction(EventHandler -> {
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
				selContext.getItems().addAll(group1, delete);

				//Node to serve as an anchor point
				Node anchorNode = new Label();
				room.getChildren().add(anchorNode);
				anchorNode.setVisible(false);

				selContext.setOnHiding(WindowEvent -> {

				});

				selection.setOnContextMenuRequested(ContextMenuEvent -> {
					System.out.println("selection context requested");
					anchorNode.relocate(selection.getLayoutX(), selection.getLayoutY());

					Point2D anchor = anchorNode.localToScreen(selection.getLayoutX(), selection.getLayoutY());
					selContext.show(anchorNode, anchor.getX(), anchor.getY());
					ContextMenuEvent.consume();
				});

			}

			//TODO: Add a context menu to that group
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

	/**
	 * Handles all dragging EventHandlers for any node object
	 *
	 * @param node
	 * 		Node to apply the ability to drag to
	 */
	private void dragNode(Node node, double height, double width) {
		// Custom object to hold x and y positions
		final Delta dragDelta = new Delta();

		//Calculate the amount dragged
		node.setOnMousePressed(mouseEvent -> {
			imgContext.hide();
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

			double y;
			double x;

			//Collision detection with programm bounds
			if (node.getLayoutY() >= room.getHeight() - height) {
				node.setRotate(180);
				y = room.getHeight() - height;

			} else if (node.getLayoutY() <= 0) {
				node.setRotate(0);
				y = 0;
			} else {
				y = node.getLayoutY();
			}

			if (node.getLayoutX() >= room.getWidth() - width) {
				node.setRotate(90);
				x = room.getWidth() - width;
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
	}


	private class Delta {
		double x, y;
	}
}
