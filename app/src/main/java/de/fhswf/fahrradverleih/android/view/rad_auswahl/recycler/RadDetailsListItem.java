package de.fhswf.fahrradverleih.android.view.rad_auswahl.recycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.FahrradTyp;
import de.fhswf.fahrradverleih.android.widget.rad_base_item.RadBaseItem;

public class RadDetailsListItem extends RadBaseItem {
    public static final int ADAPTER_TYPE = 991;

    @NonNull
    private final Fahrrad rad;

    @Nullable
    private final OnRadActionListener onReservierenSelectedListener;

    @Nullable
    private final OnRadActionListener onBuchenSelectedListener;

    public RadDetailsListItem(@NonNull Fahrrad rad,
                              @Nullable OnRadActionListener onReservierenSelectedListener,
                              @Nullable OnRadActionListener onBuchenSelectedListener) {
        this.rad = rad;
        this.onReservierenSelectedListener = onReservierenSelectedListener;
        this.onBuchenSelectedListener = onBuchenSelectedListener;
    }

    @NonNull
    public Fahrrad getRad() {
        return rad;
    }

    @Nullable
    public OnRadActionListener getOnReservierenSelectedListener() {
        return onReservierenSelectedListener;
    }

    @Nullable
    public OnRadActionListener getOnBuchenSelectedListener() {
        return onBuchenSelectedListener;
    }

    @NonNull
    @Override
    protected FahrradTyp getFahrradTyp() {
        return rad.getTyp();
    }

    @Override
    public int getAdapterType() {
        return ADAPTER_TYPE;
    }

    public interface OnRadActionListener {
        void onSelect(@NonNull Fahrrad rad);
    }
}
