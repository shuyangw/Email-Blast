package structures;

import gui.TaskList;

import java.io.Serializable;

public class TaskSettings implements Serializable{
    private static final long serialVersionUID = 123214215121L;
    private TaskList taskList;

    public TaskSettings(TaskList taskList){
        this.taskList = taskList;
    }
}
