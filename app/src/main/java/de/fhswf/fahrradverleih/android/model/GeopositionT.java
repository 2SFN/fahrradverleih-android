package de.fhswf.fahrradverleih.android.model;

public class GeopositionT {

    private double breite;
    private double laenge;

    public GeopositionT(double breite, double laenge) {
        this.breite = breite;
        this.laenge = laenge;
    }

    public GeopositionT() {
        this(0, 0);
    }

    public double getBreite() {
        return breite;
    }

    public void setBreite(double breite) {
        this.breite = breite;
    }

    public double getLaenge() {
        return laenge;
    }

    public void setLaenge(double laenge) {
        this.laenge = laenge;
    }
}
