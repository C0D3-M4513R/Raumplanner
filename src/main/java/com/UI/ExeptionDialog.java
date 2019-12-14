package com.UI;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 A class to display exceptions Properly onScreen
 */
public class ExeptionDialog extends Alert {
    public ExeptionDialog(Alert.AlertType type, Throwable throwable){
        super(type);
        setTitle("An error Occurred");
        setHeaderText("Please put only numbers in the input field");
        setContentText("Were there multiple ");

        //Get exeption as a String
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String exceptionText = sw.toString();

        //Setup the Gui
        Label label = new Label("The exception stacktrace was:");
        //Prepare a TextArea for the Exception to be displayed in
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        //Set Scaling options
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        //Pack it all into a Parent
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        // Set expandable Exception into the dialog pane.
        getDialogPane().setExpandableContent(expContent);
    }
}
