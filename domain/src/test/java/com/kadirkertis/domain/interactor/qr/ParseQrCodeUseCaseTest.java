package com.kadirkertis.domain.interactor.qr;


import com.kadirkertis.domain.model.Item;
import com.kadirkertis.domain.model.Place;
import com.kadirkertis.domain.model.QrResult;
import com.kadirkertis.domain.repository.PlaceRepository;
import com.kadirkertis.domain.repository.ProductsRepository;
import com.kadirkertis.domain.services.location.UserTrackingService;
import com.kadirkertis.domain.services.qr.QRCodeService;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

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
    QRCodeService<Object> mockQrCodeService;

    @Mock
    PlaceRepository mockPlaceRepository;

    @Mock
    ProductsRepository mockProductsRepository;

    @Mock UserTrackingService mockTrackingService;

    @InjectMocks
    ParseQrCodeUseCase qrCodeUseCase;

    private static QrResult mockQrResult = new QrResult("2", "3");
    private static Place mockPlace = new Place("1", "some",
            "me", "asd@asd.com", "asd", "123", "ss", "asd",
            100, 1000, 100, 1000);

    @Test
    public void shouldReturnIncompatibleQrCodeError() throws Exception {
        when(mockQrCodeService.parseCode(any())).thenReturn(Single.just(mockQrResult));
    }

    @Test
    public void shouldThrowUserNotAtPlaceError(){
        when(mockQrCodeService.parseCode(any())).thenReturn(Single.just(mockQrResult));
        when(mockTrackingService.checkUserIn(anyDouble(),anyDouble())).thenReturn(Single.just(false));
        when(mockPlaceRepository.getPlace(any())).thenReturn(Maybe.just(mockPlace));
        when(mockProductsRepository.getProducts(any())).thenReturn(Maybe.just(returnMockItemList()));

        TestObserver<List<Item>> resultTestObserver = qrCodeUseCase.execute("some").test();

        verify(mockQrCodeService,times(1)).parseCode(any());
        verify(mockPlaceRepository,times(1)).getPlace(any());
        verify(mockProductsRepository,times(0)).getProducts("1");

        resultTestObserver.assertError(UserNotAtPlaceException.class);
    }

    @Test
    public void shouldReturnListOfItemsWhenOthersWork() {

        String result = "dummy_result";

        when(mockQrCodeService.parseCode(any())).thenReturn(Single.just(mockQrResult));
        when(mockTrackingService.checkUserIn(anyDouble(),anyDouble())).thenReturn(Single.just(true));
        when(mockPlaceRepository.getPlace(any())).thenReturn(Maybe.just(mockPlace));
        when(mockProductsRepository.getProducts(any())).thenReturn(Maybe.just(returnMockItemList()));

        TestObserver<List<Item>> resultTestObserver = qrCodeUseCase.execute(result).test();


        resultTestObserver.assertValue(items -> items.get(0).getName().equals("New York Steak"));

    }


    private List<Item> returnMockItemList() {
        Item item1 = new Item("1", "food", "beefs", "New York Steak",
                "Some Desc", "someUrl", 25.20, 1200, 1400);
        Item item2 = new Item("1", "drink", "alcoholic", "Dry Martini",
                "Some Desc", "someUrl", 35.20, 1210, 1453);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        return items;
    }


}