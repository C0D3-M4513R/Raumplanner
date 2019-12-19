package com.UI.Menu;

import com.Repository;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.Optional;

public class MoebelMenu implements Menu {
	ContextMenu menu = new ContextMenu();

	public MoebelMenu(){
		init();
		MenuItem delete = new MenuItem("Delete"); //creating delete menu item
		//Defining delete handler
		delete.setOnAction(EventHandler -> {
			EventHandler.consume();
			System.out.println("Delete");
			//Getting coordinates
			final Point2D pos = Repository.UI.getRoom().screenToLocal(delete.getParentPopup().getAnchorX(), delete.getParentPopup().getAnchorY());
			System.out.printf("%10.1f,%10.1f%n", pos.getX(), pos.getY());


			//Deleting all nodes, intersecting that point
			// +1 and -1 for compensating the conversion from double to int
			Repository.UI.delete(Repository.UI.getRoom(), pos, Optional.of(true));
			Repository.UI.getRoom().getChildren().forEach(Node -> System.out.printf("%10.1f,%10.1f%n", Node.getLayoutX(), Node.getLayoutY()));
			hide();
		});
		//add to Context menu
		menu.getItems().add(delete);
	}

	@Override
	public void visible(Node anchor, double screenX, double screenY) {
		menu.show(anchor,screenX,screenY);
	}

	@Override
	public void hide() {
		menu.hide();
	}

	@Override
	public boolean addItem(MenuItem item) {
		return menu.getItems().add(item);
	}


}
