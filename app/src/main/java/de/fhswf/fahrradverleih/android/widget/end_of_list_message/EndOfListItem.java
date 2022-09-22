package de.fhswf.fahrradverleih.android.widget.end_of_list_message;

import de.fhswf.fahrradverleih.android.widget.recycler.BaseItem;

/**
 * {@link BaseItem}-Implementierung für das End-of-List-Message widget.
 */
public class EndOfListItem implements BaseItem {
    public static final int ADAPTER_TYPE = 307;

    @Override
    public int getAdapterType() {
        return ADAPTER_TYPE;
    }
}
