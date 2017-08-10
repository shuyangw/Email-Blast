package gui;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class NewTaskWindow {
    private Stage dialogStage;
    private Stage primaryStage;

    public NewTaskWindow(Stage primaryStage){
        this.dialogStage = new Stage();
        this.primaryStage = primaryStage;
        this.showPrompt(dialogStage, primaryStage);
    }

    public void showNewTaskWindow(){
        this.showPrompt(dialogStage, primaryStage);
    }

    public Stage getDialogStage(){
        return this.dialogStage;
    }

    private void showPrompt(Stage dialogStage, Stage primaryStage){
        //Initializes the dialog stage
        dialogStage.setResizable(false);
        dialogStage.setTitle("New Task");

        //Initializes grid pane
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Creates new scene and shows the dialog
        dialogStage.setScene(new Scene(gridPane, 320, 200));
        dialogStage.show();
    }
}
