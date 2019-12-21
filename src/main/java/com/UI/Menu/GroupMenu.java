package com.UI.Menu;

import com.UI.Group;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class GroupMenu implements Menu {
	ContextMenu menu = new ContextMenu();
	Group group;

	public GroupMenu(Group group) {
		this.group = group;
		init();

		MenuItem ungroup = new MenuItem("UnGroup");
		ungroup.setOnAction(evt -> {
			//Delete all references and transfer all children, since we are only disbanding the group
			group.getRoom().getChildren().remove(group);
			group.getChildren().forEach(Node -> {
				Point2D pos = group.localToParent(Node.getLayoutX(), Node.getLayoutY());
				Node.setLayoutX(pos.getX());
				Node.setLayoutY(pos.getY());
			});
			group.getRoom().getChildren().addAll(group.getChildren());
//			group.getRoom().getChildren().forEach(Node -> {
//				if (group.getChildren().contains(Node)) {
//					Point2D pos = group.getRoom().screenToLocal(Node.getLayoutX(), Node.getLayoutY());
//					Node.setLayoutX(pos.getX());
//					Node.setLayoutY(pos.getY());
//				}
//			});
			group.getChildren().removeAll(group.getRoom().getChildren());
		});


		menu.getItems().addAll(ungroup, MenuItems.deleteHandler(group));

		group.setOnContextMenuRequested(ContextMenuEvent -> {
			System.out.println("Region Context menu event fired");
			menu.show(group, null, 0, 0);
			ContextMenuEvent.consume();
		});
	}

	@Override
	public void visible(Node anchor, double screenX, double screenY) {
		menu.show(anchor, screenX, screenY);
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