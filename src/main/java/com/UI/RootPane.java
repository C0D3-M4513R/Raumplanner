package com.UI;

import com.Moebel.Cost;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

import static com.UI.Menu.Selection.selections;

/**
 Superclass for everything, where you position Furniture
 @author Timon Kayser
 */
public class RootPane extends AnchorPane {

	private static List<RootPane> instances = new ArrayList<>();

	/** States the price {@link Cost#totalCost}*/
	private static Label price = new Label();

	RootPane(){
		super();
		//register us
		instances.add(this);

		//prevent weird things from happening
		setMinHeight(0);
		setMinWidth(0);

		//display price on the top left
		getChildren().add(price);
		price.setVisible(true);
		price.relocate(10,10);
		price.toFront();
	}


	/**
	 Handles all EventHandlers related to dragging any displayed object@param node
	 Node to apply the ability to drag to
	 */
	void dragNode(Node node) {
		// Custom object to hold x and y positions
		final Delta dragDelta = new Delta();

		//Calculate the amount dragged
		node.setOnMousePressed(mouseEvent -> {
			dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
			dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
			mouseEvent.consume();
		});

		node.setOnMouseEntered(mouseEvent -> node.setCursor(Cursor.HAND));
		node.setOnMouseReleased(mouseEvent -> node.setCursor(Cursor.HAND));


		node.setOnMouseDragged(mouseEvent -> {
			selections.get(0).hideAll();

			//Move node, like it was dragged
			node.relocate(mouseEvent.getSceneX() + dragDelta.x,mouseEvent.getSceneY() + dragDelta.y);

			//move node appropriately after accounting for collisions
			//basically just a offset
			Point2D pos = col(node);
			node.relocate(pos.getX(),pos.getY());

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

		//Bottom Collision
		if (maxDeltaY > 0){
			rotate=180;
			no++;
			node.setRotate(rotate);
			out.y = node.getLayoutY() - maxDeltaY;
		//Top Collision
		} else if (minDeltaY < 0.0 ){
			rotate=0;
			no++;
			node.setRotate(rotate);
			out.y = node.getLayoutY() - minDeltaY;
		} else {
			out.y=node.getLayoutY();
		}
		//Right Collision
		if (maxDeltaX > 0){
			rotate=90;
			no++;
			node.setRotate(rotate);
			out.x = node.getLayoutX() - maxDeltaX ;
		//Left Collision
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

	public void delete(){
		instances.remove(this);
		((RootPane)getParent()).getChildren().remove(this);

		//unregister from cost interface
		getChildren().forEach(node -> {
			if(node instanceof Cost ) ((Cost)node).remove();
			else if (node instanceof RootPane) ((Group)node).delete();
		});
	}

	public static List<RootPane> getInstances(){
		return instances;
	}

	public static void updatePrice(){
		price.setText("Price: "+RootPane.getInstances().stream().mapToDouble(rootPane -> rootPane.getChildrenUnmodifiable().filtered(node -> node instanceof Cost).stream().mapToDouble(node -> ((Cost)node).cost()).sum()).sum());
	}

	/** Simple Class to store a xy position */
	static class Delta {
		Double x, y;
	}
}
