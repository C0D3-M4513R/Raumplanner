package com.UI;

import com.Moebel.Moebel;
import com.Moebel.SchrankWand;
import com.Moebel.SchrankWandBuilder;
import com.Operators;
import com.Repository;
import com.UI.Menu.Selection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;


/**
 Handles all of the User-Interface interactions and Processes them.
 <p>
 This is the class piecing everything together, as it is the root of the Scene, that is being displayed.<br>
 Therefore, basically all things end up linking to here.

 @author Timon Kayser */
public class UI {
	/** This is a divider (and first element), to make a view on the side, of all the elements on the side */
	@FXML
	private SplitPane divider;
	/**
	 This list is displaying everything, that can be used, to create new furniture in the {@link #room}<br>
	 All elements in this list are of the type {@link moebelListNodeController}

	 @see moebelListNodeController
	 */
	@FXML
	private ListView<moebelListNodeController> moebelList;
	/** This is the equivalent of the room, you are trying to place your Furniture into */
	@FXML
	private AnchorPane room;

	public AnchorPane getRoom() {
		return room;
	}
//	<T extends Event> void applyHandler(EventType<T> type, EventHandler<? super T> handler){room.addEventHandler(type,handler);}
//	<T extends Event> void applyFilter(EventType<T> type, EventHandler<? super T> filter){room.addEventFilter(type,filter);}

	/** This element is used to implement a drag-type of select to the {@link #room} */
	Selection selection;
	/** Keeps track of all groups currently in use */
	static ObservableList<Group> groups = FXCollections.observableList(new LinkedList<>());
	/** The list, that is being displayed by {@link #moebelList} */
	private static ObservableList<moebelListNodeController> displayList = FXCollections.observableList(new LinkedList<>());

	/**
	 First method to be run after this Object has been created from javafx ()
	 */
	@FXML
	public void initialize() {
		selection = new Selection(room);
		moebelList.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
//		room.getChildren().add(selection);
		populate();
	}

	/** This method initializes all Furniture elements, that are visible in the {@link #moebelList} */
	private void populate() {
		Repository.getAll().forEach((moebel) -> displayList.add(moebel.getMoebelListNodeController()));
		displayList.forEach(this::moebelSpawn);
		moebelList.setItems(displayList);
	}

	/**
	 This method looks for nodes at a specific location and deletes the first one it finds

	 @param room
	 Node with the Children, where you wanna delete the element from
	 @param pos
	 Position, where you would like someting deleted
	 @param loopOp
	 decides,

	 @return returns a value, that stated if a node has been FOUND not deleted, because I am asking for user verification and
	 Groups might therefore get found, but not deleted
	 */
	public boolean delete(Pane room, Point2D pos, Optional<Boolean> loopOp) {
		boolean loop = loopOp.orElse(true);

		boolean deleted = false;
		try {
			for (Node node : room.getChildrenUnmodifiable()) {
				Canvas Canvas;
				try {
					Canvas = (Canvas) node;
					if (pos.getX() >= node.getLayoutX() - 1 &&
							pos.getX() <= node.getLayoutX() + Canvas.getWidth() + 1 &&
							pos.getY() >= node.getLayoutY() - 1 &&
							pos.getY() <= node.getLayoutY() + Canvas.getHeight() + 1) {
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
						//This is actually a group or another element with children
						//Thus I am searching this as well
						return delete(((Pane) node), pos, loopOp);
					} else if (node instanceof Region) {
						//just our selection... not throwing an error, because this is somewhat expected
					} else throw t; //Okay, here something clearly has gone wrong
				}
			}
		} catch (NoSuchElementException ex) {
			System.err.println("NoSuchElementException means deletion was successful?");
		}
		return false;
	}

	//Populate the ListView and other stuff on startup

	/**
	 Makes moebels be able to spawn on the room

	 @param node
	 Node to apply the ability to drag to
	 */
	private void moebelSpawn(moebelListNodeController node) {
		node.setOnMousePressed(mouseEvent -> {
			boolean created = false;
			if (mouseEvent.getScreenX() > divider.getScene().getWindow().getX() && !created) {
				//Creating Moebel
				System.out.println("Creating moebel");
				Moebel img;
				if (!node.isType(SchrankWand.class)) img = Moebel.getPRESETS().get(node.getName()).get();
				else img = SchrankWandBuilder.SchrankWandBuilder(node.getName());

				//Set width, and make it visible
				//No need to do this anymore, because it has already been set by Moebel
//				img.setFitWidth(img.getWidth() * 50);
//				img.setFitHeight(img.getHeight() * 50);
//				img.setVisible(true);

				if (!(img instanceof Node) && img == null) {//this would mean, that something in the Schrakwandbuilder failed, or the User aborted
					return;
				}


				room.getChildren().add(img);
				img.relocate(mouseEvent.getSceneX(), mouseEvent.getSceneY());
				img.setLayoutX(colX(img, img.getWidth()));
				img.setLayoutY(colY(img, img.getHeight()));

				//applying all event handlers
				mouseHandlers(img);
				created = true;
				System.out.println("Done");
				if (!room.getChildrenUnmodifiable().contains(img))
					System.out.println("But doesn't contain the newly generated Image");
			}
			mouseEvent.consume();
		});
	}


	/**
	 This method applies dragging handlers and a right-click menu to the nodes being displayed

	 @param node
	 node to apply handlers to
	 */
	public void mouseHandlers(Canvas node) {
		double imgHeight = node.getHeight();
		double imgWidth = node.getWidth();

		dragNode(node, imgHeight, imgWidth);
		//when right clicked, create context menu
		/*node.setOnContextMenuRequested(ContextMenuEvent -> {
			ContextMenuEvent.consume();
			System.out.println("Create Menu");
			System.out.printf("%10.1f,%10.1f%n", node.getLayoutX(), node.getLayoutY());

			Point2D pos = room.localToScreen(node.getLayoutX(), node.getLayoutY());

			imgContext.show(node, pos.getX(), pos.getY());
		});*/
	}


	/**
	 Set min,preferred and max height to the specified parameters.
	 <p>
	 Essentially this is a method to add the setters for width and height back in

	 @param node
	 Node to set the width and height to
	 @param width
	 width to be set
	 @param height
	 height to be set

	 @return Returns the node, that was passed in
	 */
	public static Region setMinMax(Region node, double width, double height) {
		if (node.getMinWidth() != width) node.setMinWidth(width);
		if (node.getMaxWidth() != width) node.setMaxWidth(width);
		if (node.getPrefWidth() != width) node.setPrefWidth(width);
		if (node.getMinHeight() != height) node.setMinHeight(height);
		if (node.getMaxHeight() != height) node.setMaxHeight(height);
		if (node.getPrefHeight() != height) node.setPrefHeight(height);
		return node;
	}


	/**
	 This method is a wrapper to {@link #dragNode(Node, Supplier, Supplier)} method, but instead of passing in dynamic
	 values, they are static
	 Rest copied from {@link #dragNode(Node, Supplier, Supplier)}:
	 <p>
	 Handles all EventHandlers related to dragging any displayed object

	 @param node
	 Node to apply the ability to drag to
	 @param height
	 a static height value, that will be used to calculate collision
	 @param width
	 a static width value, that will be used to calculate collision
	 */
	void dragNode(Node node, double height, double width) {
		dragNode(node, () -> height, () -> width);
	}

	/**
	 Handles all EventHandlers related to dragging any displayed object

	 @param node
	 Node to apply the ability to drag to
	 @param height
	 a dynamic height value, that will be used to calculate collision
	 @param width
	 a dynamic width value, that will be used to calculate collision
	 */
	void dragNode(Node node, Supplier<Double> height, Supplier<Double> width) {
		//TODO: Something is fishy about this. Just wrong
		// Groups move double

		// Custom object to hold x and y positions
		final Delta dragDelta = new Delta();

		//Calculate the amount dragged
		node.setOnMousePressed(mouseEvent -> {
			selection.hideAll();
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

			double localHeight = Operators.ifNullRet(height.get(), 0.0);
			double localWidth = Operators.ifNullRet(width.get(), 0.0);

			//move node appropriately after accounting for collisions
			double x = (colX(node, localWidth));
			double y = (colY(node, localHeight));
			node.relocate(x, y);
			mouseEvent.consume();
		});
	}

	/**
	 Checks collision between program bounds in the y direction <br>
	 Also sets the Rotation variable

	 @param node
	 Node to be checked
	 @param height
	 height of the node

	 @return Returns the appropriate y value
	 */
	private double colY(Node node, double height) {
		if (node.getLayoutY() + height >= room.getHeight()) {
			System.out.println("bottom");
			node.setRotate(180);
			return room.getHeight() - height;
		} else if (node.getLayoutY() < 0) {
			node.setRotate(0);
			return 0.0;
		}

		return node.getLayoutY();
	}

	/**
	 Checks collision between program bounds in the x direction <br>
	 Also sets the Rotation variable

	 @param node
	 Node to be checked
	 @param width
	 width of the node

	 @return Returns the appropriate x value
	 */
	private double colX(Node node, double width) {

		if (node.getLayoutX() >= room.getWidth() && node.getRotate() == 270) {
			node.setRotate(90);
			return room.getWidth() - width;
		} else if (node.getLayoutX() + width >= room.getWidth()) {
			node.setRotate(90);
			return room.getWidth() - width;
		} else if (node.getLayoutX() < 0.0) {
			node.setRotate(270);
			return 0.0;
		}
		return node.getLayoutX();
	}

	/** Simple Class to store a xy position */
	private static class Delta {
		double x, y;
	}
}
