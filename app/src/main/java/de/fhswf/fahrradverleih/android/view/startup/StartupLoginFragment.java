package de.fhswf.fahrradverleih.android.view.startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.util.SimpleTextWatcher;
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.AuthenticationStatus;
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupViewModel;

public class StartupLoginFragment extends Fragment {

    private StartupViewModel viewModel;

    public static StartupLoginFragment newInstance() {
        return new StartupLoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel-Instanz
        this.viewModel = new ViewModelProvider(requireActivity()).get(StartupViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_startup_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Liste von Views, welche nur bedienbar (enabled) sein sollen, wenn der
        // Authentication-Status 'idle' ist
        ArrayList<View> enableWhenIdle = new ArrayList<>();
        viewModel.getAuthenticationStatus().observe(getViewLifecycleOwner(),
                status -> enableWhenIdle.forEach(
                        v -> v.setEnabled(status == AuthenticationStatus.IDLE)));

        // Einzelne Views konfigurieren

        // E-Mail-Input
        TextInputEditText emailInput = view.findViewById(R.id.email_input);

        // Aktualisiert den Wert des ViewModels, wenn der Anwender den Text manipuliert
        emailInput.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.setEmail(text)));

        // Deaktiviert das View, wenn die Authentifizierung im Gange ist (s. oben)
        enableWhenIdle.add(emailInput);

        // Passwort-Input
        TextInputEditText passInput = view.findViewById(R.id.password_input);
        passInput.addTextChangedListener(new SimpleTextWatcher(t -> viewModel.setPassword(t)));
        enableWhenIdle.add(passInput);

        // Anmelden-Button
        Button anmeldenButton = view.findViewById(R.id.login);
        anmeldenButton.setOnClickListener(v -> viewModel.login());
        enableWhenIdle.add(anmeldenButton);

        // Reagiere auf Authentifizierungsevents, um dem Anwender Feedback zu geben
        viewModel.getAuthenticationStatus().observe(getViewLifecycleOwner(), status -> {
            if (status == AuthenticationStatus.FAILED)
                Snackbar.make(view, "Anmeldung fehlgeschlagen!", Snackbar.LENGTH_LONG).show();
        });
    }
}
