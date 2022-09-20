package de.fhswf.fahrradverleih.android.model;

import androidx.annotation.NonNull;

public class Benutzer {

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String vorname;

    @NonNull
    private String email;

    public Benutzer(@NonNull String id, @NonNull String name,
                    @NonNull String vorname, @NonNull String email) {
        this.id = id;
        this.name = name;
        this.vorname = vorname;
        this.email = email;
    }

    public Benutzer() {
        this("", "", "", "");
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getVorname() {
        return vorname;
    }

    public void setVorname(@NonNull String vorname) {
        this.vorname = vorname;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }
}
