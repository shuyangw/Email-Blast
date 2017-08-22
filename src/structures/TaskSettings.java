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
    private Boolean oneTimeOrNot;
    private String atDate;
    private String atTime;
    private String fromDate;
    private String fromTime;
    private String frequencyMinutes;
    private String frequencyHours;

    //Non-data fields
    private boolean active;
    private boolean daemon;
    //Not even sure if will work sad face
    private boolean cloud;

    public TaskSettings(){
        this.name = "";
        this.sender = "";
        this.recipients = new ArrayList<>();
        this.content = "";

        //Must change
        this.oneTimeOrNot = null;
        this.atDate = "";
        this.atTime = "";
        this.fromDate = "";
        this.fromTime = "";
        this.frequencyMinutes = "";
        this.frequencyHours = "";
    }

    public String getName(){
        return this.name;
    }

    public String getSender(){
        return this.sender;
    }

    @SuppressWarnings("unused")
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

    public Boolean getOneTimeOrNot() {
        return oneTimeOrNot;
    }

    public void setOneTimeOrNot(Boolean oneTimeOrNot) {
        this.oneTimeOrNot = oneTimeOrNot;
    }

    public String getAtDate() {
        return atDate;
    }

    public void setAtDate(String atDate) {
        this.atDate = atDate;
    }

    public String getAtTime() {
        return atTime;
    }

    public void setAtTime(String atTime) {
        this.atTime = atTime;
    }

    public String getFrequencyMinutes() {
        return frequencyMinutes;
    }

    public void setFrequencyMinutes(String frequencyMinutes) {
        this.frequencyMinutes = frequencyMinutes;
    }

    public String getFrequencyHours() {
        return frequencyHours;
    }

    public void setFrequencyHours(String frequencyHours) {
        this.frequencyHours = frequencyHours;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    @SuppressWarnings("unused")
    public void printAll(){
        System.out.println("\nTask name: " + name);
        System.out.println("Sender name: " + sender);
        System.out.println("Recipients: " + getRecipientsText());
        System.out.println("Content: " + content);
        System.out.println("One time: " + oneTimeOrNot);
        if(oneTimeOrNot != null){
            if(oneTimeOrNot){
                System.out.println("At date: " + atDate);
                System.out.println("At time: " + atTime);
            }
            else{
                System.out.println("From date: " + fromDate);
                System.out.println("From from: " + fromTime);
                System.out.println("Frequency minutes: " + frequencyMinutes);
                System.out.println("Frequency hours: " + frequencyHours + "\n");
            }
        }

    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean running) {
        this.active = running;
    }

    public boolean getDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public boolean getCloud() {
        return cloud;
    }

    public void setCloud(boolean cloud) {
        this.cloud = cloud;
    }
}