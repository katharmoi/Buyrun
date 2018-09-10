package com.kadirkertis.orfo.ui.products.di;

import android.support.v4.app.FragmentManager;

import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.data.prodcuts.ProductsRepositoryImpl;
import com.kadirkertis.domain.interactor.product.GetProductsUseCase;
import com.kadirkertis.domain.interactor.product.GetSingleProductUseCase;
import com.kadirkertis.domain.interactor.product.repository.ProductsRepository;
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
    @ProductsActivityScope
    ProductsViewModel provideProductsViewModel() {
        return new ProductsViewModel();
    }

    @Provides
    @ProductsActivityScope
    FragmentManager provideFragmentManager(ProductsActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @Provides
    @ProductsActivityScope
    public static ProductsRepository provideProductsRepository(FirebaseDatabase db) {
        return new ProductsRepositoryImpl(db);
    }

    @Provides
    @ProductsActivityScope
    public static GetProductsUseCase provideGetProductsUseCase(ProductsRepository productsRepository) {
        return new GetProductsUseCase(productsRepository);
    }

    @Provides
    @ProductsActivityScope
    public static GetSingleProductUseCase provideSingleProductUseCase(ProductsRepository productsRepository) {
        return new GetSingleProductUseCase(productsRepository);
    }


}
