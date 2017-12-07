package com.kadirkertis.orfo.di.application.shared;

import com.google.zxing.integration.android.IntentResult;
import com.kadirkertis.domain.interactor.checkIn.CheckUserInUseCase;
import com.kadirkertis.domain.interactor.checkIn.CheckUserOutUseCase;
import com.kadirkertis.domain.interactor.product.GetProductsUseCase;
import com.kadirkertis.domain.interactor.product.GetSingleProductUseCase;
import com.kadirkertis.domain.interactor.qr.ParseQrCodeUseCase;
import com.kadirkertis.domain.repository.PlaceRepository;
import com.kadirkertis.domain.repository.ProductsRepository;
import com.kadirkertis.domain.repository.UserCheckingRepository;
import com.kadirkertis.domain.services.QRCodeService;
import com.kadirkertis.domain.services.UserTrackingService;
import com.kadirkertis.orfo.di.application.AppScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 11/6/2017.
 */

@Module
public class UseCaseModule {

    @Provides
    @AppScope
    public static GetProductsUseCase provideGetProductsUseCase(ProductsRepository productsRepository){
        return new GetProductsUseCase(productsRepository);
    }

    @Provides
    @AppScope
    public static GetSingleProductUseCase provideSingleProductUseCase(ProductsRepository productsRepository){
        return  new GetSingleProductUseCase(productsRepository);
    }

    @Provides
    @AppScope
    public static CheckUserInUseCase provideCheckUserInUseCase(UserCheckingRepository userCheckingRepository){
        return new CheckUserInUseCase(userCheckingRepository);
    }

    @Provides
    @AppScope
    public static CheckUserOutUseCase provideCheckUserOutUseCase(UserCheckingRepository userCheckingRepository){
        return new CheckUserOutUseCase(userCheckingRepository);
    }


}
