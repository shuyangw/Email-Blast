package gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class Toolbar {
    private Stage primaryStage;
    private MenuBar bar;

    public Toolbar(Stage primaryStage, MainGUI parent){
        this.primaryStage = primaryStage;
        this.makeBar();
    }

    public MenuBar getBar(){
        return this.bar;
    }

    private void makeBar(){
        Menu file = new Menu("File");
        MenuItem newTaskButton = new MenuItem("New Task");
        newTaskButton.setOnAction(evt1 -> {
            NewTaskWindow newTaskWindow = new NewTaskWindow(this.primaryStage);
            newTaskWindow.showNewTaskWindow();
            newTaskWindow.getDialogStage().setOnHiding(evt2 -> {
                //TODO: WHAT HAPPENS WHEN NEW TASK DIALOG IS FINISHED
            });
        });
        file.getItems().add(newTaskButton);

        Menu help = new Menu("Help");

        // Add Menu items here

        this.bar = new MenuBar(file, help);
    }
}
