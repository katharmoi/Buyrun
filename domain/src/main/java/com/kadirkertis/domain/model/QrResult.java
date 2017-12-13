package com.kadirkertis.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kadir Kertis on 12/9/2017.
 */

public class QrResult {
    private String tableNumber;
    private String placeId;

    public QrResult(String tableNumber, String placeId) {
        this.tableNumber = tableNumber;
        this.placeId = placeId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public String getPlaceId() {
        return placeId;
    }
}
