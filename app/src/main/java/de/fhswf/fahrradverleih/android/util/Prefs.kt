package de.fhswf.fahrradverleih.android.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Utility-Klasse für persistente Key-Value Paare.
 */
object Prefs {
    /**
     * Schlüssel der standardmäßig verwendeten {@link SharedPreferences}.
     */
    const val DEFAULT_PREFERENCES = "prefs"

    /**
     * Key, unter welchem das Token für die Rad-Api abgelegt wird.
     */
    const val KEY_TOKEN = "rad_api_token"

    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)

    fun getString(context: Context, key: String, fallback: String?): String? =
        getPrefs(context).getString(key, fallback)

    fun setString(context: Context, key: String, value: String) =
        getPrefs(context).edit().putString(key, value).apply()

    fun remove(context: Context, key: String) = getPrefs(context).edit().remove(key).apply()

}
