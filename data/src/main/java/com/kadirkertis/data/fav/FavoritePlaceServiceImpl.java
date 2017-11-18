package com.kadirkertis.data.fav;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.kadirkertis.data.database.OrfoDbContract;
import com.kadirkertis.data.model.DataPlace;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public class FavoritePlaceServiceImpl {
    private static ContentResolver contentResolver;

    public FavoritePlaceServiceImpl(){
        contentResolver = contentResolver;
    }

    public static boolean isPlaceInDb(DataPlace place) {
        Cursor c = contentResolver.query(OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_URI,
                null,
                OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID + " =?",
                new String[]{place.getId()},
                null);

        boolean isIn = c.getCount() > 0;
        c.close();
        return isIn;
    }

    public static void addToFavPlace(DataPlace place) {
        //Todo add other colums rating vs...
        if (!isPlaceInDb(place)) {
            ContentValues placeInfoCV = new ContentValues();
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID, place.getId());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_NAME, place.getPlaceName());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_TYPE, place.getPlaceType());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ADDRESS, place.getAddress());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_PHONE, place.getPhone());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_IMG_URL, place.getImageUrl());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_LAT, place.getLatitude());
            placeInfoCV.put(OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_LAT, place.getLongitude());
            contentResolver.insert(OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_URI, placeInfoCV);
        }

    }

    public static int deleteFromFavPlaces(DataPlace place) {

        int numDeleted = contentResolver.delete(OrfoDbContract.OrfoFavoritePlacesTable.CONTENT_URI,
                OrfoDbContract.OrfoFavoritePlacesTable.COLUMN_PLACE_ID + " =?",
                new String[]{"" + place.getId()});

        return numDeleted;
    }


}
