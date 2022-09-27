package de.fhswf.fahrradverleih.android.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class FahrradTyp implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FahrradTyp)) return false;

        FahrradTyp that = (FahrradTyp) o;

        return bezeichnung.equals(that.bezeichnung);
    }

    @Override
    public int hashCode() {
        return bezeichnung.hashCode();
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
