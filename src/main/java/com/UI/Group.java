package com.UI;

import com.Main;
import com.Repository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import sun.util.logging.PlatformLogger;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class handles everything, that has to do with Grouping multiple Furniture pieces together
 *
 * @author Timon Kayser
 */
public class Group extends AnchorPane {

	private static Comparator<Node> x = (n1,n2) -> (int) (n1.getLayoutX()-n2.getLayoutX());
	private static Comparator<Node> y = (n1, n2) -> (int) (n1.getLayoutY()-n2.getLayoutY());
	AnchorPane room = ((UI) Main.fxml.getController()).room;
	Region selection = ((UI) Main.fxml.getController()).selection;

	Group() {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("group.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		//Initialise and set RightClick Menu
		{
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
					Node.relocate(pos.getX(),pos.getY());
				});
				room.getChildren().remove(this);
				room.getChildren().addAll(getChildren());
				getChildren().removeAll(room);
			});


			menu.getItems().addAll(delete, ungroup);

			setOnContextMenuRequested(ContextMenuEvent -> {
				System.out.println("Region Context menu event fired");
				menu.show(this, null, 0, 0);
				ContextMenuEvent.consume();
			});
		}
		//get all Children, and reposition them correctly
		{
			//Adds all selected Nodes to be in the Group
			getChildren().addAll(
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
			if (getChildren().size() <= 0) {
				//if we selected nothing display an error and abort
				Alert noSelected = new Alert(Alert.AlertType.ERROR, "Keine Möbel ausgewählt. \n " +
						"Ein Möebel in kann nicht in mehreren Gruppen sein!", ButtonType.CANCEL);
				noSelected.show();
				//selection is done
				selection.setVisible(false);
				UI.selContext.hide();
				return;
			}

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
			room.getChildren().removeAll(getChildren());

			//selection is done
			selection.setVisible(false);
			UI.selContext.hide();
		}
		setManaged(false);
	}


	@Override public void requestLayout(){
		if(Main.layoutLogger.isLoggable(PlatformLogger.Level.FINER)) Main.layoutLogger.finer("Recomputing Layout of Group");
		//todo: validate height and width, resize, if necessary
		super.requestLayout();
	}

	/**
	 * Intercept method for the object's width
	 * @param width Width of the object to be set
	 */
	@Override protected void setWidth(double width){
		if(Main.layoutLogger.isLoggable(PlatformLogger.Level.FINEST)) Main.layoutLogger.fine("Intercepting call to width: new width is:"+ width);
		Repository.setx(this.toString(), width);
		super.setWidth(width);
	}

	/**
	 * Intercept method for the object's height
	 * @param height Height of the object to be set
	 */
	@Override protected void setHeight(double height){
		if(Main.layoutLogger.isLoggable(PlatformLogger.Level.FINEST)) Main.layoutLogger.fine("Intercepting call to width: new height is:"+ height);
		System.out.println(this.toString());
		Repository.sety(this.toString(), height);

		super.setHeight(height);
	}


	/**
	 *
	 * @param width This param is ignored. ALWAYS
	 * @return Returns the object's width
	 */
	public double getWidth(double width){
		return Repository.getx(this.toString());
	}

	/**
	 *
	 * @param height This param is ignored. ALWAYS
	 * @return Returns the object's height
	 */
	public double getHeight(double height){
		return Repository.gety(this.toString());
	}

	public Point2D getMinPos(Collection<Node> col) {
		return new Point2D(Collections.min(col,x).getLayoutX(),Collections.min(col,y).getLayoutY());
	}
	public Point2D getMaxPos(Collection<Node> col) {
		Node x = Collections.max(col,Group.x);
		Node y = Collections.max(col,Group.y);
		return new Point2D(x.getLayoutX() + ((x instanceof ImageView)?((ImageView) x).getFitWidth():0.0),y.getLayoutX() + ((y instanceof ImageView)?((ImageView) y).getFitHeight():0.0) );
	}
	public Point2D getDelta(Collection<Node> col){
		return getMaxPos(col).subtract(getMinPos(col));
	}

	public Point2D getMinPos(){return getMinPos(getChildrenUnmodifiable());}
	public Point2D getMaxPos(){return getMaxPos(getChildrenUnmodifiable());}
	public Point2D getDelta(){return getDelta(getChildrenUnmodifiable());}
}
