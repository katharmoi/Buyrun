package com.kadirkertis.data.cart;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.kadirkertis.data.database.OrfoDbContract;
import com.kadirkertis.domain.model.OrderItem;

import java.util.ArrayList;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public class CartServiceImpl {

    private static ContentResolver contentResolver;

    public CartServiceImpl(ContentResolver contentResolver) {
        contentResolver = contentResolver;
    }

    public static Uri addToCart(OrderItem orderItem) {
        ContentValues singleItem = new ContentValues();
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_ID, orderItem.getFirebaseId());
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_NAME, orderItem.getItemName());
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY, orderItem.getQuantity());
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_PRICE, orderItem.getPrice());
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_IMAGE_URL, orderItem.getImageUrl());

        return contentResolver.insert(OrfoDbContract.OrfoCartTable.CONTENT_URI, singleItem);

    }

    public static int deleteFromCart(OrderItem item) {

        return contentResolver.delete(OrfoDbContract.OrfoCartTable.CONTENT_URI,
                OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_ID + " =?",
                new String[]{item.getFirebaseId()});

    }

    public static boolean updateCart(OrderItem item, int newQuantity) {
        ContentValues updateQuantity = new ContentValues();
        updateQuantity.put(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY, newQuantity);
        int update = contentResolver.update(OrfoDbContract.OrfoCartTable.CONTENT_URI,
                updateQuantity,
                OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_ID + " =?",
                new String[]{item.getFirebaseId()});

        return update > 0;

    }

    public static boolean isItemInCart(OrderItem item) {
        Cursor c = contentResolver.query(OrfoDbContract.OrfoCartTable.CONTENT_URI,
                null,
                OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_ID + " =?",
                new String[]{item.getFirebaseId()},
                null);

        boolean isIn = c.getCount() > 0;
        c.close();
        return isIn;
    }

    public static ArrayList<OrderItem> getCart() {

        Cursor c = null;
        ArrayList<OrderItem> cart;
        try {
            c = contentResolver.query(OrfoDbContract.OrfoCartTable.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            cart = new ArrayList<>();
            while (c.moveToNext()) {
                OrderItem item = new OrderItem(c.getString(c.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_ID)),
                        c.getString(c.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_NAME)),
                        c.getDouble(c.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_PRICE)),
                        c.getInt(c.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY)),
                        c.getString(c.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_IMAGE_URL))
                );
                cart.add(item);

            }
        } finally {
            if (null != c)
                c.close();
        }

        return cart;
    }


    public static int getItemCount() {

        Cursor c = null;
        int count = 0;
        try {
            c = contentResolver.query(OrfoDbContract.OrfoCartTable.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            while (c.moveToNext()) {

                count += c.getInt(c.getColumnIndex(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY));

            }
        } finally {
            if (null != c)
                c.close();
        }

        return count;

    }

    public static int emptyCart() {
        return contentResolver.delete(OrfoDbContract.OrfoCartTable.CONTENT_URI, null, null);
    }

}
