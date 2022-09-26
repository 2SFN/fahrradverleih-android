package de.fhswf.fahrradverleih.android.view.ausleihe_beenden.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.List;
import java.util.Objects;

import de.fhswf.fahrradverleih.android.FahrradverleihApp;
import de.fhswf.fahrradverleih.android.api.RadApi;
import de.fhswf.fahrradverleih.android.api.remote.RemoteRadApi;
import de.fhswf.fahrradverleih.android.model.Ausleihe;
import de.fhswf.fahrradverleih.android.model.Station;

public class AusleiheBeendenViewModel extends ViewModel {

    @NonNull
    private final RadApi api;

    private final MutableLiveData<Status> status = new MutableLiveData<>(Status.INITIAL);
    private final MutableLiveData<Ausleihe> ausleihe = new MutableLiveData<>(new Ausleihe());
    private final MutableLiveData<List<Station>> stationen = new MutableLiveData<>(List.of());
    private final MutableLiveData<Station> auswahl = new MutableLiveData<>(null);

    public AusleiheBeendenViewModel(@NonNull RadApi api) {
        this.api = api;
    }

    public void fetchData() {
        setStationen(List.of());
        setStatus(Status.FETCHING);
        setAuswahl(null);

        api.getStationen(
                stationen -> {
                    setStatus(Status.IDLE);
                    setStationen(stationen);
                },
                e -> setStatus(Status.FAILURE)
        );
    }

    public void cancelled() {
        setStatus(Status.CANCELLED);
    }

    public void beendenRequested() {
        if (getAuswahlValue() == null) return;
        setStatus(Status.FETCHING);

        api.beendeAusleihe(getAusleiheValue().getId(), getAuswahlValue().getId(),
                ausleihe -> {
                    setAusleihe(ausleihe);
                    setStatus(Status.SUCCESS);
                },
                e -> setStatus(Status.FAILURE));
    }

    public void stationSelected(@NonNull Station station) {
        setAuswahl(station);
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }

    @NonNull
    public Status getStatusValue() {
        return Objects.requireNonNull(this.status.getValue(), "status");
    }

    private void setStatus(@NonNull Status status) {
        this.status.setValue(status);
    }

    public MutableLiveData<Ausleihe> getAusleihe() {
        return ausleihe;
    }

    @NonNull
    public Ausleihe getAusleiheValue() {
        return Objects.requireNonNull(this.ausleihe.getValue());
    }

    public void setAusleihe(@NonNull Ausleihe ausleihe) {
        this.ausleihe.setValue(ausleihe);
    }

    public MutableLiveData<List<Station>> getStationen() {
        return stationen;
    }

    @NonNull
    public List<Station> getStationenValue() {
        return Objects.requireNonNull(this.stationen.getValue());
    }

    private void setStationen(@NonNull List<Station> stationen) {
        this.stationen.setValue(stationen);
    }

    public MutableLiveData<Station> getAuswahl() {
        return auswahl;
    }

    @Nullable
    public Station getAuswahlValue() {
        return this.auswahl.getValue();
    }

    private void setAuswahl(@Nullable Station auswahl) {
        this.auswahl.setValue(auswahl);
    }

    public static final ViewModelInitializer<AusleiheBeendenViewModel> initializer =
            new ViewModelInitializer<>(AusleiheBeendenViewModel.class,
                    creationExtras -> {
                        var app = (FahrradverleihApp) creationExtras.get(
                                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
                        if (app == null)
                            throw new IllegalStateException("Application must not be null!");

                        return new AusleiheBeendenViewModel(new RemoteRadApi(app));
                    }
            );
}
