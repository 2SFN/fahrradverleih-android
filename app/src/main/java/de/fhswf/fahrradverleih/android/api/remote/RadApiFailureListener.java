package de.fhswf.fahrradverleih.android.api.remote;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.fhswf.fahrradverleih.android.api.OnFailure;
import de.fhswf.fahrradverleih.android.api.RadApiException;

class RadApiFailureListener implements Response.ErrorListener {

    @NonNull
    private final OnFailure onFailure;

    public RadApiFailureListener(@NonNull OnFailure onFailure) {
        this.onFailure = onFailure;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        onFailure.onFailure(new RadApiException(
                String.format("Fehler bei der Anfrage:\n\n%s", error.getMessage()),
                error));
    }
}
