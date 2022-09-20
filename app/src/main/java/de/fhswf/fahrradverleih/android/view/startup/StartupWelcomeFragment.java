package de.fhswf.fahrradverleih.android.view.startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupContent;
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupViewModel;

public class StartupWelcomeFragment extends Fragment {

    private StartupViewModel viewModel;

    public static StartupWelcomeFragment newInstance() {
        return new StartupWelcomeFragment();
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
        return inflater.inflate(R.layout.fragment_startup_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Views konfigurieren
        view.findViewById(R.id.login).setOnClickListener(
                v -> viewModel.setContent(StartupContent.LOGIN));

        view.findViewById(R.id.register).setOnClickListener(v ->
                Toast.makeText(view.getContext(),
                        "Im Rahmen des Fallbeispiels nicht implementiert",
                        Toast.LENGTH_LONG).show());
    }
}
