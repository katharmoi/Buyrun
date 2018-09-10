package com.kadirkertis.orfo.di.application.shared;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.kadirkertis.orfo.di.application.AppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 10/30/2017.
 */
@Module
public class FirebaseModule {

    @Provides
    @AppScope
    public static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @AppScope
    public static FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @AppScope
    public static FirebaseStorage provideFirebaseStorage() {
        return FirebaseStorage.getInstance();
    }
}
