package com.noetic.view;

import com.noetic.MainApp;
import com.noetic.data.DataStore;
import com.noetic.model.User;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginView {

    private BorderPane root;
    private DataStore store = DataStore.getInstance();
    private Label errorLabel;

    public LoginView() {
        root = new BorderPane();
        root.getStyleClass().add("login-root");
        buildUI();
    }

    private void buildUI() {
          
        StackPane leftPanel = new StackPane();
        leftPanel.getStyleClass().add("login-left");
        leftPanel.setPrefWidth(480);

        VBox brandBox = new VBox(12);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        brandBox.setPadding(new Insets(0, 60, 0, 60));

        Label brand = new Label("NOETIC\nSTUDIOS");
        brand.getStyleClass().add("brand-title");

        Label tagline = new Label("Streetwear fashion company");
        tagline.getStyleClass().add("brand-tagline");

        brandBox.getChildren().addAll(brand, tagline);
        leftPanel.getChildren().add(brandBox);

    
        StackPane rightPanel = new StackPane();
        rightPanel.getStyleClass().add("login-right");

        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setMaxWidth(380);
        formBox.setPadding(new Insets(40));

        Label title = new Label("Iniciar Sesión");
        title.getStyleClass().add("login-title");

           
        VBox emailBox = new VBox(6);
        Label emailLabel = new Label("Correo electrónico");
        emailLabel.getStyleClass().add("field-label");
        TextField emailField = new TextField();
        emailField.setPromptText("tu@email.com");
        emailField.getStyleClass().add("login-field");

        emailBox.getChildren().addAll(emailLabel, emailField);

           
        VBox passBox = new VBox(6);
        Label passLabel = new Label("Contraseña");
        passLabel.getStyleClass().add("field-label");
        PasswordField passField = new PasswordField();
        passField.setPromptText("••••••••");
        passField.getStyleClass().add("login-field");

        passBox.getChildren().addAll(passLabel, passField);

          
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

         
        Button loginBtn = new Button("Iniciar Sesión");
        loginBtn.getStyleClass().add("login-btn");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

          
        VBox hintBox = new VBox(4);
        hintBox.getStyleClass().add("hint-box");
        Label hintTitle = new Label("Credenciales de prueba:");
        hintTitle.getStyleClass().add("hint-title");
        Label userHint = new Label("Usuario: usuario@tienda.com / user123");
        userHint.getStyleClass().add("hint-text");
        Label adminHint = new Label("Admin: admin@tienda.com / admin123");
        adminHint.getStyleClass().add("hint-text");
        hintBox.getChildren().addAll(hintTitle, userHint, adminHint);

        formBox.getChildren().addAll(title, emailBox, passBox, errorLabel, loginBtn, hintBox);
        rightPanel.getChildren().add(formBox);
        StackPane.setAlignment(formBox, Pos.CENTER);

           
          loginBtn.setOnAction(e -> handleLogin(emailField.getText(), passField.getText()));
        passField.setOnAction(e -> handleLogin(emailField.getText(), passField.getText()));

           
          userHint.setOnMouseClicked(e -> {
            emailField.setText("usuario@tienda.com");
            passField.setText("user123");
        });
        adminHint.setOnMouseClicked(e -> {
            emailField.setText("admin@tienda.com");
            passField.setText("admin123");
        });
        userHint.setStyle("-fx-cursor: hand;");
        adminHint.setStyle("-fx-cursor: hand;");

        HBox mainContent = new HBox();
        mainContent.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        root.setCenter(mainContent);
    }

    private void handleLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showError("Por favor completa todos los campos.");
              return;
        }
        User user = store.login(email.trim(), password);
        if (user != null) {
            MainApp.showMain(user.getDisplayName(), user.isAdmin());
        } else {
            showError("Correo o contraseña incorrectos.");
        }
    }

         private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(300), errorLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public BorderPane getRoot() { return root; }
}
