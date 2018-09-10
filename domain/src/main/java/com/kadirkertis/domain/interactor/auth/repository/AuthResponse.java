package com.kadirkertis.domain.interactor.auth.repository;


/**
 * Created by Kadir Kertis on 8/30/2018.
 */

public class AuthResponse {

    private final AuthStatus status;

    private final String uid;

    private final String error;

    public AuthResponse(AuthStatus status, String uid, String error) {
        this.status = status;
        this.uid = uid;
        this.error = error;
    }

    public static AuthResponse success(String uid) {
        return new AuthResponse(AuthStatus.AUTHORIZED, uid, null);
    }

    public static AuthResponse error(String error) {
        return new AuthResponse(AuthStatus.UNAUTHORIZED, null, error);
    }

    public AuthStatus getStatus() {
        return status;
    }

    public String getUid() {
        return uid;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "status=" + status +
                ", uid='" + uid + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
