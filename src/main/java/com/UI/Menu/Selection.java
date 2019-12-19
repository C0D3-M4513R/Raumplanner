package com.UI.Menu;

import com.UI.Group;
import com.UI.UI;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.Arrays;

/**
 A class used to create a
 */
public class Selection extends Region implements Menu {
	/**
	 Object, where this new Drag-type selection should originate from
	 */
	private Pane root;
	/**
	 The Menu, that is going to be displayed later
	 */
	private ContextMenu menu = new ContextMenu();

	public Selection(Pane root){
		super();
		init();
		this.root=root;
		setVisible(false);
		toBack();
		setMouseTransparent(false);
		setStyle("-fx-background-color: cyan; -fx-border-style: solid; -fx-border-width: 5px; -fx-border-color: black;");
		root.getChildren().add(this);

		applyDraggable();
		initContextMenu();
	}

	private void applyDraggable(){
		//Make blue selection follow mouse

		//array for storing initial, final, delta
		final Point2D[] pos = {null, null};

		//Blue selection box
		root.addEventHandler(MouseEvent.MOUSE_PRESSED,MouseEvent -> {
			//if still null, we are just entering the drag, so setting the initial coordinates
			if (pos[0] == null && MouseEvent.getButton().equals(MouseButton.PRIMARY)) {
				pos[0] = root.sceneToLocal(MouseEvent.getSceneX(), MouseEvent.getSceneY());
				MouseEvent.consume();
			}
		});
		root.addEventHandler(MouseEvent.MOUSE_DRAGGED,MouseEvent -> {

			//Prevent creating a region, if right clicking
			if (MouseEvent.getButton().equals(MouseButton.SECONDARY)) return;
/*			if (!room.getChildren().filtered(Predicate -> {
				try {
					if (Predicate.getLayoutX() < MouseEvent.getSceneX() &&
							((Moebel) Predicate).getWidth() + Predicate.getLayoutX() > MouseEvent.getSceneX()) {
						return true;
					}
				} catch (ClassCastException E) {
					return false;
				}
				return false;
			}).isEmpty()) {
				System.out.println("");
				return;
			}*/
			//Move selection to the initial click point
			System.out.println("Creating Region");
			//Get the current mouse position, and calculate the delta
			pos[1] = root.sceneToLocal(MouseEvent.getSceneX(), MouseEvent.getSceneY());

			//Calculate starting x,y , aka min x and y
			final double xS = Math.min(pos[0].getX(), pos[1].getX());
			final double yS = Math.min(pos[0].getY(), pos[1].getY());
			relocate(xS, yS);
			//Calculate delta x,y aka max x and y
			final double xE = Math.max(pos[0].getX(), pos[1].getX());
			final double yE = Math.max(pos[0].getY(), pos[1].getY());
			final double xD = xE - xS;
			final double yD = yE - yS;
			//setting the width and height just right, to point to the mouse
			UI.setMinMax(this, xD, yD);
			setVisible(true);

			//Print debugger info
			System.out.printf("%10.1f , %10.1f %n", xS, yS);
			System.out.printf("%10.1f , %10.1f %n", xE, yE);
			MouseEvent.consume();
		});

		//reset the drag to the initial state
		root.addEventHandler(MouseEvent.MOUSE_RELEASED,MouseEvent -> {
			Arrays.fill(pos, null);
			MouseEvent.consume();
		});
		root.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, ContextMenuEvent -> setVisible(false));
	}

	private void initContextMenu(){
		//Configure context menu for the selection box

		MenuItem group1 = new MenuItem("Group");
		group1.setOnAction(EventHandler -> {
			//setup everything for the drag
			Group group = new Group(root,this);
			setVisible(false);
			EventHandler.consume();
		});

		//Configure the delete button
		MenuItem massDelete = new MenuItem("Delete");
		massDelete.setOnAction(EventHandler -> {
			//Remove all selected nodes
			root.getChildren().removeIf(
					(Node) -> Node.getLayoutX() > getLayoutX()
							&& Node.getLayoutX() < getLayoutX() + getMinWidth()
							&& Node.getLayoutY() > getLayoutY()
							&& Node.getLayoutY() < getLayoutY() + getMinHeight()
			);
			//selection is done
			setVisible(false);
			hide();
		});

		//Add both actions
		menu.getItems().addAll(group1, massDelete);

		setOnContextMenuRequested(ContextMenuEvent -> {
			System.out.println("selection context requested");
			visible(this, ContextMenuEvent.getScreenX(), ContextMenuEvent.getScreenY());
			ContextMenuEvent.consume();
		});
	}

	@Override
	public void visible(Node anchor, double screenX, double screenY) {
		menu.show(anchor,screenX,screenY);
	}

	@Override
	public void hide() {
		menu.hide();
	}

	@Override
	public boolean addItem(MenuItem item) {
		return menu.getItems().add(item);
	}
}
