package com.UI.Menu;

import com.Moebel.Moebel;
import com.UI.Group;
import com.sun.javafx.scene.control.skin.CustomColorDialog;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class MenuItems {

	private MenuItems() {
	}

	/**
	 creates a menu entry to delete things
	 @param Node the node, that the Menu belongs to
	 @return Returns an entry, that deletes things
	 */
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

	/**

	 @param node node, the Menu belongs to
	 @return Returns a Menu entry, that asks for a color, and then passes it to be set
	 */
	static MenuItem colorHandler(Node node) {
		MenuItem color = new MenuItem("Color Change");
		color.setOnAction(evt -> {
			CustomColorDialog colorDialog = new CustomColorDialog(new Stage());
			if (node instanceof Moebel) colorDialog.setCurrentColor(((Moebel) node).currentColor);
			colorDialog.show();

			Runnable run = () -> {
				colorDialog.hide();
				distributeColor(node, colorDialog.getCustomColor());
			};

			colorDialog.setOnSave(run);
			colorDialog.setOnUse(run);
		});

		return color;
	}

	/**
	 Distribute a Method call to set a color to the respective function
	 @param node node, that should get the color set
	 @param customColor Color to be set
	 */
	private static void distributeColor(Object node, Color customColor) {
		for (Method method : MenuItems.class.getDeclaredMethods()) {
			Class<?> type = method.getParameterTypes()[0];
			//check if the method is good
			//does the name match?
			//does it have the required amount of parameters?
			//do the types match?
			if (method.getName().equals("setColor") && method.getParameters().length == 2 &&
					Node.class.isAssignableFrom(method.getParameterTypes()[0]) && method.getParameterTypes()[0].asSubclass(Node.class).isInstance(node) &&
					method.getParameterTypes()[1] == Color.class) {
				try {
					//if the method is good, call it
					method.invoke(new MenuItems(), node, customColor);
					//also don't search for other matches. the check should only allow one method through at most
					break;
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 Actually changes the color

	 @param moebel moebel affected by the change
	 @param customColor Color to change the moebel to
	 */
	private static void setColor(Moebel moebel, Color customColor) {
		System.out.println("Moebel");
		moebel.changeColor(customColor);
	}

	/**
	 If we have a group, just paint all the nodes.
	 If we should implement multiple selection boxes, only select the nodes, that are selected
	 @param group group to be checked
	 @param customColor Color to be later set
	 */
	private static void setColor(Group group, Color customColor) {
		System.out.println("Group");
		for (Node target : group.getChildren()) {
			if (target instanceof Selection) setColor((Selection) target, customColor, group); //TODO: is this check useless?
			else distributeColor(target, customColor);
		}
	}

	/**
	 Color all nodes in the Selection.
	 If there is a group only color the nodes, that are in the Selection. Don't color the whole Group
	 @param selection Selection to check
	 @param customColor Color to be later set
	 */
	private static void setColor(Selection selection, Color customColor) {
		System.out.println("Selection");
		for (Node target : selection.getRoot().getChildren().filtered(node1 -> selection.getBoundsInParent().intersects(node1.getBoundsInParent()) && !node1.equals(selection))) {
			// if we encounter a group, we wanna check, which nodes need to be painted
			if(target instanceof Group) setColor(selection,customColor, (Group) target);
			else distributeColor(target, customColor);
		}
	}

	/**

	 @param selection Selection to Color
	 @param customColor the Color to color it later
	 @param group Group selected by the selection
	 */
	private static void setColor(Selection selection, Color customColor, Group group) {
		for (Node target : group.getChildren()) {
			if (selection.getBoundsInParent().intersects(group.localToParent(target.getBoundsInParent())) && !target.equals(selection)) {
				distributeColor(target, customColor);
			}
		}
	}
}
