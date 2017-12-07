package com.kadirkertis.orfo.di.application.shared;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.data.checkIn.UserCheckingRepositoryImpl;
import com.kadirkertis.data.place.PlaceRepositoryImpl;
import com.kadirkertis.data.prodcuts.ProductsRepositoryImpl;
import com.kadirkertis.data.registeration.UserRegistrationRepositoryImpl;
import com.kadirkertis.data.session.SessionService;
import com.kadirkertis.domain.repository.PlaceRepository;
import com.kadirkertis.domain.repository.ProductsRepository;
import com.kadirkertis.domain.repository.UserCheckingRepository;
import com.kadirkertis.domain.repository.UserRegisterationRepository;
import com.kadirkertis.orfo.di.application.AppScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 12/1/2017.
 */
@Module
public class RepositoriesModule {

    @Provides
    @AppScope
    public static UserCheckingRepository provideUserCheckingRepository(FirebaseDatabase db){
        return new UserCheckingRepositoryImpl(db);
    }

    @Provides
    @AppScope
    public static PlaceRepository providePlaceRepository(FirebaseDatabase db){
        return new PlaceRepositoryImpl(db);
    }

    @Provides
    @AppScope
    public static ProductsRepository provideProductsRepository(FirebaseDatabase db){
        return new ProductsRepositoryImpl(db);
    }

    @Provides
    @AppScope
    public static UserRegisterationRepository provideUserRegistrationRepository(SessionService sessionService,
                                                                                FirebaseDatabase db,
                                                                                FirebaseAuth auth){
        return new UserRegistrationRepositoryImpl(sessionService,db,auth);
    }


}
