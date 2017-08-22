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
    //Constants
    private final int RIGHT_TRANSLATE = 5;
    private final int VERTICAL_TRANSLATE = 8;
    private final int TYPE_TEXTFIELD = 1;
    private final int TYPE_TEXTAREA = 2;
    private final int TYPE_CHECKBOX = 3;

    //Regex for clearing asterisks
    private final String REGEX_AST = "[^a-zA-Z0-9]";
    private final String REGEX_SPACE = " ";

    private Stage primaryStage;
    private VBox leftBox;
    private HBox botBox;
    private GridPane midBox;

    private TaskList taskList;
    private boolean taskSelected;
    private Object selectedTask;
    private boolean doNot;
    private HashMap<String, TaskSettings> settingsMap;

    private ArrayList<Node> textFieldList;

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
    private CheckBox daemonCheckbox;
    private CheckBox cloudCheckBox;
    private CheckBox activeCheckBox;

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
        leftBox = new VBox();
        botBox = new HBox();
        midBox = new GridPane();
        midBox.setVgap(VERTICAL_TRANSLATE);
        midBox.setHgap(RIGHT_TRANSLATE);

        //Setup toolbar up top
        Toolbar bar = new Toolbar(primaryStage, this);
        topBox.getChildren().addAll(bar.getBar());
        taskSelected = false;

        //Setup task list and the new task and delete buttons underneath
        setupListElements();

        //Setup mid box with Task settings
        textFieldList = new ArrayList<>();
        setupSettings();
        setupInputListeners();

        //Setup final buttons on the bottom right
        setupFinalButtons();
        disableAll();

        settingsMap = makeSettings();

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
        for(String x: taskList.getItems()){
            TaskSettings currTask = new TaskSettings();
            currTask.setName(nameTextField.getText());
            currTask.setSender(senderTextField.getText());
            currTask.setRecipients(parseRecipients(recipientsTextField.getText()));
            currTask.setContent(contentTextField.getText());
            if(oneTimeCheckBox.isSelected() && !repeatCheckBox.isSelected()){
                currTask.setOneTimeOrNot(true);
            }
            else if(!oneTimeCheckBox.isSelected() && repeatCheckBox.isSelected()){
                currTask.setOneTimeOrNot(false);
            }
            else{
                currTask.setOneTimeOrNot(null);
            }
            currTask.setAtDate(atDateTextField.getText());
            currTask.setAtTime(atTimeTextField.getText());
            currTask.setFromDate(fromDateTextField.getText());
            currTask.setFromTime(fromTimeTextField.getText());
            currTask.setFrequencyHours(freqHoursTextField.getText());
            currTask.setFrequencyMinutes(freqMinTextField.getText());
            currTask.setDaemon(daemonCheckbox.isSelected());
            currTask.setCloud(cloudCheckBox.isSelected());
            currTask.setActive(activeCheckBox.isSelected());
            nameSettingsPairs.put(x.replaceAll(REGEX_AST,""), currTask);
        }
        return nameSettingsPairs;
    }

    private void makeSettingsFromOne(){
        refreshSelectedTask();
        if(selectedTask == null){
            System.err.println("selectedTask field is null!");
        }

        String replacedString
                = selectedTask.toString().replaceAll(REGEX_AST,"");
        TaskSettings currTask = settingsMap.get(replacedString);

        if(currTask == null){
            System.err.println("currTask field is null");
        }

        currTask.setName(nameTextField.getText());
        currTask.setSender(senderTextField.getText());
        currTask.setRecipients(parseRecipients(recipientsTextField.getText()));
        currTask.setContent(contentTextField.getText());
        if(oneTimeCheckBox.isSelected() && !repeatCheckBox.isSelected()){
            currTask.setOneTimeOrNot(true);
        }
        else if(!oneTimeCheckBox.isSelected() && repeatCheckBox.isSelected()){
            currTask.setOneTimeOrNot(false);
        }
        else{
            currTask.setOneTimeOrNot(null);
        }
        currTask.setAtDate(atDateTextField.getText());
        currTask.setAtTime(atTimeTextField.getText());
        currTask.setFromDate(fromDateTextField.getText());
        currTask.setFromTime(fromTimeTextField.getText());
        currTask.setFrequencyHours(freqHoursTextField.getText());
        currTask.setFrequencyMinutes(freqMinTextField.getText());
        currTask.setDaemon(daemonCheckbox.isSelected());
        currTask.setCloud(cloudCheckBox.isSelected());
        currTask.setActive(activeCheckBox.isSelected());

        settingsMap.put(replacedString, currTask);
    }

    private void loadFieldText(String fieldName){
        fieldName = fieldName.replaceAll(REGEX_AST, "");
        if(!settingsMap.containsKey(fieldName)){
            return;
        }
        TaskSettings currentSettings = settingsMap.get(fieldName);
        nameTextField.setText(currentSettings.getName());
        senderTextField.setText(currentSettings.getSender());
        recipientsTextField.setText(currentSettings.getRecipientsText());
        contentTextField.setText(currentSettings.getContent());
        if(currentSettings.getOneTimeOrNot() == null){
            oneTimeCheckBox.setSelected(false);
            repeatCheckBox.setSelected(false);
            disableOneTime();
            disableRepeating();
        }
        else if(currentSettings.getOneTimeOrNot()){
            oneTimeCheckBox.setSelected(true);
            repeatCheckBox.setSelected(false);
            enableOneTime();
            disableRepeating();
        }
        else if(!currentSettings.getOneTimeOrNot()){
            oneTimeCheckBox.setSelected(false);
            repeatCheckBox.setSelected(true);
            disableOneTime();
            enableRepeating();
        }
        atDateTextField.setText(currentSettings.getAtDate());
        atTimeTextField.setText(currentSettings.getAtTime());
        fromDateTextField.setText(currentSettings.getFromDate());
        fromTimeTextField.setText(currentSettings.getFromTime());
        freqHoursTextField.setText(currentSettings.getFrequencyHours());
        freqMinTextField.setText(currentSettings.getFrequencyMinutes());
        daemonCheckbox.setSelected(currentSettings.getDaemon());
        cloudCheckBox.setSelected(currentSettings.getCloud());
        activeCheckBox.setSelected(currentSettings.getActive());
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
        return taskList;
    }

    private void setupListElements(){
        taskList = new TaskList(primaryStage, this);
        taskList.initializeList();
        taskList.getListView().setOnMouseClicked(evt -> {
            enableAll();
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
                        if(selectedTask == null){
                            return;
                        }
                        if(taskList.getListView().getSelectionModel().getSelectedItem().equals(selectedTask)){
                            //Selects a null element so that the program
                            //un-selects a selected element
                            taskList.getListView().getSelectionModel().select(-1);
                            disableAll();
                            taskSelected = false;
                        }
                        else{
                            taskSelected = true;
                            selectedTask
                                    = taskList.getListView().getSelectionModel().getSelectedItem();
                            doNot = true;
                            clearFields();
                            loadFieldText(selectedTask.toString().replaceAll(REGEX_AST,""));
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
        newTaskButton.setOnAction(evt -> {
            taskList.addToTaskList("Unnamed");
            settingsMap.put("Unnamed", new TaskSettings());
        });

        Button deleteTaskButton = new Button("Delete");
        deleteTaskButton.setPrefSize(75, 35);
        deleteTaskButton.setOnAction(evt -> {
            if(taskSelected && taskList.getSize() != 0){
                Object selectedTask
                        = taskList.getListView().getSelectionModel().getSelectedItem();
                String taskListString = selectedTask.toString().replaceAll(REGEX_AST, "");
                taskList.removeFromTaskList(taskListString);
                settingsMap.remove(taskListString);
                if(taskList.getSize() == 0){
                    taskSelected = false;
                }
            }
            refreshSelectedTask();
            if(selectedTask == null){
                disableAll();
            }
        });
        botBox.getChildren().addAll(newTaskButton, deleteTaskButton);
    }

    private void correctElement(
            int type, Node element, int col, int row, int prefWidth, int prefHeight, int transX, int transY){
            layoutCorrection(element);
            GridPane.setConstraints(element, col, row);
            if(prefWidth != -1){
                ((Control)element).setPrefWidth(prefWidth);
            }
            if(prefHeight != -1){
                ((Control)element).setPrefHeight(prefHeight);
            }
            if(transX != 0){
                element.setTranslateX(transX);
            }
            if(transY != 0){
                element.setTranslateY(transY);
            }
            if(type == TYPE_TEXTFIELD){
                textFieldList.add(element);
            }
            midBox.getChildren().add(element);
    }

    private void setupSettings(){
        //Row 0 - Name field
        final Text nameDescription = new Text("Subject");
        int TYPE_TEXT = 4;
        correctElement(TYPE_TEXT, nameDescription, 0, 0, -1, -1, 0, 0);
        nameTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, nameTextField, 1, 0, 250, -1, 0, 0);

        //Row 1 - Sender field
        final Text senderBoxDescription = new Text("Sender:");
        correctElement(TYPE_TEXT, senderBoxDescription, 0, 1, -1, -1, 0, 0);
        senderTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, senderTextField, 1, 1, 250, -1, 0, 0);

        //Row 2 - Recipients field
        final Text recipientBoxDescription = new Text("Recipients:");
        correctElement(TYPE_TEXT, recipientBoxDescription, 0, 2, -1, -1,0 ,0);
        recipientsTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, recipientsTextField, 1, 2, 250, -1, 0, 0);

        //Row 3 - Content field
        final Text contentDescription = new Text("Content:");
        correctElement(TYPE_TEXT, contentDescription, 0, 3, -1, -1, 0, -85);
        contentTextField = new TextArea();
        correctElement(TYPE_TEXTAREA, contentTextField, 1, 3, 250, 200, 0 ,0);

        //Row 4 - One time checkbox
        final Text oneTimeDescription = new Text("One time:");
        correctElement(TYPE_TEXT, oneTimeDescription, 0, 4, -1, -1, 0, 0);
        oneTimeCheckBox = new CheckBox();
        correctElement(TYPE_CHECKBOX, oneTimeCheckBox, 1, 4, -1, -1 ,0 ,0);

        //Row 5 - If one time, select date and time
        final Text atTime = new Text("At");
        correctElement(TYPE_TEXT, atTime, 0, 5, -1, -1, 0, 0);
        final Text atDateInputDescription = new Text("Date:");
        correctElement(TYPE_TEXT, atDateInputDescription, 1, 5, -1, -1, 0, 0);
        atDateTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, atDateTextField, 2, 5, 50, -1, -140, 0);
        final Text atTimeInputDescription = new Text("Time:");
        correctElement(TYPE_TEXT, atTimeInputDescription, 3, 5, -1, -1, -140, 0);
        atTimeTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, atTimeTextField, 4, 5, 50, -1, -140, 0);

        //Row 6 - Repeating checkbox
        final Text repeatingDescription = new Text("Repeating:");
        correctElement(TYPE_TEXT, repeatingDescription, 0, 6, -1, -1, 0, 0);
        repeatCheckBox = new CheckBox();
        correctElement(TYPE_CHECKBOX, repeatCheckBox, 1, 6, -1, -1, 0, 0);

        //Row 7 - If repeating, start from date/time
        Text fromTime = new Text("From");
        correctElement(TYPE_TEXT, fromTime, 0, 7, -1, -1, 0, 0);
        final Text fromDateInputDescription = new Text("Date:");
        correctElement(TYPE_TEXT, fromDateInputDescription, 1, 7, -1, -1, 0, 0);
        fromDateTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, fromDateTextField, 2, 7, 50, 0, -140, 0);
        final Text fromTimeInputDescription = new Text("Time:");
        correctElement(TYPE_TEXT, fromTimeInputDescription, 3, 7, -1, -1, -140, 0);
        fromTimeTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, fromTimeTextField, 4, 7 , 50, -1, -140, 0);

        //Row 8 - If repeating, declares frequency
        final Text frequencyDescription = new Text("Frequency:");
        correctElement(TYPE_TEXT, frequencyDescription, 0, 8, -1, -1, 0, 0);
        final Text minutesFreqInput = new Text("Minutes:");
        correctElement(TYPE_TEXT, minutesFreqInput, 1, 8, -1, -1, 0, 0);
        freqMinTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, freqMinTextField, 2, 8, 100, -1, -120, 0);
        final Text hoursFreqInput = new Text("Hours:");
        correctElement(TYPE_TEXT, hoursFreqInput, 3, 8, -1, -1, -120, 0);
        freqHoursTextField = new TextField();
        correctElement(TYPE_TEXTFIELD, freqHoursTextField, 4, 8, 100, -1, -120, 0);

        //Row 9, 10 - Special settings
        final Text daemonDescription = new Text("Run when closed:");
        correctElement(TYPE_TEXT, daemonDescription, 0, 9, -1, -1, 0, 0);
        daemonCheckbox = new CheckBox();
        correctElement(TYPE_CHECKBOX, daemonCheckbox, 1, 9, -1, -1, 0, 0);
        final Text cloudDescription = new Text("Run on cloud:");
        correctElement(TYPE_TEXT, cloudDescription, 0, 10, -1, -1, 0, 0);
        cloudCheckBox = new CheckBox();
        correctElement(TYPE_CHECKBOX, cloudCheckBox, 1, 10, -1, -1, 0, 0);

        //Row 11 - Active or not
        final Text activeDescription = new Text("Active:");
        correctElement(TYPE_TEXT, activeDescription, 0, 11, -1, -1, 0, 0);
        activeCheckBox = new CheckBox();
        correctElement(TYPE_CHECKBOX, activeCheckBox, 1, 11, -1, -1, 0, 0);
    }

    private void layoutCorrection(Node element){
        element.setTranslateX(RIGHT_TRANSLATE);
        element.setTranslateY(VERTICAL_TRANSLATE);
    }

    private void setupFinalButtons(){
        Button applyButton = new Button("Apply");
        applyButton.setPrefSize(65, 25);
        applyButton.setTranslateX(210);
        applyButton.setOnAction(evt -> makeSaved());

        Button clearButton = new Button("Clear");
        clearButton.setPrefSize(65, 25);
        clearButton.setTranslateX(220);
        clearButton.setOnAction(evt -> clearFieldsWithWarning());

        botBox.getChildren().addAll(applyButton, clearButton);
    }

    public static ArrayList<String> parseRecipients(String input){
        ArrayList<String> recipientList = new ArrayList<>();
        input = input.replaceAll(" ", "");
        String currentEmail = "";
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == '@'){
                while(i < input.length() && input.charAt(i) != ','){
                    currentEmail += input.charAt(i++);
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
            for(Node element: textFieldList){
                ((TextField)element).clear();
            }
            contentTextField.clear();
            oneTimeCheckBox.setSelected(false);
            repeatCheckBox.setSelected(false);
        }
    }

    //Clears every input field without warning
    private void clearFields(){
        for(Node element: textFieldList){
            ((TextField)element).clear();
        }
        contentTextField.clear();
        oneTimeCheckBox.setSelected(false);
        repeatCheckBox.setSelected(false);
    }

    //Sets up the listeners for changes in user input settings
    private void setupInputListeners(){
        oneTimeCheckBox.selectedProperty().addListener(e -> {
            if(doNot){
                return;
            }
            if(repeatCheckBox.isSelected()){
                doNot = true;
                repeatCheckBox.setSelected(false);
                doNot = false;
            }
            enableOneTime();
            disableRepeating();
            makeUnsaved();
            makeSettingsFromOne();
        });
        repeatCheckBox.selectedProperty().addListener(e -> {
            if(doNot){
                return;
            }
            if(oneTimeCheckBox.isSelected()){
                doNot = true;
                oneTimeCheckBox.setSelected(false);
                doNot = false;
            }
            enableRepeating();
            disableOneTime();
            makeUnsaved();
            makeSettingsFromOne();
        });
        daemonCheckbox.selectedProperty().addListener(e -> {
            if(doNot){
                return;
            }
            makeUnsaved();
            makeSettingsFromOne();
        });
        cloudCheckBox.selectedProperty().addListener(e -> {
            if(doNot){
                return;
            }
            makeUnsaved();
            makeSettingsFromOne();
        });
        activeCheckBox.selectedProperty().addListener(e -> {
            if(doNot){
                return;
            }
            if(nameTextField.getText().isEmpty()){
                showErrorAlert("Subject field cannot be empty!");
                return;
            }
            else if(senderTextField.getText().isEmpty()){
                showErrorAlert("Semder field cannot be empty!");
                return;
            }
            else if(recipientsTextField.getText().isEmpty()){
                showErrorAlert("Recipients field cannot be empty!");
                return;
            }
            if(contentTextField.getText().isEmpty()){
                if(!showConfirmationAlert("Content field is empty, would you" +
                        " like to continue?")){
                    return;
                }
            }
            makeUnsaved();
            makeSettingsFromOne();
        });
        contentTextField.textProperty().addListener(e -> {
            if(doNot){
                return;
            }
            makeUnsaved();
            makeSettingsFromOne();
        });
        for(Node element: textFieldList){
            ((TextField)element).textProperty().addListener(e -> {
                if(doNot){
                    return;
                }
                makeUnsaved();
                makeSettingsFromOne();
            });
        }
    }

    private void makeUnsaved(){
        if(!taskSelected){
            return;
        }
        refreshSelectedTask();
        String selectedString = "";
        try{
            selectedString = selectedTask.toString();
        }
        catch(NullPointerException e){
            showErrorAlert("Please select a task before editing");
            return;
        }

        int index = taskList.getItems().indexOf(selectedString);
        if(index == -1){
            return;
        }

        if(taskList.getItems().get(index).charAt(selectedString.length() - 1) == '*'){
            return;
        }
        taskList.getItems().set(index, selectedString+"*");
        refreshSelectedTask();
    }



    private void makeSaved(){
        if(!taskSelected){
            return;
        }

        String selectedString = selectedTask.toString();
        int index = taskList.getItems().indexOf(selectedString);
        if(index == -1){
            return;
        }
        if(taskList.getItems().get(index).charAt(selectedString.length() - 1) != '*'){
            return;
        }
        taskList.getItems().set(
                index, selectedString.substring(0, selectedString.length() - 1));
        refreshSelectedTask();
    }

    private void refreshSelectedTask(){
        selectedTask
                = taskList.getListView().getSelectionModel().getSelectedItem();
        if(selectedTask == null){
            taskList.getListView().getSelectionModel().select(0);
        }
    }

    private void disable(Object element, int type){
        switch(type){
            case TYPE_TEXTFIELD:
                ((TextField) element).setEditable(false);
                ((TextField) element).setStyle("-fx-background-color: #D3D3D3;");
                break;
            case TYPE_TEXTAREA:
                ((TextArea) element).setEditable(false);
                ((TextArea) element).getStyleClass().add("application/application.css");
                break;
            case TYPE_CHECKBOX:
                ((CheckBox) element).setDisable(true);
                break;
            default:
                break;
        }
    }

    private void enable(Object element, int type){
        switch(type){
            case TYPE_TEXTFIELD:
                ((TextField) element).setEditable(true);
                ((TextField) element).setStyle(null);
                break;
            case TYPE_TEXTAREA:
                ((TextArea) element).setEditable(true);
                ((TextArea) element).getStyleClass().clear();
                ((TextArea) element).setStyle(null);
                break;
            case TYPE_CHECKBOX:
                ((CheckBox) element).setDisable(false);
                break;
            default:
                break;
        }
    }

    //Assigns light grey CSS coloring to input fields and makes them uneditable
    private void disableAll(){
        disable(nameTextField, TYPE_TEXTFIELD);
        disable(senderTextField, TYPE_TEXTFIELD);
        disable(recipientsTextField, TYPE_TEXTFIELD);
        disable(contentTextField, TYPE_TEXTAREA);
        disable(oneTimeCheckBox, TYPE_CHECKBOX);
        disableOneTime();
        disable(repeatCheckBox, TYPE_CHECKBOX);
        disableRepeating();
        disable(daemonCheckbox, TYPE_CHECKBOX);
        disable(cloudCheckBox, TYPE_CHECKBOX);
        disable(activeCheckBox, TYPE_CHECKBOX);
    }

    //Clears CSS styling for input fields and makes them editable
    private void enableAll(){
        enable(nameTextField, TYPE_TEXTFIELD);
        enable(senderTextField, TYPE_TEXTFIELD);
        enable(recipientsTextField, TYPE_TEXTFIELD);
        enable(contentTextField, TYPE_TEXTAREA);
        enable(oneTimeCheckBox, TYPE_CHECKBOX);
        enableOneTime();
        enable(repeatCheckBox, TYPE_CHECKBOX);
        enableRepeating();
        enable(daemonCheckbox, TYPE_CHECKBOX);
        enable(cloudCheckBox, TYPE_CHECKBOX);
        enable(activeCheckBox, TYPE_CHECKBOX);
    }

    private void disableOneTime(){
        disable(atDateTextField, TYPE_TEXTFIELD);
        disable(atTimeTextField, TYPE_TEXTFIELD);
    }

    private void enableOneTime(){
        enable(atDateTextField, TYPE_TEXTFIELD);
        enable(atTimeTextField, TYPE_TEXTFIELD);
    }

    private void disableRepeating(){
        disable(fromDateTextField, TYPE_TEXTFIELD);
        disable(fromTimeTextField, TYPE_TEXTFIELD);
        disable(freqMinTextField, TYPE_TEXTFIELD);
        disable(freqHoursTextField, TYPE_TEXTFIELD);
    }

    private void enableRepeating(){
        enable(fromDateTextField, TYPE_TEXTFIELD);
        enable(fromTimeTextField, TYPE_TEXTFIELD);
        enable(freqMinTextField, TYPE_TEXTFIELD);
        enable(freqHoursTextField, TYPE_TEXTFIELD);
    }

    private void showErrorAlert(String contentText){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Error!");
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private boolean showConfirmationAlert(String contentText){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Error!");
        alert.setHeaderText("Error!");
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
