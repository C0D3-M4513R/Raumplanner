package com.UI;

public class Group extends javafx.scene.Group {

	public Group(Group group){super(group);}

	public Group(){super();}

	public void recursiveRelocate(double x, double y) {
		System.out.println("Recursive Relcoate run ");
		relocate(x, y);
		getChildren().forEach(Node->Node.relocate(x,y));
	}

}
