package com.Moebel;

import com.UI.RootPane;
import javafx.application.Platform;

public interface Cost {
	double[] totalCost = {0};

	default void add(){
		totalCost[0]+=cost();
		RootPane.updatePrice();
		Platform.runLater(this::update);
	}

	default void remove(){
		totalCost[0]-=cost();
		RootPane.updatePrice();
		Platform.runLater(this::update);
	}

	default void update(){
		totalCost[0]=
				RootPane.getInstances().stream().mapToDouble(                                                           //calculate (and later compound) all costs from all Panes together. That cost is calculated below
						rootPane -> rootPane.getChildrenUnmodifiable().filtered(node -> node instanceof Cost)
								.stream().mapToDouble(node -> ((Cost)node).cost()).sum()                                //Compound all cost from all Moebel instances in that RootPane
				).sum();
		RootPane.updatePrice();
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
