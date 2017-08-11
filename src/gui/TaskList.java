package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import structures.AlphabetComparator;

public class TaskList{

    private final ListView taskList = new ListView();
    private ObservableList<String> items;
    private Stage primaryStage;
    private MainGUI mainGUI;
    private int unnamedCount;

    public TaskList(Stage primaryStage, MainGUI mainGUI){
        this.primaryStage = primaryStage;
        this.mainGUI = mainGUI;
    }

    public void initializeList(){
        this.taskList.setPrefSize(150, 550);
        this.taskList.setEditable(true);
        this.items = FXCollections.observableArrayList("DEALOL","CEALOL","BEALOL","AEALOL");
        this.taskList.setItems(items);
        this.mainGUI.getPaneLeft().getChildren().add(taskList);
        this.unnamedCount = 0;
    }

    public void addToTaskList(String name){
        if(name.equals("Unnamed")){
            if(unnamedCount == 0){
                this.items.add(name);
                ++this.unnamedCount;
            }
            else{
                this.items.add(name+"("+unnamedCount+")");
                ++this.unnamedCount;
            }
        }
        else{
            this.items.add(name);
        }
    }

    public void removeFromTaskList(String name){
        this.items.remove(name);
        if(this.getSize() == 0){
            this.unnamedCount = 0;
        }
    }

    public void sortAlphabetically(){
        this.items.sort(new AlphabetComparator());
    }

    public ListView getListView(){
        return this.taskList;
    }

    public ObservableList<String> getItems(){
        return this.items;
    }

    public int getSize(){
        return this.items.size();
    }
}
