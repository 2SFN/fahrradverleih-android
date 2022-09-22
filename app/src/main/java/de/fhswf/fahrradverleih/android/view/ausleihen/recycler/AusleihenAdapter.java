package de.fhswf.fahrradverleih.android.view.ausleihen.recycler;

import androidx.annotation.NonNull;

import java.util.List;

import de.fhswf.fahrradverleih.android.widget.end_of_list_message.EndOfListItem;
import de.fhswf.fahrradverleih.android.widget.end_of_list_message.EndOfListViewHolder;
import de.fhswf.fahrradverleih.android.widget.error_panel.ErrorPanelListItem;
import de.fhswf.fahrradverleih.android.widget.error_panel.ErrorPanelViewHolder;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseAdapter;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseItem;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseViewHolder;
import de.fhswf.fahrradverleih.android.widget.recycler.InflationHelper;

@SuppressWarnings("rawtypes")
public class AusleihenAdapter extends BaseAdapter {

    public AusleihenAdapter(@NonNull List<BaseItem> items) {
        super(items);
    }

    @NonNull
    @Override
    protected BaseViewHolder createViewHolderFor(
            @NonNull InflationHelper inflater, int adapterType) {
        switch (adapterType) {
            default:
                throw new IllegalArgumentException(
                        "Typ wird von diesem Adapter nicht unterstützt.");
            case AusleiheListItem.ADAPTER_TYPE:
                return new AusleiheViewHolder(inflater.inflate(AusleiheViewHolder.LAYOUT));
            case ErrorPanelListItem.ADAPTER_TYPE:
                return new ErrorPanelViewHolder(inflater.inflate(ErrorPanelViewHolder.LAYOUT));
            case EndOfListItem.ADAPTER_TYPE:
                return new EndOfListViewHolder(inflater.inflate(EndOfListViewHolder.LAYOUT));
        }
    }
}
