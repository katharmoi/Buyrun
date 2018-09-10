package com.kadirkertis.domain.interactor.checkIn.model;

/**
 * Created by Kadir Kertis on 9/3/2018.
 */

public class CheckInRequest {
    private String uid;
    private String placeId;
    private String tableNumber;

    public CheckInRequest() {

    }

    public CheckInRequest(String uid, String placeId, String tableNumber) {
        this.uid = uid;
        this.placeId = placeId;
        this.tableNumber = tableNumber;
    }

    public String getUid() {
        return uid;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckInRequest that = (CheckInRequest) o;

        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (placeId != null ? !placeId.equals(that.placeId) : that.placeId != null) return false;
        return tableNumber != null ? tableNumber.equals(that.tableNumber) : that.tableNumber == null;
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (placeId != null ? placeId.hashCode() : 0);
        result = 31 * result + (tableNumber != null ? tableNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CheckInRequest{" +
                "uid='" + uid + '\'' +
                ", placeId='" + placeId + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                '}';
    }
}
