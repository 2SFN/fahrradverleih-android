package de.fhswf.fahrradverleih.android.view.neue_ausleihe.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.time.LocalDateTime;
import java.util.Objects;

import de.fhswf.fahrradverleih.android.FahrradverleihApp;
import de.fhswf.fahrradverleih.android.api.RadApi;
import de.fhswf.fahrradverleih.android.api.remote.RemoteRadApi;
import de.fhswf.fahrradverleih.android.model.Ausleihe;
import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.Station;

public class NeueAusleiheViewModel extends ViewModel {
    private static final int DAUER_MAX = 48;

    @NonNull
    private final RadApi api;

    private final MutableLiveData<Status> status = new MutableLiveData<>(Status.INITIAL);
    private final MutableLiveData<Station> station = new MutableLiveData<>(new Station());
    private final MutableLiveData<Fahrrad> rad = new MutableLiveData<>(new Fahrrad());
    private final MutableLiveData<Integer> dauer = new MutableLiveData<>(0);
    private final MutableLiveData<Ausleihe> ausleihe = new MutableLiveData<>(null);

    public NeueAusleiheViewModel(@NonNull RadApi api) {
        this.api = api;
    }

    public void init(@NonNull Station station, @NonNull Fahrrad rad) {
        setStation(station);
        setRad(rad);
        setStatus(Status.IDLE);
        setDauer(rad.getTyp().getTarif().getTaktung());
    }

    public void plusClicked() {
        int neueDauer = getDauerValue() + getRadValue().getTyp().getTarif().getTaktung();
        if (neueDauer <= DAUER_MAX)
            setDauer(neueDauer);
    }

    public void minusClicked() {
        int neueDauer = getDauerValue() - getRadValue().getTyp().getTarif().getTaktung();
        if (neueDauer >= getRadValue().getTyp().getTarif().getTaktung())
            setDauer(neueDauer);
    }

    public void buchenClicked() {
        setStatus(Status.FETCHING);
        api.neueAusleihe(
                getRadValue().getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(getDauerValue()),
                ausleihe -> {
                    setAusleihe(ausleihe);
                    setStatus(Status.SUCCESS);
                },
                e -> {
                    setStatus(Status.FAILURE);
                    setStatus(Status.IDLE);
                }
        );
    }

    public void cancelClicked() {
        setStatus(Status.CANCELLED);
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

    private void setStation(@NonNull Station station) {
        this.station.setValue(station);
    }

    public MutableLiveData<Fahrrad> getRad() {
        return rad;
    }

    @NonNull
    public Fahrrad getRadValue() {
        return Objects.requireNonNull(this.rad.getValue());
    }

    private void setRad(@NonNull Fahrrad rad) {
        this.rad.setValue(rad);
    }

    public MutableLiveData<Integer> getDauer() {
        return dauer;
    }

    @NonNull
    public Integer getDauerValue() {
        return Objects.requireNonNull(this.dauer.getValue());
    }

    private void setDauer(@NonNull Integer dauer) {
        this.dauer.setValue(dauer);
    }

    public MutableLiveData<Ausleihe> getAusleihe() {
        return ausleihe;
    }

    @Nullable
    public Ausleihe getAusleiheValue() {
        return this.ausleihe.getValue();
    }

    private void setAusleihe(@Nullable Ausleihe ausleihe) {
        this.ausleihe.setValue(ausleihe);
    }

    public static final ViewModelInitializer<NeueAusleiheViewModel> initializer =
            new ViewModelInitializer<>(NeueAusleiheViewModel.class,
                    creationExtras -> {
                        var app = (FahrradverleihApp) creationExtras.get(
                                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
                        if (app == null)
                            throw new IllegalStateException("Application must not be null!");

                        return new NeueAusleiheViewModel(new RemoteRadApi(app));
                    }
            );
}
