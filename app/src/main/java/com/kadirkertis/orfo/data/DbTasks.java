package com.kadirkertis.orfo.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.OrderItem;
import com.kadirkertis.orfo.model.PlaceInfo;
import com.kadirkertis.orfo.services.AlarmReceiver;

import java.util.ArrayList;

/**
 * Created by Kadir Kertis on 5.3.2017.
 */

public final class DbTasks extends AsyncTask<DbTaskParams, Void, Void> {
    private ContentResolver mContentResolver;

    public static final int TASK_INSERT_TO_CART = 0;
    public static final int TASK_DELETE_FROM_CART = 1;
    public static final int TASK_UPDATE_CART = 2;
    public static final int TASK_ITEM_IN_CART = 3;
    public static final int TASK_GET_CART = 4;
    public static final int TASK_EMPTY_CART = 5;
    public static final int TASK_INSERT_ORDER = 6;
    public static final int TASK_FAV_PLACE = 7;
    public static final int TASK_DELETE_FROM_FAV_PLACE = 8;
    public static final int TASK_GET_ITEM_COUNT_IN_CART = 9;

    private ProgressBar mProgress;
    private Context mContext;


    public DbTasks(Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
        if (context instanceof AppCompatActivity) {
            mProgress = (ProgressBar) ((AppCompatActivity) context).findViewById(R.id.progress_spinner);
            if(mProgress != null){
                mProgress.setVisibility(View.GONE);
            }

        }

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mContext instanceof AppCompatActivity) {
            if(mProgress != null){
                mProgress.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    protected Void doInBackground(DbTaskParams... tasks) {
        DbTaskParams params = tasks[0];
        switch (params.getTaskId()) {
            case TASK_INSERT_TO_CART:
                insertToCart(params.getOrderItem());
                break;
            case TASK_DELETE_FROM_CART:
                deleteFromCart(params.getCartId());
                break;
            case TASK_UPDATE_CART:
                updateCart(params.getCartId(), params.getNewQuantity());
                break;
            case TASK_ITEM_IN_CART:
                isItemInCart(params.getOrderItem());
                break;
            case TASK_GET_CART:
                getCart();
                break;
            case TASK_EMPTY_CART:
                emptyCart();
                break;
            case TASK_INSERT_ORDER:
                insertOrder(params.getOrderFirebaseId(), params.getPlaceId(),
                        params.getDate(), params.getCustomerId(), params.getTableNumber());
                break;
            case TASK_FAV_PLACE:
                addToFavPlace(params.getPlaceInfo());
                break;
            case TASK_DELETE_FROM_FAV_PLACE:
                deleteFromFavPlaces(params.getPlaceInfo());
                break;
            case TASK_GET_ITEM_COUNT_IN_CART:
                getItemCountInCart(params.getItemCount());
                break;
            default:
                throw new UnsupportedOperationException("Unknown Task");
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mContext instanceof AppCompatActivity) {
            if(mProgress != null){
                mProgress.setVisibility(View.GONE);
            }

        }
    }

    private Uri insertToCart(OrderItem orderItem) {
        ContentValues singleItem = new ContentValues();
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_ID, orderItem.getFirebaseId());
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_NAME, orderItem.getItemName());
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY, orderItem.getQuantity());
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_PRICE, orderItem.getPrice());
        singleItem.put(OrfoDbContract.OrfoCartTable.COLUMN_IMAGE_URL, orderItem.getImageUrl());

        Uri insertUri =
                mContentResolver.insert(OrfoDbContract.OrfoCartTable.CONTENT_URI, singleItem);
        setCartAlarm();
        return insertUri;

    }

    private int deleteFromCart(int id) {
        int numDeleted = mContentResolver.delete(OrfoDbContract.OrfoCartTable.CONTENT_URI,
                OrfoDbContract.OrfoCartTable._ID + " =?",
                new String[]{"" + id});

        return numDeleted;

    }

    private boolean updateCart(int id, int newQuantity) {
        ContentValues updateQuantity = new ContentValues();
        updateQuantity.put(OrfoDbContract.OrfoCartTable.COLUMN_QUANTITY, newQuantity);
        int update = mContentResolver.update(OrfoDbContract.OrfoCartTable.CONTENT_URI,
                updateQuantity,
                OrfoDbContract.OrfoCartTable._ID + " =?",
                new String[]{"" + id});

        return update > 0;

    }

    private boolean isItemInCart(OrderItem item) {
        Cursor c = mContentResolver.query(OrfoDbContract.OrfoCartTable.CONTENT_URI,
                null,
                OrfoDbContract.OrfoCartTable.COLUMN_PRODUCT_ID + " =?",
                new String[]{item.getFirebaseId()},
                null);

        boolean isIn = c.getCount() > 0;
        c.close();
        return isIn;
    }

    private ArrayList<OrderItem> getCart() {

        Cursor c = null;
        ArrayList<OrderItem> cart;
        try {
            c = mContentResolver.query(OrfoDbContract.OrfoCartTable.CONTENT_URI,
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

    private void getItemCountInCart(int[] itemCount) {

        Cursor c = null;
        int count=0;
        try {
            c = mContentResolver.query(OrfoDbContract.OrfoCartTable.CONTENT_URI,
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

        itemCount[0] = count;

    }

    public static int numOfItems(ContentResolver contentResolver) {

        Cursor c = null;
        int count=0;
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

    private int emptyCart() {
        int numDeleted = mContentResolver.delete(OrfoDbContract.OrfoCartTable.CONTENT_URI, null, null);
        return numDeleted;
    }

    //Insert to Order table and order deails table
    //atomicly
    private void insertOrder(String orderFirebaseId, String placeId, long date, String customerId, String tableNumber) {
        ArrayList<OrderItem> orderedItems = getCart();
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
                mContentResolver.applyBatch(OrfoDbContract.CONTENT_AUTHORITY, orderAndOrderDetailsInsert);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isPlaceInDb(PlaceInfo placeInfo) {
        Cursor c = mContentResolver.query(OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_URI,
                null,
                OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID + " =?",
                new String[]{placeInfo.getId()},
                null);

        boolean isIn = c.getCount() > 0;
        c.close();
        return isIn;
    }

    private void addToFavPlace(PlaceInfo placeInfo) {
        //Todo add other colums rating vs...
        if (!isPlaceInDb(placeInfo)) {
            ContentValues placeInfoCV = new ContentValues();
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID, placeInfo.getId());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_NAME, placeInfo.getPlaceName());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_TYPE, placeInfo.getPlaceType());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ADDRESS, placeInfo.getAddress());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_PHONE, placeInfo.getPhone());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_IMG_URL, placeInfo.getImageUrl());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_LAT, placeInfo.getLatitude());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_LAT, placeInfo.getLongitude());
            mContentResolver.insert(OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_URI, placeInfoCV);
        }

    }

    private int deleteFromFavPlaces(PlaceInfo placeInfo) {

        int numDeleted = mContentResolver.delete(OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_URI,
                OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID + " =?",
                new String[]{"" + placeInfo.getId()});

        return numDeleted;
    }

    private void setCartAlarm() {
        AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent mAlarmIntent = PendingIntent.getBroadcast(mContext, 0, alarmIntent, 0);
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES, mAlarmIntent);
    }

    public boolean isPlaceInDb(String placeId) {
        Cursor c = mContentResolver.query(OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_URI,
                null,
                OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID + " =?",
                new String[]{placeId},
                null);

        boolean isIn = c.getCount() > 0;
        c.close();
        return isIn;
    }

}


