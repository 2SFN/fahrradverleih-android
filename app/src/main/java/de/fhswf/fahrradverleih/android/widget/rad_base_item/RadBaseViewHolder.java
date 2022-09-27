package de.fhswf.fahrradverleih.android.widget.rad_base_item;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.model.FahrradTyp;
import de.fhswf.fahrradverleih.android.util.RadIconUtil;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseViewHolder;

/**
 * ViewHolder für erweiterbare Rad-Item-Layouts.
 * <p>
 * Layouts mit zusätzlichen Views können in Implementierungen im super-call des Constructors
 * {@link this#RadBaseViewHolder(View, Integer)} übergeben werden.
 * <p>
 * Der Container dieser Erweiterungen ist ein vertikales {@link LinearLayout}. Um Verschachtelung
 * zu vermeiden, kann als Basis-Layout {@code <merge>...</merge>} verwendet werden.
 *
 * @param <T> Verwalteter Typ, erbt von {@link RadBaseItem} (für Zugriff auf Rad-Typen).
 */
public abstract class RadBaseViewHolder<T extends RadBaseItem> extends BaseViewHolder<T> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_rad_base;

    @LayoutRes
    private final Integer extensionLayout;

    private final View container;
    private final ImageView radIcon;
    private final TextView title;
    private final LinearLayout extensions;

    public RadBaseViewHolder(@NonNull View itemView, @LayoutRes @Nullable Integer extensionLayout) {
        super(itemView);
        this.extensionLayout = extensionLayout;

        this.container = itemView;
        this.radIcon = itemView.findViewById(R.id.rad_icon);
        this.title = itemView.findViewById(R.id.title);
        this.extensions = itemView.findViewById(R.id.extensions);
    }

    public RadBaseViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }

    @CallSuper
    @Override
    public void onBind(@NonNull T item) {
        // Bekannte Werte setzen
        final FahrradTyp typ = item.getFahrradTyp();
        radIcon.setImageResource(RadIconUtil.iconFor(typ));
        title.setText(typ.getBezeichnung());

        // Möglicher ClickListener auf den gesamten Container
        if (item.getOnRadClickListener() == null) {
            container.setOnClickListener(null);
            container.setBackground(null);
        } else {
            container.setOnClickListener(v -> item.getOnRadClickListener().onClick());

            // Ripple-Effekt als Indikator, dass Item auswählbar ist
            TypedValue typedValue = new TypedValue();
            container.getContext().getTheme().resolveAttribute(
                    androidx.appcompat.R.attr.selectableItemBackground, typedValue, true);
            container.setBackgroundResource(typedValue.resourceId);
        }

        // Erweiterungslayout einbinden, falls vorhanden
        extensions.removeAllViews();

        if (extensionLayout != null) {
            LayoutInflater.from(extensions.getContext())
                    .inflate(extensionLayout, extensions, true);
        }
    }
}
