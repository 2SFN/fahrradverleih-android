package de.fhswf.fahrradverleih.android.api;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

public interface OnFailure {

    @MainThread
    void onFailure(@NonNull RadApiException e);

}
