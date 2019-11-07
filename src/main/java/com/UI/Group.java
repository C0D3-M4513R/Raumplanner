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
		for (Node node : this.getChildrenUnmodifiable()) {
			if (pos[0] == null) pos[0] = new Point2D(node.getLayoutX(), node.getLayoutY());
			else
				pos[0] = new Point2D(Math.min(node.getLayoutX(), pos[0].getX()), Math.min(node.getLayoutX(), pos[0].getY()));
			pos[1] = new Point2D(Math.max(pos[1].getX(), node.getLayoutX()), Math.max(pos[1].getY(), node.getLayoutY()));
			if ((node.getLayoutX() == pos[1].getX() || node.getLayoutY() == pos[1].getY())
					&& node instanceof ImageView) {
				pos[1] = pos[1].add(((ImageView) node).getFitWidth(), ((ImageView) node).getFitHeight());
			}
		}
		if (pos[0] == null) pos[0] = new Point2D(0, 0);
		pos[2] = pos[1].subtract(pos[0]);
		return pos;
	}

	public void recursiveRelocate(double x, double y) {
		System.out.println("Recursive Relcoate run ");
		this.relocate(x, y);
		for (Node node : this.getChildren()) {
			node.relocate(x, y);
		}
	}

}
