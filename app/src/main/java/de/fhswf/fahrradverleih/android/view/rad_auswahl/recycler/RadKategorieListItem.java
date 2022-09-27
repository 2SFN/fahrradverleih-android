package de.fhswf.fahrradverleih.android.view.rad_auswahl.recycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.fhswf.fahrradverleih.android.model.FahrradTyp;
import de.fhswf.fahrradverleih.android.view.rad_auswahl.viewmodel.RadKategorie;
import de.fhswf.fahrradverleih.android.widget.rad_base_item.RadBaseItem;

public class RadKategorieListItem extends RadBaseItem {
    public static final int ADAPTER_TYPE = 992;

    @NonNull
    private final RadKategorie kategorie;

    @Nullable
    private final OnKategorieSelectedListener onKategorieSelectedListener;

    public RadKategorieListItem(@NonNull RadKategorie kategorie,
                                @Nullable OnKategorieSelectedListener onKategorieSelectedListener) {
        this.kategorie = kategorie;
        this.onKategorieSelectedListener = onKategorieSelectedListener;
    }

    @NonNull
    public RadKategorie getKategorie() {
        return kategorie;
    }

    @Nullable
    public OnKategorieSelectedListener getOnKategorieSelectedListener() {
        return onKategorieSelectedListener;
    }

    @NonNull
    @Override
    protected FahrradTyp getFahrradTyp() {
        return kategorie.getTyp();
    }

    @Override
    public int getAdapterType() {
        return ADAPTER_TYPE;
    }

    public interface OnKategorieSelectedListener {
        void onSelect(@NonNull RadKategorie kategorie);
    }
}
