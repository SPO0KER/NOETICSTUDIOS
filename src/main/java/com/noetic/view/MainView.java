package com.noetic.view;

import com.noetic.MainApp;
import com.noetic.data.DataStore;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class MainView {

    private BorderPane root;
    private DataStore store = DataStore.getInstance();
    private String username;
    private boolean isAdmin;

     
    private Button btnTienda, btnWishlist, btnCarrito, btnHistorial, btnAdmin;
    private Label cartBadge, wishBadge;

      
    private StackPane contentArea;

       
    private Label toastLabel;

       
    private ShopView shopView;

    public MainView(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        root = new BorderPane();
        root.getStyleClass().add("main-root");
        buildNavbar();
        buildContent();
        buildToast();
        navigateTo("tienda");
    }

    private void buildNavbar() {
        HBox navbar = new HBox();
        navbar.getStyleClass().add("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPadding(new Insets(0, 30, 0, 30));
        navbar.setSpacing(0);

          
           Label logo = new Label("NOETIC STUDIOS");
           logo.getStyleClass().add("navbar-logo");
           logo.setOnMouseClicked(e -> navigateTo("tienda"));
           logo.setStyle("-fx-cursor: hand;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        
        HBox navLinks = new HBox(5);
        navLinks.setAlignment(Pos.CENTER);

        btnTienda = navBtn("🛍 Tienda", "tienda");
        btnWishlist = navBtnWithBadge("♡ Lista de Deseos", "wishlist");
        btnCarrito = navBtnWithBadge("🛒 Carrito", "carrito");
        btnHistorial = navBtn("📋 Historial", "historial");

        navLinks.getChildren().addAll(btnTienda);

        StackPane wishStack = new StackPane();
        wishBadge = new Label("0");
        wishBadge.getStyleClass().add("badge");
        wishBadge.setVisible(false);
        wishStack.getChildren().addAll(btnWishlist, wishBadge);
        StackPane.setAlignment(wishBadge, Pos.TOP_RIGHT);

        StackPane cartStack = new StackPane();
        cartBadge = new Label("0");
        cartBadge.getStyleClass().add("badge");
        cartBadge.setVisible(false);
        cartStack.getChildren().addAll(btnCarrito, cartBadge);
        StackPane.setAlignment(cartBadge, Pos.TOP_RIGHT);

        navLinks.getChildren().addAll(wishStack, cartStack, btnHistorial);

        if (isAdmin) {
            btnAdmin = navBtn("⚙ Admin", "admin");
            navLinks.getChildren().add(btnAdmin);
        }

          
        HBox userBox = new HBox(8);
        userBox.setAlignment(Pos.CENTER);
        Label userLabel = new Label("👤 " + username);
        userLabel.getStyleClass().add("navbar-user");

        if (isAdmin) {
            Label adminBadge = new Label("Admin");
            adminBadge.getStyleClass().add("admin-badge");
            userBox.getChildren().addAll(userLabel, adminBadge);
        } else {
            userBox.getChildren().add(userLabel);
        }

        Button logoutBtn = new Button("⏏");
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setOnAction(e -> {
            store.logout();
            MainApp.showLogin();
        });

        navbar.getChildren().addAll(logo, spacer, navLinks, new Region() {{ setPrefWidth(20); }}, userBox, logoutBtn);
        root.setTop(navbar);
    }

    private Button navBtn(String text, String target) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-btn");
        btn.setOnAction(e -> navigateTo(target));
        return btn;
    }

    private Button navBtnWithBadge(String text, String target) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-btn");
        btn.setOnAction(e -> navigateTo(target));
        return btn;
    }

    private void buildContent() {
        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");
        root.setCenter(contentArea);
    }

    private void buildToast() {
        toastLabel = new Label();
        toastLabel.getStyleClass().add("toast");
        toastLabel.setVisible(false);
        toastLabel.setMouseTransparent(true);

        StackPane overlay = new StackPane(toastLabel);
        overlay.setMouseTransparent(true);
        overlay.setPadding(new Insets(20));
        StackPane.setAlignment(toastLabel, Pos.TOP_RIGHT);

        root.getChildren().add(overlay);
    }

    public void showToast(String message) {
        toastLabel.setText("✓  " + message);
        toastLabel.setVisible(true);
        FadeTransition show = new FadeTransition(Duration.millis(300), toastLabel);
        show.setFromValue(0);
        show.setToValue(1);
        show.setOnFinished(e -> {
            FadeTransition hide = new FadeTransition(Duration.millis(500), toastLabel);
            hide.setFromValue(1);
            hide.setToValue(0);
            hide.setDelay(Duration.millis(1800));
            hide.setOnFinished(ev -> toastLabel.setVisible(false));
            hide.play();
        });
        show.play();
    }

    public void updateBadges() {
        int cartCount = store.getCartCount();
        cartBadge.setText(String.valueOf(cartCount));
        cartBadge.setVisible(cartCount > 0);

        int wishCount = store.getWishlistCount();
        wishBadge.setText(String.valueOf(wishCount));
        wishBadge.setVisible(wishCount > 0);
    }

    public void navigateTo(String section) {
        contentArea.getChildren().clear();

       
        for (Button b : new Button[]{btnTienda, btnWishlist, btnCarrito, btnHistorial}) {
            if (b != null) b.getStyleClass().remove("nav-btn-active");
        }
        if (btnAdmin != null) btnAdmin.getStyleClass().remove("nav-btn-active");

        switch (section) {
            case "tienda":
                shopView = new ShopView(this);
                contentArea.getChildren().add(shopView.getRoot());
                btnTienda.getStyleClass().add("nav-btn-active");
                break;
            case "wishlist":
                WishlistView wv = new WishlistView(this);
                contentArea.getChildren().add(wv.getRoot());
                if (btnWishlist != null) btnWishlist.getStyleClass().add("nav-btn-active");
                break;
            case "carrito":
                CartView cv = new CartView(this);
                contentArea.getChildren().add(cv.getRoot());
                if (btnCarrito != null) btnCarrito.getStyleClass().add("nav-btn-active");
                break;
            case "historial":
                HistorialView hv = new HistorialView(this);
                contentArea.getChildren().add(hv.getRoot());
                if (btnHistorial != null) btnHistorial.getStyleClass().add("nav-btn-active");
                break;
            case "admin":
                if (isAdmin) {
                    AdminView av = new AdminView(this);
                    contentArea.getChildren().add(av.getRoot());
                    if (btnAdmin != null) btnAdmin.getStyleClass().add("nav-btn-active");
                }
                break;
        }

        updateBadges();
    }

    public BorderPane getRoot() { return root; }
    public boolean isAdmin() { return isAdmin; }
}
