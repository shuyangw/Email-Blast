package application;

import javafx.application.Application;
import javafx.stage.Stage;

import gui.MainGUI;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainGUI gui = new MainGUI();
        gui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}