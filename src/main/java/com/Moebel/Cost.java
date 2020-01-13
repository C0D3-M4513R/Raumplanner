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

	double total = 0.0;

	default void add() {
		totalCost.add(cost());
		Platform.runLater(this::update);
	}

	default void remove() {
		totalCost.add(-cost());
		Platform.runLater(this::update);
	}

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
	 @return Returns the total cost of putting the furniture pice in
	 */
	double cost();
	//TODO: Make the prices more realistic
}
