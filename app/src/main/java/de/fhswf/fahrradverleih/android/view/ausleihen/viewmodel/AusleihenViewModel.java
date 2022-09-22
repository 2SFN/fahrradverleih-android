package de.fhswf.fahrradverleih.android.view.ausleihen.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.List;

import de.fhswf.fahrradverleih.android.FahrradverleihApp;
import de.fhswf.fahrradverleih.android.api.RadApi;
import de.fhswf.fahrradverleih.android.api.remote.RemoteRadApi;
import de.fhswf.fahrradverleih.android.model.Ausleihe;

public class AusleihenViewModel extends ViewModel {
    private static final String TAG = AusleihenViewModel.class.getSimpleName();

    @NonNull
    private final RadApi api;

    private final MutableLiveData<Status> status = new MutableLiveData<>(Status.INITIAL);
    private final MutableLiveData<List<Ausleihe>> ausleihen = new MutableLiveData<>(List.of());
    private final MutableLiveData<Ausleihe> auswahl = new MutableLiveData<>(null);

    public AusleihenViewModel(@NonNull RadApi api) {
        this.api = api;
    }

    /**
     * Wird aufgerufen, sobald der Anwender eine einzelne Ausleihe auswählt,
     * bzw. das Rad zurückgeben möchte.
     *
     * @param ausleihe Ausgewähltes Ausleihe-Objekt.
     */
    public void ausleiheSelected(@NonNull Ausleihe ausleihe) {
        setAuswahl(ausleihe);
        setStatus(Status.RUECKGABE);
    }

    /**
     * Meldet, dass der laufende Rückgabe-Prozess abgeschlossen ist.
     *
     * @param changed Ob Daten geändert wurden.
     */
    public void rueckgabeAbgeschlossen(boolean changed) {
        if(changed) fetchData();
        else setStatus(Status.IDLE);
    }

    public void fetchData() {
        if(getStatusValue() == Status.FETCHING) return;
        Log.i(TAG, "fetchData: Fetching Data...");

        setStatus(Status.FETCHING);
        setAuswahl(null);
        api.getAusleihen(
                ausleihen -> {
                    setAusleihen(ausleihen);
                    setStatus(Status.IDLE);
                },
                e -> setStatus(Status.FAILED)
        );
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
        Log.i(TAG, "setStatus: Status is now " + status.name());
        this.status.setValue(status);
    }

    public MutableLiveData<List<Ausleihe>> getAusleihen() {
        return ausleihen;
    }

    @NonNull
    public List<Ausleihe> getAusleihenValue() {
        if (ausleihen.getValue() == null) throw new IllegalStateException();
        return ausleihen.getValue();
    }

    public void setAusleihen(@NonNull List<Ausleihe> ausleihen) {
        this.ausleihen.setValue(ausleihen);
    }

    public MutableLiveData<Ausleihe> getAuswahl() {
        return auswahl;
    }

    @Nullable
    public Ausleihe getAuswahlValue() {
        return auswahl.getValue();
    }

    public void setAuswahl(@Nullable Ausleihe ausleihe) {
        this.auswahl.setValue(ausleihe);
    }

    public static final ViewModelInitializer<AusleihenViewModel> initializer =
            new ViewModelInitializer<>(AusleihenViewModel.class,
                    creationExtras -> {
                        var app = (FahrradverleihApp) creationExtras.get(
                                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
                        if (app == null)
                            throw new IllegalStateException("Application must not be null!");

                        return new AusleihenViewModel(new RemoteRadApi(app));
                    }
            );
}
