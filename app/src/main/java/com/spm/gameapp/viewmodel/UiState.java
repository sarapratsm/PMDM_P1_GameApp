package com.spm.gameapp.viewmodel;

/**
 * Classe per gestionar l'estat de la UI.
 * @param <T>
 */
public class UiState<T> {

    public enum Status { LOADING, SUCCESS, ERROR }

    public final Status status;
    public final T data;
    public final Throwable error;

    private UiState(Status status, T data, Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> UiState<T> loading() {
        return new UiState<>(Status.LOADING, null, null);
    }

    public static <T> UiState<T> success(T data) {
        return new UiState<>(Status.SUCCESS, data, null);
    }

    public static <T> UiState<T> error(Throwable error) {
        return new UiState<>(Status.ERROR, null, error);
    }
}