package gui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import structures.TaskSettings;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;

public class MainGUI {
    private final int RIGHT_TRANSLATE = 5;
    private final int VERTICAL_TRANSLATE = 5;

    private Stage primaryStage;
    private VBox topBox;
    private VBox leftBox;
    private HBox botBox;
    private GridPane midBox;

    private TaskList taskList;
    private boolean taskSelected;
    private Object selectedTask;
    private boolean doNot;
    private HashMap<String, TaskSettings> settingsMap;

    private TextField nameTextField;
    private TextField senderTextField;
    private TextField recipientsTextField;
    private TextArea contentTextField;

    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;

        //Setup primary stage
        primaryStage.setTitle("Email Blast");
        primaryStage.setResizable(false);
        //TODO: REDO LATER WHEN IMPLEMENTING RUNNING WHILE CLOSED MECHANISM
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        //Setup border pane and VBox and HBox
        BorderPane borderPane = new BorderPane();
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
        this.setupInputListeners();


        //Setup final buttons on the bottom right
        this.setupFinalButtons();

        this.makeUneditable();

        this.settingsMap = this.makeSettings();

        //Setup scene and shows the application
        borderPane.setTop(topBox);
        borderPane.setLeft(leftBox);
        borderPane.setBottom(botBox);
        borderPane.setCenter(midBox);

        Scene scene = new Scene(borderPane, 500, 600);
        scene.getStylesheets().add("application/application.css");


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public HashMap<String, TaskSettings> makeSettings(){
        if(taskList.getItems().isEmpty()){
            return new HashMap<>();
        }

        HashMap<String, TaskSettings> nameSettingsPairs = new HashMap<>();
        for(String x: this.taskList.getItems()){
            TaskSettings currentTask = new TaskSettings();
            currentTask.setName(nameTextField.getText());
            currentTask.setSender(senderTextField.getText());
            currentTask.setRecipients(
                    parseRecipients(recipientsTextField.getText())
            );
            currentTask.setContent(contentTextField.getText());

            nameSettingsPairs.put(x.replaceAll("[^a-zA-Z0-9]",""), currentTask);
        }
        return nameSettingsPairs;
    }



    //TODO TODO TODO TODO TODO TODO TODO TODO
    private void makeSettingsFromOne(){
        String replacedString
                = this.selectedTask.toString().replaceAll("[^a-zA-Z0-9]","");
        TaskSettings currTask = settingsMap.get(replacedString);

        currTask.setName(
                this.nameTextField.getText()
        );
        currTask.setSender(
                this.senderTextField.getText()
        );
        currTask.setRecipients(
                parseRecipients(this.recipientsTextField.getText())
        );
        currTask.setContent(
                this.contentTextField.getText()
        );

        settingsMap.put(replacedString, currTask);
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
            this.makeEditable();
            if(!taskSelected){
                selectedTask
                        = taskList.getListView().getSelectionModel().getSelectedItem();
                taskSelected = true;
            }
            //There's a weird exception here that really bothers me but idk how to fix
            else{
                try{
                    //In this if statement, cursed code
                    if(taskList.getSize() != 0){
                        if(taskList.getListView().getSelectionModel().getSelectedItem().equals(selectedTask)){
                            //Selects a null element so that the program
                            //un-selects a selected element
                            taskList.getListView().getSelectionModel().select(-1);
                            taskSelected = false;
                        }
                        else{
                            taskSelected = true;
                            selectedTask
                                    = taskList.getListView().getSelectionModel().getSelectedItem();

                            TaskSettings currSettings
                                    = this.settingsMap.get(selectedTask.toString().replaceAll("[^a-zA-Z0-9]",""));
                            doNot = true;
                            this.clearFields();
                            this.loadFieldText(selectedTask.toString().replaceAll("[^a-zA-Z0-9]",""));
                            doNot = false;
                        }
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

    private void loadFieldText(String fieldName){
        fieldName = fieldName.replaceAll("[^a-zA-Z0-9]", "");
        if(!this.settingsMap.containsKey(fieldName)){
            return;
        }

        TaskSettings currentSettings = this.settingsMap.get(fieldName);
        this.nameTextField.setText(currentSettings.getName());
        this.senderTextField.setText(currentSettings.getSender());
        this.recipientsTextField.setText(currentSettings.getRecipientsText());
        this.contentTextField.setText(currentSettings.getContent());
    }

    private void setupSettings(){
        final Text nameDescription = new Text("Name");
        this.layoutCorrection(nameDescription);
        GridPane.setConstraints(nameDescription, 0, 0);
        this.nameTextField = new TextField();
        this.layoutCorrection(nameTextField);
        this.nameTextField.setPrefWidth(200);
        GridPane.setConstraints(nameTextField, 1, 0);

        final Text senderBoxDescription = new Text("Sender:");
        this.layoutCorrection(senderBoxDescription);
        GridPane.setConstraints(senderBoxDescription, 0, 1);
        this.senderTextField = new TextField();
        this.senderTextField.setPrefWidth(200);
        this.layoutCorrection(senderTextField);
        GridPane.setConstraints(senderTextField, 1, 1);

        final Text recipientBoxDescription = new Text("Recipients:");
        this.layoutCorrection(recipientBoxDescription);
        GridPane.setConstraints(recipientBoxDescription, 0, 2);
        this.recipientsTextField = new TextField();
        this.recipientsTextField.setPrefWidth(200);
        this.layoutCorrection(recipientsTextField);
        GridPane.setConstraints(recipientsTextField, 1, 2);

        final Text contentDescription = new Text("Content:");
        this.layoutCorrection(contentDescription);
        contentDescription.setTranslateY(-85);
        GridPane.setConstraints(contentDescription, 0, 3);
        this.contentTextField = new TextArea();
        this.contentTextField.setPrefWidth(200);
        contentTextField.setPrefHeight(200);
        this.layoutCorrection(contentTextField);
        GridPane.setConstraints(contentTextField, 1, 3);


        midBox.getChildren().addAll(
                nameDescription, nameTextField, senderBoxDescription,
                senderTextField, recipientBoxDescription, recipientsTextField,
                contentDescription, contentTextField
        );
    }

    private void layoutCorrection(Node element){
        element.setTranslateX(RIGHT_TRANSLATE);
        element.setTranslateY(VERTICAL_TRANSLATE);
    }

    private void setupFinalButtons(){
        Button applyButton = new Button("Apply");
        applyButton.setPrefSize(65, 25);
        applyButton.setTranslateX(210);
        applyButton.setOnAction(evt -> this.makeSaved());

        Button clearButton = new Button("Clear");
        clearButton.setPrefSize(65, 25);
        clearButton.setTranslateX(220);
        clearButton.setOnAction(evt -> this.clearFieldsWithWarning());

        botBox.getChildren().addAll(applyButton, clearButton);
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

    private boolean areFieldsEmpty(){
        boolean nameFieldEmpty = nameTextField.getText().isEmpty();
        boolean senderTextFieldEmpty = senderTextField.getText().isEmpty();
        boolean recipientsTextFieldEmpty = recipientsTextField.getText().isEmpty();
        boolean contentTextFieldEmpty = contentTextField.getText().isEmpty();

        return nameFieldEmpty && senderTextFieldEmpty
                && recipientsTextFieldEmpty && contentTextFieldEmpty;
    }

    private void clearFieldsWithWarning(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning");
        alert.setContentText("Are you sure you want to clear all inputs?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            nameTextField.clear();
            senderTextField.clear();
            recipientsTextField.clear();
            contentTextField.clear();
        }
    }

    private void clearFields(){
        nameTextField.clear();
        senderTextField.clear();
        recipientsTextField.clear();
        contentTextField.clear();
    }

    private void setupInputListeners(){
        nameTextField.textProperty().addListener(e -> {
            if(doNot){
                return;
            }
            this.makeUnsaved();
            this.makeSettingsFromOne();
        });
        senderTextField.textProperty().addListener(e -> {
            if(doNot){
                return;
            }
            this.makeUnsaved();
            this.makeSettingsFromOne();
        });
        recipientsTextField.textProperty().addListener(e -> {
            if(doNot){
                return;
            }
            this.makeUnsaved();
            this.makeSettingsFromOne();
        });
        contentTextField.textProperty().addListener(e -> {
            if(doNot){
                return;
            }
            this.makeUnsaved();
            this.makeSettingsFromOne();
        });
    }

    private void makeUnsaved(){
        if(!taskSelected){
            return;
        }

        String selectedString = this.selectedTask.toString();
        int index = this.taskList.getItems().indexOf(selectedString);
        if(index == -1){
            return;
        }

        if(this.taskList.getItems().get(index).charAt(selectedString.length() - 1) == '*'){
            return;
        }
        this.taskList.getItems().set(index, selectedString+"*");
        this.refreshSelectedTask();
    }

    private void makeSaved(){
        if(!taskSelected){
            return;
        }

        String selectedString = this.selectedTask.toString();
        int index = this.taskList.getItems().indexOf(selectedString);
        if(index == -1){
            return;
        }
        if(this.taskList.getItems().get(index).charAt(selectedString.length() - 1) != '*'){
            return;
        }
        this.taskList.getItems().set(
                index, selectedString.substring(0, selectedString.length() - 1));
        this.refreshSelectedTask();
    }

    private void refreshSelectedTask(){
        this.selectedTask
                = taskList.getListView().getSelectionModel().getSelectedItem();
    }

    //Assigns light grey CSS coloring to input fields and makes them uneditable
    private void makeUneditable(){
        nameTextField.setEditable(false);
        nameTextField.setStyle("-fx-background-color: #D3D3D3;");
        senderTextField.setEditable(false);
        senderTextField.setStyle("-fx-background-color: #D3D3D3;");
        recipientsTextField.setEditable(false);
        recipientsTextField.setStyle("-fx-background-color: #D3D3D3;");
        contentTextField.getStyleClass().add("application/application.css");
        contentTextField.setEditable(false);
    }

    //Clears CSS styling for input fields and makes them editable
    private void makeEditable(){
        nameTextField.setEditable(true);
        nameTextField.setStyle(null);
        senderTextField.setEditable(true);
        senderTextField.setStyle(null);
        recipientsTextField.setEditable(true);
        recipientsTextField.setStyle(null);
        contentTextField.setEditable(true);
        contentTextField.getStyleClass().clear();
        contentTextField.setStyle(null);
    }

    private void printAllSenders(){
        for(String x: this.settingsMap.keySet()){
            System.out.println(x + " " + this.settingsMap.get(x).getName());
        }
        System.out.println("------------");
    }
}
