package com.UI;

import com.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.io.IOException;

/**
 * This class handles everything, that has to do with Grouping multiple Furniture pieces together
 *
 * @author Timon Kayser
 */
public class Group extends AnchorPane {

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
			final Double[] pos = {null, null};
			getChildrenUnmodifiable().forEach(Node -> {
				pos[0] = Math.min(pos[0] != null ? pos[0] : Node.getLayoutX(), Node.getLayoutX());
				pos[1] = Math.min(pos[1] != null ? pos[1] : Node.getLayoutY(), Node.getLayoutY());
			});

			//set all edge-points
			relocate(pos[0], pos[1]);


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
		//if(Main.layoutLogger.isLoggable(PlatformLogger.Level.FINER)) Main.layoutLogger.finer("Recomputing Layout of Group");
		//todo: validate height and width, resize, if necessary
		super.requestLayout();
	}


}
