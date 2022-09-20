package de.fhswf.fahrradverleih.android.model;

import androidx.annotation.NonNull;

public class Station {

    @NonNull
    private final String id;

    @NonNull
    private final String bezeichnung;

    @NonNull
    private final GeopositionT position;

    private final int verfuegbar;

    public Station(@NonNull String id, @NonNull String bezeichnung,
                   @NonNull GeopositionT position, int verfuegbar) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.position = position;
        this.verfuegbar = verfuegbar;
    }

    public Station() {
        this("", "", new GeopositionT(), 0);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getBezeichnung() {
        return bezeichnung;
    }

    @NonNull
    public GeopositionT getPosition() {
        return position;
    }

    public int getVerfuegbar() {
        return verfuegbar;
    }
}
