package com.UI;

import com.Main;
import com.Moebel.Moebel;
import com.Moebel.SchrankWand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;

import java.io.IOException;

public class moebelListNodeController extends SplitPane {

	/**
	 The title in the List, this node is displayed in
	 */
	@FXML
	private Label title = new Label();
	/**
	 A description, to be put below the title
	 */
	@FXML
	private Label description = new Label();
	/**
	 A preview of what a Moebel is gonna look like
	 */
	@FXML
	private Canvas img = new Canvas();

	public moebelListNodeController(Moebel moebel, double width, double height) {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("moebelListNode.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		moebel.setHeight(img.getHeight());
		moebel.setWidth(img.getWidth());
		moebel.setScaleX(0.5);
		moebel.setScaleY(0.5);
		img = moebel;
		img.setVisible(true);

//        moebel.getImage(true);  //sets fallback
//        img.imageProperty().bindBidirectional(moebel.imageProperty());
		title.textProperty().bindBidirectional(moebel.nameProperty());
		title.setTooltip(new Tooltip(moebel.getClass().getSimpleName()));
		if(width!=0.0&&height!=0.0) description.setText("" + height + "x" + width);
		else {
			description.setText("Various?");
			String txt = "width or height is zero. This shouldn't happen with non modular Furniture! Class is "+moebel.getClass().getSimpleName()+" \n width is "+width+" height is "+height;
			if(!(moebel instanceof SchrankWand)) Main.layoutLogger.info(txt);
			else {
				Main.layoutLogger.finest("Below statement is to be expected. This Furniture is of a modular type.");
				Main.layoutLogger.finest(txt);
			}
		}
	}

	public String getTitle() {
		return title.getText();
	}

	public String getDescription() {
		return description.getText();
	}

	/**
	 @param type Type to be checked
	 @param <T> Type to be checked
	 @return Returns true, if {@link #img} is of the type, that is being put in
	 */
	public <T> boolean isType(Class<T> type) {
		return type.isInstance(img);
	}

	public String getName() {
		return ((Moebel) img).getName();
	}
}
