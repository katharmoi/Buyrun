package com.kadirkertis.domain.interactor.qr;


import com.kadirkertis.domain.model.Item;
import com.kadirkertis.domain.repository.PlaceRepository;
import com.kadirkertis.domain.repository.ProductsRepository;
import com.kadirkertis.domain.services.UserTrackingService;
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
import static org.mockito.Mockito.when;

/**
 * Created by Kadir Kertis on 12/11/2017.
 */
public class ParseQrCodeUseCaseTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    UserTrackingService mockTrackingService;

    @Mock
    QRCodeService<Object> mockQrCodeService;

    @Mock
    PlaceRepository mockPlaceRepository;

    @Mock
    ProductsRepository mockProductsRepository;

    @InjectMocks
    ParseQrCodeUseCase qrCodeUseCase;


    @Test
    public void shouldReturnItems() {

        when(mockQrCodeService.parseCode(any())).thenReturn(any());
        when(mockTrackingService.isUserIn(any(), any())).thenReturn(Single.just(true));
        when(mockPlaceRepository.getPlace(any())).thenReturn(any());
        when(mockProductsRepository.getProducts(any())).thenReturn(Maybe.just(returnMockItemList()));
        TestObserver<List<Item>> observer = qrCodeUseCase.execute(null).test();
        observer.awaitTerminalEvent();

        observer.assertNoErrors()
                .assertValue(returnMockItemList());

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