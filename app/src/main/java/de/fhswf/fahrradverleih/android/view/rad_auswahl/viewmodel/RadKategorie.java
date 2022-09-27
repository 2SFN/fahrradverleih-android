package de.fhswf.fahrradverleih.android.view.rad_auswahl.viewmodel;

import androidx.annotation.NonNull;

import de.fhswf.fahrradverleih.android.model.FahrradTyp;

public class RadKategorie {

    @NonNull
    private final FahrradTyp typ;

    private int verfuegbar;

    public RadKategorie(@NonNull FahrradTyp typ, int verfuegbar) {
        this.typ = typ;
        this.verfuegbar = verfuegbar;
    }

    @NonNull
    public FahrradTyp getTyp() {
        return typ;
    }

    public int getVerfuegbar() {
        return verfuegbar;
    }

    public void setVerfuegbar(int verfuegbar) {
        this.verfuegbar = verfuegbar;
    }

    public RadKategorie add() {
        this.verfuegbar++;
        return this;
    }
}
