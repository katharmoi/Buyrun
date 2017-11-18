package com.kadirkertis.data.prodcuts;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.data.mappers.DataItemToItemMapperImpl;
import com.kadirkertis.data.model.DataItem;
import com.kadirkertis.domain.model.Item;
import com.kadirkertis.domain.repository.ProductsRepository;
import com.kadirkertis.domain.utils.Constants;

import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kadir Kertis on 11/6/2017.
 */

public class ProductsRepositoryImpl implements ProductsRepository {

    private FirebaseDatabase db;

    public ProductsRepositoryImpl(FirebaseDatabase db) {
        this.db = db;
    }


    public Maybe<List<Item>> getProducts(String storeId) {
        return RxFirebaseDatabase
                .observeSingleValueEvent(
                        getProductsTableReference(storeId),
                        DataSnapshotMapper.listOf(DataItem.class))
                .map(itemList -> new DataItemToItemMapperImpl().mapList(itemList))
                .subscribeOn(Schedulers.io());
    }

    public Maybe<Item> getProduct(String productId, String storeId) {

        return RxFirebaseDatabase
                .observeSingleValueEvent(
                        getProductsTableReference(storeId).child(productId),
                        DataItem.class)
                .map(item -> new DataItemToItemMapperImpl().map(item))
                .subscribeOn(Schedulers.io());
    }

    private DatabaseReference getProductsTableReference(String storeId) {
        return this.db
                .getReference()
                .child(Constants.DB_PLACES)
                .child(storeId)
                .child(Constants.TABLE_PRODUCTS);
    }
}
