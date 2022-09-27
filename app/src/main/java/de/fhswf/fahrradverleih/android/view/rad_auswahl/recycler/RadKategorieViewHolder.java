package de.fhswf.fahrradverleih.android.view.rad_auswahl.recycler;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Locale;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.widget.rad_base_item.RadBaseViewHolder;

public class RadKategorieViewHolder extends RadBaseViewHolder<RadKategorieListItem> {

    public RadKategorieViewHolder(@NonNull View itemView) {
        super(itemView, R.layout.item_rad_kategorie);
    }

    @Override
    public void onBind(@NonNull RadKategorieListItem item) {
        super.onBind(item);

        // Label
        ((TextView) findViewById(R.id.verfuegbar)).setText(String.format(
                Locale.GERMANY, getContext().getString(R.string.rad_kategorie_verfuegbar),
                item.getKategorie().getVerfuegbar()));

        // Button Callback
        Button auswahl = findViewById(R.id.auswahl);

        if (item.getOnKategorieSelectedListener() == null) {
            auswahl.setOnClickListener(null);
        } else {
            auswahl.setOnClickListener(v -> item.getOnKategorieSelectedListener()
                    .onSelect(item.getKategorie()));
        }
    }
}
