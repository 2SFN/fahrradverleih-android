package de.fhswf.fahrradverleih.android.view.map.viewmodel;

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
import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.Station;

public class MapViewModel extends ViewModel {
    private static final String TAG = MapViewModel.class.getSimpleName();

    @NonNull
    private final RadApi api;

    private final MutableLiveData<Status> status = new MutableLiveData<>(Status.INITIAL);
    private final MutableLiveData<List<Station>> stationen = new MutableLiveData<>(List.of());
    private final MutableLiveData<Station> auswahlStation = new MutableLiveData<>(null);
    private final MutableLiveData<Fahrrad> auswahlRad = new MutableLiveData<>(null);

    public MapViewModel(@NonNull RadApi api) {
        this.api = api;
    }

    public void fetchStationen() {
        setStatus(Status.FETCHING);
        setStationen(List.of());
        setAuswahlStation(null);
        setAuswahlRad(null);

        api.getStationen(
                stationen -> {
                    setStationen(stationen);
                    setStatus(Status.PERMISSIONS_CHECK);
                },
                e -> setStatus(Status.FAILURE)
        );
    }

    public void permissionsCheckFinished() {
        setStatus(Status.IDLE);
    }

    public void stationSelected(@NonNull Station station) {
        setAuswahlStation(station);
        setStatus(Status.RAD_AUSWAHL);
    }

    public void radSelected(@NonNull Fahrrad rad) {
        setAuswahlRad(rad);
        setStatus(Status.BUCHUNG);
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

    public MutableLiveData<List<Station>> getStationen() {
        return stationen;
    }

    @NonNull
    public List<Station> getStationenValue() {
        return Objects.requireNonNull(this.stationen.getValue(), "stationen");
    }

    public void setStationen(@NonNull List<Station> stationen) {
        this.stationen.setValue(stationen);
    }

    public MutableLiveData<Station> getAuswahlStation() {
        return auswahlStation;
    }

    @Nullable
    public Station getAuswahlStationValue() {
        return this.auswahlStation.getValue();
    }

    public void setAuswahlStation(@Nullable Station auswahlStation) {
        this.auswahlStation.setValue(auswahlStation);
    }

    public MutableLiveData<Fahrrad> getAuswahlRad() {
        return auswahlRad;
    }

    @Nullable
    public Fahrrad getAuswahlRadValue() {
        return this.auswahlRad.getValue();
    }

    public void setAuswahlRad(@Nullable Fahrrad auswahlRad) {
        this.auswahlRad.setValue(auswahlRad);
    }

    public static final ViewModelInitializer<MapViewModel> initializer =
            new ViewModelInitializer<>(MapViewModel.class,
                    creationExtras -> {
                        var app = (FahrradverleihApp) creationExtras.get(
                                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
                        if (app == null)
                            throw new IllegalStateException("Application must not be null!");

                        return new MapViewModel(new RemoteRadApi(app));
                    }
            );
}
