package com.kadirkertis.data.cart;

import com.kadirkertis.domain.interactor.product.model.OrderItem;
import com.kadirkertis.domain.interactor.cart.repository.CartRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public class CartRepositoryImpl implements CartRepository {
    @Override
    public Completable addtocart(OrderItem item) {
        return Completable
                .fromCallable(() -> CartServiceImpl.addToCart(item));
    }

    @Override
    public Completable removeFromCart(OrderItem item) {
        return Completable.fromCallable(() -> CartServiceImpl.deleteFromCart(item));
    }

    @Override
    public Completable updateItem(OrderItem item, int quantity) {
        return Completable.fromCallable(() -> CartServiceImpl.updateCart(item, quantity));
    }

    @Override
    public Single<Boolean> isItemInCart(OrderItem item) {
        return Single.fromCallable(() -> CartServiceImpl.isItemInCart(item));
    }

    @Override
    public Single<List<OrderItem>> getCart() {
        return Single.fromCallable(CartServiceImpl::getCart);
    }

    @Override
    public Completable clearCart() {
        return Completable.fromCallable(CartServiceImpl::emptyCart);
    }

    @Override
    public Single<Integer> getItemCountInCart() {
        return Single.fromCallable(CartServiceImpl::getItemCount);
    }
}
