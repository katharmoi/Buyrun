package com.kadirkertis.data.place;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.data.mappers.DataPlaceToPlaceMapperImpl;
import com.kadirkertis.data.model.DataPlace;
import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.domain.interactor.place.repository.PlaceRepository;
import com.kadirkertis.domain.utils.Constants;

import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public class PlaceRepositoryImpl implements PlaceRepository {
    private final FirebaseDatabase db;

    public PlaceRepositoryImpl(FirebaseDatabase db) {
        this.db = db;
    }

    @Override
    public Maybe<Place> getPlace(String placeId) {
        return RxFirebaseDatabase
                .observeSingleValueEvent(getPlaceDatabaseReference(placeId), DataPlace.class)
                .map((dataPlace -> (new DataPlaceToPlaceMapperImpl()).map(dataPlace)));

    }

    @Override
    public Maybe<List<Place>> getAllPlaces() {
        return RxFirebaseDatabase.observeSingleValueEvent(getAllPlacesDatabaseReference(),
                DataSnapshotMapper.listOf(Place.class));
    }

    @Override
    public Maybe<List<Place>> getAllPlacesByDistance(double lat, double lng, double dist) {
        return null;
    }

    private DatabaseReference getPlaceDatabaseReference(String placeId) {
        return db.getReference()
                .child(Constants.DB_PLACES)
                .child(placeId)
                .child(Constants.TABLE_COMPANY_INFO);
    }

    private DatabaseReference getAllPlacesDatabaseReference() {
        return db.getReference()
                .child(Constants.DB_PLACE_LIST);
    }
}
