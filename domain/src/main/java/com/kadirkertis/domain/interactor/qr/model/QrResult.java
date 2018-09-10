package com.kadirkertis.domain.interactor.qr.model;

/**
 * Created by Kadir Kertis on 12/9/2017.
 */

public class QrResult {
    private String tableNumber;
    private String placeId;

    public QrResult() {
    }

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

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
