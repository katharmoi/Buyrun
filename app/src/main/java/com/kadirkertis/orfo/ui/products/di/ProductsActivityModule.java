package com.kadirkertis.orfo.ui.products.di;

import android.support.v4.app.FragmentManager;

import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.data.prodcuts.ProductsRepositoryImpl;
import com.kadirkertis.orfo.ui.base.activity.ActivityScope;
import com.kadirkertis.orfo.ui.products.ProductsActivity;
import com.kadirkertis.orfo.ui.products.ProductsViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 10/31/2017.
 */
@Module
public class ProductsActivityModule {

    @Provides
    @ActivityScope
    ProductsViewModel provideProductsViewModel() {
        return new ProductsViewModel();
    }

    @Provides
    @ActivityScope
    FragmentManager provideFragmentManager(ProductsActivity activity){
        return activity.getSupportFragmentManager();
    }


}
