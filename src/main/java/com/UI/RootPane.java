package com.UI;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import static com.UI.Menu.Selection.selections;

public class RootPane extends AnchorPane {

	RootPane(){
		super();
		setMinHeight(0);
		setMinWidth(0);
	}


	/**
	 Handles all EventHandlers related to dragging any displayed object@param node
	 Node to apply the ability to drag to
	 */
	void dragNode(Node node) {
		//TODO: Something is fishy about this. Just wrong
		// Groups move double

		// Custom object to hold x and y positions
		final Delta dragDelta = new Delta();

		//Calculate the amount dragged
		node.setOnMousePressed(mouseEvent -> {
			selections.get(0).hideAll();
			dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
			dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
			mouseEvent.consume();
		});

		node.setOnMouseEntered(mouseEvent -> node.setCursor(Cursor.HAND));
		node.setOnMouseReleased(mouseEvent -> node.setCursor(Cursor.HAND));


		node.setOnMouseDragged(mouseEvent -> {

			//Move node, like it was dragged
			node.relocate(mouseEvent.getSceneX() + dragDelta.x,mouseEvent.getSceneY() + dragDelta.y);

			//move node appropriately after accounting for collisions
			//basically just a offset
			Point2D pos = col(node);
			node.relocate(pos.getX(),pos.getY());

			//check if node is in a Group
			if(node.getParent().getParent() instanceof Group){
				Group group = (Group) node.getParent().getParent();
				group.checkLocation();

			}
			mouseEvent.consume();
		});
	}

	/**
	 Checks collision between program bounds in the y direction <br>
	 Also sets the Rotation variable

	 @return Returns the appropriate xy value
	 @param node
	 Node to be checked
	 */
	Point2D col(Node node) {
		double maxDeltaY = node.getBoundsInParent().getMaxY() - getHeight();
		double minDeltaY = node.getBoundsInParent().getMinY();
		double maxDeltaX = node.getBoundsInParent().getMaxX() - getWidth();
		double minDeltaX = node.getBoundsInParent().getMinX();
		double height = node.getBoundsInLocal().getHeight();
		double width = node.getBoundsInLocal().getWidth();
		double rotatePrev = node.getRotate();
		int rotate;
		int no = 0;
		Delta out = new Delta();

		if (maxDeltaY > 0){
			rotate=180;
			no++;
			node.setRotate(rotate);
			out.y = node.getLayoutY() - maxDeltaY;
		} else if (minDeltaY < 0.0 ){
			rotate=0;
			no++;
			node.setRotate(rotate);
			out.y = node.getLayoutY() - minDeltaY;
		} else {
			out.y=node.getLayoutY();
		}

		if (maxDeltaX > 0){
			rotate=90;
			no++;
			node.setRotate(rotate);
			out.x = node.getLayoutX() - maxDeltaX ;
		} else if (minDeltaX < 0.0){
			rotate = 270;
			no++;
			node.setRotate(rotate);
			out.x = node.getLayoutX() - minDeltaX;
		}else {
			out.x = node.getLayoutX();
		}
		if (no > 1)node.setRotate(rotatePrev);
		return new Point2D(out.x,out.y);
	}

	/** Simple Class to store a xy position */
	static class Delta {
		Double x, y;
	}
}
