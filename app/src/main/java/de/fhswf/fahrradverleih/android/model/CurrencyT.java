package de.fhswf.fahrradverleih.android.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CurrencyT implements Serializable {

    private int betrag;

    @NonNull
    private String iso4217;

    public CurrencyT(int betrag, @NonNull String iso4217) {
        this.betrag = betrag;
        this.iso4217 = iso4217;
    }

    public CurrencyT() {
        this(0, "EUR");
    }

    public int getBetrag() {
        return betrag;
    }

    public void setBetrag(int betrag) {
        this.betrag = betrag;
    }

    @NonNull
    public String getIso4217() {
        return iso4217;
    }

    public void setIso4217(@NonNull String iso4217) {
        this.iso4217 = iso4217;
    }
}
