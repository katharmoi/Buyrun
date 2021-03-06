package com.kadirkertis.domain.interactor.cart;

import com.kadirkertis.domain.interactor.product.model.OrderItem;
import com.kadirkertis.domain.interactor.cart.repository.CartRepository;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/15/2017.
 */

public final class GetCartUseCase {

    private final CartRepository cartRepository;

    public GetCartUseCase(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Single<List<OrderItem>> execute() {
        return cartRepository.getCart();
    }
}
