package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import structures.TaskSettings;

public class MainGUI {
    private static BorderPane borderPane;
    private Stage primaryStage;
    private VBox topBox;
    private VBox leftBox;
    private HBox botBox;

    private TaskList taskList;
    private boolean taskSelected;
    private Object selectedTask;

    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;

        //Setup primary stage
        primaryStage.setTitle("Email Blast");
        primaryStage.setResizable(false);
        //TODO: REDO LATER WHEN IMPLEMENTING RUNNING WHILE CLOSED MECHANISM
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        //Setup border pane and VBox and HBox
        this.borderPane = new BorderPane();
        this.topBox = new VBox();
        this.leftBox = new VBox();
        this.botBox = new HBox();

        //Setup toolbar up top
        Toolbar bar = new Toolbar(primaryStage, this);
        topBox.getChildren().addAll(bar.getBar());
        this.taskSelected = false;

        //Setup task list and the new task button underneath
        this.taskList = new TaskList(primaryStage, this);
        taskList.initializeList();
        taskList.getListView().setOnMouseClicked(evt -> {
            if(!taskSelected){
                selectedTask = taskList.getListView().getSelectionModel().getSelectedItem();
                taskSelected = true;
            }
            //There's a weird exception here that really bothers me but idk how to fix
            else{
                try{
                    //In this if statement, cursed code
                    if(taskList.getSize() != 0
                            && taskList.getListView().getSelectionModel().getSelectedItem().equals(selectedTask)){
                        taskList.getListView().getSelectionModel().select(null);
                        taskSelected = false;
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Setup buttons underneath the list
        Button newTaskButton = new Button("New");
        newTaskButton.setPrefSize(75, 35);
        newTaskButton.setOnAction(evt -> {
           this.taskList.addToTaskList("Unnamed");
        });
        Button deleteTaskButton = new Button("Delete");
        deleteTaskButton.setPrefSize(75, 35);
        deleteTaskButton.setOnAction(evt -> {
            if(this.taskSelected && taskList.getSize() != 0){
                Object selectedTask = taskList.getListView().getSelectionModel().getSelectedItem();
                taskList.removeFromTaskList(selectedTask.toString());
                if(taskList.getSize() == 0){
                    this.taskSelected = false;
                }
            }
        });
        botBox.getChildren().addAll(newTaskButton, deleteTaskButton);

        //Setup scene and shows the application
        borderPane.setTop(topBox);
        borderPane.setLeft(leftBox);
        borderPane.setBottom(botBox);
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadSettings(TaskSettings settings){
        //TODO
    }

    public VBox getPaneLeft(){
        return leftBox;
    }

    public TaskList getTaskList(){
        return this.taskList;
    }
}
