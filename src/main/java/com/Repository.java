package com;

import com.Moebel.Moebel;
import com.Moebel.SchrankWand;
import com.UI.UI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.function.Function;

import static com.Moebel.Bett.LEFRANCE;
import static com.Moebel.Bett.LEPETITFRANCE;
import static com.Moebel.Esstisch.WIKINGER;
import static com.Moebel.Schrank.BILLY;
import static com.Moebel.Sessel.DELUXE;
import static com.Moebel.Sofa.KOMFORT;
import static com.Moebel.Sofatisch.ADMIRAL;
import static com.Moebel.Sofatisch.MONTECARLO;
import static com.Moebel.Stuhl.NOMADE;

/**
 This is to satisfy the Project requirements. One could do this with less code.
 <br>
 also this class is final instead of abstract, because it has no constructors and therefore cannot be instantiated<br>
 furthermore, you can't extend this class, because it is final
 */
public final class Repository {

	private static final Function<Node, String> noMethod = node -> node.toString() + " does not have an exposed getChildren function. Not Considering it in Price calculations";

	/**
	 A reference to the controller class, that is setting everything up
	 */
	public final static UI UI = Main.fxml.getController();

	/** This is a dirty way of forcing java to load all classes once, so we can use {@link Moebel#PRESETS} later */
	public final static ObservableList<? extends Moebel> Presets = FXCollections.observableArrayList(NOMADE.get(), KOMFORT.get(), DELUXE.get(), ADMIRAL.get(), MONTECARLO.get(), LEFRANCE.get(), LEPETITFRANCE.get(), WIKINGER.get(), BILLY.get(), SchrankWand.builder("BigBilly", 0));

}
