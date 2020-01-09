package com.UI;

import com.Moebel.Moebel;
import com.Moebel.SchrankWand;
import com.Repository;
import com.UI.Menu.Selection;
import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;


/**
 Handles all of the User-Interface interactions and Processes them.
 <p>
 This is the class piecing everything together, as it is the root of the Scene, that is being displayed.<br>
 Therefore, basically all things end up linking to here.

 @author Timon Kayser */
public class UI {
	/** This is a divider (and first element), to make a view on the side, of all the elements on the side */
	@FXML
	private SplitPane divider = new SplitPane();
	/**
	 This list is displaying everything, that can be used, to create new furniture in the {@link #room}<br>
	 All elements in this list are of the type {@link moebelListNodeController}.

	 The List representing everything displayed in here is {@link #displayList} though.

	 @see moebelListNodeController
	 */
	@FXML
	private ListView<moebelListNodeController> moebelList;
	/** This is the equivalent of the room, you are trying to place your Furniture into */
	private RootPane room = new RootPane();

	public RootPane getRoom() {
		return room;
	}
//	<T extends Event> void applyHandler(EventType<T> type, EventHandler<? super T> handler){room.addEventHandler(type,handler);}
//	<T extends Event> void applyFilter(EventType<T> type, EventHandler<? super T> filter){room.addEventFilter(type,filter);}

	/** This element is used to implement a drag-type of select to the {@link #room} */
	Selection selection;
	/** Keeps track of all groups currently in use */
	static ObservableList<Group> groups = FXCollections.observableList(new LinkedList<>());
	/** The list, that is being displayed by {@link #moebelList} */
	private ObservableList<moebelListNodeController> displayList = FXCollections.observableList(new LinkedList<>());

	/**
	 First method to be run after this Object has been created from javafx
	 */
	@FXML
	public void initialize() {
		divider.getItems().add(1, room);
		selection = new Selection(room);
		moebelList.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
//		room.getChildren().add(selection);
		populate();
	}

	/** This method initializes all Furniture elements, that are visible in the {@link #moebelList} */
	private void populate() {
		Repository.Presets.forEach((moebel) -> displayList.add(moebel.getMoebelListNodeController()));
		displayList.forEach(this::moebelSpawn);
		moebelList.setItems(displayList);
	}

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
				else img = SchrankWand.SchrankWandBuilder(node.getName());

				//Set width, and make it visible
				//No need to do this anymore, because it has already been set by Moebel
//				img.setFitWidth(img.getWidth() * 50);
//				img.setFitHeight(img.getHeight() * 50);
//				img.setVisible(true);

				if (img == null) {//this would mean, that something in the Schrakwandbuilder failed, or the User aborted
					return;
				}


				room.getChildren().add(img);
				img.relocate(mouseEvent.getSceneX(), mouseEvent.getSceneY());
				Point2D pos = room.col(img);
				img.relocate(pos.getX(),pos.getY());
				img.add();

				//applying all event handlers
				room.dragNode(img);
				created = true;
				System.out.println("Done");
				if (!room.getChildrenUnmodifiable().contains(img))
					System.out.println("But doesn't contain the newly generated Image");
				img.draw();
			}
			mouseEvent.consume();
		});
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
	public static boolean delete(Pane room, Point2D pos, Optional<Boolean> loopOp) {
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

	/**
	 Deletes all nodes, that are in nodes from target.
	 If there is a Node with children, they will also be deleted from target

	 @param target
	 List of nodes to delete from
	 @param nodes
	 nodes to delete in target

	 @return returns true, if at least one element has been deleted
	 */
	public static boolean delete(@NotNull ObservableList<Node> target, ObservableList<Node> nodes) {
		boolean rem = target.removeAll(nodes);
		for (Node pane : target) {
			if (pane instanceof Pane) {
				rem = delete(((Pane) pane).getChildren(), nodes) || rem;
			}
		}
		for (Node pane : nodes) {
			if (pane instanceof Pane) {
				rem = delete(target, ((Pane) pane).getChildren()) || rem;
			}
		}
		return rem;
	}

	/**
	 Deletes all nodes, that are in nodes from target.
	 If there is a Node with children, they will also be deleted from target

	 @param target
	 List of nodes to delete from
	 @param node
	 node to delete in target

	 @return returns true, if at least one element has been deleted
	 */
	public static boolean delete(@NotNull ObservableList<Node> target, Node node) {
		return delete(target, FXCollections.singletonObservableList(node));
	}


	/**
	 Set min,preferred and max height to the specified parameters.
	 <p>
	 Essentially this is a method to add the setters for width and height back in

	 @param <T>
	 Type of the Node
	 @param node
	 Node to set the width and height to
	 @param width
	 width to be set
	 @param height
	 height to be set

	 @return Returns the node, that was passed in
	 */
	public static <T extends Region> T setMinMax(T node, double width, double height) {
		if (node.getMinWidth() != width) node.setMinWidth(width);
		if (node.getMaxWidth() != width) node.setMaxWidth(width);
		if (node.getPrefWidth() != width) node.setPrefWidth(width);
		if (node.getMinHeight() != height) node.setMinHeight(height);
		if (node.getMaxHeight() != height) node.setMaxHeight(height);
		if (node.getPrefHeight() != height) node.setPrefHeight(height);
		return node;
	}


}
