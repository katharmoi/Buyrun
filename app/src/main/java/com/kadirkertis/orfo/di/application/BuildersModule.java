package com.kadirkertis.orfo.di.application;

import com.kadirkertis.orfo.di.activity.ActivityScope;
import com.kadirkertis.orfo.di.fragment.FragmentScope;
import com.kadirkertis.orfo.ui.main.MainActivity;
import com.kadirkertis.orfo.ui.main.di.MainActivityModule;
import com.kadirkertis.orfo.ui.main.fragments.places.PlacesFragment;
import com.kadirkertis.orfo.ui.main.fragments.places.di.PlacesFragmentModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Kadir Kertis on 27.10.2017.
 */
//Used to bind all subcomponents in the app
@Module
public abstract class BuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {MainActivityModule.class, PlacesFragmentModule.class})
    abstract MainActivity bindMainActivity();

    @FragmentScope
    @ContributesAndroidInjector
    abstract PlacesFragment bindPlacesFragment();


//    @ContributesAndroidInjector(modules = ProductsActivityModule.class)
//    abstract ProductsActivity bindProductsActivity();
//
//    @ContributesAndroidInjector(modules = ProductsActivityModule.class)
//    abstract ProductsFragment bindProductsFragment();
}
