package com.kadirkertis.orfo.ui.Router;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public interface Router {

    void showCartScreen(@NonNull AppCompatActivity source);
    void showProductsScreen(@NonNull AppCompatActivity source);
    void showMainScreen(@NonNull AppCompatActivity source);
    void showChatScreen(@NonNull AppCompatActivity source, @Nullable String storeId);
    void showPreferencesScreen(@NonNull AppCompatActivity source);
}
