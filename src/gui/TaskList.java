package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TaskList {
    private final ListView taskList = new ListView();
    private Stage primaryStage;
    private MainGUI mainGUI;

    public TaskList(Stage primaryStage, MainGUI mainGUI){
        this.primaryStage = primaryStage;
        this.mainGUI = mainGUI;
    }

    public void initializeList(){
        taskList.setPrefSize(150, 550);
        taskList.setEditable(true);

        ObservableList<String> items = FXCollections.observableArrayList("ITEM");
        taskList.setItems(items);
        this.mainGUI.getPaneLeft().getChildren().add(taskList);
    }

    public ListView getListView(){
        return this.taskList;
    }
}
