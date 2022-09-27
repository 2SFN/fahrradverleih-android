package de.fhswf.fahrradverleih.android.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import de.fhswf.fahrradverleih.android.R;

/**
 * DialogFragment, welches den gesamten Bildschirm einnimmt.
 */
public class FullscreenDialogFragment extends DialogFragment {

    public FullscreenDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Eigenes Theme für Fullscreen-Dialog
        setStyle(DialogFragment.STYLE_NORMAL,
                R.style.Theme_FahrradverleihAndroid_FullscreenDialog);

        // Um sicherzustellen, dass der Dialog immer ein Fragment Result an den Manager
        // weitergibt, wird das normale Schließen hier verhindert (Zurück-Geste/Button)
        setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Größe des Dialogs auf Bildschirmgröße anpassen
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
