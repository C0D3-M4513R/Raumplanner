package com.UI.Menu;

import com.UI.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

class MenuItems {
	static MenuItem deleteHandler(Node Node) {
		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(evt -> {
			if (Node instanceof Group) {
				Alert sure = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure, that you want to delete this Group and EVERYTHING inside it?", ButtonType.NO, ButtonType.YES);
				sure.show();
				sure.setOnCloseRequest(DialogEvent -> {
					if (sure.getResult().equals(ButtonType.YES)) {
						((Group) Node).getRoom().getChildren().remove(Node);
					}
				});
			} else if (Node instanceof Selection) {
				((Selection) Node).getRoot().getChildren().removeIf(
						(node) -> Node.getBoundsInParent().intersects(node.getBoundsInParent()) && node != Node
				);
				Node.setVisible(false);
			} else {
				((Pane) Node.getParent()).getChildren().remove(Node);
			}
//			delete.getParentMenu().hide();
		});
		return delete;
	}
}
