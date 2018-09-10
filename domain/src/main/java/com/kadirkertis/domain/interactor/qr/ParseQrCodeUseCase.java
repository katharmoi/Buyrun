package com.kadirkertis.domain.interactor.qr;

import com.kadirkertis.domain.interactor.place.GetPlaceUseCase;
import com.kadirkertis.domain.interactor.qr.exceptions.UserNotAtPlaceException;
import com.kadirkertis.domain.interactor.qr.model.QrResult;
import com.kadirkertis.domain.interactor.qr.repository.QRCodeService;
import com.kadirkertis.domain.interactor.tracking.IsUserInUseCase;
import com.kadirkertis.domain.interactor.type.SingleUseCase;

import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public final class ParseQrCodeUseCase implements SingleUseCase<QrResult> {

    private final QRCodeService qrService;
    private final IsUserInUseCase isUserInUseCase;
    private final GetPlaceUseCase getPlaceUseCase;
    private QrResult result;

    public ParseQrCodeUseCase(QRCodeService qrService,
                              IsUserInUseCase isUserInUseCase,
                              GetPlaceUseCase getPlaceUseCase) {
        this.qrService = qrService;
        this.isUserInUseCase = isUserInUseCase;
        this.getPlaceUseCase = getPlaceUseCase;
    }

    @Override
    public Single<QrResult> execute() {

        return qrService.parseCode()
                .doOnSuccess(qr -> result = new QrResult(qr.getTableNumber(), qr.getPlaceId()))
                .flatMap(qrResult -> getPlaceUseCase.execute(qrResult.getPlaceId()))
                .flatMap(place -> isUserInUseCase.execute(place)
                        .map(isIn -> {
                            if (isIn) return result;
                            else throw new UserNotAtPlaceException();
                        }));

    }
}
