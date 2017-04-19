package com.kadirkertis.orfo.utilities;

/**
 * Created by Kadir Kertis on 18.2.2017.
 */

public class Constants {

    public static final String ORFO_PREFIX = "orfo_qr_code";

    public static final String DB_PLACES = "places";
    public static final String TABLE_PRODUCTS = "products";
    public static final String DB_PLACE_LIST ="place_list" ;
    public static final String TABLE_PLACE_CURRENT_CHECKED_INS = "current_checked_in";
    public static final String TABLE_PLACE_CHECKED_INS = "checked_in";
    public static final String TABLE_COMPANY_INFO = "company_info";
    public static final String TABLE_PLACE_PENDING_ORDERS = "pending_orders";
    public static final String TABLE_PLACE_REVIEWS ="reviews" ;
    public static final String TABLE_USER_REVIEWS ="reviews" ;
    public static final String PLACE_MESSAGES_TABLE ="messages" ;


    public static final String DB_USERS ="users";
    public static final String TABLE_USER_CHECKED_IN_PLACES = "checked_in";
    public static final String TABLE_USER_ORDERS = "orders";

    //Firebase Storage Constants
    public static final String STORAGE_URL="gs://atyourservice-5b5b7.appspot.com";
    public static final String STORAGE_ROOT="places";
    public static final String STORAGE_IMAGES ="images";
    public static final String STORAGE_PRODUCT_IMAGES ="products";
    public static final String STORAGE_TABLE_QR_IMAGES ="qr_images" ;

     /*
    *Category & subcategory
     */

    public static final String[] MAIN_CATEGORY = {"Foods","Beverages"};
    public static final String[] SUB_CATEGORY_FOODS ={"Starters","Main Course","House Specials","Desserts"};
    public static final String[] SUB_CATEGORY_BEVERAGES ={"Alcoholic","Non Alcoholic"};

    public static final int INDEX_MAIN_FOODS = 0;
    public static final int INDEX_MAIN_BEVERAGES = 1;

    public static final int INDEX_FOODS_STARTERS = 0;
    public static final int INDEX_FOODS_MAIN_COURSE = 1;
    public static final int INDEX_FOODS_SPECIALS = 2;
    public static final int INDEX_FOODS_DESSERTS = 3;

    public static final int INDEX_BVRGS_ALCOHOLIC = 0;
    public static final int INDEX_BVRGS_NONALCOHOLIC = 1;

    //Checked in stores

    public static final String CHECKED_IN_STORE_ID = "checked_in_store_id";
    public static final String CHECKED_IN_TABLE_NUMBER = "checked_in_table_number";

    public static final String PREFS_CHECKED_IN_PLACE ="checked_in_place_prefs" ;

    public static final String PREFS_CHECKED_IN_PLACE_ID ="last_checked_in_place_id" ;
    public static final String PREFS_CHECKED_IN_TABLE_NUMBER ="last_checked_in_table_number" ;
    public static final String PREFS_LAST_CHECK_IN_TIME ="last_check_in_time" ;
    public static final String PREFS_USER_AT_PLACE = "user_at_place";
    public static final String PREFS_ORDER_TIME ="last_order_time" ;



    //Actions
    public static final String  ACTION_USER_EXIT_PLACE ="ACTION_USER_EXIT_PLACE" ;
    public static final String ACTION_USER_AT_PLACE = "ACTION_USER_AT_PLACE";

    public static final String PROPERTY_TIME_ADDED ="time_added";
    public static final String PREFS_USER_ID = "user_id";

    public static final String EXTRA_PLACE_LAT_LONG ="extra_place_lat_long" ;
    public static final String EXTRA_PLACE_ID ="place_id" ;


    //Extras
    public static final String PLACE_ID_EXTRA = "placeId";
    public static final String PLACE_NAME_EXTRA ="place_name" ;
    public static final String PLACE_LAT_LONG_EXTRA ="place_lat_long" ;


    public static final String ACTION_WIDGET_DATA_FETCHED ="com.kadirkertis.orfo.WIDGET_DATA_FETCHED" ;

    //Typefaces
    public static final String TYPEFACE_COND_BOLD = "cond_bold";
    public static final String TYPEFACE_COND_ITALIC = "cond_italic";
    public static final String TYPEFACE_COND_LIGHT = "cond_light";

    public static final String EXTRA_USER_ID = "user_id";
}
