package de.fhswf.fahrradverleih.android.view.startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.fhswf.fahrradverleih.android.R;

public class StartupAuthFragment extends Fragment {

    public static StartupAuthFragment newInstance() {
        return new StartupAuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_startup_auth, container, false);
    }
}
