package de.fhswf.fahrradverleih.android.widget.rad_base_item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.fhswf.fahrradverleih.android.model.FahrradTyp;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseItem;

public abstract class RadBaseItem implements BaseItem {

    @Nullable
    private OnRadClickListener onRadClickListener;

    @NonNull
    protected abstract FahrradTyp getFahrradTyp();

    public void setOnRadClickListener(@Nullable OnRadClickListener onRadClickListener) {
        this.onRadClickListener = onRadClickListener;
    }

    @Nullable
    public OnRadClickListener getOnRadClickListener() {
        return onRadClickListener;
    }
}
