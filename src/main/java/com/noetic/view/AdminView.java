package com.noetic.view;

import com.noetic.data.DataStore;
import com.noetic.model.Order;
import com.noetic.model.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class AdminView {

    private ScrollPane root;
    private DataStore store = DataStore.getInstance();
    private MainView mainView;

    private String currentTab = "Resumen";
    private HBox tabBar;
    private VBox contentBox;

    public AdminView(MainView mainView) {
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

        Label pageTitle = new Label("Panel de Administración");
        pageTitle.getStyleClass().add("page-title");

        
        tabBar = new HBox(8);
        tabBar.setAlignment(Pos.CENTER_LEFT);
        for (String tab : new String[]{"Resumen", "Productos", "Pedidos"}) {
            Button btn = new Button(tab);
            btn.getStyleClass().add("filter-btn");
            if (tab.equals(currentTab)) btn.getStyleClass().add("filter-btn-active");
            btn.setOnAction(e -> {
                currentTab = tab;
                updateTabStyles();
                loadTabContent();
            });
            tabBar.getChildren().add(btn);
        }

        contentBox = new VBox(20);

        container.getChildren().addAll(pageTitle, tabBar, contentBox);
        root.setContent(container);

        loadTabContent();
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabBar.getChildren().size(); i++) {
            Button btn = (Button) tabBar.getChildren().get(i);
            btn.getStyleClass().remove("filter-btn-active");
            if (btn.getText().equals(currentTab)) {
                btn.getStyleClass().add("filter-btn-active");
            }
        }
    }

    private void loadTabContent() {
        contentBox.getChildren().clear();
        switch (currentTab) {
            case "Resumen": buildResumen(); break;
            case "Productos": buildProductos(); break;
            case "Pedidos": buildPedidos(); break;
        }
    }

 
    private void buildResumen() {
        HBox statsRow = new HBox(16);
        statsRow.setAlignment(Pos.TOP_LEFT);

        statsRow.getChildren().addAll(
            statCard("💲", "Ingresos Totales", formatPrice(store.getTotalRevenue()), "#4CAF50", "#e8f5e9"),
            statCard("📦", "Total Pedidos", String.valueOf(store.getTotalOrders()), "#7C4DFF", "#ede7f6"),
            statCard("🛍", "Productos", String.valueOf(store.getTotalProducts()), "#E91E63", "#fce4ec"),
            statCard("📊", "Valor Promedio", formatPrice(store.getAverageOrderValue()), "#FF9800", "#fff3e0")
        );

        contentBox.getChildren().add(statsRow);
    }

    private VBox statCard(String icon, String label, String value, String iconBg, String cardBg) {
        VBox card = new VBox(8);
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(200);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 22px; -fx-background-color: " + cardBg + "; -fx-padding: 8; -fx-background-radius: 8;");

        VBox textBox = new VBox(2);
        Label labelL = new Label(label);
        labelL.setStyle("-fx-font-size: 11px; -fx-text-fill: #888; -fx-font-weight: 500;");
        Label valueL = new Label(value);
        valueL.setStyle("-fx-font-size: 18px; -fx-font-weight: 700; -fx-text-fill: #1a1a1a;");
        textBox.getChildren().addAll(labelL, valueL);

        topRow.getChildren().addAll(iconLabel, textBox);
        card.getChildren().add(topRow);
        return card;
    }

     
    private void buildProductos() {
           
        HBox toolbar = new HBox();
        toolbar.setAlignment(Pos.CENTER_RIGHT);
        Button addBtn = new Button("+ Agregar Producto");
        addBtn.getStyleClass().add("primary-btn");
        addBtn.setOnAction(e -> showAddProductDialog());
        toolbar.getChildren().add(addBtn);

        
        TableView<Product> table = new TableView<>();
        table.getStyleClass().add("admin-table");
        table.setItems(FXCollections.observableArrayList(store.getProducts()));
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));

        TableColumn<Product, String> catCol = new TableColumn<>("Categoría");
        catCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCategory()));
        catCol.setMaxWidth(110);

        TableColumn<Product, String> priceCol = new TableColumn<>("Precio");
        priceCol.setCellValueFactory(d -> new SimpleStringProperty(formatPrice(d.getValue().getPrice())));
        priceCol.setMaxWidth(110);

        TableColumn<Product, String> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getStock())));
        stockCol.setMaxWidth(70);

        TableColumn<Product, Void> actionsCol = new TableColumn<>("Acciones");
        actionsCol.setMaxWidth(120);
        actionsCol.setCellFactory(col -> new TableCell<Product, Void>() {
            Button editBtn = new Button("✏");
            Button delBtn = new Button("🗑");
            HBox box = new HBox(8, editBtn, delBtn);

            {
                editBtn.getStyleClass().add("table-btn-edit");
                delBtn.getStyleClass().add("table-btn-del");
                box.setAlignment(Pos.CENTER);
                editBtn.setOnAction(e -> showEditProductDialog(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> {
                    Product p = getTableView().getItems().get(getIndex());
                    store.removeProduct(p.getId());
                    table.setItems(FXCollections.observableArrayList(store.getProducts()));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.getColumns().addAll(nameCol, catCol, priceCol, stockCol, actionsCol);
        contentBox.getChildren().addAll(toolbar, table);
    }

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Agregar Producto");
        dialog.setHeaderText("Nuevo Producto");
        dialog.getDialogPane().getStylesheets().add(
            getClass().getResource("/styles/main.css").toExternalForm()
        );

        GridPane grid = buildProductForm(null);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setText("Guardar");
        okBtn.getStyleClass().add("primary-btn");

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    TextField nameF = (TextField) grid.lookup("#nameF");
                    TextField descF = (TextField) grid.lookup("#descF");
                    TextField priceF = (TextField) grid.lookup("#priceF");
                    TextField stockF = (TextField) grid.lookup("#stockF");
                    ComboBox catF = (ComboBox) grid.lookup("#catF");

                    Product p = new Product(
                        store.getNextProductId(),
                        nameF.getText(), descF.getText(),
                        Double.parseDouble(priceF.getText()),
                        Integer.parseInt(stockF.getText()),
                        catF.getValue().toString(), ""
                    );
                    store.addProduct(p);
                    mainView.showToast("Producto agregado");
                } catch (Exception ex) { /* ignore */ }
            }
            return null;
        });

        dialog.showAndWait();
        loadTabContent();
    }

    private void showEditProductDialog(Product product) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Editar Producto");
        dialog.setHeaderText(product.getName());
        dialog.getDialogPane().getStylesheets().add(
            getClass().getResource("/styles/main.css").toExternalForm()
        );

        GridPane grid = buildProductForm(product);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setText("Guardar cambios");

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    TextField nameF = (TextField) grid.lookup("#nameF");
                    TextField descF = (TextField) grid.lookup("#descF");
                    TextField priceF = (TextField) grid.lookup("#priceF");
                    TextField stockF = (TextField) grid.lookup("#stockF");
                    ComboBox catF = (ComboBox) grid.lookup("#catF");

                    product.setName(nameF.getText());
                    product.setDescription(descF.getText());
                    product.setPrice(Double.parseDouble(priceF.getText()));
                    product.setStock(Integer.parseInt(stockF.getText()));
                    product.setCategory(catF.getValue().toString());
                    mainView.showToast("Producto actualizado");
                } catch (Exception ex) { /* ignore */ }
            }
            return null;
        });

        dialog.showAndWait();
        loadTabContent();
    }

    private GridPane buildProductForm(Product product) {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(10));
        grid.setPrefWidth(380);

        String[] labels = {"Nombre:", "Descripción:", "Precio:", "Stock:", "Categoría:"};
        for (int i = 0; i < labels.length; i++) {
            Label l = new Label(labels[i]);
            l.getStyleClass().add("field-label");
            grid.add(l, 0, i);
        }

        TextField nameF = new TextField(product != null ? product.getName() : "");
        nameF.setId("nameF"); nameF.getStyleClass().add("login-field");
        TextField descF = new TextField(product != null ? product.getDescription() : "");
        descF.setId("descF"); descF.getStyleClass().add("login-field");
        TextField priceF = new TextField(product != null ? String.valueOf((int)product.getPrice()) : "");
        priceF.setId("priceF"); priceF.getStyleClass().add("login-field");
        TextField stockF = new TextField(product != null ? String.valueOf(product.getStock()) : "");
        stockF.setId("stockF"); stockF.getStyleClass().add("login-field");

        ComboBox<String> catF = new ComboBox<>();
        catF.setId("catF");
        catF.getItems().addAll("Camisetas", "Gorras", "Pantalones", "Jeans", "Buzos");
        catF.setValue(product != null ? product.getCategory() : "Camisetas");
        catF.setPrefWidth(200);

        grid.add(nameF, 1, 0);
        grid.add(descF, 1, 1);
        grid.add(priceF, 1, 2);
        grid.add(stockF, 1, 3);
        grid.add(catF, 1, 4);

        return grid;
    }

   
    private void buildPedidos() {
        List<Order> orders = store.getOrders();

        if (orders.isEmpty()) {
            Label emptyLabel = new Label("No hay pedidos registrados.");
            emptyLabel.getStyleClass().add("empty-message");
            contentBox.getChildren().add(emptyLabel);
            return;
        }

        TableView<Order> table = new TableView<>();
        table.getStyleClass().add("admin-table");
        table.setItems(FXCollections.observableArrayList(orders));
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Order, String> idCol = new TableColumn<>("# Pedido");
        idCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getOrderId().substring(0, 16)));

        TableColumn<Order, String> dateCol = new TableColumn<>("Fecha");
        dateCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFormattedDate()));

        TableColumn<Order, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFormattedTotal()));
        totalCol.setMaxWidth(120);

        TableColumn<Order, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
        statusCol.setMaxWidth(120);

        table.getColumns().addAll(idCol, dateCol, totalCol, statusCol);
        contentBox.getChildren().add(table);
    }

    private String formatPrice(double price) {
        return String.format("$ %,.0f", price).replace(",", ".");
    }

    public ScrollPane getRoot() { return root; }
}
