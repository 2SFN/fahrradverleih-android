package de.fhswf.fahrradverleih.android.model;

import androidx.annotation.NonNull;

public class TarifT {

    @NonNull
    private CurrencyT preis;

    private int taktung;

    public TarifT(@NonNull CurrencyT preis, int taktung) {
        this.preis = preis;
        this.taktung = taktung;
    }

    public TarifT() {
        this(new CurrencyT(), 1);
    }

    @NonNull
    public CurrencyT getPreis() {
        return preis;
    }

    public void setPreis(@NonNull CurrencyT preis) {
        this.preis = preis;
    }

    public int getTaktung() {
        return taktung;
    }

    public void setTaktung(int taktung) {
        this.taktung = taktung;
    }
}
