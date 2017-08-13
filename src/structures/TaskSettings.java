package structures;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskSettings implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String sender;
    private ArrayList<String> recipients;
    private String content;

    public String getName(){
        return this.name;
    }

    public String getSender(){
        return this.sender;
    }

    public ArrayList<String> getRecipients(){
        return this.recipients;
    }

    public String getContent() {
        return this.content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }
}
