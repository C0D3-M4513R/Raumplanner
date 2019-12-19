package com.UI;

import com.Main;
import com.Repository;
import com.UI.Menu.Selection;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import sun.util.logging.PlatformLogger;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 This class handles everything, that has to do with Grouping multiple Furniture pieces together

 @author Timon Kayser
 */
 public class Group extends javafx.scene.Group {

	private static Comparator<Node> x = (n1, n2) -> (int) (n1.getLayoutX() - n2.getLayoutX());
	private static Comparator<Node> y = (n1, n2) -> (int) (n1.getLayoutY() - n2.getLayoutY());

	Pane room;
	Selection selection;

	/**
	 * <p>
	 * A level below the Group node, to allow for styling.
	 * </p>
	 * This holds all the information, however a group is in some cases easier to work with.
	 */
	private AnchorPane root = new AnchorPane();

	public Group(Pane room, Selection selection) {
		super();
		this.room=room;
		this.selection=selection;
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

		getChildren().add(root);
		root.layoutXProperty().bindBidirectional(layoutXProperty());
		root.layoutYProperty().bindBidirectional(layoutYProperty());
		Repository.UI.dragNode(root, this::getHeight, this::getWidth);
		setMenu();
		//get all Children, and reposition them correctly
		{
			//Adds all selected Nodes to be in the Group
			root.getChildren().addAll(
					//Get all nodes in the selection
					room.getChildren().filtered(
							(Node) -> Node.getLayoutX() > selection.getLayoutX()
									&& Node.getLayoutX() < selection.getLayoutX() + selection.getMinWidth()
									&& Node.getLayoutY() > selection.getLayoutY()
									&& Node.getLayoutY() < selection.getLayoutY() + selection.getMinHeight()
									&& !Node.getClass().isInstance(Region.class)
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
			} else {

				//get the min locations
				final Point2D pos = getMinPos();

				//set all edge-points
				relocate(pos.getX(), pos.getY());


				//make nodes location relative to the new Pane/Scene for them to stay in the same place
				//Only now do the transform, because we needed the coordinates for the pos array
				getChildren().forEach(Node -> {
					Point2D poi = parentToLocal(Node.getLayoutX(), Node.getLayoutY());
					Node.relocate(poi.getX(), poi.getY());
				});

				UI.groups.add(this);


				//move all children to the right parent
				room.getChildren().removeAll(root.getChildren());

				//selection is done
				selection.setVisible(false);
				selection.hide();
			}
		}
		System.out.println("isVo = " + isVisible());
		System.out.println(" room.getChilden().contains(this) = " + room.getChildren().contains(this));
//		throw new NullPointerException("Test");
	}

	/**
	 * Adds a menu on a right-click
	 */
	private void setMenu(){
		ContextMenu menu = new ContextMenu();

		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(evt -> {
			Alert sure = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure, that you want to delete this Group and EVERYTHING inside it?", ButtonType.NO, ButtonType.YES);
			sure.show();
			sure.setOnCloseRequest(DialogEvent -> {
				if (sure.getResult().equals(ButtonType.YES)) {
					room.getChildren().remove(this);
				}
			});

			evt.consume();
		});

		MenuItem ungroup = new MenuItem("UnGroup");
		ungroup.setOnAction(evt -> {
			//Delete all references and transfer all children, since we are only disbanding the group
			getChildren().forEach(Node -> {
				Point2D pos = localToParent(Node.getLayoutX(), Node.getLayoutY());
				Node.relocate(pos.getX(), pos.getY());
			});
			room.getChildren().remove(this);
			room.getChildren().addAll(root.getChildren());
			root.getChildren().removeAll(room);
		});


		menu.getItems().addAll(delete, ungroup);

		setOnContextMenuRequested(ContextMenuEvent -> {
			System.out.println("Region Context menu event fired");
			menu.show(this, null, 0, 0);
			ContextMenuEvent.consume();
		});
	}


	@Override
	public void relocate(double x,double y){
		root.relocate(x,y);
		super.relocate(x,y);
	}

	/**
	 * Because this Node is unmanaged, we have to do this ourselves
	 */
	@Override
	public void requestLayout() {
		if (Main.layoutLogger.isLoggable(PlatformLogger.Level.FINER))
			Main.layoutLogger.finer("Recomputing Layout of Group");
		setLayoutX(getMinNodeX().getLayoutX());
		setLayoutY(getMinNodeY().getLayoutY());

		resize(getWidth(),getHeight());

		super.requestLayout();
	}

	/**
	 *
	 * @return Returns the object's width
	 */
	public double getWidth() {
		return getMaxNodeX().getLayoutX()-getMinNodeX().getLayoutX();
	}

	/**
	 * @return Returns the object's height
	 */
	public double getHeight() {
		return getMaxNodeY().getLayoutY()-getMinNodeY().getLayoutY();
	}

	private Node getMinNodeX(Collection<Node> col){
		return Collections.min(col, x);
	}
	private Node getMinNodeY(Collection<Node> col){
		return Collections.min(col, x);
	}
	private Node getMinNodeX(){
		return getMinNodeX(getChildren());
	}
	private Node getMinNodeY(){
		return getMinNodeY(getChildren());
	}

	public Point2D getMinPos(Collection<Node> col) {
		return new Point2D(getMinNodeX(col).getLayoutX(),getMinNodeY(col).getLayoutY());
	}

	private Node getMaxNodeX(Collection<Node> col) {
		return Collections.max(col, Group.x);
	}

	private Node getMaxNodeY(Collection<Node> col) {
		return Collections.max(col, Group.y);
	}

	private Node getMaxNodeX() {
		return getMaxNodeX(getChildren());
	}

	private Node getMaxNodeY() {
		return getMaxNodeY(getChildren());
	}

	public Point2D getMaxPos(Collection<Node> col) {
		Node xnode= getMaxNodeX();
		Node ynode= getMaxNodeY();
		double x = xnode.getLayoutX() + ((xnode instanceof Canvas) ? ((Canvas) xnode).getWidth() : 0.0);
		double y = ynode.getLayoutX() + ((ynode instanceof Canvas) ? ((Canvas) ynode).getHeight() : 0.0);
		System.out.println("y = " + y);
		System.out.println("x = " + x);
		return new Point2D(x,y);
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
}
