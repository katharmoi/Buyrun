package com.kadirkertis.domain.interactor.qr;

import com.kadirkertis.domain.model.Item;
import com.kadirkertis.domain.repository.PlaceRepository;
import com.kadirkertis.domain.repository.ProductsRepository;
import com.kadirkertis.domain.services.qr.QRCodeService;
import com.kadirkertis.domain.services.UserTrackingService;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public class ParseQrCodeUseCase {

    private UserTrackingService userTrackingService;
    private QRCodeService<Object> qrService;
    private PlaceRepository placeRepository;
    private ProductsRepository productsRepository;

    public ParseQrCodeUseCase(UserTrackingService userTrackingService,
                              QRCodeService<Object> qrService,
                              PlaceRepository placeRepository,
                              ProductsRepository productsRepository) {
        this.userTrackingService = userTrackingService;
        this.qrService = qrService;
        this.placeRepository = placeRepository;
        this.productsRepository = productsRepository;
    }

    public Maybe<List<Item>> execute(Object codeResult) {
        return qrService.parseCode(codeResult)
                .flatMapMaybe(qrResult -> placeRepository.getPlace(qrResult.getPlaceId()))
                .concatMap(place -> userTrackingService.checkUserIn(place.getLatitude(), place.getLongitude())
                    .flatMapMaybe(isIn -> {
                        if (isIn) return productsRepository.getProducts(place.getId());
                        else
                            return Maybe.error(new UserNotAtPlaceException("User is not in place"));
                    }));
    }
}
