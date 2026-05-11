package com.noetic.view;

import com.noetic.data.DataStore;
import com.noetic.model.CartItem;
import com.noetic.model.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

public class CartView {

    private ScrollPane root;
    private DataStore store = DataStore.getInstance();
    private MainView mainView;
    private VBox cartItemsBox;
    private Label subtotalLabel, totalLabel;

    public CartView(MainView mainView) {
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

        Label pageTitle = new Label("Carrito de compras");
        pageTitle.getStyleClass().add("page-title");

        List<CartItem> items = store.getCart();

        if (items.isEmpty()) {
            VBox emptyBox = new VBox(16);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(80, 0, 80, 0));
            Label emptyIcon = new Label("🛒");
            emptyIcon.setStyle("-fx-font-size: 60px;");
            Label emptyMsg = new Label("Tu carrito está vacío");
            emptyMsg.getStyleClass().add("empty-message");
            Button goShop = new Button("Ir a la tienda");
            goShop.getStyleClass().add("primary-btn");
            goShop.setOnAction(e -> mainView.navigateTo("tienda"));
            emptyBox.getChildren().addAll(emptyIcon, emptyMsg, goShop);
            container.getChildren().addAll(pageTitle, emptyBox);
        } else {
            HBox mainRow = new HBox(24);

            
            cartItemsBox = new VBox(12);
            cartItemsBox.setPrefWidth(580);
            HBox.setHgrow(cartItemsBox, Priority.ALWAYS);
            refreshCartItems();
   
            VBox summaryBox = new VBox(16);
            summaryBox.getStyleClass().add("summary-box");
            summaryBox.setPrefWidth(280);
            summaryBox.setPadding(new Insets(24));

            Label summaryTitle = new Label("Resumen de Compra");
            summaryTitle.getStyleClass().add("summary-title");

            HBox subtotalRow = new HBox();
            Label subtotalKey = new Label("Subtotal");
            subtotalKey.getStyleClass().add("summary-key");
            Region sp1 = new Region(); HBox.setHgrow(sp1, Priority.ALWAYS);
            subtotalLabel = new Label(formatPrice(store.getCartTotal()));
            subtotalLabel.getStyleClass().add("summary-value");
            subtotalRow.getChildren().addAll(subtotalKey, sp1, subtotalLabel);

            HBox envioRow = new HBox();
            Label envioKey = new Label("Envío");
            envioKey.getStyleClass().add("summary-key");
            Region sp2 = new Region(); HBox.setHgrow(sp2, Priority.ALWAYS);
            Label envioValue = new Label("Gratis");
            envioValue.getStyleClass().add("summary-free");
            envioRow.getChildren().addAll(envioKey, sp2, envioValue);

            Separator sep = new Separator();
            sep.getStyleClass().add("summary-sep");

            HBox totalRow = new HBox();
            Label totalKey = new Label("Total");
            totalKey.getStyleClass().add("total-key");
            Region sp3 = new Region(); HBox.setHgrow(sp3, Priority.ALWAYS);
            totalLabel = new Label(formatPrice(store.getCartTotal()));
            totalLabel.getStyleClass().add("total-value");
            totalRow.getChildren().addAll(totalKey, sp3, totalLabel);

            Button checkoutBtn = new Button("Finalizar Compra");
            checkoutBtn.getStyleClass().add("checkout-btn");
            checkoutBtn.setMaxWidth(Double.MAX_VALUE);
            checkoutBtn.setOnAction(e -> handleCheckout());

            summaryBox.getChildren().addAll(summaryTitle, subtotalRow, envioRow, sep, totalRow, checkoutBtn);

            mainRow.getChildren().addAll(cartItemsBox, summaryBox);
            container.getChildren().addAll(pageTitle, mainRow);
        }

        root.setContent(container);
    }

    private void refreshCartItems() {
        cartItemsBox.getChildren().clear();
        for (CartItem item : store.getCart()) {
            cartItemsBox.getChildren().add(createCartItem(item));
        }
    }

    private HBox createCartItem(CartItem item) {
        HBox row = new HBox(16);
        row.getStyleClass().add("cart-item");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16));

         
        VBox imgBox = new VBox();
        imgBox.setAlignment(Pos.CENTER);
        imgBox.getStyleClass().add("cart-item-image");
        imgBox.setPrefSize(80, 80);
        Label imgIcon = new Label(getCategoryEmoji(item.getProduct().getCategory()));
        imgIcon.setStyle("-fx-font-size: 36px;");
        imgBox.getChildren().add(imgIcon);

        
        VBox infoBox = new VBox(4);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        Label nameLabel = new Label(item.getProduct().getName());
        nameLabel.getStyleClass().add("cart-item-name");
        Label catLabel = new Label(item.getProduct().getCategory());
        catLabel.getStyleClass().add("cart-item-cat");
        Label priceLabel = new Label(formatPrice(item.getProduct().getPrice()));
        priceLabel.getStyleClass().add("cart-item-price");
        infoBox.getChildren().addAll(nameLabel, catLabel, priceLabel);

         
        HBox qtyBox = new HBox(8);
        qtyBox.setAlignment(Pos.CENTER);
        Button minusBtn = new Button("−");
        minusBtn.getStyleClass().add("qty-btn");
        Label qtyLabel = new Label(String.valueOf(item.getQuantity()));
        qtyLabel.getStyleClass().add("qty-label");
        Button plusBtn = new Button("+");
        plusBtn.getStyleClass().add("qty-btn");

        minusBtn.setOnAction(e -> {
            store.updateQuantity(item, item.getQuantity() - 1);
            mainView.updateBadges();
            rebuildCart();
        });
        plusBtn.setOnAction(e -> {
            store.updateQuantity(item, item.getQuantity() + 1);
            mainView.updateBadges();
            updateSummary();
            qtyLabel.setText(String.valueOf(item.getQuantity()));
        });

        qtyBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

        // Delete
        Button deleteBtn = new Button("🗑");
        deleteBtn.getStyleClass().add("delete-btn");
        deleteBtn.setOnAction(e -> {
            store.removeFromCart(item);
            mainView.updateBadges();
            rebuildCart();
        });

        row.getChildren().addAll(imgBox, infoBox, qtyBox, deleteBtn);
        return row;
    }

    private void rebuildCart() {
        // Rebuild the whole view
        mainView.navigateTo("carrito");
    }

    private void updateSummary() {
        if (subtotalLabel != null) subtotalLabel.setText(formatPrice(store.getCartTotal()));
        if (totalLabel != null) totalLabel.setText(formatPrice(store.getCartTotal()));
    }

    private void handleCheckout() {
        Order order = store.checkout();
        if (order != null) {
            mainView.showToast("Compra realizada con éxito");
            mainView.updateBadges();
            mainView.navigateTo("historial");
        }
    }

    private String getCategoryEmoji(String category) {
        switch (category) {
            case "Camisetas": return "👕";
            case "Gorras": return "🧢";
            case "Pantalones": return "👖";
            case "Jeans": return "👖";
            case "Buzos": return "🧥";
            default: return "🛍";
        }
    }

    private String formatPrice(double price) {
        return String.format("$ %,.0f", price).replace(",", ".");
    }

    public ScrollPane getRoot() { return root; }
}
