package gui;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;


public class AlertManager {
    private final int MAX_ARGS_SIZE = 3;

    public boolean booleanAlert(AlertType type, String ... args){
        Alert alert = new Alert(type);
        if(args.length != MAX_ARGS_SIZE){
            System.err.println("ALERT INPUT SIZE NOT 3!");
            return false;
        }
        alert.setTitle(args[0]);
        alert.setHeaderText(args[1]);
        alert.setContentText(args[2]);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void waitAlert(AlertType type, String ... args){
        Alert alert = new Alert(type);
        if(args.length != MAX_ARGS_SIZE){
            System.err.println("ALERT INPUT SIZE NOT 3!");
            return;
        }
        alert.setTitle(args[0]);
        alert.setHeaderText(args[1]);
        alert.setContentText(args[2]);
        alert.showAndWait();
    }


}
