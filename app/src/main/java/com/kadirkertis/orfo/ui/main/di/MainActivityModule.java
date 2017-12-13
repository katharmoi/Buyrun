package com.kadirkertis.orfo.ui.main.di;

import com.kadirkertis.device.location.UserTrackingServiceImpl;
import com.kadirkertis.device.qr.QRCodeServiceImpl;
import com.kadirkertis.domain.interactor.qr.ParseQrCodeUseCase;
import com.kadirkertis.domain.repository.PlaceRepository;
import com.kadirkertis.domain.repository.ProductsRepository;
import com.kadirkertis.domain.services.qr.QRCodeService;
import com.kadirkertis.domain.services.UserTrackingService;
import com.kadirkertis.orfo.ui.base.activity.ActivityScope;
import com.kadirkertis.orfo.ui.main.MainActivity;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 12/1/2017.
 */
@Module
public class MainActivityModule {
    @Provides
    @ActivityScope
    public static QRCodeService provideQrCodeService(MainActivity activity) {
        return new QRCodeServiceImpl(activity);
    }

    @Provides
    @ActivityScope
    public static RxPermissions provideRxPermissions(MainActivity activity){return new RxPermissions(activity);}

    @Provides
    @ActivityScope
    public static UserTrackingService provideUserTrackingService(RxLocation location, RxPermissions rxPermissions) {
        return new UserTrackingServiceImpl(location, rxPermissions);
    }

    @Provides
    @ActivityScope
    public static ParseQrCodeUseCase provideParseQrCodeUseCase(UserTrackingService userTrackingService,
                                                               QRCodeService qrCodeService,
                                                               PlaceRepository placeRepository,
                                                               ProductsRepository productsRepository){
        return new ParseQrCodeUseCase(userTrackingService,qrCodeService,placeRepository,productsRepository);
    }
}
