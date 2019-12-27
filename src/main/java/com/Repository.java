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

	/** This is a dirty way of forcing java to load all classes once, so we can use {@link Moebel#getPRESETS()} later */
	public final static ObservableList<? extends Moebel> Presets = FXCollections.observableArrayList(NOMADE.get(), KOMFORT.get(), DELUXE.get(), ADMIRAL.get(), MONTECARLO.get(), LEFRANCE.get(), LEPETITFRANCE.get(), WIKINGER.get(), BILLY.get(), SchrankWand.builder("BigBilly", 0));







//	While this is functional, it takes wayyy to long
//	Maybe in a separate thread..
//	Found a better way

//	/**
//	 This observable tracks all potential changes in children, that could be visible (we never hide Furniture or groups,
//	 just {@link com.UI.Menu.Selection})<br>
//	 since getChildren is already an observable there is no need to set up a complex structure, because it already exists
//	 and we can just attach ourselves, recalculate the cost, when
//	 */
/*	public final static ObservableValueBase<String> price = new ObservableValueBase<String>() {
		@Override
		public String getValue() {
//			final int[] tmp = {0};

			//get all elements with children, and compound th
//			RootPane.getInstances().forEach(
//					pane ->
//					{
//						//tmp[0] +=
//							pane.getChildren().forEach(node -> {if(node instanceof Cost) tmp[0] += ((Cost) node).cost();});
//							//get all children, that actually have a cost
//
//
//									// this gets slow really quick, so use another implementation .stream().mapToDouble(node -> ((Cost) node).cost()).sum(); //compound their cost together
//								//.forEach(node -> tmp[0]+=((Cost)node).cost());
//						//add the result to a tmp variable
//						//if any list of children changes, i.e. a child got added or removed, we will have another value
//						pane.getChildren().addListener((InvalidationListener) observable -> fireValueChangedEvent());
//					}
//			);

			double tmp = Cost.costs.stream().mapToDouble(Cost::cost).sum();
			tmp = tmp<0?0:tmp;
			return "Price: " + tmp;
		}

		public void fireValueChanged() {
			fireValueChangedEvent();
		}
	};*/

}
