package de.fhswf.fahrradverleih.android.widget.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * ViewHolder Basis-Klasse, welche mehrere Adapter-Typen unterstützt.
 * <p>
 * Soll der Datensatz ({@link this#getItems()} geändert werden, sollte eine neue
 * Adapter-Instanz generiert werden.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @NonNull
    private final List<BaseItem> items;

    public BaseAdapter(@NonNull List<BaseItem> items) {
        this.items = items;
    }

    /**
     * Wird aufgerufen, wenn der Adapter eine neue ViewHolder-Instanz zu einem
     * bestimmten Adapter-Typen braucht.
     *
     * @param inflater    LayoutInflater-Instanz.
     * @param adapterType Adapter-Typ des ViewHolders, s. {@link BaseItem#getAdapterType()}.
     * @return Zum Typen passende {@link BaseViewHolder}-Instanz.
     */
    @NonNull
    protected abstract BaseViewHolder createViewHolderFor(
            @NonNull InflationHelper inflater, int adapterType);

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createViewHolderFor(new InflationHelper(parent.getContext(), parent), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getAdapterType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    public List<BaseItem> getItems() {
        return items;
    }
}
