package com.kadirkertis.device.qr;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kadirkertis.domain.services.QRCodeService;
import com.kadirkertis.domain.utils.Constants;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/23/2017.
 */

public class QRCodeServiceImpl implements QRCodeService<IntentResult, String[]> {
    private Activity source;

    public QRCodeServiceImpl(Activity source) {
        this.source = source;
    }

    @Override
    public Completable initiateScan() {
        return Completable.fromAction(this::scanBlocking);
    }

    @Override
    public Single<String[]> parseCode(IntentResult result) {
        return Single.fromCallable(() -> parseCodeBlocking(result));
    }


    private String[] parseCodeBlocking(IntentResult result) throws Exception {

        if (result.getContents() == null) {
            throw new ScanCancelledException("Scan canceled");
        } else {

            String results = result.getContents();
            String[] sa = results.split("@");
            String qrPrefix = sa[0];
            String tableNumber = sa[1];
            String storeId = sa[2];

            if (!results.contains(Constants.ORFO_PREFIX)) {
                throw new UnknownQRCodeException("Unknown QR Code");
            }
            if (!qrPrefix.equals(Constants.ORFO_PREFIX)) {
                throw new UnknownQRCodeException("Unknown QR Code");
            }
            return new String[]{tableNumber, storeId};
        }
    }


    private void scanBlocking() {
        IntentIntegrator integrator = new IntentIntegrator(source);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan QR Code");
        integrator.setCameraId(0);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }


}
