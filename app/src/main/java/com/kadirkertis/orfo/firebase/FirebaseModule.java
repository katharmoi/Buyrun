package com.kadirkertis.orfo.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 10/30/2017.
 */
@Module
public class FirebaseModule {

    @Provides
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    FirebaseStorage provideFirebaseStorage() { return FirebaseStorage.getInstance();}
}
