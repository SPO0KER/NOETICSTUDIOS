package com.noetic;

import com.noetic.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Noetic Studios");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);

        
        try {
            Font.loadFont(MainApp.class.getResourceAsStream("/fonts/BebasNeue-Regular.ttf"), 14);
        } catch (Exception e) {
          
        }

        showLogin();
        primaryStage.show();
    }

    public static void showLogin() {
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getRoot(), 1100, 700);
        scene.getStylesheets().add(MainApp.class.getResource("/styles/main.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void showMain(String username, boolean isAdmin) {
        com.noetic.view.MainView mainView = new com.noetic.view.MainView(username, isAdmin);
        Scene scene = new Scene(mainView.getRoot(), 1200, 800);
        scene.getStylesheets().add(MainApp.class.getResource("/styles/main.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
