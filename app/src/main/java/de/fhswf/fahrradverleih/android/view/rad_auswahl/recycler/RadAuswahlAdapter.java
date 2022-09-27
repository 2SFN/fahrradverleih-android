package de.fhswf.fahrradverleih.android.view.rad_auswahl.recycler;

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
public class RadAuswahlAdapter extends BaseAdapter {

    public RadAuswahlAdapter(@NonNull List<BaseItem> items) {
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
            case RadDetailsListItem.ADAPTER_TYPE:
                return new RadDetailsViewHolder(inflater.inflate(RadDetailsViewHolder.LAYOUT));
            case RadKategorieListItem.ADAPTER_TYPE:
                return new RadKategorieViewHolder(inflater.inflate(RadKategorieViewHolder.LAYOUT));
            case ErrorPanelListItem.ADAPTER_TYPE:
                return new ErrorPanelViewHolder(inflater.inflate(ErrorPanelViewHolder.LAYOUT));
            case EndOfListItem.ADAPTER_TYPE:
                return new EndOfListViewHolder(inflater.inflate(EndOfListViewHolder.LAYOUT));
        }
    }
}
