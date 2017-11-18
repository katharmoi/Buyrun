package com.kadirkertis.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kadirkertis.data.database.OrfoDbContract.OrfoFavoritePlacesTable;
import com.kadirkertis.data.database.OrfoDbContract.OrfoOrdersTable;
import com.kadirkertis.data.database.OrfoDbContract.OrfoOrderDetailsTable;
import com.kadirkertis.data.database.OrfoDbContract.OrfoCartTable;

/**
 * Created by Kadir Kertis on 24.2.2017.
 */

public class OrfoDbHelper extends SQLiteOpenHelper {

    private static final int ORFO_DB_VERSION = 1;

    static final String ORFO_DB_NAME = "orfo.db";

    public OrfoDbHelper(Context context) {
        super(context, ORFO_DB_NAME, null, ORFO_DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVPLACES_TABLE = "CREATE TABLE " + OrfoFavoritePlacesTable.TABLE_NAME + " (" +
                OrfoFavoritePlacesTable._ID + " INTEGER PRIMARY KEY," +
                OrfoFavoritePlacesTable.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                OrfoFavoritePlacesTable.COLUMN_PLACE_NAME + " TEXT NOT NULL, " +
                OrfoFavoritePlacesTable.COLUMN_PLACE_TYPE + " INTEGER NOT NULL, " +
                OrfoFavoritePlacesTable.COLUMN_PLACE_SUBTYPE + " TEXT, " +
                OrfoFavoritePlacesTable.COLUMN_PLACE_ADDRESS + " TEXT NOT NULL, " +
                OrfoFavoritePlacesTable.COLUMN_PLACE_RATING + " REAL, " +
                OrfoFavoritePlacesTable.COLUMN_PLACE_PHONE + " TEXT, " +
                OrfoFavoritePlacesTable.COLUMN_PLACE_IMG_URL + " TEXT, "+
                OrfoFavoritePlacesTable.COLUMN_PLACE_LAT + " REAL, "+
                OrfoFavoritePlacesTable.COLUMN_PLACE_LNG + " REAL "
                + " );";

        final String SQL_CREATE_ORDERS_TABLE = "CREATE TABLE " + OrfoOrdersTable.TABLE_NAME + " (" +
                OrfoOrdersTable._ID + " INTEGER PRIMARY KEY, " +
                OrfoOrdersTable.COLUMN_ORDER_FIREBASE_ID + " TEXT NOT NULL, " +
                OrfoOrdersTable.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                OrfoOrdersTable.COLUMN_ORDER_TIME + " INTEGER NOT NULL, " +
                OrfoOrdersTable.COLUMN_CUSTOMER_ID + " TEXT NOT NULL, " +
                OrfoOrdersTable.COLUMN_TABLE_NUMBER + " INTEGER NOT NULL" +

                 " );";


        final String SQL_CREATE_ORDER_DETAILS_TABLE = "CREATE TABLE " + OrfoOrderDetailsTable.TABLE_NAME + "( " +
                OrfoOrderDetailsTable._ID + " INTEGER PRIMARY KEY," +
                OrfoOrderDetailsTable.COLUMN_ORDER_FIREBASE_ID + " TEXT NOT NULL, " +
                OrfoOrderDetailsTable.COLUMN_PRODUCT_ID +  " TEXT NOT NULL, " +
                OrfoOrderDetailsTable.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                OrfoOrderDetailsTable.COLUMN_PRICE + " REAL NOT NULL, " +
                OrfoOrderDetailsTable.COLUMN_ORDER_ID + " INTEGER NOT NULL, "

                +"FOREIGN KEY(" +OrfoOrderDetailsTable.COLUMN_ORDER_ID +") REFERENCES " +
                OrfoOrdersTable.TABLE_NAME +"(" +OrfoOrdersTable._ID +")" +

                " );" ;

        final String SQL_CREATE_CART_TABLE = "CREATE TABLE " + OrfoCartTable.TABLE_NAME + "( " +
                OrfoCartTable._ID + " INTEGER PRIMARY KEY, " +
                OrfoCartTable.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                OrfoCartTable.COLUMN_PRODUCT_ID + " TEXT NOT NULL, " +
                OrfoCartTable.COLUMN_PRICE + " REAL NOT NULL, " +
                OrfoCartTable.COLUMN_QUANTITY  + " INTEGER NOT NULL, " +
                OrfoCartTable.COLUMN_IMAGE_URL + " TEXT" +
                " );";


        sqLiteDatabase.execSQL(SQL_CREATE_FAVPLACES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ORDERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ORDER_DETAILS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrfoFavoritePlacesTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrfoOrdersTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrfoOrderDetailsTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrfoCartTable.TABLE_NAME);

    }
}
