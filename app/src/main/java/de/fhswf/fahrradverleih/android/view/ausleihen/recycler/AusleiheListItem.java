package de.fhswf.fahrradverleih.android.view.ausleihen.recycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.fhswf.fahrradverleih.android.model.Ausleihe;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseItem;

/**
 * {@link BaseItem}-Implementierung für {@link Ausleihe}-Items.
 *
 * @see AusleihenAdapter Adapter.
 */
public class AusleiheListItem implements BaseItem {
    public static final int ADAPTER_TYPE = 47;

    @NonNull
    private final Ausleihe ausleihe;

    @Nullable
    private final OnRueckgabeListener onRueckgabeListener;

    public AusleiheListItem(@NonNull Ausleihe ausleihe,
                            @Nullable OnRueckgabeListener onRueckgabeListener) {
        this.ausleihe = ausleihe;
        this.onRueckgabeListener = onRueckgabeListener;
    }

    @NonNull
    public Ausleihe getAusleihe() {
        return ausleihe;
    }

    @Nullable
    public OnRueckgabeListener getOnRueckgabeListener() {
        return onRueckgabeListener;
    }

    @Override
    public int getAdapterType() {
        return ADAPTER_TYPE;
    }

    public interface OnRueckgabeListener {
        void onRueckgabe(@NonNull Ausleihe ausleihe);
    }
}
