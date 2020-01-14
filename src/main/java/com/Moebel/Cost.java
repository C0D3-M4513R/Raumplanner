package com.Moebel;

import com.UI.RootPane;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

public interface Cost {
	DoubleProperty totalCost = new DoublePropertyBase() {
		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "Total Cost: ";
		}
	};

	/**
	 Add the price of the Current Node.
	 */
	default void add() {
		totalCost.add(cost());
		Platform.runLater(this::update);
	}

	/**
	 Remove the price of the Current Node.
	 */
	default void remove() {
		totalCost.subtract(cost());
		Platform.runLater(this::update);
	}

	/**
	 Recalculates the Price from scratch
	 @deprecated Note: in the newer java versions this would be private
	 */
	default void update() {
		totalCost.set(
				RootPane.getInstances().stream().mapToDouble(                                                           //calculate (and later compound) all costs from all Panes together. That cost is calculated below
						rootPane -> rootPane.getChildrenUnmodifiable().filtered(node -> node instanceof Cost)
								.stream().mapToDouble(node -> ((Cost) node).cost()).sum()                                //Compound all cost from all Moebel instances in that RootPane
				).sum());
	}

	/**
	 A number, of how much is paid per hour
	 */
	int hourlyCost = 30;

	/**
	 @return Returns the total cost of making this Moebel
	 */
	double getCostMoebel();
	/**
	 @return Returns the total cost of putting the furniture pice in
	 */
	double cost();
}
