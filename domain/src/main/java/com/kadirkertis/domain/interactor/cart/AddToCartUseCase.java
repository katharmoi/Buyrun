package com.kadirkertis.domain.interactor.cart;

import com.kadirkertis.domain.model.OrderItem;
import com.kadirkertis.domain.repository.CartRepository;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/15/2017.
 */

public class AddToCartUseCase {
    private final CartRepository cartRepository;

    public AddToCartUseCase(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Completable execute(OrderItem item) {
        return cartRepository.addtocart(item);
    }
}
