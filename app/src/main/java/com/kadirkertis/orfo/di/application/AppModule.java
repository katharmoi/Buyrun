package com.kadirkertis.orfo.di.application;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kadirkertis.orfo.App;
import com.kadirkertis.orfo.services.UserService;
import com.kadirkertis.orfo.ui.Router.Router;
import com.kadirkertis.orfo.ui.Router.RouterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 26.10.2017.
 */

@Module
public class AppModule {

    @Provides
    @AppScope
    Context provideAppContext(App application) {
        return application;
    }

    @Provides
    @AppScope
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @AppScope
    public Router provideRouter(){
        return new RouterImpl();
    }

    @Provides
    @AppScope
    UserService provideUserService() {
        return new UserService();
    }

}
