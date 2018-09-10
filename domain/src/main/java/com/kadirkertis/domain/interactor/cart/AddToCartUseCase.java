package com.kadirkertis.domain.interactor.cart;

import com.kadirkertis.domain.interactor.product.model.OrderItem;
import com.kadirkertis.domain.interactor.cart.repository.CartRepository;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/15/2017.
 */

public final class AddToCartUseCase {
    private final CartRepository cartRepository;

    public AddToCartUseCase(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Completable execute(OrderItem item) {
        return cartRepository.addtocart(item);
    }
}
