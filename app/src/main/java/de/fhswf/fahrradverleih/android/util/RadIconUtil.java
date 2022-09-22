package de.fhswf.fahrradverleih.android.util;

import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.model.FahrradTyp;

/**
 * Utility, welches Icon-Ressourcen zu Fahrrad-Typen ausgibt.
 */
public final class RadIconUtil {
    private static final String TAG = RadIconUtil.class.getSimpleName();

    private RadIconUtil() {}

    @DrawableRes
    public static int iconFor(@NonNull String typ) {
        switch (typ) {
            default:
                Log.w(TAG, "iconFor: No Icon for " + typ);
            case "City-Bike":
                return R.drawable.bike_smart;
            case "Jugendrad":
                return R.drawable.bike_kids;
            case "E-Bike":
                return R.drawable.bike_ebike;
            case "Lastenrad":
                return R.drawable.bike_cargo;
        }
    }

    @DrawableRes
    public static int iconFor(@NonNull FahrradTyp typ) {
        return iconFor(typ.getBezeichnung());
    }

}
