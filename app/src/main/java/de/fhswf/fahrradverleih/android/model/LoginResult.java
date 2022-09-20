package de.fhswf.fahrradverleih.android.model;

import androidx.annotation.NonNull;

public class LoginResult {

    private final boolean ok;

    @NonNull
    private final String token;

    public LoginResult(boolean ok, @NonNull String token) {
        this.ok = ok;
        this.token = token;
    }

    public LoginResult() {
        this(false, "");
    }

    public boolean isOk() {
        return ok;
    }

    @NonNull
    public String getToken() {
        return token;
    }
}
