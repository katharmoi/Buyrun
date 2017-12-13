package com.kadirkertis.device.qr;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kadirkertis.domain.model.QrResult;
import com.kadirkertis.domain.services.qr.QRCodeService;
import com.kadirkertis.domain.services.qr.ScanCancelledException;
import com.kadirkertis.domain.services.qr.UnknownQRCodeException;
import com.kadirkertis.domain.utils.Constants;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/23/2017.
 */

public class QRCodeServiceImpl implements QRCodeService<IntentResult> {
    private Activity source;

    public QRCodeServiceImpl(Activity source) {
        this.source = source;
    }

    @Override
    public Completable initiateScan() {
        return Completable.fromAction(this::scanBlocking);
    }

    @Override
    public Single<QrResult> parseCode(IntentResult result) {
        return Single.fromCallable(() -> parseCodeBlocking(result));
    }


    private QrResult parseCodeBlocking(IntentResult result) throws Exception {

        if (result.getContents() == null) {
            throw new ScanCancelledException("Scan canceled");
        } else if(validateScanResult(result.getContents())) {

            String results = result.getContents();
            String[] sa = results.split("@");
            String tableNumber = sa[1];
            String placeId = sa[2];
            return new QrResult(tableNumber, placeId);
        }else {
            throw  new UnknownQRCodeException("Unknown Qr Code");
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

    private boolean validateScanResult(String result){
        String pattern = Constants.ORFO_PREFIX + "@.+"  +"@.+";
        return result.matches(pattern);
    }


}
