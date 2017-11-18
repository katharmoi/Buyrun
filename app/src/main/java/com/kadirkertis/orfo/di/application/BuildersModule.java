package com.kadirkertis.orfo.di.application;

import com.kadirkertis.orfo.ui.MainActivity;
import com.kadirkertis.orfo.ui.base.activity.ActivityScope;
import com.kadirkertis.orfo.ui.products.ProductsActivity;
import com.kadirkertis.orfo.ui.products.ProductsFragment;
import com.kadirkertis.orfo.ui.products.di.ProductsActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Kadir Kertis on 27.10.2017.
 */
//Used to bind all subcomponents in the app
@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ProductsActivityModule.class})
    abstract ProductsActivity bindProductsActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = ProductsActivityModule.class)
    abstract ProductsFragment bindProductsFragment();


}
