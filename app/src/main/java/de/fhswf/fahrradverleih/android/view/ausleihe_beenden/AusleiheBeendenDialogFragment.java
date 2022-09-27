package de.fhswf.fahrradverleih.android.view.ausleihe_beenden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.radiobutton.MaterialRadioButton;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.model.Ausleihe;
import de.fhswf.fahrradverleih.android.model.Station;
import de.fhswf.fahrradverleih.android.view.ausleihe_beenden.viewmodel.AusleiheBeendenViewModel;
import de.fhswf.fahrradverleih.android.widget.FullscreenDialogFragment;

public class AusleiheBeendenDialogFragment extends FullscreenDialogFragment {
    /**
     * Schlüssel für den {@link androidx.fragment.app.FragmentManager}, um Callbacks
     * mit Results zu identifizieren.
     */
    public static final String REQUEST_KEY = "ausleihe_beenden_result";

    /**
     * Im Result-Bundle des Fragments wird unter diesem Key ein {@link Ausleihe}-Item
     * abgelegt, sofern die Rückgabe erfolgreich war.
     * <p>
     * Gab es einen Fehler, oder wurde der Prozess abgebrochen, ist das Feld leer.
     */
    public static final String RESULT_AUSLEIHE = "ausleihe";

    private static final String ARG_AUSLEIHE = "ausleihe";

    private AusleiheBeendenViewModel viewModel;

    public AusleiheBeendenDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Erstelle eine neue Fragment-Instanz zu einer bestimmten Ausleihe.
     *
     * @param ausleihe Zu beendende {@link Ausleihe}.
     * @return Fragment-Instanz.
     */
    public static AusleiheBeendenDialogFragment newInstance(@NonNull Ausleihe ausleihe) {
        AusleiheBeendenDialogFragment fragment = new AusleiheBeendenDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_AUSLEIHE, ausleihe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel-Instanz
        this.viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(
                AusleiheBeendenViewModel.initializer)).get(AusleiheBeendenViewModel.class);

        // Argumente lesen
        if (getArguments() != null) {
            var ausleihe = (Ausleihe) getArguments().getSerializable(ARG_AUSLEIHE);
            viewModel.setAusleihe(ausleihe);
        }
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container,
                             Bundle savedInstanceState) {
        return i.inflate(R.layout.fragment_ausleihe_beenden_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Toolbar Titel
        Toolbar toolbar = view.findViewById(R.id.dialog_toolbar);
        viewModel.getAusleihe().observe(getViewLifecycleOwner(),
                ausleihe -> toolbar.setSubtitle(String.format(
                        "Rad-ID: %s", ausleihe.getFahrrad().getId())));

        // Stationen Radio-Buttons
        RadioGroup radioGroup = view.findViewById(R.id.radio_container);

        viewModel.getStationen().observe(getViewLifecycleOwner(), stationen -> {
            radioGroup.removeAllViews();
            for (var s : stationen) radioGroup.addView(buildRadioButton(s));
        });

        // Abbrechen Button
        view.findViewById(R.id.cancel).setOnClickListener(v -> viewModel.cancelled());

        // Bestätigen Button
        view.findViewById(R.id.confirm).setOnClickListener(v -> viewModel.beendenRequested());

        // Status-Navigation
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case INITIAL:
                    // Anfänglich: Daten abrufen
                    viewModel.fetchData();
                    break;
                case FAILURE:
                    // Zeige Dialog bei Fehlern
                    showFailureDialog();
                    break;
                case SUCCESS:
                    // Rückgabe erfolgreich -> Fragment Result setzen und Dialog beenden
                    Bundle result = new Bundle();
                    result.putSerializable(RESULT_AUSLEIHE, viewModel.getAusleiheValue());
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                    dismiss();
                    break;
                case CANCELLED:
                    // Dialog abgebrochen -> Leeres Result setzen und Dialog beenden
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, new Bundle());
                    dismiss();
                    break;
            }
        });
    }

    private RadioButton buildRadioButton(@NonNull Station station) {
        var radio = new MaterialRadioButton(requireContext());
        radio.setText(station.getBezeichnung());
        radio.setOnCheckedChangeListener((v, checked) -> {
            if (checked) viewModel.stationSelected(station);
        });

        var params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        radio.setLayoutParams(params);

        return radio;
    }

    private void showFailureDialog() {
        if (getContext() == null) return;
        new AlertDialog.Builder(requireContext())
                .setMessage(R.string.error_panel_title)
                .setNegativeButton(R.string.cancel, (d, w) -> dismiss())
                .setPositiveButton(R.string.error_panel_retry_button, (dialog, w) -> {
                    viewModel.fetchData();
                    dialog.dismiss();
                }).show();
    }
}