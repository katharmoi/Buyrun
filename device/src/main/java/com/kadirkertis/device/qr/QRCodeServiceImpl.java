package com.kadirkertis.device.qr;

import android.app.Activity;
import android.content.Intent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kadirkertis.domain.interactor.qr.exceptions.ScanCancelledException;
import com.kadirkertis.domain.interactor.qr.exceptions.UnknownQRCodeException;
import com.kadirkertis.domain.interactor.qr.model.QrResult;
import com.kadirkertis.domain.interactor.qr.repository.QRCodeService;
import com.kadirkertis.domain.utils.Constants;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import rx_activity_result2.RxActivityResult;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Kadir Kertis on 11/23/2017.
 */

public class QRCodeServiceImpl implements QRCodeService {
    private final Activity activity;
    public static final int REQUEST_CODE = 0x0000c0de;

    public QRCodeServiceImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Single<QrResult> parseCode() {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan QR Code");
        integrator.setBarcodeImageEnabled(true);
        Intent scanIntent = integrator.createScanIntent();

        return RxActivityResult.on(activity).startIntent(scanIntent)
                .map(activityResult -> {
                    Intent data = activityResult.data();
                    int resultCode = activityResult.resultCode();
                    if (resultCode == RESULT_OK) {
                        IntentResult intentResult = IntentIntegrator
                                .parseActivityResult(REQUEST_CODE, resultCode, data);
                        if (intentResult != null) return parseCodeBlocking(intentResult);
                    }
                    throw new UnknownQRCodeException();

                })
                .singleOrError()
                .subscribeOn(Schedulers.io());
    }


    private QrResult parseCodeBlocking(IntentResult result) throws Exception {

        if (result.getContents() == null) {
            throw new ScanCancelledException("Scan canceled");
        } else if (validateScanResult(result.getContents())) {

            String results = result.getContents();
            String[] sa = results.split("@");
            String tableNumber = sa[1];
            String placeId = sa[2];
            return new QrResult(tableNumber, placeId);
        } else {
            throw new UnknownQRCodeException("Unknown Qr Code");
        }
    }

    private boolean validateScanResult(String result) {
        String pattern = Constants.ORFO_PREFIX + "@.+" + "@.+";
        return result.matches(pattern);
    }

}
