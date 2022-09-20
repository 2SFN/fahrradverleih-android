package de.fhswf.fahrradverleih.android.view.profil.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import de.fhswf.fahrradverleih.android.FahrradverleihApp;
import de.fhswf.fahrradverleih.android.api.RadApi;
import de.fhswf.fahrradverleih.android.api.remote.RemoteRadApi;
import de.fhswf.fahrradverleih.android.model.Benutzer;

public class ProfilViewModel extends ViewModel {

    private final RadApi api;

    private final MutableLiveData<Status> status = new MutableLiveData<>(Status.FETCHING);
    private final MutableLiveData<Benutzer> benutzer = new MutableLiveData<>(new Benutzer());

    public ProfilViewModel(RadApi api) {
        this.api = api;
    }

    public void fetchBenutzer() {
        setStatus(Status.FETCHING);
        api.getBenutzer(
                benutzer -> {
                    setBenutzer(benutzer);
                    setStatus(Status.IDLE);
                },
                e -> setStatus(Status.FAILED));
    }

    public void saveBenutzer() {
        setStatus(Status.SAVING);
        api.setBenutzer(getBenutzerValue(),
                res -> {
                    setBenutzer(res);
                    setStatus(Status.IDLE);
                },
                e -> setStatus(Status.FAILED)
        );
    }

    public void logout() {
        // Token löschen und Status aktualisieren
        if (api instanceof RemoteRadApi) ((RemoteRadApi) api).updateToken(null);
        setStatus(Status.LOGOUT);
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }

    @NonNull
    public Status getStatusValue() {
        if (status.getValue() == null) throw new IllegalStateException();
        return status.getValue();
    }

    private void setStatus(@NonNull Status status) {
        this.status.setValue(status);
    }

    public MutableLiveData<Benutzer> getBenutzer() {
        return benutzer;
    }

    @NonNull
    public Benutzer getBenutzerValue() {
        if (benutzer.getValue() == null) throw new IllegalStateException();
        return benutzer.getValue();
    }

    public void setBenutzer(@NonNull Benutzer benutzer) {
        this.benutzer.setValue(benutzer);
    }

    public static final ViewModelInitializer<ProfilViewModel> initializer =
            new ViewModelInitializer<>(ProfilViewModel.class,
                    creationExtras -> {
                        var app = (FahrradverleihApp) creationExtras.get(
                                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
                        if (app == null)
                            throw new IllegalStateException("Application must not be null!");
                        return new ProfilViewModel(new RemoteRadApi(app));
                    }
            );
}
