package de.fhswf.fahrradverleih.android.model;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Ausleihe implements Serializable {

    @NonNull
    private String id;

    @NonNull
    private Fahrrad fahrrad;

    @NonNull
    private TarifT tarif;

    @NonNull
    private LocalDateTime von;

    @NonNull
    private LocalDateTime bis;

    public Ausleihe(@NonNull String id, @NonNull Fahrrad fahrrad, @NonNull TarifT tarif,
                    @NonNull LocalDateTime von, @NonNull LocalDateTime bis) {
        this.id = id;
        this.fahrrad = fahrrad;
        this.tarif = tarif;
        this.von = von;
        this.bis = bis;
    }

    public Ausleihe() {
        this("", new Fahrrad(), new TarifT(), LocalDateTime.now(), LocalDateTime.now());
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public Fahrrad getFahrrad() {
        return fahrrad;
    }

    public void setFahrrad(@NonNull Fahrrad fahrrad) {
        this.fahrrad = fahrrad;
    }

    @NonNull
    public TarifT getTarif() {
        return tarif;
    }

    public void setTarif(@NonNull TarifT tarif) {
        this.tarif = tarif;
    }

    @NonNull
    public LocalDateTime getVon() {
        return von;
    }

    public void setVon(@NonNull LocalDateTime von) {
        this.von = von;
    }

    @NonNull
    public LocalDateTime getBis() {
        return bis;
    }

    public void setBis(@NonNull LocalDateTime bis) {
        this.bis = bis;
    }

    public boolean isAktiv() {
        return bis.isAfter(LocalDateTime.now());
    }
}
