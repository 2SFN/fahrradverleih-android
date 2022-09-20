package de.fhswf.fahrradverleih.android.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.fhswf.fahrradverleih.android.R;

/**
 * Einfaches Fragment, das auf einen kritischen Fehler hinweist und in der Regel den
 * eigentlichen Inhalt ersetzt.
 * <p>
 * Bietet dem Anwender die Option an, die fehlgeschlagene Aktion erneut zu Versuchen.
 */
public class ErrorPanelFragment extends Fragment {
    private static final String DEFAULT_REQUEST_KEY = "error_panel_retry_requested";

    /**
     * Diesem Fragment kann ein 'request key' übergeben werden, welcher beim Auslösen
     * des Erneut-Versuchen-Buttons an den Fragment-Manager weitergegeben wird.
     */
    private static final String ARG_REQUEST_KEY = "request_key";

    private String requestKey = DEFAULT_REQUEST_KEY;

    /**
     * Erstellt eine neue Fragment-Instanz.
     *
     * @param requestKey Bei Retry-Event zu verwendender RequestKey, s.
     *                   {@link androidx.fragment.app.FragmentManager#setFragmentResult(String, Bundle)}
     * @return Fragment-Instanz.
     */
    public static ErrorPanelFragment newInstance(String requestKey) {
        ErrorPanelFragment fragment = new ErrorPanelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REQUEST_KEY, requestKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestKey = getArguments().getString(ARG_REQUEST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Button konfigurieren
        view.findViewById(R.id.retry).setOnClickListener(button ->
                getParentFragmentManager()
                        .setFragmentResult(requestKey, new Bundle()));
    }
}