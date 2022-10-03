package de.fhswf.fahrradverleih.android.view.profil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashSet;
import java.util.Set;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.util.SimpleTextWatcher;
import de.fhswf.fahrradverleih.android.view.profil.viewmodel.ProfilViewModel;
import de.fhswf.fahrradverleih.android.view.profil.viewmodel.Status;
import de.fhswf.fahrradverleih.android.view.startup.StartupActivity;
import de.fhswf.fahrradverleih.android.widget.IconInputView;

public class ProfilFragment extends Fragment {

    private ProfilViewModel viewModel;

    public static ProfilFragment newInstance() {
        return new ProfilFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel-Instanz
        this.viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(
                ProfilViewModel.initializer)).get(ProfilViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /// Container/Layer konfigurieren (Error-Panel; Form-Panel)
        ViewGroup errorPanel = view.findViewById(R.id.error_panel);
        ViewGroup formPanel = view.findViewById(R.id.form_panel);

        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status == Status.FAILED) {
                errorPanel.setVisibility(View.VISIBLE);
                formPanel.setVisibility(View.GONE);
            } else {
                errorPanel.setVisibility(View.GONE);
                formPanel.setVisibility(View.VISIBLE);
            }
        });

        // Erneut Versuchen Button konfigurieren
        view.findViewById(R.id.retry).setOnClickListener(v -> viewModel.fetchBenutzer());

        /// Eingabefelder

        // Liste von Views, die nur aktiviert sein sollen, wenn der Status idle ist
        Set<View> enableWhenIdle = new HashSet<>(6);
        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->
                enableWhenIdle.forEach(v -> v.setEnabled(status == Status.IDLE)));

        // Form Inputs konfigurieren, Vorname:
        IconInputView vornameInput = view.findViewById(R.id.vorname_input);

        // I. Aktualisiere das ViewModel bei Änderungen
        vornameInput.addTextWatcher(new SimpleTextWatcher(
                t -> viewModel.getBenutzerValue().setVorname(t)));

        // II. Übernehme neuen Wert, sobald der Benutzer im VM sich ändert
        viewModel.getBenutzer().observe(getViewLifecycleOwner(),
                benutzer -> vornameInput.setText(benutzer.getVorname()));

        // III. In Liste zu (de-)aktivierender Views eintragen
        enableWhenIdle.add(vornameInput);

        // Nachname
        IconInputView nachnameInput = view.findViewById(R.id.nachname_input);
        nachnameInput.addTextWatcher(new SimpleTextWatcher(
                t -> viewModel.getBenutzerValue().setName(t)));
        viewModel.getBenutzer().observe(getViewLifecycleOwner(),
                benutzer -> nachnameInput.setText(benutzer.getName()));
        enableWhenIdle.add(nachnameInput);

        // E-Mail Adresse
        IconInputView emailInput = view.findViewById(R.id.email_input);
        emailInput.addTextWatcher(new SimpleTextWatcher(
                t -> viewModel.getBenutzerValue().setEmail(t)));
        viewModel.getBenutzer().observe(getViewLifecycleOwner(),
                benutzer -> emailInput.setText(benutzer.getEmail()));
        enableWhenIdle.add(emailInput);

        // Anmeldung Ändern: nur (de-)aktivieren; Funktion nicht implementiert
        enableWhenIdle.add(view.findViewById(R.id.configure_login));

        // Profil-ID: Read only; nur Daten aktualisieren
        IconInputView idInput = view.findViewById(R.id.id_input);
        viewModel.getBenutzer().observe(getViewLifecycleOwner(),
                benutzer -> idInput.setText(benutzer.getId()));

        /// Buttons

        Button abmeldenButton = view.findViewById(R.id.logout);
        abmeldenButton.setOnClickListener(v -> viewModel.logout());
        enableWhenIdle.add(abmeldenButton);

        Button anwendenButton = view.findViewById(R.id.apply);
        anwendenButton.setOnClickListener(v -> viewModel.saveBenutzer());
        enableWhenIdle.add(anwendenButton);

        /// Navigationsevents (Abmelden)
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if(status == Status.LOGOUT) restartApplication();
        });

        /// Anfänglich: Lade Details
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if(status == Status.INITIAL) viewModel.fetchBenutzer();
        });
    }

    private void restartApplication() {
        if(getActivity() == null) return;
        startActivity(new Intent(requireActivity(), StartupActivity.class));
        requireActivity().finish();
    }
}
