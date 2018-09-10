package com.kadirkertis.orfo.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Kadir Kertis on 4/27/2018.
 */

public final class Response<T> {

    private final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    private Response(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Response<T> loading() {
        return new Response<T>(Status.LOADING, null, null);
    }

    public static <T> Response<T> success(@NonNull T data) {
        return new Response<T>(Status.SUCCESS, data, null);
    }

    public static <T> Response<T> error(@NonNull Throwable error) {
        return new Response<T>(Status.ERROR, null, error);
    }

    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}
