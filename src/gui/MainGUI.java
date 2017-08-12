package gui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import structures.TaskSettings;

import java.util.HashMap;
import java.util.ArrayList;

public class MainGUI {
    private static BorderPane borderPane;
    private Stage primaryStage;
    private VBox topBox;
    private VBox leftBox;
    private HBox botBox;
    private GridPane midBox;

    private TaskList taskList;
    private boolean taskSelected;
    private Object selectedTask;

    private final int RIGHT_TRANSLATE = 5;
    private final int VERTICAL_TRANSLATE = 5;

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
        this.midBox = new GridPane();
        this.midBox.setVgap(VERTICAL_TRANSLATE);
        this.midBox.setHgap(RIGHT_TRANSLATE);

        //Setup toolbar up top
        Toolbar bar = new Toolbar(primaryStage, this);
        topBox.getChildren().addAll(bar.getBar());
        this.taskSelected = false;

        //Setup task list and the new task and delete buttons underneath
        this.setupListElements();

        //Setup mid box with Task settings
        this.setupSettings();

        //Setup scene and shows the application
        borderPane.setTop(topBox);
        borderPane.setLeft(leftBox);
        borderPane.setBottom(botBox);
        borderPane.setCenter(midBox);
        Scene scene = new Scene(borderPane, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public HashMap<String, TaskSettings> makeSettings(){
        HashMap<String, TaskSettings> nameSettingsPairs = new HashMap<>();
        for(String x: this.taskList.getItems()){
            nameSettingsPairs.put(x, null);
        }
        return nameSettingsPairs;
    }

    //TODO: COMPLETE WHEN ATTRIBUTES OF EACH TASK IS FINISHED
    public void receiveSettings(HashMap<String, TaskSettings> settings){
        for(String key: settings.keySet()){
            System.out.println(key + " " + settings.get(key));
        }
    }

    public VBox getPaneLeft(){
        return leftBox;
    }

    public TaskList getTaskList(){
        return this.taskList;
    }

    private void setupListElements(){
        this.taskList = new TaskList(primaryStage, this);
        taskList.initializeList();
        taskList.getListView().setOnMouseClicked(evt -> {
            if(!taskSelected){
                selectedTask
                        = taskList.getListView().getSelectionModel().getSelectedItem();
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

        Button newTaskButton = new Button("New");
        newTaskButton.setPrefSize(75, 35);
        newTaskButton.setOnAction(evt -> this.taskList.addToTaskList("Unnamed"));

        Button deleteTaskButton = new Button("Delete");
        deleteTaskButton.setPrefSize(75, 35);
        deleteTaskButton.setOnAction(evt -> {
            if(this.taskSelected && taskList.getSize() != 0){
                Object selectedTask
                        = taskList.getListView().getSelectionModel().getSelectedItem();
                taskList.removeFromTaskList(selectedTask.toString());
                if(taskList.getSize() == 0){
                    this.taskSelected = false;
                }
            }
        });
        botBox.getChildren().addAll(newTaskButton, deleteTaskButton);
    }

    private void setupSettings(){
        final Text senderBoxDescription = new Text("Sender:");
        this.layoutCorrection(senderBoxDescription);
        GridPane.setConstraints(senderBoxDescription, 0, 0);

        final TextField senderTextField = new TextField();
        this.layoutCorrection(senderTextField);
        GridPane.setConstraints(senderTextField, 1, 0);

        final Text recipientBoxDescription = new Text("Recipients:");
        this.layoutCorrection(recipientBoxDescription);
        GridPane.setConstraints(recipientBoxDescription, 0, 1);

        final TextField recipientTextField = new TextField();
        this.layoutCorrection(recipientTextField);
        GridPane.setConstraints(recipientTextField, 1, 1);

        final Text contentDescription = new Text("Content:");
        this.layoutCorrection(contentDescription);
        contentDescription.setTranslateY(-85);
        GridPane.setConstraints(contentDescription, 0, 2);

        final TextField contentTextField = new TextField();
        this.layoutCorrection(contentTextField);
        contentTextField.setPrefHeight(200);
        GridPane.setConstraints(contentTextField, 1, 2);



        midBox.getChildren().addAll(
                senderBoxDescription, senderTextField, recipientBoxDescription,
                recipientTextField, contentDescription, contentTextField
        );
    }

    private void layoutCorrection(Node element){
        element.setTranslateX(RIGHT_TRANSLATE);
        element.setTranslateY(VERTICAL_TRANSLATE);
    }

    public static ArrayList<String> parseRecipients(String input){
        ArrayList<String> recipientList = new ArrayList<>();
        input = input.replaceAll(" ", "");
        String currentEmail = "";
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == '@'){
                while(i < input.length() && input.charAt(i) != ','){
                    currentEmail += input.charAt(i);
                    ++i;
                }
                recipientList.add(currentEmail);
                if(i == input.length()){
                    return recipientList;
                }
                else{
                    currentEmail = "";
                    ++i;
                }
            }
            currentEmail += input.charAt(i);
        }

        return recipientList;
    }
}
