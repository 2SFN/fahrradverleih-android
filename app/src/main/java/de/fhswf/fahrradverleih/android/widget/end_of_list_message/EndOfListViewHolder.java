package de.fhswf.fahrradverleih.android.widget.end_of_list_message;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseViewHolder;

public class EndOfListViewHolder extends BaseViewHolder<EndOfListItem> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_end_of_list;

    public EndOfListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(@NonNull EndOfListItem item) {
    }
}
