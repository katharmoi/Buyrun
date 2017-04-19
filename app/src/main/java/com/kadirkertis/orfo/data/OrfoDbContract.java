package com.kadirkertis.orfo.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kadir Kertis on 24.2.2017.
 */

public final class OrfoDbContract {

    public static final String CONTENT_AUTHORITY ="com.kadirkertis.orfo.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAV_PLACES = "fav_places";

    public static final String PATH_ORDERS="orders";

    public static final String PATH_ORDER_DETAILS ="order_details";

    public static final String PATH_CART = "cart";

    public static final class OrfoFavoritePlacesTable implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAV_PLACES)
                .build();

        // directory MIME
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_FAV_PLACES;

        //Single item MIME
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_FAV_PLACES;


        public static final String TABLE_NAME ="favorite_places";

        public static final String COLUMN_PLACE_ID = "place_id";
        public static final String COLUMN_PLACE_NAME = "place_name";
        public static final String COLUMN_PLACE_TYPE ="place_type";
        public static final String COLUMN_PLACE_SUBTYPE ="place_subtype";
        public static final String COLUMN_PLACE_ADDRESS = "place_address";
        public static final String COLUMN_PLACE_RATING = "place_rating";
        public static final String COLUMN_PLACE_PHONE ="place_phone";
        public static final String COLUMN_PLACE_IMG_URL="place_image_url";
        public static final String COLUMN_PLACE_LAT = "place_lat";
        public static final String COLUMN_PLACE_LNG ="place_lng";

        public static Uri buildFavPlacesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }


    }

    public static final class OrfoOrdersTable implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ORDERS)
                .build();

        // directory MIME
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_ORDERS;

        //Single item MIME
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_ORDERS;


        public static final String TABLE_NAME ="orders";

        public static final String COLUMN_ORDER_FIREBASE_ID = "order_firebase_id";
        public static final String COLUMN_PLACE_ID = "place_id";
        public static final String COLUMN_ORDER_TIME = "order_time";
        public static final String COLUMN_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_TABLE_NUMBER = "table_number";

        public static Uri buildOrdersUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }


    }

    public static final class OrfoOrderDetailsTable implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ORDER_DETAILS)
                .build();

        // directory MIME
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_ORDER_DETAILS;

        //Single item MIME
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_ORDER_DETAILS;


        public static final String TABLE_NAME ="order_details";

        public static final String COLUMN_ORDER_FIREBASE_ID = "order_firebase_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE ="price";
        public static final String COLUMN_ORDER_ID ="order_id";

        public static Uri buildOrderDetailsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }


    }

    public static final class OrfoCartTable implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CART)
                .build();

        //// directory MIME
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_CART;

        //Single item MIME
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_CART;


        public static final String TABLE_NAME ="cart";

        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE ="price";
        public static final String COLUMN_IMAGE_URL ="image_url";


        public static Uri buildCartUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }


    }
}
