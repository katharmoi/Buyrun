package com.kadirkertis.data.checkIn;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.kadirkertis.data.model.DataCheckInPlace;
import com.kadirkertis.data.model.DataCheckInUser;
import com.kadirkertis.domain.repository.UserCheckingRepository;
import com.kadirkertis.domain.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public class UserCheckingRepositoryImpl implements UserCheckingRepository {

    private FirebaseDatabase db;

    public UserCheckingRepositoryImpl(FirebaseDatabase db) {
        this.db = db;
    }

    @Override
    public Completable checkUserIn(String placeId, String tableNumber, String userId) {
        HashMap<String, Object> timeAdded = new HashMap<>();
        timeAdded.put(Constants.PROPERTY_TIME_ADDED, ServerValue.TIMESTAMP);

        Map<String, Object> checkIns = new HashMap<>();

        Map<String, Object> userCheckIn = new DataCheckInUser(placeId, timeAdded).toMap();
        Map<String, Object> placeCheckIn = new DataCheckInPlace(userId, timeAdded).toMap();
        Map<String, Object> placeCuurentlyIn = new DataCheckInPlace(userId, timeAdded).toMap();

        String checkInId = getCheckInId(placeId);

        String placeReference = "/" + Constants.DB_PLACES + "/" + placeId + "/" + Constants.TABLE_PLACE_CHECKED_INS + "/" + checkInId;
        String currentlyInReference = "/" + Constants.DB_PLACES + "/" + placeId + "/" + Constants.TABLE_PLACE_CURRENT_CHECKED_INS + "/" + checkInId;
        String userReference = "/" + Constants.DB_USERS + "/" + userId + "/" + Constants.TABLE_USER_CHECKED_IN_PLACES + "/" + checkInId;
        checkIns.put(placeReference,
                placeCheckIn);
        checkIns.put(currentlyInReference,
                placeCuurentlyIn);
        checkIns.put(userReference,
                userCheckIn);

        return RxFirebaseDatabase.updateChildren(db.getReference(), checkIns)
                .observeOn(Schedulers.io());
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
