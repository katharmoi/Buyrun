package com.kadirkertis.domain.interactor.qr;


import com.kadirkertis.domain.interactor.qr.exceptions.ScanCancelledException;
import com.kadirkertis.domain.interactor.qr.exceptions.UserNotAtPlaceException;
import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.domain.interactor.qr.model.QrResult;
import com.kadirkertis.domain.interactor.place.repository.PlaceRepository;
import com.kadirkertis.domain.interactor.product.repository.ProductsRepository;
import com.kadirkertis.domain.interactor.tracking.repository.UserTrackingService;
import com.kadirkertis.domain.interactor.qr.repository.QRCodeService;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Kadir Kertis on 12/11/2017.
 */
public class ParseQrCodeUseCaseTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    QRCodeService mockQrCodeService;

    @Mock
    PlaceRepository mockPlaceRepository;

    @Mock
    ProductsRepository mockProductsRepository;

    @Mock
    UserTrackingService mockTrackingService;

    @InjectMocks
    ParseQrCodeUseCase qrCodeUseCase;

    private static final QrResult mockQrResult = new QrResult("2", "3");
    private static final Place mockPlace = new Place("1", "some",
            "me", "asd@asd.com", "asd", "123", "ss", "asd",
            100, 1000, 100, 1000);

//    @Test
//    public void shouldReturnScanCanceledException() {
//        when(mockQrCodeService.parseCode()).thenReturn(Single.error(new ScanCancelledException()));
//        TestObserver<String> testObserver = qrCodeUseCase.
//                execute("Wrong QR").
//                test();
//
//        verify(mockQrCodeService, times(1)).parseCode(any());
//        verify(mockPlaceRepository, times(0)).getPlace(any());
//
//        testObserver.assertError(ScanCancelledException.class);
//
//    }
//
//    @Test
//    public void shouldThrowUserNotAtPlaceError() {
//        when(mockQrCodeService.parseCode(any())).thenReturn(Single.just(mockQrResult));
//        when(mockTrackingService.isUserIn(anyDouble(), anyDouble())).thenReturn(Single.just(false));
//        when(mockPlaceRepository.getPlace(any())).thenReturn(Maybe.just(mockPlace));
//
//
//        TestObserver<String> resultTestObserver = qrCodeUseCase.
//                execute("some").
//                test();
//
//        verify(mockQrCodeService, times(1)).parseCode(any());
//        verify(mockPlaceRepository, times(1)).getPlace(any());
//
//        resultTestObserver.assertError(UserNotAtPlaceException.class);
//    }
//
//    @Test
//    public void shouldReturnPlaceIdWhenEverythingOk() {
//
//        String result = "dummy_result";
//
//        when(mockQrCodeService.parseCode(any())).thenReturn(Single.just(mockQrResult));
//        when(mockTrackingService.isUserIn(anyDouble(), anyDouble())).thenReturn(Single.just(true));
//        when(mockPlaceRepository.getPlace(any())).thenReturn(Maybe.just(mockPlace));
//
//        TestObserver<String> resultTestObserver = qrCodeUseCase.
//                execute(result).
//                test();
//
//
//        resultTestObserver.assertValue(items -> items.equals(mockPlace.getId()));
//
//    }

}