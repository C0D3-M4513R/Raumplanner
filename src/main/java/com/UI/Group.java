package com.UI;

import com.UI.Menu.GroupMenu;
import com.UI.Menu.Selection;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Collection;
import java.util.HashMap;

/**
 This class handles everything, that has to do with Grouping multiple Furniture pieces together

 @author Timon Kayser */
public class Group extends RootPane {
	//TODO: allow groups to expand up and left
	//TODO: Make all Moebels spawn inside the group
	private GroupMenu menu = new GroupMenu(this);
	private RootPane root;

	public RootPane getRoot() {
		return root;
	}

	public Group(RootPane root, Selection selection) {
		super();
		this.root = root;

		setStyle("-fx-background-color: rgba(0,255,255,0.25);" +
				" -fx-border-style: solid;" +
				" -fx-border-radius: 2px;" +
				" -fx-border-color: black;" +
				" -fx-border-width: 2px;");

		//make visible
		Platform.runLater(() -> {
			setVisible(true);
			root.getChildren().add(this);
			root.dragNode(this);
		});
		//Adds all selected Nodes to be in the Group
		getChildren().addAll(
				//Get all nodes in the selection
				root.getChildren().filtered(
						(Node) -> selection.getBoundsInParent().intersects(Node.getBoundsInParent()) && !Node.equals(selection) && !Node.equals(price)//better to let javafx handle this
				)
		);

		//Check if we selected something
		if (getChildren().size() <= 0) {
			//if we selected nothing display an error and abort
			Alert noSelected = new Alert(Alert.AlertType.ERROR, "Keine Möbel ausgewählt. \n " +
					"Ein Möebel in kann nicht in mehreren Gruppen sein!", ButtonType.CANCEL);
			noSelected.show();
			//selection is done
			((RootPane) getParent()).getChildren().remove(this);
			throw new IllegalStateException("We shoudln't have no Items here!");
		}


		HashMap<Node, Point2D> poss = new HashMap<>();
		//make nodes location relative to the new Pane/Scene for them to stay in the same place
		getChildren().forEach(Node -> {
			poss.put(Node,root.localToScreen(Node.getLayoutX(), Node.getLayoutY()));
		});

		//relocate group
		relocate(2*getLayoutX() + getBoundsInLocal().getMinX(),
				2*getLayoutY() + getBoundsInLocal().getMinY());
		//and then move them back to make them "stay" in place
		getChildren().forEach(Node ->{
			Point2D pos = screenToLocal(poss.get(Node));
			if(pos!=null)Node.relocate(pos.getX(), pos.getY());
		});

		UI.groups.add(this);

		requestLayout();
		System.out.println("Done");
	}

	/* ****************************************************************************************************************
	 Start Convinience methods
	 *****************************************************************************************************************/

	public void relocate(Point2D pos) {
		relocate(pos.getX(), pos.getY());
	}

	private Node getMinNodeX(Collection<Node> col) {
		Node smallest = col.iterator().next();
		for (Node node : col) {
			if (node.getBoundsInParent().getMinX() < smallest.getBoundsInParent().getMinX()) smallest = node;
		}
		smallest.setRotate(270);
		return smallest;
	}

	private Node getMinNodeX() {
		return getMinNodeX(getChildrenUnmodifiable());
	}

	private double getMinX(Collection<Node> col) {
		return getMinNodeY(col).getLayoutX();
	}

	private Node getMinNodeY(Collection<Node> col) {
		Node smallest = col.iterator().next();
		for (Node node : col) {
			if (node.getBoundsInParent().getMinY() < smallest.getBoundsInParent().getMinY()) smallest = node;
		}
		smallest.setRotate(0);
		return smallest;
	}

	private Node getMinNodeY() {
		return getMinNodeY(getChildrenUnmodifiable());
	}

	private double getMinY(Collection<Node> col) {
		return getMinNodeY(col).getLayoutY();
	}

	private double getMinX() {
		return getMinX(getChildrenUnmodifiable());
	}

	private double getMinY() {
		return getMinY(getChildrenUnmodifiable());
	}

	public Point2D getMinPos(Collection<Node> col) {
		if (col.isEmpty()) return null;
		return new Point2D(getMinX(col), getMinY(col));
	}

	private Node getMaxNodeX(Collection<Node> col) {
		Node biggest = col.iterator().next();
		for (Node node : col) {
			if (node.getLayoutX() > biggest.getLayoutX()) biggest = node;
		}
		biggest.setRotate(90);
		return biggest;
	}

	private Node getMaxNodeY(Collection<Node> col) {
		Node biggest = col.iterator().next();
		for (Node node : col) {
			if (node.getLayoutY() > biggest.getLayoutY()) biggest = node;
		}
		biggest.setRotate(180);
		return biggest;
	}

	private Node getMaxNodeX() {
		return getMaxNodeX(getChildrenUnmodifiable());
	}

	private Node getMaxNodeY() {
		return getMaxNodeY(getChildrenUnmodifiable());
	}

	public Point2D getMaxPos(Collection<Node> col) {
		if (col.isEmpty()) return null;
		Node xnode = getMaxNodeX();
		Node ynode = getMaxNodeY();
		double x = xnode.getLayoutX() + ((xnode instanceof Canvas) ? ((Canvas) xnode).getWidth() : 0.0);
		double y = ynode.getLayoutX() + ((ynode instanceof Canvas) ? ((Canvas) ynode).getHeight() : 0.0);
		System.out.println("y = " + y);
		System.out.println("x = " + x);
		return new Point2D(x, y);
	}

	public Point2D getDelta(Collection<Node> col) {
		return getMaxPos(col).subtract(getMinPos(col));
	}

	public Point2D getMinPos() {
		return getMinPos(getChildrenUnmodifiable());
	}

	public Point2D getMaxPos() {
		return getMaxPos(getChildrenUnmodifiable());
	}

	public Point2D getDelta() {
		return getDelta(getChildrenUnmodifiable());
	}
}
