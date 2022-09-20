package de.fhswf.fahrradverleih.android.api;

import androidx.annotation.MainThread;

public interface OnSuccess<T> {

    @MainThread
    void onSuccess(T result);
}
