package de.fhswf.fahrradverleih.android.view.neue_ausleihe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.Station;
import de.fhswf.fahrradverleih.android.model.TarifT;
import de.fhswf.fahrradverleih.android.util.RadIconUtil;
import de.fhswf.fahrradverleih.android.view.neue_ausleihe.viewmodel.NeueAusleiheViewModel;
import de.fhswf.fahrradverleih.android.view.neue_ausleihe.viewmodel.Status;
import de.fhswf.fahrradverleih.android.widget.FullscreenDialogFragment;

public class NeueAusleiheDialogFragment extends FullscreenDialogFragment {
    /**
     * Schlüssel für {@link androidx.fragment.app.FragmentManager}, um Callbacks mit
     * Result zu identifizieren.
     */
    public static final String REQUEST_KEY = "neue_ausleihe_request";

    /**
     * Result-Bundle Key für die neue Ausleihe.
     * <p>
     * Bei Fehlern oder Abbruch durch den Anwender ist das Feld leer.
     */
    public static final String RESULT_AUSLEIHE = "ausleihe";

    private static final String ARG_STATION = "station";
    private static final String ARG_FAHRRAD = "fahrrad";

    private NeueAusleiheViewModel viewModel;

    public NeueAusleiheDialogFragment() {
        // Required empty public constructor
    }

    public static NeueAusleiheDialogFragment newInstance(
            @NonNull Station station, @NonNull Fahrrad rad) {
        NeueAusleiheDialogFragment fragment = new NeueAusleiheDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATION, station);
        args.putSerializable(ARG_FAHRRAD, rad);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel-Instanz
        this.viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(
                NeueAusleiheViewModel.initializer)).get(NeueAusleiheViewModel.class);

        // Argumente lesen
        if(getArguments() != null) {
            var station = (Station) getArguments().getSerializable(ARG_STATION);
            var fahrrad = (Fahrrad) getArguments().getSerializable(ARG_FAHRRAD);
            viewModel.init(station, fahrrad);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_neue_ausleihe_dialog, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.dialog_toolbar);

        // Reagiere auf Status-Änderungen
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case IDLE:
                    toolbar.setTitle(viewModel.getStationValue().getBezeichnung());
                    break;
                case FAILURE:
                    Snackbar.make(view, R.string.neue_ausleihe_error_message,
                            Snackbar.LENGTH_LONG).show();
                    break;
                case SUCCESS:
                    // Fragment Result mit neuer Ausleihe
                    Bundle result = new Bundle();
                    result.putSerializable(RESULT_AUSLEIHE, viewModel.getAusleiheValue());
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                    dismiss();
                    break;
                case CANCELLED:
                    // Leeres Result
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, new Bundle());
                    dismiss();
                    break;
            }
        });

        // Rad-Info
        viewModel.getRad().observe(getViewLifecycleOwner(), rad -> {
            ((TextView) view.findViewById(R.id.rad_typ))
                    .setText(rad.getTyp().getBezeichnung());
            ((TextView) view.findViewById(R.id.tarif))
                    .setText(tarifInfo(rad.getTyp().getTarif()));
            ((TextView) view.findViewById(R.id.id_info))
                    .setText(idInfo(rad));
            ((ImageView) view.findViewById(R.id.rad_icon))
                    .setImageResource(RadIconUtil.iconFor(rad.getTyp()));
        });

        // Dauer aktualisieren
        viewModel.getDauer().observe(getViewLifecycleOwner(), dauer -> {
            ((TextView) view.findViewById(R.id.dauer)).setText(dauer.toString());
            ((TextView) view.findViewById(R.id.info)).setText(
                    ausleiheInfo(dauer, viewModel.getRadValue().getTyp().getTarif()));
        });

        // Während der Anfrage zu deaktivierende Views
        Set<View> enableWhenIdle = new HashSet<>();
        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->
                enableWhenIdle.forEach(v -> v.setEnabled(status == Status.IDLE)));

        // Input Buttons
        Button minus = view.findViewById(R.id.minus);
        minus.setOnClickListener(v -> viewModel.minusClicked());
        enableWhenIdle.add(minus);

        Button plus = view.findViewById(R.id.plus);
        plus.setOnClickListener(v -> viewModel.plusClicked());
        enableWhenIdle.add(plus);

        // Bottom Buttons
        Button buchen = view.findViewById(R.id.buchen);
        buchen.setOnClickListener(v -> viewModel.buchenClicked());
        enableWhenIdle.add(buchen);

        view.findViewById(R.id.back).setOnClickListener(v -> viewModel.cancelClicked());
    }

    private static String ausleiheInfo(int dauer, @NonNull TarifT t) {
        int preis = (int) (((float) dauer / t.getTaktung()) * t.getPreis().getBetrag());
        var zeit = LocalDateTime.now().plusHours(dauer);

        return String.format("Gesamtpreis %s %s\nRückgabe bis %s Uhr",
                t.getPreis().getIso4217(), preis,
                zeit.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
    }

    private static String tarifInfo(@NonNull TarifT t) {
        return String.format(Locale.GERMANY, "Tarif: %s %d für %d Stunde(n)",
                t.getPreis().getIso4217(), t.getPreis().getBetrag(), t.getTaktung());
    }

    private static String idInfo(@NonNull Fahrrad rad) {
        return String.format(Locale.GERMANY, "ID: %s", rad.getId());
    }
}