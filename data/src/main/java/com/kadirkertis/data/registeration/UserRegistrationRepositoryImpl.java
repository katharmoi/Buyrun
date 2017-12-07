package com.kadirkertis.data.registeration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.kadirkertis.data.model.User;
import com.kadirkertis.data.session.SessionService;
import com.kadirkertis.domain.repository.UserRegisterationRepository;
import com.kadirkertis.domain.utils.Constants;

import java.util.HashMap;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public class UserRegistrationRepositoryImpl implements UserRegisterationRepository {

    private SessionService sessionService;
    private FirebaseDatabase db;
    private FirebaseAuth auth;

    public UserRegistrationRepositoryImpl(SessionService sessionService, FirebaseDatabase db,
                                          FirebaseAuth auth) {
        this.sessionService = sessionService;
        this.db = db;
        this.auth = auth;
    }

    @Override
    public Completable saveAuthUserToDbUser() {
        final String uid = db.getReference()
                .child(Constants.DB_USERS)
                .push()
                .getKey();

        FirebaseUser fbUser = auth.getCurrentUser();
        String userName;
        if (fbUser != null) {
            userName = fbUser.getDisplayName();

            if (userName == null) {
                for (UserInfo userInfo : fbUser.getProviderData()) {
                    userName = userInfo.getDisplayName();
                }
            }

            HashMap<String, Object> regDate = new HashMap<>();
            regDate.put("date", ServerValue.TIMESTAMP);
            User user = new User(uid, userName, fbUser.getEmail(), regDate);

            return RxFirebaseDatabase.setValue(getUserReference(uid), user)
                    .concatWith(sessionService.setUser(uid))
                    .subscribeOn(Schedulers.io());
        } else
            return Completable.error(new UserDataDoesNotExistOnAuthError());


    }

    @Override
    public Maybe<Boolean> isUserRegistered() {

        Query query = db.getReference()
                .child(Constants.DB_USERS)
                .orderByChild("email")
                .equalTo(auth.getCurrentUser().getEmail());

        return RxFirebaseDatabase.observeSingleValueEvent(query)
                .map(DataSnapshot::exists);


    }

    private DatabaseReference getUserReference(String key) {
        return db.getReference()
                .child(Constants.DB_USERS)
                .child(key);
    }
}
