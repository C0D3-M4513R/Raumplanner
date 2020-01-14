package com.UI.Menu;

import com.UI.Group;
import com.UI.RootPane;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import static com.UI.RootPane.delete;

public class GroupMenu implements Menu {
	ContextMenu menu = new ContextMenu();
	Group group;

	/**
	 Initialises this Menu.
	 @param group The Group is needed for ungrouping
	 */
	public GroupMenu(Group group) {
		this.group = group;
		init();

		MenuItem ungroup = new MenuItem("UnGroup");
		ungroup.setOnAction(evt -> {
			//save important vars
			RootPane root = group.getRoot();
			ObservableList<Node> children = group.getChildren();


			Platform.runLater(()-> {
						//Delete all references and transfer all children, since we are only disbanding the group
						children.forEach(Node -> {
							//relocate Nodes
							Point2D pos = group.localToScreen(Node.getLayoutX(), Node.getLayoutY());
							pos = root.screenToLocal(pos);
							Node.relocate(pos.getX(), pos.getY());
						});
					});

			//Transfer children and delete group from view
			delete(group);
			root.getChildren().addAll(children);
		});

		menu.getItems().addAll(MenuItems.colorHandler(group),ungroup, MenuItems.deleteHandler(group));

		group.setOnContextMenuRequested(ContextMenuEvent -> {
			System.out.println("Region Context menu event fired");
			menu.show(group, null, 0, 0);
			ContextMenuEvent.consume();
		});
	}

	/**
	 @{inheritDoc}
	 */
	@Override
	public void visible(Node anchor, double screenX, double screenY) {
		menu.show(anchor, screenX, screenY);
	}

	/**
	 @{inheritDoc}
	 */
	@Override
	public void hide() {
		menu.hide();
	}

	/**
	 @{inheritDoc}
	 */
	@Override
	public boolean addItem(MenuItem item) {
		return menu.getItems().add(item);
	}

}
