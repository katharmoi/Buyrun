package com.kadirkertis.domain.interactor.product;

import com.kadirkertis.domain.interactor.product.model.Item;
import com.kadirkertis.domain.interactor.product.repository.ProductsRepository;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/4/2017.
 */

public final class GetProductsUseCase {
    private final ProductsRepository productsRepository;

    public GetProductsUseCase(final ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }


    public Maybe<List<Item>> execute(String storeId) {
        return productsRepository.getProducts(storeId);
    }

}
