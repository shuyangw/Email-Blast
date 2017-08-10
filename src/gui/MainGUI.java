package gui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGUI {
    private static BorderPane borderPane;
    private Stage primaryStage;
    private VBox topBox;
    private VBox leftBox;
    private HBox botBox;

    private boolean taskSelected;
    private Object selectedTask;

    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;

        //Setup primary stage
        primaryStage.setTitle("Email Blast");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        //Setup border pane and VBox and HBox
        this.borderPane = new BorderPane();
        this.topBox = new VBox();
        this.leftBox = new VBox();
        this.botBox = new HBox();

        //Setup toolbar up top
        Toolbar bar = new Toolbar(primaryStage, this);
        topBox.getChildren().addAll(bar.getBar());

        //Setup task list and the new task button underneath
        final TaskList taskList = new TaskList(primaryStage, this);
        taskList.initializeList();
        taskList.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!taskSelected){
                    selectedTask = taskList.getListView().getSelectionModel().getSelectedItem();
                    taskSelected = true;
                }
                else if(taskList.getListView().getSelectionModel().getSelectedItem().equals(selectedTask)){
                    taskList.getListView().getSelectionModel().select(null);
                    taskSelected = false;
                }
            }
        });

        //Setup buttons underneath the list
        Button newTaskButton = new Button("New");
        newTaskButton.setPrefSize(75, 35);
        /*  TODO:   ADD NEW TASK BUTTON FUNCTIONALITY
            TODO:   SHOULD BE SIMILAR TO Toolbar.java in makeBar()
         */
        Button deleteTaskbutton = new Button("Delete");
        deleteTaskbutton.setPrefSize(75, 35);

        botBox.getChildren().addAll(newTaskButton, deleteTaskbutton);

        //Setup scene and shows the application
        borderPane.setTop(topBox);
        borderPane.setLeft(leftBox);
        borderPane.setBottom(botBox);
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public VBox getPaneLeft(){
        return leftBox;
    }
}
