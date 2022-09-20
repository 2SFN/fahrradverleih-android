package de.fhswf.fahrradverleih.android.util;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

/**
 * Einfache Implementierung des {@link TextWatcher}, welche z.B. die Verwendung
 * als Lambda erlaubt.
 */
public class SimpleTextWatcher implements TextWatcher {

    @NonNull
    private final TextChanged textChanged;

    public SimpleTextWatcher(@NonNull TextChanged textChanged) {
        this.textChanged = textChanged;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textChanged.onTextChanged(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public interface TextChanged {
        void onTextChanged(@NonNull String text);
    }

}
