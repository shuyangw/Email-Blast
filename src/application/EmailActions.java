package application;

import javafx.scene.control.TextInputDialog;
import structures.TaskSettings;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Authenticator;
import java.util.Optional;
import java.util.Properties;

public class EmailActions {
    private TaskSettings currTask;
    private String password;
    private Session session;

    public EmailActions(TaskSettings currTask){
        this.currTask = currTask;
    }

    public void promptPassword(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password");
        dialog.setHeaderText("Please enter your password for the email account");
        dialog.setContentText("Please enter your password");
        Optional<String> result = dialog.showAndWait();
        password = result.isPresent() ? result.get() : "";
    }

    public void setup(){
        //Setup sender and stuff
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties,
                new Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(currTask.getSender(), password);
                    }
                }
        );
    }

}
