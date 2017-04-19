package com.kadirkertis.orfo.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kadir Kertis on 30.1.2017.
 */

public class Order {
    private String orderId;
    private String placeId;
    private String whoOrdered;
    private int tableNumber;
    private List<OrderItem> orderedItems;
    private HashMap<String,Object> orderTime;
    private HashMap<String,Object> timeLastEdited;

    /*
    *Required no arg cns for Firebase
     */
    public Order(){

    }

    public Order(String orderId,
                 String placeId,
                 HashMap<String, Object> orderTime,
                 String whoOrdered,
                 int tableNumber,
                 List<OrderItem> orderedItems) {
        this.orderId = orderId;
        this.placeId = placeId;
        this.orderTime = orderTime;
        this.whoOrdered = whoOrdered;
        this.tableNumber = tableNumber;
        this.orderedItems = orderedItems;

        HashMap<String,Object> timeLastEditedObj = new HashMap<>();
        timeLastEditedObj.put("date", ServerValue.TIMESTAMP);
        this.timeLastEdited = timeLastEditedObj;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public HashMap<String, Object> getOrderTime() {
        if(orderTime != null){
            return orderTime;
        }

        HashMap<String,Object> orderTimeObj = new HashMap<>();
        orderTimeObj.put("date", ServerValue.TIMESTAMP);
        return  orderTimeObj;
    }
    public HashMap<String, Object> getTimeLastEdited() {
        return timeLastEdited;
    }
    public String getWhoOrdered() {
        return whoOrdered;
    }


    public int getTableNumber() {
        return tableNumber;
    }


    public List<OrderItem> getOrderedItems() {
        return orderedItems;
    }

    @Exclude
    public long getOrderTimeLong(){
        return (long) orderTime.get("date");
    }

    @Exclude
    public long getTimeLastEditedLong(){
        return (long) timeLastEdited.get("date");
    }

    @Exclude
    public HashMap<String,Object> toMap(){
        HashMap<String,Object> order = new HashMap<>();
        order.put("orderId",orderId);
        order.put("placeId",placeId);
        order.put("whoOrdered",whoOrdered);
        order.put("tableNumber",tableNumber);
        order.put("orderedItems",orderedItems);
        order.put("orderTime",orderTime);
        order.put("timeLastEdited",timeLastEdited);
        return order;
    }


}
