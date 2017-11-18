package com.kadirkertis.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.kadirkertis.data.R;


public class OrfoContentProvider extends ContentProvider {

    public static final int ORDERS = 100;
    public static final int ORDERS_WITH_ID = 101;
    public static final int ORDERS_WITH_FIREBASE_ID = 102;
    public static final int ORDER_DETAILS = 103;
    public static final int ORDER_DETAILS_WITH_ID = 104;

    public static final int CART = 110;
    public static final int FAV_PLACES = 120;
    public static final int FAV_PLACES_WITH_ID = 121;

    private static final SQLiteQueryBuilder sOrdersQueryBuilder;

    static {
        sOrdersQueryBuilder = new SQLiteQueryBuilder();

        sOrdersQueryBuilder.setTables(
                OrfoDbContract.OrfoOrdersTable.TABLE_NAME
                        + " INNER JOIN "
                        + OrfoDbContract.OrfoOrderDetailsTable.TABLE_NAME
                        + " ON "
                        + OrfoDbContract.OrfoOrdersTable.TABLE_NAME
                        + "."
                        + OrfoDbContract.OrfoOrdersTable._ID
                        + " = "
                        + OrfoDbContract.OrfoOrderDetailsTable.TABLE_NAME
                        + "."
                        + OrfoDbContract.OrfoOrderDetailsTable.COLUMN_ORDER_ID);
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = OrfoDbContract.CONTENT_AUTHORITY;

        //Orders
        matcher.addURI(authority, OrfoDbContract.PATH_ORDERS, ORDERS);
        matcher.addURI(authority, OrfoDbContract.PATH_ORDERS + "/#", ORDERS_WITH_ID);
        matcher.addURI(authority, OrfoDbContract.PATH_ORDERS + "/*", ORDERS_WITH_FIREBASE_ID);

        //Order Details
        matcher.addURI(authority, OrfoDbContract.PATH_ORDER_DETAILS, ORDER_DETAILS);
        matcher.addURI(authority, OrfoDbContract.PATH_ORDER_DETAILS + "/#", ORDER_DETAILS_WITH_ID);


        //Cart
        matcher.addURI(authority, OrfoDbContract.PATH_CART, CART);

        //Fav Places

        matcher.addURI(authority, OrfoDbContract.PATH_FAV_PLACES, FAV_PLACES);
        matcher.addURI(authority, OrfoDbContract.PATH_FAV_PLACES + "/#", FAV_PLACES_WITH_ID);

        return matcher;
    }


    private OrfoDbHelper mDbHelper;

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case ORDERS:
                return OrfoDbContract.OrfoOrdersTable.CONTENT_DIR_TYPE;
            case ORDERS_WITH_ID:
                return OrfoDbContract.OrfoOrdersTable.CONTENT_ITEM_TYPE;
            case ORDER_DETAILS:
                return OrfoDbContract.OrfoOrderDetailsTable.CONTENT_DIR_TYPE;
            case ORDER_DETAILS_WITH_ID:
                return OrfoDbContract.OrfoOrderDetailsTable.CONTENT_ITEM_TYPE;
            case CART:
                return OrfoDbContract.OrfoCartTable.CONTENT_DIR_TYPE;
            case FAV_PLACES:
                return OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_DIR_TYPE;
            case FAV_PLACES_WITH_ID:
                return OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + ": " + uri);

        }
    }

    private static final String orderWithDateSelection =
            OrfoDbContract.OrfoOrdersTable.TABLE_NAME
                    +"."
                    + OrfoDbContract.OrfoOrdersTable.COLUMN_ORDER_TIME + " >= ?";

    private static final String orderWithFirebaseIdSelection =
            OrfoDbContract.OrfoOrdersTable.TABLE_NAME
            +"."
            + OrfoDbContract.OrfoOrdersTable.COLUMN_ORDER_FIREBASE_ID + " = ?" ;

    private static final String ordersWithIdSelection =
            OrfoDbContract.OrfoOrdersTable.TABLE_NAME
            +"."
            + OrfoDbContract.OrfoOrdersTable._ID +" = ? ";


    public OrfoContentProvider() {
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new OrfoDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor responseCursor;

        switch (sUriMatcher.match(uri)) {
            case ORDERS:
                responseCursor = sOrdersQueryBuilder.query(mDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case ORDERS_WITH_ID:
                responseCursor = sOrdersQueryBuilder.query(mDbHelper.getReadableDatabase(),
                        projection,
                        ordersWithIdSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case ORDERS_WITH_FIREBASE_ID:
                responseCursor = sOrdersQueryBuilder.query(mDbHelper.getReadableDatabase(),
                        projection,
                        orderWithFirebaseIdSelection ,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case ORDER_DETAILS:
                responseCursor = mDbHelper.getReadableDatabase()
                        .query(OrfoDbContract.OrfoOrderDetailsTable.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder
                        );
                break;

            case ORDER_DETAILS_WITH_ID:
                responseCursor = mDbHelper.getReadableDatabase()
                        .query(OrfoDbContract.OrfoOrderDetailsTable.TABLE_NAME,
                                projection,
                                OrfoDbContract.OrfoOrderDetailsTable.COLUMN_ORDER_FIREBASE_ID + " =?",
                                new String[]{String.valueOf(ContentUris.parseId(uri))}, //TODO This is normal id
                                null,
                                null,
                                sortOrder
                        );
                break;

            case CART:
                responseCursor = mDbHelper.getReadableDatabase()
                        .query(OrfoDbContract.OrfoCartTable.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;

            case FAV_PLACES:
                responseCursor = mDbHelper.getReadableDatabase()
                        .query(OrfoDbContract.OrfoFavoritePlacesTable.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;

            case FAV_PLACES_WITH_ID:
                responseCursor = mDbHelper.getReadableDatabase()
                        .query(OrfoDbContract.OrfoFavoritePlacesTable.TABLE_NAME,
                                projection,
                                OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID + " =?",
                                new String[]{String.valueOf(ContentUris.parseId(uri))},//// TODO: 28.2.2017 Firebase ID
                                null,
                                null,
                                sortOrder);
                break;


            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + ": " + uri);


        }
        responseCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return responseCursor;

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int numDeleted;

        switch (sUriMatcher.match(uri)) {
            case FAV_PLACES:
                numDeleted = db.delete(OrfoDbContract.OrfoFavoritePlacesTable.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case ORDERS:
                numDeleted = db.delete(OrfoDbContract.OrfoOrdersTable.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case ORDER_DETAILS:
                numDeleted = db.delete(OrfoDbContract.OrfoOrderDetailsTable.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CART:
                numDeleted = db.delete(OrfoDbContract.OrfoCartTable.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + ": " + uri);
        }

        if (numDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numDeleted;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int numInserted;
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case FAV_PLACES: {
                long id = db.insert(OrfoDbContract.OrfoFavoritePlacesTable.TABLE_NAME,
                        null,
                        values);
                if (id > 0) {
                    returnUri = OrfoDbContract.OrfoFavoritePlacesTable.buildFavPlacesUri(id);
                } else {
                    throw new SQLException(getContext().getString(R.string.db_fail_insert));
                }
                break;
            }
            case ORDERS: {
                long id = db.insert(OrfoDbContract.OrfoOrdersTable.TABLE_NAME,
                        null,
                        values);
                if (id > 0) {
                    returnUri = OrfoDbContract.OrfoOrdersTable.buildOrdersUri(id);
                } else {
                    throw new SQLException(getContext().getString(R.string.db_fail_insert));
                }

                break;
            }

            case ORDER_DETAILS: {
                long id = db.insert(OrfoDbContract.OrfoOrderDetailsTable.TABLE_NAME,
                        null,
                        values);
                if (id > 0) {
                    returnUri = OrfoDbContract.OrfoOrderDetailsTable.buildOrderDetailsUri(id);
                } else {
                    throw new SQLException(getContext().getString(R.string.db_fail_insert));
                }
                break;

            }
            case CART: {
                long id = db.insert(OrfoDbContract.OrfoCartTable.TABLE_NAME,
                        null,
                        values);
                if (id > 0) {
                    returnUri = OrfoDbContract.OrfoCartTable.buildCartUri(id);
                } else {
                    throw new SQLException(getContext().getString(R.string.db_fail_insert));
                }
                break;
            }

            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + ": " + uri);


        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int numUpdated;

        switch (sUriMatcher.match(uri)) {
            case FAV_PLACES:
                numUpdated = db.update(OrfoDbContract.OrfoFavoritePlacesTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ORDERS:
                numUpdated = db.update(OrfoDbContract.OrfoOrdersTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ORDER_DETAILS:
                numUpdated = db.update(OrfoDbContract.OrfoOrderDetailsTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CART:
                numUpdated = db.update(OrfoDbContract.OrfoCartTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + ": " + uri);
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numUpdated;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case FAV_PLACES : {
                db.beginTransaction();
                int numInserted = 0;
                try {
                    for (ContentValues val : values) {
                        long id = db.insert(OrfoDbContract.OrfoFavoritePlacesTable.TABLE_NAME,
                                null,
                                val);
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return numInserted;
            }
            case ORDERS : {
                db.beginTransaction();
                int numInserted = 0;
                try {
                    for (ContentValues val : values) {
                        long id = db.insert(OrfoDbContract.OrfoOrdersTable.TABLE_NAME,
                                null,
                                val);
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return numInserted;
            }

            case ORDER_DETAILS : {
                db.beginTransaction();
                int numInserted = 0;
                try {
                    for (ContentValues val : values) {
                        long id = db.insert(OrfoDbContract.OrfoOrderDetailsTable.TABLE_NAME,
                                null,
                                val);
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return numInserted;
            }

            default:
                return super.bulkInsert(uri,values);
        }

    }
}
