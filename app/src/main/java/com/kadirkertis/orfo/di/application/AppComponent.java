package com.kadirkertis.orfo.di.application;


import com.kadirkertis.orfo.App;
import com.kadirkertis.orfo.di.application.shared.NetworkModule;
import com.kadirkertis.orfo.di.application.shared.RepositoriesModule;
import com.kadirkertis.orfo.di.application.shared.ServicesModule;
import com.kadirkertis.orfo.di.application.shared.UseCaseModule;
import com.kadirkertis.orfo.di.application.shared.UtilsModule;
import com.kadirkertis.orfo.firebase.FirebaseModule;

import dagger.BindsInstance;
import dagger.Component;
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
        UseCaseModule.class,
        UtilsModule.class,
        NetworkModule.class,
        ServicesModule.class,
        RepositoriesModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);

        AppComponent build();
    }

    void inject(App app);
}
