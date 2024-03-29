package de.hawlandshut.sharedwallet.model.retro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This Class is used to retrofit Data from Backend Requests.
 */
public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable public final String message;

    private Resource(@NonNull Status status, @Nullable T data,
                     @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public enum Status { SUCCESS, ERROR }
}
