package com.kadirkertis.orfo.ui.products;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kadirkertis.domain.interactor.cart.AddToCartUseCase;
import com.kadirkertis.domain.interactor.cart.GetCartUseCase;
import com.kadirkertis.domain.interactor.product.GetProductsUseCase;
import com.kadirkertis.domain.interactor.product.GetSingleProductUseCase;
import com.kadirkertis.domain.interactor.product.model.Item;
import com.kadirkertis.domain.interactor.product.model.OrderItem;
import com.kadirkertis.orfo.model.CheckInPlace;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Kadir Kertis on 10/31/2017.
 */

public class ProductsViewModel extends ViewModel {

    @Inject
    GetProductsUseCase getProductsUseCase;

    @Inject
    GetSingleProductUseCase getSingleProductUseCase;

    @Inject
    GetCartUseCase getCartUseCase;

    @Inject
    AddToCartUseCase addToCartUseCase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    private final MutableLiveData<CheckInPlace> currentPlace = new MutableLiveData<>();
    private final MutableLiveData<List<Item>> products = new MutableLiveData<>();

    public MutableLiveData<CheckInPlace> getCurrentPlaceObservable() {
        return currentPlace;
    }

    public MutableLiveData<List<Item>> getProductsObservable(String placeId) {
        getProducts(placeId);
        return products;
    }


    public void addToCart(OrderItem item) {
        disposable.add(addToCartUseCase.execute(item)
                .subscribe());
    }

    private void getProducts(String placeId) {
        disposable.add(
                getProductsUseCase.execute(placeId)
                        .subscribe(
                                itemsList -> products.setValue(itemsList)
                        ));
    }


    public void updateCartCount() {
//        numOfItemsInCart = cartRepository.getItemCountInCart();
//        if (cartCountTextView == null) return;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (numOfItemsInCart == 0)
//                    cartCountTextView.setVisibility(View.INVISIBLE);
//                else {
//                    cartCountTextView.setVisibility(View.VISIBLE);
//                    cartCountTextView.setText(Integer.toString(numOfItemsInCart));
//                }
//            }
//        });
    }
}
