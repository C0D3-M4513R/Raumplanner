package com.UI;

import com.Main;
import com.Repository;
import com.UI.Menu.GroupMenu;
import com.UI.Menu.Selection;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sun.util.logging.PlatformLogger;

import java.util.Collection;
import java.util.Comparator;

/**
 This class handles everything, that has to do with Grouping multiple Furniture pieces together

 @author Timon Kayser */
public class Group extends javafx.scene.Group {
	private static Comparator<Node> x = (n1, n2) -> (int) (n1.getLayoutX() - n2.getLayoutX());
	private static Comparator<Node> y = (n1, n2) -> (int) (n1.getLayoutY() - n2.getLayoutY());
	private boolean setup = false;
	private GroupMenu menu = new GroupMenu(this);

	Pane room;
	Selection selection;

	public Pane getRoom() {
		return room;
	}

	public Selection getSelection() {
		return selection;
	}

	/**
	 <p>
	 A level below the Group node, to allow for styling.
	 </p>
	 This holds all the information however.
	 */
	private AnchorPane root = new AnchorPane();

	public Group(Pane room, Selection selection) {
		super();
		this.room = room;
		this.selection = selection;
		setManaged(false); //This means, that we are not influenced by our Parent. So basically this is another layout root. See javadoc for more info
//		root.setMouseTransparent(true);
		root.setStyle("-fx-background-color: rgba(0,255,255,0.25);" +
				" -fx-border-style: solid;" +
				" -fx-border-radius: 2px;" +
				" -fx-border-color: black;" +
				" -fx-border-width: 2px;");
//		setStyle(root.getStyle());
		setVisible(true);
		root.setVisible(true);
		room.getChildren().add(this);

		super.getChildren().add(root);
		root.layoutXProperty().bindBidirectional(layoutXProperty());
		root.layoutYProperty().bindBidirectional(layoutYProperty());
		Repository.UI.dragNode(this, this::getHeight, this::getWidth);
		//get all Children, and reposition them correctly

		//Adds all selected Nodes to be in the Group
		getChildren().addAll(
				//Get all nodes in the selection
				room.getChildren().filtered(
						(Node) -> selection.getBoundsInParent().intersects(Node.getBoundsInParent()) && !Node.equals(selection)//better to let javafx handle this
//							(Node) -> Node.getLayoutX() > selection.getLayoutX()
//									&& Node.getLayoutX() < selection.getLayoutX() + selection.getMinWidth()
//									&& Node.getLayoutY() > selection.getLayoutY()
//									&& Node.getLayoutY() < selection.getLayoutY() + selection.getMinHeight()
//									&& !Node.getClass().isInstance(Region.class)
				)
		);

		//Check if we selected something
		if (root.getChildren().size() <= 0) {
			//if we selected nothing display an error and abort
			Alert noSelected = new Alert(Alert.AlertType.ERROR, "Keine Möbel ausgewählt. \n " +
					"Ein Möebel in kann nicht in mehreren Gruppen sein!", ButtonType.CANCEL);
			noSelected.show();
			//selection is done
			selection.setVisible(false);
			selection.hide();
			room.getChildren().remove(this);
			throw new IllegalStateException("We shoudln't have no Items here!");
		}


		relocate(getMinPos());

		//make nodes location relative to the new Pane/Scene for them to stay in the same place
		//Only now do the transform, because we needed the coordinates for the pos array
		getChildren().forEach(Node -> {
//			Point2D poi = parentToLocal(Node.getLayoutX(), Node.getLayoutY());
//			Node.relocate(poi.getX(), poi.getY());
			Node.setLayoutX(Node.getLayoutX() - getLayoutX());
			Node.setLayoutY(Node.getLayoutY() - getLayoutY());
		});


		UI.groups.add(this);


		//move all children to the right parent
		room.getChildren().removeAll(root.getChildren());

		//selection is done
		selection.setVisible(false);
		selection.hide();


//		throw new NullPointerException("Test");
		setup = true;

		requestLayout();
	}

	/**
	 Make this element return it's actual Children, not the "fake" {@link #root} Node
	 esentially delegate the cll through

	 @return modifiable list of children.
	 */
	@Override
	public ObservableList<Node> getChildren() {
		return root.getChildren();
	}

	@Override
	public ObservableList<Node> getChildrenUnmodifiable() {
		return root.getChildrenUnmodifiable();
	}

	/**
	 this class essentially contains two layers, so we have to compensate
	 <p>
	 {@inheritDoc}
	 */
	@Override
	public Point2D localToParent(double x, double y) {
		Point2D pos = root.localToParent(x, y);
		return super.localToParent(pos.getX(), pos.getY());
	}

//	/**
//	 this class essentially contains two layers, so we have to compensate
//	 <p>
//	 {@inheritDoc}
//	 */
//	@Override
//	public Point2D localToParent(Point2D pos) {
//		return super.localToParent(root.localToParent(pos));
//	}

	/**
	 this class essentially contains two layers, so we have to compensate
	 <p>
	 {@inheritDoc}
	 */
	@Override
	public Point2D parentToLocal(double x, double y) {
		Point2D pos = root.parentToLocal(x, y);
		return super.parentToLocal(pos.getX(), pos.getY());
	}

	//prevent a loop from happening
	@Override
	public Point2D parentToLocal(Point2D parentPoint) {
		return super.parentToLocal(parentPoint.getX(), parentPoint.getY());
	}

	/**
	 this class essentially contains two layers, so we have to compensate
	 <p>
	 {@inheritDoc}
	 */
	@Override
	public void relocate(double x, double y) {
		super.relocate(x, y);
		root.relocate(x, y);
	}

	public void relocate(Point2D pos) {
		relocate(pos.getX(), pos.getY());
	}

	/**
	 Because this Node is unmanaged, we have to do this ourselves
	 */
	@Override
	public void requestLayout() {
		if (Main.layoutLogger.isLoggable(PlatformLogger.Level.FINER))
			Main.layoutLogger.finer("Recomputing Layout of Group");

		if (!setup) {
			super.requestLayout();
			return;
//			throw new ConcurrentModificationException("The Constructor is still active!");
		}

		Point2D pos = localToParent(getMinNodeX(), getMinNodeY());
		if (getLayoutX() - getLayoutBounds().getMinX() != pos.getX())
			setLayoutX(pos.getX() - getLayoutBounds().getMinX());
		if (getLayoutY() - getLayoutBounds().getMinY() != pos.getY())
			setLayoutY(pos.getY() - getLayoutBounds().getMinY());
		root.resize(getWidth(),getHeight());

		super.requestLayout();
	}

	/**
	 @return Returns the object's width
	 */
	public double getWidth() {
		return getMaxNodeX().getLayoutX() - getMinNodeX();
	}

	/**
	 @return Returns the object's height
	 */
	public double getHeight() {
		return getMaxNodeY().getLayoutY() - getMinNodeY();
	}

	private double getMinNodeX(Collection<Node> col) {
		Node smallest = col.iterator().next();
		for (Node node : col) {
			if (node.getLayoutX() < smallest.getLayoutX()) smallest = node;
		}
		smallest.setRotate(270);
		return smallest.getLayoutX();
	}

	private double getMinNodeY(Collection<Node> col) {
		Node smallest = col.iterator().next();
		for (Node node : col) {
			if (node.getLayoutY() < smallest.getLayoutY()) smallest = node;
		}
		smallest.setRotate(0);
		return smallest.getLayoutY();
	}

	private double getMinNodeX() {
		return getMinNodeX(getChildrenUnmodifiable());
	}

	private double getMinNodeY() {
		return getMinNodeY(getChildrenUnmodifiable());
	}

	public Point2D getMinPos(Collection<Node> col) {
		if (col.isEmpty()) return null;
		return new Point2D(getMinNodeX(col), getMinNodeY(col));
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
		//TODO: Something is wrong here. Dunno what Probably rotation is messing the sub-methods up...
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

	/**
	 Custom delete handler, to ensure, that this class gets deleted properly

	 @
	 */
	protected void finalize() throws Throwable {
		super.getChildren().remove(root);
		root = null;
		super.finalize();
	}
}
