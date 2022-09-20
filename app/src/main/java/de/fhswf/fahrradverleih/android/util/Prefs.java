package de.fhswf.fahrradverleih.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Utility-Klasse für persistente Key-Value Paare.
 */
public final class Prefs {

    private Prefs() {}

    /**
     * Schlüssel der standardmäßig verwendeten {@link SharedPreferences}.
     */
    private static final String DEFAULT_PREFERENCES = "prefs";

    /**
     * Key, unter welchem das Token für die Rad-Api abgelegt wird.
     */
    public static final String KEY_TOKEN = "rad_api_token";

    public static String getString(@NonNull Context context,
                                   @NonNull String key, @Nullable String fallback) {
        return getPrefs(context).getString(key, fallback);
    }

    @Nullable
    public static String getString(@NonNull Context context, @NonNull String key) {
        return getString(context, key, null);
    }

    public static void setString(@NonNull Context context,
                                 @NonNull String key, @NonNull String value) {
        getPrefs(context).edit().putString(key, value).apply();
    }

    public static void remove(@NonNull Context context, @NonNull String key) {
        getPrefs(context).edit().remove(key).apply();
    }

    private static SharedPreferences getPrefs(@NonNull Context context) {
        return context.getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
    }

}
