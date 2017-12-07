package com.kadirkertis.data.place;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.data.mappers.DataPlaceToPlaceMapperImpl;
import com.kadirkertis.data.model.DataPlace;
import com.kadirkertis.domain.model.Place;
import com.kadirkertis.domain.repository.PlaceRepository;
import com.kadirkertis.domain.utils.Constants;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public class PlaceRepositoryImpl implements PlaceRepository {
    private FirebaseDatabase db;

    public PlaceRepositoryImpl(FirebaseDatabase db) {
        this.db = db;
    }

    @Override
    public Maybe<Place> getPlace(String placeId) {
        return RxFirebaseDatabase.observeSingleValueEvent(getPlaceDatabaseReference(placeId), DataPlace.class)
                .map((dataPlace -> (new DataPlaceToPlaceMapperImpl()).map(dataPlace)));

    }

    private DatabaseReference getPlaceDatabaseReference(String placeId){
        return db.getReference()
                .child(Constants.DB_PLACES)
                .child(placeId)
                .child(Constants.TABLE_COMPANY_INFO);
    }
}
