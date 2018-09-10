package com.kadirkertis.domain.interactor.product;

import com.kadirkertis.domain.interactor.product.model.Item;
import com.kadirkertis.domain.interactor.product.repository.ProductsRepository;

import java.util.Map;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/4/2017.
 */

public class GetSingleProductUseCase {

    private final ProductsRepository productsRepository;

    public GetSingleProductUseCase(final ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Maybe<Item> execute(Map.Entry<String, String> params) {
        return productsRepository.getProduct(params.getKey(), params.getValue());
    }
}
