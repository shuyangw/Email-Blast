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
    private final int VERTICAL_TRANSLATE = 8;

    private Stage primaryStage;
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
    private CheckBox oneTimeCheckBox;
    private TextField atDateTextField;
    private TextField atTimeTextField;
    private CheckBox repeatCheckBox;
    private TextField fromDateTextField;
    private TextField fromTimeTextField;
    private TextField freqMinTextField;
    private TextField freqHoursTextField;

    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;

        //Setup primary stage
        primaryStage.setTitle("Email Blast");
        primaryStage.setResizable(false);
        //TODO: REDO LATER WHEN IMPLEMENTING RUNNING WHILE CLOSED MECHANISM
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        //Setup border pane and VBox and HBox
        BorderPane borderPane = new BorderPane();
        VBox topBox = new VBox();
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
        //Row 0 - Name field
        final Text nameDescription = new Text("Name");
        this.layoutCorrection(nameDescription);
        GridPane.setConstraints(nameDescription, 0, 0);

        this.nameTextField = new TextField();
        this.layoutCorrection(nameTextField);
        this.nameTextField.setPrefWidth(250);
        GridPane.setConstraints(nameTextField, 1, 0);

        //Row 1 - Sender field
        final Text senderBoxDescription = new Text("Sender:");
        this.layoutCorrection(senderBoxDescription);
        GridPane.setConstraints(senderBoxDescription, 0, 1);

        this.senderTextField = new TextField();
        this.senderTextField.setPrefWidth(250);
        this.layoutCorrection(senderTextField);
        GridPane.setConstraints(senderTextField, 1, 1);

        //Row 2 - Recipients field
        final Text recipientBoxDescription = new Text("Recipients:");
        this.layoutCorrection(recipientBoxDescription);
        GridPane.setConstraints(recipientBoxDescription, 0, 2);

        this.recipientsTextField = new TextField();
        this.recipientsTextField.setPrefWidth(250);
        this.layoutCorrection(recipientsTextField);
        GridPane.setConstraints(recipientsTextField, 1, 2);

        //Row 3 - Content field
        final Text contentDescription = new Text("Content:");
        this.layoutCorrection(contentDescription);
        contentDescription.setTranslateY(-85);
        GridPane.setConstraints(contentDescription, 0, 3);

        this.contentTextField = new TextArea();
        this.contentTextField.setPrefWidth(250);
        contentTextField.setPrefHeight(200);
        this.layoutCorrection(contentTextField);
        GridPane.setConstraints(contentTextField, 1, 3);

        //Row 4 - One time checkbox
        final Text oneTimeDescription = new Text("One time:");
        this.layoutCorrection(oneTimeDescription);
        GridPane.setConstraints(oneTimeDescription, 0, 4);

        this.oneTimeCheckBox = new CheckBox();
        this.layoutCorrection(oneTimeCheckBox);
        GridPane.setConstraints(oneTimeCheckBox, 1, 4);

        //Row 5 - If one time, select date and time
        final Text atTime = new Text("At");
        this.layoutCorrection(atTime);
        GridPane.setConstraints(atTime, 0, 5);

        final Text atDateInputDescription = new Text("Date:");
        this.layoutCorrection(atDateInputDescription);
        GridPane.setConstraints(atDateInputDescription, 1, 5);

        this.atDateTextField = new TextField();
        this.layoutCorrection(atDateTextField);
        atDateTextField.setPrefWidth(50);
        atDateTextField.setTranslateX(-150);
        GridPane.setConstraints(atDateTextField, 2, 5);

        final Text atTimeInputDescription = new Text("Time:");
        this.layoutCorrection(atTimeInputDescription);
        atTimeInputDescription.setTranslateX(-150);
        GridPane.setConstraints(atTimeInputDescription, 3, 5);

        this.atTimeTextField = new TextField();
        this.layoutCorrection(atTimeTextField);
        atTimeTextField.setPrefWidth(50);
        atTimeTextField.setTranslateX(-155);
        GridPane.setConstraints(atTimeTextField, 4, 5);

        //Row 6 - Repeating checkbox
        final Text repeatingDescription = new Text("Repeating:");
        this.layoutCorrection(repeatingDescription);
        GridPane.setConstraints(repeatingDescription, 0, 6);

        this.repeatCheckBox = new CheckBox();
        this.layoutCorrection(repeatCheckBox);
        GridPane.setConstraints(repeatCheckBox, 1, 6);

        //Row 7 - If repeating, start from date/time
        Text fromTime = new Text("From");
        this.layoutCorrection(fromTime);
        GridPane.setConstraints(fromTime, 0, 7);

        final Text fromDateInputDescription = new Text("Date:");
        this.layoutCorrection(fromDateInputDescription);
        GridPane.setConstraints(fromDateInputDescription, 1, 7);

        this.fromDateTextField = new TextField();
        this.layoutCorrection(fromDateTextField);
        fromDateTextField.setPrefWidth(50);
        fromDateTextField.setTranslateX(-150);
        GridPane.setConstraints(fromDateTextField, 2, 7);

        final Text fromTimeInputDescription = new Text("Time:");
        this.layoutCorrection(fromTimeInputDescription);
        fromTimeInputDescription.setTranslateX(-150);
        GridPane.setConstraints(fromTimeInputDescription, 3, 7);

        this.fromTimeTextField = new TextField();
        this.layoutCorrection(fromTimeTextField);
        fromTimeTextField.setPrefWidth(50);
        fromTimeTextField.setTranslateX(-155);
        GridPane.setConstraints(fromTimeTextField, 4, 7);

        //Row 8 - If repeating, declares frequency
        final Text frequencyDescription = new Text("Frequency");
        this.layoutCorrection(frequencyDescription);
        GridPane.setConstraints(frequencyDescription, 0, 8);

        final Text minutesFreqInput = new Text("Minutes:");
        this.layoutCorrection(minutesFreqInput);
        GridPane.setConstraints(minutesFreqInput, 1, 8);

        this.freqMinTextField = new TextField();
        this.layoutCorrection(freqMinTextField);
        freqMinTextField.setPrefWidth(100);
        freqMinTextField.setTranslateX(-130);
        GridPane.setConstraints(freqMinTextField, 2, 8);

        final Text hoursFreqInput = new Text("Hours:");
        this.layoutCorrection(hoursFreqInput);
        hoursFreqInput.setTranslateX(-130);
        GridPane.setConstraints(hoursFreqInput, 3, 8);

        this.freqHoursTextField = new TextField();
        this.layoutCorrection(freqHoursTextField);
        freqHoursTextField.setPrefWidth(100);
        freqHoursTextField.setTranslateX(-130);
        GridPane.setConstraints(freqHoursTextField, 4, 8); 

        midBox.getChildren().addAll(
                nameDescription, nameTextField, senderBoxDescription,
                senderTextField, recipientBoxDescription, recipientsTextField,
                contentDescription, contentTextField, oneTimeDescription,
                oneTimeCheckBox, atTime, atDateInputDescription,
                atDateTextField, atTimeInputDescription, atTimeTextField,
                repeatingDescription, repeatCheckBox, fromTime,
                fromDateInputDescription, fromDateTextField,
                fromTimeInputDescription, fromTimeTextField, frequencyDescription,
                minutesFreqInput, freqMinTextField, hoursFreqInput, freqHoursTextField
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

    //Clears every input field with a warning dialog
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

    //Clears every input field without warning
    private void clearFields(){
        nameTextField.clear();
        senderTextField.clear();
        recipientsTextField.clear();
        contentTextField.clear();
    }

    //Sets up the listeners for changes in user input settings
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

        oneTimeCheckBox.setDisable(true);

        this.disableOneTime();

        repeatCheckBox.setDisable(true);

        this.disableRepeating();
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

        oneTimeCheckBox.setDisable(false);

        this.enableOneTime();

        repeatCheckBox.setDisable(false);

        this.enableRepeating();
    }

    private void disableOneTime(){
        atDateTextField.setEditable(false);
        atDateTextField.setStyle("-fx-background-color: #D3D3D3;");

        atTimeTextField.setEditable(false);
        atTimeTextField.setStyle("-fx-background-color: #D3D3D3;");
    }

    private void enableOneTime(){
        atDateTextField.setEditable(true);
        atDateTextField.setStyle(null);

        atTimeTextField.setEditable(true);
        atTimeTextField.setStyle(null);
    }

    private void disableRepeating(){
        fromDateTextField.setEditable(false);
        fromDateTextField.setStyle("-fx-background-color: #D3D3D3;");

        fromTimeTextField.setEditable(false);
        fromTimeTextField.setStyle("-fx-background-color: #D3D3D3;");

        freqMinTextField.setEditable(false);
        freqMinTextField.setStyle("-fx-background-color: #D3D3D3;");

        freqHoursTextField.setEditable(false);
        freqHoursTextField.setStyle("-fx-background-color: #D3D3D3;");
    }

    private void enableRepeating(){
        fromDateTextField.setEditable(true);
        fromDateTextField.setStyle(null);

        fromTimeTextField.setEditable(true);
        fromTimeTextField.setStyle(null);

        freqMinTextField.setEditable(true);
        freqMinTextField.setStyle(null);

        freqHoursTextField.setEditable(true);
        freqHoursTextField.setStyle(null);
    }
}
