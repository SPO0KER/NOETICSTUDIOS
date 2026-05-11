package com.noetic.view;

import com.noetic.data.DataStore;
import com.noetic.model.CartItem;
import com.noetic.model.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class HistorialView {

    private ScrollPane root;
    private DataStore store = DataStore.getInstance();
    private MainView mainView;

    public HistorialView(MainView mainView) {
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

        Label pageTitle = new Label("Historial de Pedidos");
        pageTitle.getStyleClass().add("page-title");

        List<Order> orders = store.getOrders();

        if (orders.isEmpty()) {
            VBox emptyBox = new VBox(16);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(80, 0, 80, 0));
            Label emptyIcon = new Label("📋");
            emptyIcon.setStyle("-fx-font-size: 60px;");
            Label emptyMsg = new Label("No tienes pedidos todavía");
            emptyMsg.getStyleClass().add("empty-message");
            Button goShop = new Button("Ir a la tienda");
            goShop.getStyleClass().add("primary-btn");
            goShop.setOnAction(e -> mainView.navigateTo("tienda"));
            emptyBox.getChildren().addAll(emptyIcon, emptyMsg, goShop);
            container.getChildren().addAll(pageTitle, emptyBox);
        } else {
            Label countLabel = new Label(orders.size() + " pedido" + (orders.size() > 1 ? "s" : ""));
            countLabel.getStyleClass().add("count-label");

            VBox ordersBox = new VBox(16);
            for (Order order : orders) {
                ordersBox.getChildren().add(createOrderCard(order));
            }

            container.getChildren().addAll(pageTitle, countLabel, ordersBox);
        }

        root.setContent(container);
    }

    private VBox createOrderCard(Order order) {
        VBox card = new VBox(12);
        card.getStyleClass().add("order-card");
        card.setPadding(new Insets(20, 24, 20, 24));
   
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox orderInfo = new VBox(2);
        Label orderIdLabel = new Label("Pedido #" + order.getOrderId());
        orderIdLabel.getStyleClass().add("order-id");
        Label dateLabel = new Label(order.getFormattedDate());
        dateLabel.getStyleClass().add("order-date");
        orderInfo.getChildren().addAll(orderIdLabel, dateLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox rightInfo = new VBox(4);
        rightInfo.setAlignment(Pos.CENTER_RIGHT);
        Label totalLabel = new Label(order.getFormattedTotal());
        totalLabel.getStyleClass().add("order-total");
        Label statusLabel = new Label(order.getStatus());
        statusLabel.getStyleClass().add("status-completed");
        rightInfo.getChildren().addAll(totalLabel, statusLabel);

        header.getChildren().addAll(orderInfo, spacer, rightInfo);

        Separator sep = new Separator();

         
        VBox itemsBox = new VBox(10);
        for (CartItem item : order.getItems()) {
            HBox itemRow = new HBox(12);
            itemRow.setAlignment(Pos.CENTER_LEFT);

            VBox imgBox = new VBox();
            imgBox.setAlignment(Pos.CENTER);
            imgBox.getStyleClass().add("order-item-image");
            imgBox.setPrefSize(56, 56);
            Label imgIcon = new Label(getCategoryEmoji(item.getProduct().getCategory()));
            imgIcon.setStyle("-fx-font-size: 28px;");
            imgBox.getChildren().add(imgIcon);

            VBox infoBox = new VBox(2);
            HBox.setHgrow(infoBox, Priority.ALWAYS);
            Label nameLabel = new Label(item.getProduct().getName());
            nameLabel.getStyleClass().add("order-item-name");
            Label qtyLabel = new Label("Cantidad: " + item.getQuantity());
            qtyLabel.getStyleClass().add("order-item-qty");
            infoBox.getChildren().addAll(nameLabel, qtyLabel);

            Label priceLabel = new Label(formatPrice(item.getSubtotal()));
            priceLabel.getStyleClass().add("order-item-price");

            itemRow.getChildren().addAll(imgBox, infoBox, priceLabel);
            itemsBox.getChildren().add(itemRow);
        }

        card.getChildren().addAll(header, sep, itemsBox);
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
