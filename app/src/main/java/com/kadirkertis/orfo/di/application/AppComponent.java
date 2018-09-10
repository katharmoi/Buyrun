package com.kadirkertis.orfo.di.application;


import com.kadirkertis.orfo.App;
import com.kadirkertis.orfo.di.application.shared.NetworkModule;
import com.kadirkertis.orfo.di.application.shared.ServicesModule;
import com.kadirkertis.orfo.di.application.shared.UtilsModule;
import com.kadirkertis.orfo.di.application.shared.FirebaseModule;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Kadir Kertis on 26.10.2017.
 */
@AppScope
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        BuildersModule.class,
        FirebaseModule.class,
        UtilsModule.class,
        NetworkModule.class,
        ServicesModule.class
})
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {
    }
}
