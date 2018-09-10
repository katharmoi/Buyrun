package com.kadirkertis.domain.interactor.product.model;

/**
 * Created by Kadir Kertis on 11/2/2017.
 */

public class Item {
    private String key;
    private String category;
    private String subCategory;
    private String name;
    private String description;
    private String url;
    private double price;
    private long timeAdded;
    private long timeLastEdited;

    public Item() {

    }

    public Item(String key,
                String category,
                String subCategory,
                String name,
                String description,
                String url,
                double price,
                long timeAdded,
                long timeLastEdited) {
        this.key = key;
        this.category = category;
        this.subCategory = subCategory;
        this.name = name;
        this.description = description;
        this.url = url;
        this.price = price;
        this.timeAdded = timeAdded;
        this.timeLastEdited = timeLastEdited;
    }

    public String getKey() {
        return key;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public double getPrice() {
        return price;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public long getTimeLastEdited() {
        return timeLastEdited;
    }
}
