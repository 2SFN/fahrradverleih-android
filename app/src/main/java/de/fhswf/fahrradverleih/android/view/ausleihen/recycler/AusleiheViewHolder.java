package de.fhswf.fahrradverleih.android.view.ausleihen.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.model.Ausleihe;
import de.fhswf.fahrradverleih.android.model.TarifT;
import de.fhswf.fahrradverleih.android.widget.rad_base_item.RadBaseViewHolder;

public class AusleiheViewHolder extends RadBaseViewHolder<AusleiheListItem> {

    public AusleiheViewHolder(@NonNull View itemView) {
        super(itemView, R.layout.item_ausleihe);
    }

    @Override
    public void onBind(@NonNull AusleiheListItem item) {
        super.onBind(item);

        final Ausleihe a = item.getAusleihe();

        // Labels
        ((TextView) findViewById(R.id.tarif)).setText(getTarifInfo(a));
        ((TextView) findViewById(R.id.id_info)).setText(a.getFahrrad().getId());
        ((TextView) findViewById(R.id.rueckgabe_info)).setText(getRueckgabeInfo(a));

        // Rückgabe-Button
        findViewById(R.id.rueckgabe_button).setOnClickListener(v -> {
            if (item.getOnRueckgabeListener() != null)
                item.getOnRueckgabeListener().onRueckgabe(a);
        });
    }

    private String getTarifInfo(@NonNull Ausleihe ausleihe) {
        final int duration = (int) ausleihe.getVon().until(ausleihe.getBis(), ChronoUnit.HOURS);
        final TarifT t = ausleihe.getTarif();
        double ppu = (double) t.getPreis().getBetrag() / t.getTaktung();
        int kosten = (int) (duration * ppu);
        return String.format(Locale.GERMANY, "Tarif: %s %d für %d Stunden",
                t.getPreis().getIso4217(),
                kosten, duration);
    }

    private String getRueckgabeInfo(@NonNull Ausleihe ausleihe) {
        final LocalDateTime bis = ausleihe.getBis();
        if(ausleihe.isAktiv()) {
            return String.format(Locale.GERMANY, "Rückgabe bis %s um %s Uhr",
                    bis.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    bis.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        } else {
            return String.format(Locale.GERMANY, "Zurückgegeben am %s",
                    bis.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        }
    }
}
