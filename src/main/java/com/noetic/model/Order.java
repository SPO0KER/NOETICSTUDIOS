package com.noetic.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class Order {
    private String orderId;
    private List<CartItem> items;
    private double total;
    private LocalDateTime dateTime;
    private String status;

    public Order(List<CartItem> items, double total) {
        this.orderId = "order-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        this.items = items;
        this.total = total;
        this.dateTime = LocalDateTime.now();
        this.status = "Completado";
    }

    public String getOrderId() { return orderId; }
    public List<CartItem> getItems() { return items; }
    public double getTotal() { return total; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, HH:mm",
                new java.util.Locale("es", "CO"));
        return dateTime.format(formatter);
    }

    public String getFormattedTotal() {
        return String.format("$ %,.0f", total).replace(",", ".");
    }
}