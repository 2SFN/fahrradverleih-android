package de.fhswf.fahrradverleih.android.util;

import android.content.res.Resources;

import androidx.annotation.NonNull;

public final class DimenUtil {

    private DimenUtil() {}

    /**
     * Konvertiert anhand der Pixeldichte dp in tatsächliche Pixel.
     *
     * @param resources Ressourcen, erforderlich.
     * @param dp Zu Konvertierender Wert in dp.
     * @return Wert in px.
     */
    public static int dp2px(@NonNull Resources resources, int dp) {
        return (int) (resources.getDisplayMetrics().density * dp);
    }

}
