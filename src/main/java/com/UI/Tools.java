package com.UI;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;

public class Tools extends Region {

	public Tools(){super();}

	public void recursiveRelocate(double x, double y) {
		System.out.println("Recursive Relcoate run ");
		relocate(x, y);
		getChildren().forEach(Node->Node.relocate(x,y));
	}

}
