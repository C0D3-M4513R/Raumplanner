package com.UI;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class Group extends javafx.scene.Group {

	public Group(javafx.scene.Group group) {
		super(group);
	}

	public Group() {
		super();
	}

	/**
	 * @return returns an array with the max point and the min point, where a Node/Element resides
	 */
	public Point2D[] getPoints() {

		Point2D[] pos = {null, new Point2D(0, 0), null};
		for (Node node : getChildrenUnmodifiable()) {

			//determine top left point of all children
			if (pos[0] == null) pos[0] = new Point2D(node.getLayoutX(), node.getLayoutY());
			else {
				pos[0] = new Point2D(
						Math.min(node.getLayoutX(), pos[0].getX()), //left
						Math.min(node.getLayoutY(), pos[0].getY())); //top
			}

			//determine bottom right point
			if (node instanceof ImageView) {
				pos[1] = new Point2D(Math.max(pos[1].getX(), node.getLayoutX() + ((ImageView)node).getX() + ((ImageView)node).getFitWidth()), //right
						Math.max(pos[1].getY(), node.getLayoutY() + ((ImageView)node).getY() + ((ImageView)node).getFitHeight() )); //bottom
			} else {
				pos[1] = new Point2D(Math.max(pos[1].getX(), node.getLayoutX()), //right
						Math.max(pos[1].getY(), node.getLayoutY())); //bottom
				System.out.println("Else?!?! What??");
			}
		}
		if (pos[0] == null) pos[0] = new Point2D(0, 0);
		pos[2] = pos[1].subtract(pos[0]);
		return pos;
	}

	public void recursiveRelocate(double x, double y) {
		System.out.println("Recursive Relcoate run ");
		relocate(x, y);
		for (Node node : this.getChildren()) {
			node.relocate(x, y);
		}
	}

}
