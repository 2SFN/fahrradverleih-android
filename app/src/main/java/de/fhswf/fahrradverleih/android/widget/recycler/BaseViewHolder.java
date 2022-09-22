package de.fhswf.fahrradverleih.android.widget.recycler;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder-Superklasse zum {@link BaseAdapter}.
 * <p>
 * Unterstützt das binding von {@link BaseItem} Klassen.
 *
 * @param <T> Item-Typ (muss {@link BaseItem} implementieren).
 */
public abstract class BaseViewHolder<T extends BaseItem> extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * Wird vom Adapter aufgerufen, wenn dieser ViewHolder ein Item darstellen soll.
     *
     * @param item Darzustellendes Item.
     */
    public abstract void onBind(@NonNull T item);

    // Helfer, um einfacher auf Views zugreifen zu können
    protected <V extends View> V findViewById(@IdRes int id) {
        return this.itemView.findViewById(id);
    }
}
