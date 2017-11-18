package com.kadirkertis.domain.interactor.product;

import com.kadirkertis.domain.interactor.type.MaybeUseCaseWithParameter;
import com.kadirkertis.domain.model.Item;
import com.kadirkertis.domain.repository.ProductsRepository;

import io.reactivex.Maybe;
import javafx.util.Pair;

/**
 * Created by Kadir Kertis on 11/4/2017.
 */

public class GetSingleProductUseCase implements MaybeUseCaseWithParameter<Pair<String,String>, Item> {

    private final ProductsRepository productsRepository;

    public GetSingleProductUseCase(final ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Maybe<Item> execute(Pair<String,String> params) {
        return productsRepository.getProduct(params.getKey(), params.getValue());
    }
}
