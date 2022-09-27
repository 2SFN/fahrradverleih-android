package de.fhswf.fahrradverleih.android.view.rad_auswahl.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.fhswf.fahrradverleih.android.FahrradverleihApp;
import de.fhswf.fahrradverleih.android.api.RadApi;
import de.fhswf.fahrradverleih.android.api.remote.RemoteRadApi;
import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.FahrradTyp;
import de.fhswf.fahrradverleih.android.model.Station;

public class RadAuswahlViewModel extends ViewModel {

    @NonNull
    private final RadApi api;

    private final MutableLiveData<Status> status = new MutableLiveData<>(Status.INITIAL);
    private final MutableLiveData<Station> station = new MutableLiveData<>(new Station());
    private final MutableLiveData<List<Fahrrad>> raeder = new MutableLiveData<>(List.of());
    private final MutableLiveData<List<RadKategorie>> typen = new MutableLiveData<>(List.of());
    private final MutableLiveData<FahrradTyp> auswahlTyp = new MutableLiveData<>(null);
    private final MutableLiveData<Fahrrad> auswahlRad = new MutableLiveData<>(null);

    public RadAuswahlViewModel(@NonNull RadApi api) {
        this.api = api;
    }

    public void fetchData() {
        setStatus(Status.FETCHING);
        setRaeder(List.of());
        setTypen(List.of());
        setAuswahlRad(null);
        setAuswahlTyp(null);

        api.getRaeder(
                getStationValue().getId(),
                raeder -> {
                    // Typen sammeln
                    Map<String, RadKategorie> typen = new HashMap<>();
                    raeder.forEach(rad -> typen.merge(rad.getTyp().getBezeichnung(),
                            new RadKategorie(rad.getTyp(), 1),
                            (old, val) -> old.add()));

                    setTypen(new ArrayList<>(typen.values()));
                    setRaeder(raeder);
                    setStatus(Status.AUSWAHL_TYP);
                },
                e -> setStatus(Status.FAILED)
        );
    }

    public void radSelected(@NonNull Fahrrad rad) {
        setAuswahlRad(rad);
        setStatus(Status.DONE);
    }

    public void typSelected(@NonNull FahrradTyp typ) {
        setAuswahlTyp(typ);
        setStatus(Status.AUSWAHL_RAD);
    }

    public void navigateBack() {
        if(getStatusValue() == Status.AUSWAHL_RAD) {
            // Wenn bereits eine Kategorie ausgewählt wurde, wird diese Auswahl
            // nur gelöscht und zurück zur Kategorien-Übersicht gewechselt
            setAuswahlTyp(null);
            setStatus(Status.AUSWAHL_TYP);
        } else {
            setStatus(Status.CANCELLED);
        }
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }

    @NonNull
    public Status getStatusValue() {
        return Objects.requireNonNull(this.status.getValue());
    }

    private void setStatus(@NonNull Status status) {
        this.status.setValue(status);
    }

    public MutableLiveData<Station> getStation() {
        return station;
    }

    @NonNull
    public Station getStationValue() {
        return Objects.requireNonNull(this.station.getValue());
    }

    public void setStation(@NonNull Station station) {
        this.station.setValue(station);
    }

    public MutableLiveData<List<Fahrrad>> getRaeder() {
        return raeder;
    }

    @NonNull
    public List<Fahrrad> getRaederValue() {
        return Objects.requireNonNull(this.raeder.getValue());
    }

    @NonNull
    public List<Fahrrad> getRaederGefiltert() {
        return getRaederValue().stream()
                .filter(rad -> rad.getTyp().equals(getAuswahlTypValue()))
                .collect(Collectors.toList());
    }

    private void setRaeder(@NonNull List<Fahrrad> raeder) {
        this.raeder.setValue(raeder);
    }

    public MutableLiveData<List<RadKategorie>> getTypen() {
        return typen;
    }

    @NonNull
    public List<RadKategorie> getTypenValue() {
        return Objects.requireNonNull(this.typen.getValue());
    }

    private void setTypen(@NonNull List<RadKategorie> typen) {
        this.typen.setValue(typen);
    }

    public MutableLiveData<FahrradTyp> getAuswahlTyp() {
        return auswahlTyp;
    }

    @Nullable
    public FahrradTyp getAuswahlTypValue() {
        return this.auswahlTyp.getValue();
    }

    private void setAuswahlTyp(@Nullable FahrradTyp auswahlTyp) {
        this.auswahlTyp.setValue(auswahlTyp);
    }

    public MutableLiveData<Fahrrad> getAuswahlRad() {
        return auswahlRad;
    }

    @Nullable
    public Fahrrad getAuswahlRadValue() {
        return this.auswahlRad.getValue();
    }

    private void setAuswahlRad(@Nullable Fahrrad auswahlRad) {
        this.auswahlRad.setValue(auswahlRad);
    }

    public static final ViewModelInitializer<RadAuswahlViewModel> initializer =
            new ViewModelInitializer<>(RadAuswahlViewModel.class,
                    creationExtras -> {
                        var app = (FahrradverleihApp) creationExtras.get(
                                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
                        if (app == null)
                            throw new IllegalStateException("Application must not be null!");

                        return new RadAuswahlViewModel(new RemoteRadApi(app));
                    }
            );

}
