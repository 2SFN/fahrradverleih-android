package de.fhswf.fahrradverleih.android.widget.recycler;

/**
 * Kann durch den {@link BaseViewHolder} verwaltet werden.
 */
public interface BaseItem {

    /**
     * Wird vom Adapter aufgerufen, um den Item-Typen zu ermitteln.
     *
     * @return Typ-ID. Muss pro item eindeutig sein.
     */
    int getAdapterType();

}
