package com.UI;

import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class moebelView extends ImageView {
	private boolean selected = false;
	private ObjectProperty<Image> display = this.imageProperty();

	private moebelView(){
		super();
	}

	public moebelView(Image display){
		this();
		this.display.set(display);
	}

	public void select() {
		selected = true;

	}
}
