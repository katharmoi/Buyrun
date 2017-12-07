package com.kadirkertis.data.session;

import android.content.SharedPreferences;

import com.kadirkertis.domain.utils.Constants;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public class SessionServiceImpl implements SessionService {
    private SharedPreferences preferences;

    public SessionServiceImpl(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Completable setPlace(String placeId, String tableNumber) {
        return Completable.fromAction(() -> setPlaceBlocking(placeId, tableNumber))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable setUser(String userId) {
        return Completable.fromAction(() -> setUserBlocking(userId))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Maybe<String> getPlace() {
        return Maybe.fromCallable(this::getPlaceBlocking);
    }

    @Override
    public Maybe<String> getUser() {
        return Maybe.fromCallable(this::getUserBlocking);
    }


    private void setPlaceBlocking(String placeId, String tableNumber) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREFS_CHECKED_IN_PLACE, placeId);
        editor.putString(Constants.PREFS_CHECKED_IN_TABLE_NUMBER, tableNumber);
        editor.putLong(Constants.PREFS_LAST_CHECK_IN_TIME, System.currentTimeMillis());
        editor.apply();
    }


    private void setUserBlocking(String userId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREFS_USER_ID, userId);
        editor.apply();

    }

    private String getPlaceBlocking() {
        return preferences.getString(Constants.PREFS_CHECKED_IN_PLACE, null);

    }


    private String getUserBlocking() {
        return preferences.getString(Constants.PREFS_USER_ID, null);

    }


}
