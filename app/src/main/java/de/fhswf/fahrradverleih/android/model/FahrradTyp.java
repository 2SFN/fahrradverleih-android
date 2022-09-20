package de.fhswf.fahrradverleih.android.model;

import androidx.annotation.NonNull;

public class FahrradTyp {

    @NonNull
    private String bezeichnung;

    @NonNull
    private TarifT tarif;

    public FahrradTyp(@NonNull String bezeichnung, @NonNull TarifT tarif) {
        this.bezeichnung = bezeichnung;
        this.tarif = tarif;
    }

    public FahrradTyp() {
        this("", new TarifT());
    }

    @NonNull
    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(@NonNull String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @NonNull
    public TarifT getTarif() {
        return tarif;
    }

    public void setTarif(@NonNull TarifT tarif) {
        this.tarif = tarif;
    }
}
