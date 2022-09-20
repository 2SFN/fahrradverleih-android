package de.fhswf.fahrradverleih.android.view.startup.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import de.fhswf.fahrradverleih.android.FahrradverleihApp;
import de.fhswf.fahrradverleih.android.api.RadApi;
import de.fhswf.fahrradverleih.android.api.remote.RemoteRadApi;

public class StartupViewModel extends ViewModel {

    private final RadApi api;

    private final MutableLiveData<StartupContent> content =
            new MutableLiveData<>(StartupContent.AUTHENTICATION);

    private final MutableLiveData<AuthenticationStatus> authenticationStatus =
            new MutableLiveData<>(AuthenticationStatus.IDLE);

    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");

    public StartupViewModel(RadApi api) {
        this.api = api;
    }

    public void setContent(@NonNull StartupContent content) {
        this.content.setValue(content);

        switch (content) {
            case AUTHENTICATION:
                authenticate();
                break;
            case LOGIN:
                setAuthenticationStatus(AuthenticationStatus.IDLE);
                setEmail("");
                setPassword("");
                break;
        }
    }

    /**
     * Geht mit Navigationsevents um.
     */
    public void backPressed() {
        switch (getContentValue()) {
            case AUTHENTICATION:
            case WELCOME:
                return;
            case LOGIN:
            case REGISTER:
            default:
                setContent(StartupContent.AUTHENTICATION);
        }
    }

    /**
     * Löst den Login-Prozess aus (erstellt eine neue Session).
     */
    public void login() {
        if (getAuthenticationStatusValue() != AuthenticationStatus.IDLE) return;
        setAuthenticationStatus(AuthenticationStatus.AUTHENTICATING);
        api.login(
                getEmailValue(), getPasswordValue(),
                result -> {
                    if (api instanceof RemoteRadApi)
                        ((RemoteRadApi) api).updateToken(result.getToken());
                    setContent(StartupContent.AUTHENTICATION);
                },
                e -> {
                    setAuthenticationStatus(AuthenticationStatus.FAILED);
                    setAuthenticationStatus(AuthenticationStatus.IDLE);
                }
        );
    }

    /**
     * Prüft, ob die Authentifizierung bei der Rad-API gegeben ist.
     * <p>
     * Falls nicht, wird auf die Willkommensseite gewechselt, auf welcher der
     * Anwender sich (erneut) anmelden kann.
     */
    private void authenticate() {
        setAuthenticationStatus(AuthenticationStatus.AUTHENTICATING);
        api.auth(
                (res) -> setAuthenticationStatus(AuthenticationStatus.AUTHENTICATED),
                (e) -> {
                    setAuthenticationStatus(AuthenticationStatus.FAILED);
                    setAuthenticationStatus(AuthenticationStatus.IDLE);
                    setContent(StartupContent.WELCOME);
                }
        );
    }

    public LiveData<StartupContent> getContent() {
        return content;
    }

    @NonNull
    public StartupContent getContentValue() {
        if (content.getValue() == null) throw new IllegalStateException();
        return content.getValue();
    }

    public LiveData<AuthenticationStatus> getAuthenticationStatus() {
        return authenticationStatus;
    }

    @NonNull
    public AuthenticationStatus getAuthenticationStatusValue() {
        if (authenticationStatus.getValue() == null) throw new IllegalStateException();
        return authenticationStatus.getValue();
    }

    public void setAuthenticationStatus(@NonNull AuthenticationStatus authenticationStatus) {
        this.authenticationStatus.setValue(authenticationStatus);
    }

    public LiveData<String> getEmail() {
        return email;
    }

    @NonNull
    public String getEmailValue() {
        if (email.getValue() == null) throw new IllegalStateException();
        return email.getValue();
    }

    public void setEmail(@NonNull String email) {
        this.email.setValue(email);
    }

    public LiveData<String> getPassword() {
        return password;
    }

    @NonNull
    public String getPasswordValue() {
        if (password.getValue() == null) throw new IllegalStateException();
        return password.getValue();
    }

    public void setPassword(@NonNull String password) {
        this.password.setValue(password);
    }

    /**
     * Eigene ViewModel-Factory/Initializer.
     * <p>
     * Liefert in der Funktion für die Erstellung einer ViewModel-Instanz
     * {@link CreationExtras} mit, durch welche unter anderem die Anwendungsinstanz
     * bezogen werden kann. Diese dient als Context für die {@link RemoteRadApi}.
     */
    public static final ViewModelInitializer<StartupViewModel> initializer =
            new ViewModelInitializer<>(
                    StartupViewModel.class,
                    creationExtras -> {
                        var app = (FahrradverleihApp) creationExtras.get(
                                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
                        if (app == null)
                            throw new IllegalStateException("Application must not be null!");

                        return new StartupViewModel(new RemoteRadApi(app));
                    }
            );
}
