package de.fhswf.fahrradverleih.android.view.rad_auswahl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.model.Station;
import de.fhswf.fahrradverleih.android.view.rad_auswahl.recycler.RadAuswahlAdapter;
import de.fhswf.fahrradverleih.android.view.rad_auswahl.recycler.RadDetailsListItem;
import de.fhswf.fahrradverleih.android.view.rad_auswahl.recycler.RadKategorieListItem;
import de.fhswf.fahrradverleih.android.view.rad_auswahl.viewmodel.RadAuswahlViewModel;
import de.fhswf.fahrradverleih.android.view.rad_auswahl.viewmodel.Status;
import de.fhswf.fahrradverleih.android.widget.FullscreenDialogFragment;
import de.fhswf.fahrradverleih.android.widget.end_of_list_message.EndOfListItem;
import de.fhswf.fahrradverleih.android.widget.error_panel.ErrorPanelListItem;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseItem;

public class RadAuswahlDialogFragment extends FullscreenDialogFragment {
    /**
     * Schlüssel für {@link androidx.fragment.app.FragmentManager}, um Callbacks mit
     * Result zu identifizieren.
     */
    public static final String REQUEST_KEY = "rad_auswahl_result";

    /**
     * Result-Bundle Key für das ausgewählte Fahrrad.
     * <p>
     * Bei Fehlern oder Abbruch durch den Anwender ist das Feld leer.
     */
    public static final String RESULT_FAHRRAD = "rad";

    private static final String ARG_STATION = "station";

    private RadAuswahlViewModel viewModel;

    public RadAuswahlDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Erstelle eine neue Fragment-Instanz.
     *
     * @param station Station, von welcher ein verfügbares Rad ausgewählt werden soll.
     * @return Fragment-Instanz.
     */
    public static RadAuswahlDialogFragment newInstance(@NonNull Station station) {
        RadAuswahlDialogFragment fragment = new RadAuswahlDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATION, station);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel-Instanz
        this.viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(
                RadAuswahlViewModel.initializer)).get(RadAuswahlViewModel.class);

        // Argumente lesen
        if(getArguments() != null) {
            var station = (Station) getArguments().getSerializable(ARG_STATION);
            viewModel.setStation(station);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.fragment_rad_auswahl_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.dialog_toolbar);

        // Reagiere auf Status-Änderungen
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            // Inhalt des RecyclerViews aktualisieren
            refreshRecycler();

            switch (status) {
                case INITIAL:
                    // Anfänglich Daten abrufen
                    viewModel.fetchData();
                    break;
                case CANCELLED:
                    // Leeres Result
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, new Bundle());
                    dismiss();
                    break;
                case DONE:
                    // Fragment Result setzen
                    Bundle result = new Bundle();
                    result.putSerializable(RESULT_FAHRRAD, viewModel.getAuswahlRadValue());
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                    dismiss();
                    break;
                case AUSWAHL_TYP:
                    toolbar.setTitle(viewModel.getStationValue().getBezeichnung());
                    toolbar.setSubtitle(R.string.rad_auswahl_subtitle_typ);
                    break;
                case AUSWAHL_RAD:
                    toolbar.setTitle(viewModel.getStationValue().getBezeichnung());
                    toolbar.setSubtitle(R.string.rad_auswahl_subtitle_rad);
                    break;
            }
        });

        // Einmaliges RecyclerView Setup
        RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.addItemDecoration(new DividerItemDecoration(
                view.getContext(), DividerItemDecoration.VERTICAL));

        // Zurück Button
        view.findViewById(R.id.back).setOnClickListener(v -> viewModel.navigateBack());
    }

    /**
     * Aktualisiert den Inhalt des RecyclerViews dem aktuellen {@link Status} entsprechend.
     */
    private void refreshRecycler() {
        if(getView() == null) return;

        RecyclerView recycler = getView().findViewById(R.id.recycler);
        List<BaseItem> items;

        switch (viewModel.getStatusValue()){
            default:
                // Lasse den Inhalt bei einem irrelevanten Status unberührt
                return;
            case FAILED:
                items = List.of(new ErrorPanelListItem(() -> viewModel.fetchData()));
                break;
            case AUSWAHL_TYP:
                RadKategorieListItem.OnKategorieSelectedListener onKategorieSelectedListener =
                        kategorie -> viewModel.typSelected(kategorie.getTyp());
                items = viewModel.getTypenValue().stream()
                        .map(k -> new RadKategorieListItem(
                                k, onKategorieSelectedListener))
                        .collect(Collectors.toList());
                items.add(new EndOfListItem());
                break;
            case AUSWAHL_RAD:
                RadDetailsListItem.OnRadActionListener onBuchungSelectedListener =
                        rad -> viewModel.radSelected(rad);
                items = viewModel.getRaederGefiltert().stream()
                        .map(rad -> new RadDetailsListItem(rad, null,
                                onBuchungSelectedListener))
                        .collect(Collectors.toList());
                items.add(new EndOfListItem());
                break;
        }

        recycler.setAdapter(new RadAuswahlAdapter(items));
    }
}