package com.kadirkertis.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

/**
 * Created by Kadir Kertis on 11/13/2017.
 */

public class DataOrderItem {

    private String firebaseId;
    private String itemName;
    private double price;
    private double totalPrice;
    private int quantity;
    private String imageUrl;

    public DataOrderItem(){
    }

    public DataOrderItem(String firebaseId, String itemName, double price, int howMany,String imageUrl){
        this.itemName = itemName;
        this.firebaseId = firebaseId;
        this.price = price;
        this.quantity = howMany;
        this.totalPrice = howMany * price;
        this.imageUrl = imageUrl;
    }


    public String getItemName(){return itemName;}

    public String getFirebaseId(){return firebaseId;}

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    @Exclude
    public HashMap<String,Object> toMap(){
        HashMap<String,Object> orderItem = new HashMap<>();
        orderItem.put("firebaseId",firebaseId);
        orderItem.put("itemName",itemName);
        orderItem.put(" price", price);
        orderItem.put("totalPrice",totalPrice);
        orderItem.put("quantity",quantity);
        orderItem.put("imageUrl",imageUrl);
        return orderItem;
    }
}
