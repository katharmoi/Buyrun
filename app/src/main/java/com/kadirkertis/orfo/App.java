package com.kadirkertis.orfo;

import com.kadirkertis.orfo.di.application.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import rx_activity_result2.RxActivityResult;
import timber.log.Timber;

/**
 * Created by Kadir Kertis on 26.10.2017.
 */

public class App extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });
        RxActivityResult.register(this);

    }

    @Override
    protected AndroidInjector<? extends App> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }

}
