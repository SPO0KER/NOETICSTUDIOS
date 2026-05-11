package com.noetic.view;

import com.noetic.data.DataStore;
import com.noetic.model.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class WishlistView {

    private ScrollPane root;
    private DataStore store = DataStore.getInstance();
    private MainView mainView;

    public WishlistView(MainView mainView) {
        this.mainView = mainView;
        root = new ScrollPane();
        root.setFitToWidth(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getStyleClass().add("content-scroll");
        buildUI();
    }

    private void buildUI() {
        VBox container = new VBox(24);
        container.setPadding(new Insets(40, 60, 40, 60));
        container.getStyleClass().add("page-container");

        Label pageTitle = new Label("Lista de Deseos");
        pageTitle.getStyleClass().add("page-title");

        List<Product> wishlist = store.getWishlist();

        if (wishlist.isEmpty()) {
            VBox emptyBox = new VBox(16);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(80, 0, 80, 0));
            Label emptyIcon = new Label("♡");
            emptyIcon.setStyle("-fx-font-size: 60px; -fx-text-fill: #7b1c1c;");
            Label emptyMsg = new Label("Tu lista de deseos está vacía");
            emptyMsg.getStyleClass().add("empty-message");
            Button goShop = new Button("Explorar tienda");
            goShop.getStyleClass().add("primary-btn");
            goShop.setOnAction(e -> mainView.navigateTo("tienda"));
            emptyBox.getChildren().addAll(emptyIcon, emptyMsg, goShop);
            container.getChildren().addAll(pageTitle, emptyBox);
        } else {
            Label countLabel = new Label(wishlist.size() + " producto" + (wishlist.size() > 1 ? "s" : ""));
            countLabel.getStyleClass().add("count-label");

            FlowPane grid = new FlowPane();
            grid.setHgap(20);
            grid.setVgap(20);

            for (Product product : wishlist) {
                grid.getChildren().add(createWishCard(product));
            }

            container.getChildren().addAll(pageTitle, countLabel, grid);
        }

        root.setContent(container);
    }

    private VBox createWishCard(Product product) {
        VBox card = new VBox(0);
        card.getStyleClass().add("product-card");
        card.setPrefWidth(260);

        StackPane imageArea = new StackPane();
        imageArea.getStyleClass().add("product-image-area");
        imageArea.setPrefHeight(180);
        Label imgIcon = new Label(getCategoryEmoji(product.getCategory()));
        imgIcon.setStyle("-fx-font-size: 55px;");

        Button removeBtn = new Button("♥");
        removeBtn.getStyleClass().add("wish-btn-active");
        removeBtn.setOnAction(e -> {
            store.toggleWishlist(product);
            mainView.updateBadges();
            mainView.navigateTo("wishlist");
        });

        imageArea.getChildren().addAll(imgIcon, removeBtn);
        StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(removeBtn, new Insets(10, 10, 0, 0));

        VBox body = new VBox(6);
        body.setPadding(new Insets(14));

        Label catLabel = new Label(product.getCategory());
        catLabel.getStyleClass().add("product-category");
        Label nameLabel = new Label(product.getName());
        nameLabel.getStyleClass().add("product-name");
        nameLabel.setWrapText(true);

        HBox priceRow = new HBox(10);
        priceRow.setAlignment(Pos.CENTER_LEFT);
        Label priceLabel = new Label(formatPrice(product.getPrice()));
        priceLabel.getStyleClass().add("product-price");
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);

        Button addBtn = new Button("🛒 Agregar");
        addBtn.getStyleClass().add("add-btn");
        addBtn.setOnAction(e -> {
            store.addToCart(product);
            mainView.updateBadges();
            mainView.showToast("Producto agregado al carrito");
        });
        priceRow.getChildren().addAll(priceLabel, sp, addBtn);
        body.getChildren().addAll(catLabel, nameLabel, priceRow);

        card.getChildren().addAll(imageArea, body);
        return card;
    }

    private String getCategoryEmoji(String cat) {
        switch (cat) {
        case "Camisetas": return "👕";
   case "Gorras": return "🧢";
   case "Pantalones": case "Jeans": return "👖";
   case "Buzos": return "🧥";
    default: return "🛍";
        }
    }

    private String formatPrice(double price) {
        return String.format("$ %,.0f", price).replace(",", ".");
    }

    public ScrollPane getRoot() { return root; }
}
