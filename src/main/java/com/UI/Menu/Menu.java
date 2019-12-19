package com.UI.Menu;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;


interface Menu{
	/**
	 this variable keeps track of all
	 */
	List<Menu> menus = new ArrayList<>();

	/**
	 initializer function
	 */
	default void init(){
		menus.add(this);
	}

	/**
	 hides all menus
	 */
	default void hideAll(){
		menus.forEach(Menu::hide);
	}

	/**
	 Show this Menu
	 */
	void visible(Node anchor, double screenX, double screenY);

	/**
	 Hide this Menu
	 */
	void hide();

	/**
	 Allows to add a Menu entry
	 @param item entry to add
	 */
	boolean addItem(MenuItem item);
}
