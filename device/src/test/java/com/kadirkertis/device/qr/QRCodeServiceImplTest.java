package com.kadirkertis.device.qr;

import android.app.Activity;

import com.google.zxing.integration.android.IntentResult;
import com.kadirkertis.domain.model.QrResult;
import com.kadirkertis.domain.services.qr.ScanCancelledException;
import com.kadirkertis.domain.services.qr.UnknownQRCodeException;
import com.kadirkertis.domain.utils.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

/**
 * Created by Kadir Kertis on 12/13/2017.
 */

public class QRCodeServiceImplTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    Activity activity;

    @Mock
    IntentResult mockResult;

    @InjectMocks
    QRCodeServiceImpl qrCodeService;


    @Test
    public void shouldThrowScanCancelledWhenResultNull() {
        when(mockResult.getContents()).thenReturn(null);
        TestObserver<QrResult> testObserver = qrCodeService.parseCode(mockResult).test();
        testObserver.assertError(ScanCancelledException.class);

    }

    @Test
    public void shouldThrowUnknownQrCodeExceptionWhenResultIsMisformatted() {
        when(mockResult.getContents()).thenReturn("MisformattedString");

        TestObserver<QrResult> testObserver = qrCodeService.parseCode(mockResult).test();
        testObserver.assertError(UnknownQRCodeException.class);
    }

    @Test
    public void shouldReturnCorrectTableAndPlaceId() {
        when(mockResult.getContents()).thenReturn(Constants.ORFO_PREFIX + "@2@-asdfgk");
        TestObserver<QrResult> testObserver = qrCodeService.parseCode(mockResult).test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertValue(result ->
                        result.getTableNumber().equals("2")
                )
                .assertValue(qrResult -> qrResult.getPlaceId().equals("-asdfgk"));
    }


}