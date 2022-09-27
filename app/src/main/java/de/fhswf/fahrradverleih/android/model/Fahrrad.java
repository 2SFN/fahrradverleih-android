package de.fhswf.fahrradverleih.android.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Fahrrad implements Serializable {

    @NonNull
    private String id;

    @NonNull
    private GeopositionT position;

    @NonNull
    private FahrradTyp typ;

    public Fahrrad(@NonNull String id,
                   @NonNull GeopositionT position,
                   @NonNull FahrradTyp typ) {
        this.id = id;
        this.position = position;
        this.typ = typ;
    }

    public Fahrrad() {
        this("", new GeopositionT(), new FahrradTyp());
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public GeopositionT getPosition() {
        return position;
    }

    public void setPosition(@NonNull GeopositionT position) {
        this.position = position;
    }

    @NonNull
    public FahrradTyp getTyp() {
        return typ;
    }

    public void setTyp(@NonNull FahrradTyp typ) {
        this.typ = typ;
    }
}
