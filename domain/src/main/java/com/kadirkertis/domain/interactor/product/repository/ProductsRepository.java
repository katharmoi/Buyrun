package com.kadirkertis.domain.interactor.product.repository;

import com.kadirkertis.domain.interactor.product.model.Item;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/4/2017.
 */

public interface ProductsRepository {

    Maybe<List<Item>> getProducts(String storeId);

    Maybe<Item> getProduct(String productId, String storeId);

}
