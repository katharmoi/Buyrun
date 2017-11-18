package com.kadirkertis.orfo.di.application.shared;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.kadirkertis.orfo.BuildConfig;
import com.kadirkertis.orfo.di.application.AppScope;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Kadir Kertis on 11/13/2017.
 */

@Module
public class NetworkModule {

    @Provides
    @AppScope
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        //TODO register Idling
//        if (BuildConfig.DEBUG) {
//            IdlingResources.registerOkHttp(client);
//        }
        return client;
    }

    @AppScope
    @Provides
    public Picasso providePicasso(Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }
}
