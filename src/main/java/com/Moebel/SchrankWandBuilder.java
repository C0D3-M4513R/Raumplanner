package com.Moebel;

import com.UI.ExeptionDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class SchrankWandBuilder {
    public static SchrankWand SchrankWandBuilder(String name){
        final SchrankWand[] sw = new SchrankWand[1];
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input the number of Billies you want");
        dialog.setHeaderText("Input the number of Billies you want");
        dialog.setContentText("Please input the number of Billies you would like in BigBilly:");
        dialog.showAndWait().ifPresent(input->{
            try {
                int no = Integer.parseInt(input);
                sw[0] = new SchrankWand(name,no);
            }catch (Throwable throwable){
                ExeptionDialog ex = new ExeptionDialog(Alert.AlertType.ERROR,throwable);
                ex.show();
            }
        });
        return sw[0];
    }
}
