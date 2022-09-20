package de.fhswf.fahrradverleih.android.api;

public class RadApiException extends Exception {

    public RadApiException() {
    }

    public RadApiException(String message) {
        super(message);
    }

    public RadApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public RadApiException(Throwable cause) {
        super(cause);
    }
}
