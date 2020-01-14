package com.UI;

import com.Main;
import com.Moebel.Moebel;
import com.Moebel.SchrankWand;
import com.Supplier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 This class represents individual entries in the {@link UI#moebelList}
 */
public class moebelListNodeController<T extends Moebel> extends GridPane {

	private static final String FILE_NAME = "moebelListNode.fxml";
	public static final String NULL_WIDTH = "Various?";
	public static final String NULL_WIDTH_EXPECTED = "Below statement is to be expected. This Furniture is of a modular type.";
	public static final Supplier<Moebel,Double,Double,String> NULL_WIDTH_WARNING = (moebel,width,height)-> "width or height is zero. This shouldn't happen with non modular Furniture! Class is " + moebel.getClass().getSimpleName() + " \n width is " + width + " height is " + height;
	public static final Color base = Color.GRAY;

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

	Moebel moebel;

	public moebelListNodeController(T moebel, double width, double height) {
		//Load Preset
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(FILE_NAME));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Add a Moebel to our preview
		moebel.setVisible(true);
		add(moebel.getNode(), 2, 2, 1, 2);
		this.moebel = moebel;

		//Set all texts
		title.textProperty().bindBidirectional(moebel.nameProperty());
		title.setTooltip(new Tooltip(moebel.getClass().getSimpleName()));
		if (width != 0.0 && height != 0.0) description.setText("" + width + "x" + height);
		else {
			description.setText(NULL_WIDTH);
			if (!(moebel instanceof SchrankWand)) Main.layoutLogger.info(NULL_WIDTH_WARNING.get(moebel,width,height));
			else {
				Main.layoutLogger.finest(NULL_WIDTH_EXPECTED);
				Main.layoutLogger.finest(NULL_WIDTH_WARNING.get(moebel,width,height));
			}
		}
		moebel.changeColor(base);
	}

	/**
	 @param type
	 Type to be checked
	 @param <C>
	 Type to be checked

	 @return Returns true, if {@link #moebel} is of the type, that is being put in
	 */
	public <C> boolean isType(Class<C> type) {
		return type.isInstance(moebel);
	}

	public String getName() {
		return moebel.getName();
	}
}
