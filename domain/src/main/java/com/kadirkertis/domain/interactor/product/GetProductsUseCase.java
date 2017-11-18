package com.kadirkertis.domain.interactor.product;

import com.kadirkertis.domain.interactor.type.MaybeUseCaseWithParameter;
import com.kadirkertis.domain.model.Item;
import com.kadirkertis.domain.repository.ProductsRepository;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/4/2017.
 */

public class GetProductsUseCase implements MaybeUseCaseWithParameter<String,List<Item>> {
    private final ProductsRepository productsRepository;

    public GetProductsUseCase(final ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }


    @Override
    public Maybe<List<Item>> execute(String storeId) {
        return productsRepository.getProducts(storeId);
    }

}
