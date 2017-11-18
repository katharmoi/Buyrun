package com.kadirkertis.orfo.ui.cart;

import android.arch.lifecycle.ViewModel;

import com.kadirkertis.domain.interactor.cart.GetCartUseCase;

import javax.inject.Inject;

/**
 * Created by Kadir Kertis on 1.5.2017.
 */

public class CartActivityViewModel extends ViewModel {
    private static final int CART_LOADER = 100;

    @Inject
    GetCartUseCase getCartUseCase;


    public void loadProducts() {
        getCartUseCase.execute();
    }


}
