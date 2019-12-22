package com.UI.Menu;

import com.Moebel.Moebel;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MoebelMenu implements Menu {
	ContextMenu menu = new ContextMenu();

	public MoebelMenu(Moebel moebel){
		init();
		//add to Context menu
		menu.getItems().addAll(MenuItems.colorHandler(moebel),MenuItems.deleteHandler(moebel));
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
