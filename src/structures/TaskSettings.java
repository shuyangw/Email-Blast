package structures;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskSettings implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String sender;
    private ArrayList<String> recipients;
    private String content;

    public TaskSettings(){
        this.name = "";
        this.sender = "";
        this.recipients = new ArrayList<>();
        this.content = "";
    }

    public String getName(){
        return this.name;
    }

    public String getSender(){
        return this.sender;
    }

    public ArrayList<String> getRecipients(){
        return this.recipients;
    }

    public String getRecipientsText(){
        String recipients = "";
        for(String recipient: this.recipients){
            recipients += recipient;
        }
        return recipients;
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

    public void printElements(){
        System.out.println(this.name);
        System.out.println(this.sender);
        System.out.println(this.getRecipientsText());
        System.out.println(this.content);
    }
}
