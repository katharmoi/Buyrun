package com.kadirkertis.orfo.di.application.shared;

import com.kadirkertis.orfo.di.application.AppScope;
import com.kadirkertis.orfo.utils.ActivityUtils;
import com.kadirkertis.orfo.utils.ActivityUtilsImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 11/6/2017.
 */
@Module
public final class UtilsModule {

    @Provides
    @AppScope
    ActivityUtils provideActivityUtils(){
        return new ActivityUtilsImpl();
    }
}
