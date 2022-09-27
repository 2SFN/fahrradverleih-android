package de.fhswf.fahrradverleih.android.view.rad_auswahl.recycler;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Locale;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.TarifT;
import de.fhswf.fahrradverleih.android.widget.rad_base_item.RadBaseViewHolder;

public class RadDetailsViewHolder extends RadBaseViewHolder<RadDetailsListItem> {

    public RadDetailsViewHolder(@NonNull View itemView) {
        super(itemView, R.layout.item_rad_details);
    }

    @Override
    public void onBind(@NonNull RadDetailsListItem item) {
        super.onBind(item);

        // Labels
        final Fahrrad rad = item.getRad();

        ((TextView) findViewById(R.id.tarif)).setText(tarifInfo(rad.getTyp().getTarif()));
        ((TextView) findViewById(R.id.id_info)).setText(idInfo(rad));

        // Button Callbacks
        Button reservieren = findViewById(R.id.reservieren);
        reservieren.setOnClickListener(
                (item.getOnReservierenSelectedListener() == null) ? null :
                        v -> item.getOnReservierenSelectedListener().onSelect(rad));

        Button buchen = findViewById(R.id.buchen);
        buchen.setOnClickListener(
                (item.getOnBuchenSelectedListener() == null) ? null :
                        v -> item.getOnBuchenSelectedListener().onSelect(rad));

    }

    private static String tarifInfo(@NonNull TarifT t) {
        return String.format(Locale.GERMANY, "Tarif: %s %d für %d Stunde(n)",
                t.getPreis().getIso4217(), t.getPreis().getBetrag(), t.getTaktung());
    }

    private static String idInfo(@NonNull Fahrrad rad) {
        return String.format(Locale.GERMANY, "ID: %s", rad.getId());
    }
}
