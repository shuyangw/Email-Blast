package structures;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskSettings implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String sender;
    private ArrayList<String> recipients;
    private String content;

    //Preliminary other values
    private boolean oneTimeOrNot;
    private String startDate;
    private String startTime;
    private int frequencyMinutes;
    private int frequencyHours;

    public TaskSettings(){
        this.name = "";
        this.sender = "";
        this.recipients = new ArrayList<>();
        this.content = "";

        //Must change
        this.oneTimeOrNot = false;
        this.startDate = "";
        this.startTime = "";
        this.frequencyMinutes = 0;
        this.frequencyHours = 0;
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

    public boolean isOneTimeOrNot() {
        return oneTimeOrNot;
    }

    public void setOneTimeOrNot(boolean oneTimeOrNot) {
        this.oneTimeOrNot = oneTimeOrNot;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getFrequencyMinutes() {
        return frequencyMinutes;
    }

    public void setFrequencyMinutes(int frequencyMinutes) {
        this.frequencyMinutes = frequencyMinutes;
    }

    public int getFrequencyHours() {
        return frequencyHours;
    }

    public void setFrequencyHours(int frequencyHours) {
        this.frequencyHours = frequencyHours;
    }

}