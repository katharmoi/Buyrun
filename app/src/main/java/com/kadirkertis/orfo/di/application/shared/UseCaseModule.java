package com.kadirkertis.orfo.di.application.shared;

import com.kadirkertis.domain.interactor.product.GetProductsUseCase;
import com.kadirkertis.domain.interactor.product.GetSingleProductUseCase;
import com.kadirkertis.domain.repository.ProductsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 11/6/2017.
 */

@Module
public class UseCaseModule {

    @Provides
    @Singleton
    public static GetProductsUseCase provideGetProductsUseCase(ProductsRepository productsRepository){
        return new GetProductsUseCase(productsRepository);
    }

    @Provides
    @Singleton
    public static GetSingleProductUseCase provideSingleProductUseCase(ProductsRepository productsRepository){
        return  new GetSingleProductUseCase(productsRepository);
    }
}
