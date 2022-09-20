package de.fhswf.fahrradverleih.android.view.ausleihen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.fhswf.fahrradverleih.android.R;

public class AusleihenFragment extends Fragment {

    public static AusleihenFragment newInstance() {
        return new AusleihenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ausleihen, container, false);
    }
}