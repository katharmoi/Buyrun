package com.kadirkertis.orfo.services;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Kadir Kertis on 10/31/2017.
 */

public class CartService {

    private FirebaseDatabase db;

    public CartService(FirebaseDatabase db) {

        this.db = db;
    }


}
