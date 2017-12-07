package com.kadirkertis.orfo.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Kadir Kertis on 13.4.2017.
 */

public class  Utility {
    public static int getColumCount(Context context, boolean isTwoPane) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (isTwoPane) {
            return (int) (dpWidth / 360);
        } else {
            return (int) (dpWidth / 180);
        }

    }

    public static int getSpacing(Context context, boolean isTwoPane) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpPlaceCardWidth = 180;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columnCount = getColumCount(context, isTwoPane);

        if (isTwoPane)
            return (int) (((dpWidth / 2 - columnCount * dpPlaceCardWidth) / (columnCount + 1)) * displayMetrics.density);
        else
            return (int) (((dpWidth - columnCount * dpPlaceCardWidth) / (columnCount + 1)) * displayMetrics.density);


    }
}
