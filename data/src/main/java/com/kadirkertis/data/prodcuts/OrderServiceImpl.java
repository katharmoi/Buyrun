package com.kadirkertis.data.prodcuts;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.kadirkertis.data.cart.CartServiceImpl;
import com.kadirkertis.data.database.OrfoDbContract;
import com.kadirkertis.domain.interactor.product.model.OrderItem;

import java.util.ArrayList;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public class OrderServiceImpl {

    private static ContentResolver contentResolver;

    public OrderServiceImpl(ContentResolver contentResolver) {
        contentResolver = contentResolver;
    }

    //Insert to Order table and order deails table
    //atomicly
    public static void insertOrder(String orderFirebaseId, String placeId, long date, String customerId, String tableNumber) {
        ArrayList<OrderItem> orderedItems = CartServiceImpl.getCart();
        if (!orderedItems.isEmpty()) {
            ArrayList<ContentProviderOperation> orderAndOrderDetailsInsert
                    = new ArrayList<>();

            int orderId = orderAndOrderDetailsInsert.size();
            orderAndOrderDetailsInsert.add(
                    ContentProviderOperation.newInsert(OrfoDbContract.OrfoOrdersTable.CONTENT_URI)
                            .withValue(OrfoDbContract.OrfoOrdersTable.COLUMN_ORDER_FIREBASE_ID, orderFirebaseId)
                            .withValue(OrfoDbContract.OrfoOrdersTable.COLUMN_PLACE_ID, placeId)
                            .withValue(OrfoDbContract.OrfoOrdersTable.COLUMN_ORDER_TIME, date)
                            .withValue(OrfoDbContract.OrfoOrdersTable.COLUMN_CUSTOMER_ID, customerId)
                            .withValue(OrfoDbContract.OrfoOrdersTable.COLUMN_TABLE_NUMBER, tableNumber)
                            .build());
            for (OrderItem singleItem : orderedItems) {
                orderAndOrderDetailsInsert.add(
                        ContentProviderOperation.newInsert(OrfoDbContract.OrfoOrderDetailsTable.CONTENT_URI)
                                .withValue(OrfoDbContract.OrfoOrderDetailsTable.COLUMN_ORDER_FIREBASE_ID, orderFirebaseId)
                                .withValue(OrfoDbContract.OrfoOrderDetailsTable.COLUMN_PRODUCT_ID, singleItem.getFirebaseId())
                                .withValue(OrfoDbContract.OrfoOrderDetailsTable.COLUMN_QUANTITY, singleItem.getQuantity())
                                .withValue(OrfoDbContract.OrfoOrderDetailsTable.COLUMN_PRICE, singleItem.getPrice())
                                .withValueBackReference(OrfoDbContract.OrfoOrdersTable._ID, orderId)
                                .build()
                );
            }

            try {
                contentResolver.applyBatch(OrfoDbContract.CONTENT_AUTHORITY, orderAndOrderDetailsInsert);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }

        }
    }
}
