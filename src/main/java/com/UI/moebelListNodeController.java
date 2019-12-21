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

import java.io.IOException;


public class moebelListNodeController extends GridPane {

	private static final String FILE_NAME = "moebelListNode.fxml";
	public static final String NULL_WIDTH = "Various?";
	public static final String NULL_WIDTH_EXPECTED = "Below statement is to be expected. This Furniture is of a modular type.";
	public static final Supplier<Moebel,Double,Double,String> NULL_WIDTH_WARNING = (moebel,width,height)-> "width or height is zero. This shouldn't happen with non modular Furniture! Class is " + moebel.getClass().getSimpleName() + " \n width is " + width + " height is " + height;

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

	public moebelListNodeController(Moebel moebel, double width, double height) {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(FILE_NAME));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		moebel.setVisible(true);
		moebel.setWidth(moebel.getHeight() * moebel.getWidth() /50.0);
		moebel.setHeight(50.0);
		add(moebel, 2, 2, 1, 2);
		this.moebel = moebel;

//        moebel.getImage(true);  //sets fallback
//        img.imageProperty().bindBidirectional(moebel.imageProperty());
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
	}

	public String getTitle() {
		return title.getText();
	}

	public String getDescription() {
		return description.getText();
	}

	/**
	 @param type
	 Type to be checked
	 @param <T>
	 Type to be checked

	 @return Returns true, if {@link #moebel} is of the type, that is being put in
	 */
	public <T> boolean isType(Class<T> type) {
		return type.isInstance(moebel);
	}

	public String getName() {
		return moebel.getName();
	}
}
