package de.fhswf.fahrradverleih.android.view.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.Station;
import de.fhswf.fahrradverleih.android.view.map.viewmodel.MapViewModel;
import de.fhswf.fahrradverleih.android.view.rad_auswahl.RadAuswahlDialogFragment;

/**
 * Fragment, welches die Map anzeigt und Marker für die Stationen hinzufügt.
 * <p>
 * Details zum Umgang mit {@link MapView}:
 * https://github.com/googlemaps/android-samples/blob/main/ApiDemos/java/app/src/gms/java/com/example/mapdemo/RawMapViewDemoActivity.java
 */
public class MapFragment extends Fragment {
    private static final String MAPVIEW_BUNDLE_KEY = "mapview_bundle";

    private MapViewModel viewModel;
    private ActivityResultLauncher<String[]> permissionsLauncher;
    private MapView mapView;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel-Instanz
        this.viewModel = new ViewModelProvider(this,
                ViewModelProvider.Factory.from(MapViewModel.initializer)).get(MapViewModel.class);

        // Registriere Callback für Runtime-Permissions Anfrage
        permissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                this::handlePermissionsResult
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Container konfigurieren (Error-Panel; Map)
        ViewGroup errorPanel = view.findViewById(R.id.error_panel);
        mapView = view.findViewById(R.id.map);

        // Status-Änderungen
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch(status){
                case INITIAL:
                    // Anfänglich Daten abrufen
                    viewModel.fetchStationen();
                    break;
                case PERMISSIONS_CHECK:
                    // Runtime-Permissions abfragen (optional für Anwender-Standort auf Karte)
                    requestLocationPermissions();
                    break;
                case FAILURE:
                    // Fehler-Panel anzeigen
                    errorPanel.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.GONE);
                    break;
                case IDLE:
                    // Fehler-Panel ausblenden
                    errorPanel.setVisibility(View.GONE);
                    mapView.setVisibility(View.VISIBLE);
                    break;
                case RAD_AUSWAHL:
                    showRadAuswahl();
                    break;
                case BUCHUNG:
                    break;
                case BUCHUNG_OK:
                    break;
            }
        });

        // Erneut Versuchen Button
        view.findViewById(R.id.retry).setOnClickListener(v -> viewModel.fetchStationen());

        // Map konfigurieren (Lifecycle; Stationen-Liste)
        Bundle mapViewBundle = null;
        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        mapView.onCreate(mapViewBundle);

        viewModel.getStationen().observe(getViewLifecycleOwner(), s -> setupMap());
    }

    /**
     * Konfiguriert die Karte und fügt für jede Station einen Marker hinzu.
     * <p>
     * Einige weitere Schlüssel des MapViews sind außerdem direkt im Layout konfiguriert.
     */
    private void setupMap() {
        if (getView() == null) return;
        MapView mapView = getView().findViewById(R.id.map);
        mapView.getMapAsync(map -> {
            map.clear();

            // Marker generieren
            for (var s : viewModel.getStationenValue()) {
                var pos = new LatLng(s.getPosition().getBreite(), s.getPosition().getLaenge());
                var marker = map.addMarker(
                        new MarkerOptions()
                                .draggable(false)
                                .alpha(1.0f)
                                .position(pos)
                                .snippet(s.getBezeichnung()));
                if (marker == null) continue;
                marker.setTag(s);
            }

            // Callback für Marker-Clicks
            map.setOnMarkerClickListener(marker -> {
                if (marker.getTag() != null && marker.getTag() instanceof Station)
                    viewModel.stationSelected((Station) marker.getTag());
                return true;
            });
        });
    }

    /**
     * Zeigt eine neue Instanz des {@link RadAuswahlDialogFragment} und verwaltet den
     * dazugehörigen Result-Listener.
     */
    private void showRadAuswahl() {
        if(viewModel.getAuswahlStationValue() == null) return;

        getParentFragmentManager().setFragmentResultListener(
                RadAuswahlDialogFragment.REQUEST_KEY, this,
                (key, bundle) -> {
                    // ViewModel informieren
                    Fahrrad rad = (Fahrrad) bundle.getSerializable(
                            RadAuswahlDialogFragment.RESULT_FAHRRAD);
                    viewModel.radSelected(rad);

                    // Result-Listener löschen
                    getParentFragmentManager().clearFragmentResult(
                            RadAuswahlDialogFragment.REQUEST_KEY);
                    getParentFragmentManager().clearFragmentResultListener(
                            RadAuswahlDialogFragment.REQUEST_KEY);
                }
        );

        RadAuswahlDialogFragment.newInstance(viewModel.getAuswahlStationValue())
                .show(getParentFragmentManager(), "rad_auswahl");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Zustand des MapViews ablegen
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    /**
     * Fragt über das in {@link this#onCreate(Bundle)} registrierte Callback
     * Runtime-Permissions für den Standort des Anwenders an.
     *
     * @see this#handlePermissionsResult(Map) Ergebnis-Callback.
     */
    private void requestLocationPermissions() {
        if (getContext() == null || getView() == null) return;
        permissionsLauncher.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION});
    }

    /**
     * Reagiert auf das Ergebnis der Runtime-Permissions Anfrage.
     * <p>
     * Unabhängig davon, ob der Anwender akzeptiert hat, wird das ViewModel informiert.
     *
     * @param results Key -> Permission-Identifier; Value -> Zugriff gestattet (oder nicht)
     */
    @SuppressLint("MissingPermission")
    private void handlePermissionsResult(Map<String, Boolean> results) {
        viewModel.permissionsCheckFinished();
        if (Boolean.TRUE.equals(results.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION, false)))
            mapView.getMapAsync(map -> map.setMyLocationEnabled(true));
    }
}
