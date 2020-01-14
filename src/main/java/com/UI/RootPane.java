package com.UI;

import com.Moebel.Cost;
import com.Repository;
import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

import static com.UI.Menu.Selection.selections;

/**
 Superclass for everything, where you position Furniture
 @see com.Moebel.Moebel
 @author Timon Kayser
 */
public class RootPane extends AnchorPane {


	/**
	 All instances of RootPane Objects
	 */
	private static List<Pane> instances = new ArrayList<>();

	/** States the price {@link Cost#totalCost}*/
	protected static final Label price = new Label();
	/** Used to track, if this is the first instance*/
	protected static boolean first = true;

	RootPane(){
		super();
		//register us
		instances.add(this);

		//check for empty groups. If there are some, delete them
		getChildren().addListener((InvalidationListener) (observable)->
				getChildrenUnmodifiable().forEach(Node->{
			if(Node instanceof Group && ((Group) Node).getChildren().size()==0) getChildren().remove(Node);
		}));

		//prevent weird things from happening
		setMinHeight(0);
		setMinWidth(0);

		//display price on the top left
		if (first) {
			//Make the Price visible in the right position
			getChildren().add(price);
			price.setVisible(true);
			price.relocate(10, 10);
			price.toFront();
			//Make a property, that is updating with each change to the cost
			StringProperty str = new StringPropertyBase() {
				@Override
				public Object getBean() {
					return this;
				}

				@Override
				public String getName() {
					return "Text Property";
				}
			};
			Cost.totalCost.addListener((observable)-> str.setValue(Cost.totalCost.getName()+Cost.totalCost.get()));
			//Then Bind that
			price.textProperty().bind(str);
			first=false;
		}
	}


	/**
	 Handles all EventHandlers related to dragging any displayed object@param node
	 Node to apply the ability to drag to
	 */
	public void dragNode(Node node) {
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

	/**
	 A methid to safely delete A RootPane
	 @param pane Pane to be deleted
	 */
	public static void delete(Pane pane){
		instances.remove(pane);


		if(pane.getParent()!=null) ((Pane)pane.getParent()).getChildren().remove(pane);
		UI.delete(Repository.UI.getRoom().getChildren(),pane);

		//unregister from cost interface
		pane.getChildren().forEach(node -> {
			if(node instanceof Cost ) ((Cost)node).remove();
			else if (node instanceof Pane) delete((Pane) node);
		});
		pane=null;
	}

	/**
	 Getter for @link(#instances)
	 @return Returns a List of all instances of RootPane Object
	 */
	public static List<Pane> getInstances(){
		return instances;
	}

	/**
	 Adds a Node to the @link(#instances) list.
	 @param node Node to be added
	 */
	public static void add(Pane node) {
		instances.add(node);
	}

	/** Simple Class to store a xy position */
	static class Delta {
		Double x, y;
	}
}
