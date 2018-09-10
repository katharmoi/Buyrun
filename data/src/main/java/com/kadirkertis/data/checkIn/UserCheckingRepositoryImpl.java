package com.kadirkertis.data.checkIn;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.kadirkertis.data.model.DataCheckInPlace;
import com.kadirkertis.data.model.DataCheckInUser;
import com.kadirkertis.domain.interactor.checkIn.model.CheckInRequest;
import com.kadirkertis.domain.interactor.checkIn.repository.UserCheckingRepository;
import com.kadirkertis.domain.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public class UserCheckingRepositoryImpl implements UserCheckingRepository {

    private final FirebaseDatabase db;

    public UserCheckingRepositoryImpl(FirebaseDatabase db) {
        this.db = db;
    }

    @Override
    public Single<String> checkUserIn(CheckInRequest request) {
        HashMap<String, Object> timeAdded = new HashMap<>();
        timeAdded.put(Constants.PROPERTY_TIME_ADDED, ServerValue.TIMESTAMP);

        Map<String, Object> checkIns = new HashMap<>();

        Map<String, Object> userCheckIn = new DataCheckInUser(request.getPlaceId(), timeAdded).toMap();
        Map<String, Object> placeCheckIn = new DataCheckInPlace(request.getUid(), timeAdded).toMap();
        Map<String, Object> placeCuurentlyIn = new DataCheckInPlace(request.getUid(), timeAdded).toMap();

        String checkInId = getCheckInId(request.getPlaceId());

        String placeReference = "/" + Constants.DB_PLACES + "/" + request.getPlaceId()
                + "/" + Constants.TABLE_PLACE_CHECKED_INS
                + "/" + checkInId;
        String currentlyInReference = "/" + Constants.DB_PLACES + "/"
                + request.getPlaceId() + "/"
                + Constants.TABLE_PLACE_CURRENT_CHECKED_INS + "/" + checkInId;
        String userReference = "/" + Constants.DB_USERS + "/"
                + request.getUid() + "/" + Constants.TABLE_USER_CHECKED_IN_PLACES + "/" + checkInId;

        checkIns.put(placeReference,
                placeCheckIn);
        checkIns.put(currentlyInReference,
                placeCuurentlyIn);
        checkIns.put(userReference,
                userCheckIn);

        return RxFirebaseDatabase.updateChildren(db.getReference(), checkIns)
                .andThen(Single.just(request.getPlaceId()))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable checkUserOut(String userId) {
        return null;
    }

    private String getCheckInId(String placeId) {
        return db.getReference()
                .child(Constants.DB_PLACES)
                .child(placeId)
                .child(Constants.TABLE_PLACE_CHECKED_INS)
                .push()
                .getKey();
    }
}
