package com.kadirkertis.domain.repository;

import com.kadirkertis.domain.model.OrderItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public interface CartRepository {

    Completable addtocart(OrderItem item);

    Completable removeFromCart(OrderItem item);

    Completable updateItem(OrderItem item, int quantity);

    Single<Boolean> isItemInCart(OrderItem item);

    Single<List<OrderItem>> getCart();

    Completable clearCart();

    Single<Integer> getItemCountInCart();
}
