package com.kadirkertis.domain.interactor.product.model;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public class OrderItem {

    private final String firebaseId;
    private final String itemName;
    private final double price;
    private final double totalPrice;
    private final int quantity;
    private final String imageUrl;

    public OrderItem(String firebaseId, String itemName, double price, int quantity, String imageUrl) {
        this.firebaseId = firebaseId;
        this.itemName = itemName;
        this.price = price;
        this.totalPrice = price * quantity;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
