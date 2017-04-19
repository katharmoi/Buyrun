package com.kadirkertis.orfo.data;

import com.kadirkertis.orfo.model.OrderItem;
import com.kadirkertis.orfo.model.PlaceInfo;

/**
 * Created by Kadir Kertis on 6.3.2017.
 */

public class DbTaskParams {
    private int taskId;
    private OrderItem orderItem;
    private int cartId;
    private int newQuantity;
    private String orderFirebaseId;
    private String placeId;
    private long date;
    private String customerId;
    private String tableNumber;
    private PlaceInfo placeInfo;
    private int[] itemCount;

    public DbTaskParams(int taskId){

        this.taskId = taskId;
    }

    public DbTaskParams(int taskId, int[] itemCount){
        this.taskId = taskId;
        this.itemCount = itemCount;

    }
    public DbTaskParams(int taskId, OrderItem item){
        this.taskId = taskId;
        this.orderItem = item;
    }

    public DbTaskParams(int taskId, int cartId){
        this.taskId = taskId;
        this.cartId = cartId;
    }
    public DbTaskParams(int taskId, int cartId,int newQuantity){
        this.taskId = taskId;
        this.cartId = cartId;
        this.newQuantity = newQuantity;
    }
    public DbTaskParams(int task_id, String orderFirebaseId, String placeId, long date, String customerId, String tableNumber) {

        this.taskId = task_id;
        this.orderFirebaseId = orderFirebaseId;
        this.placeId = placeId;
        this.date = date;
        this.customerId = customerId;
        this.tableNumber = tableNumber;
    }

    public DbTaskParams(int taskId, PlaceInfo placeInfo){
        this.taskId = taskId;
        this.placeInfo = placeInfo;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getCartId(){
        return cartId;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public String getOrderFirebaseId() {
        return orderFirebaseId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public long getDate() {
        return date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public PlaceInfo getPlaceInfo(){return placeInfo;}

    public int[] getItemCount(){return itemCount;}


}
