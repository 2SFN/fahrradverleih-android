package de.fhswf.fahrradverleih.android.api.remote

import com.android.volley.Request
import com.android.volley.RequestQueue
import de.fhswf.fahrradverleih.android.api.RadApi
import de.fhswf.fahrradverleih.android.api.RadApiException
import de.fhswf.fahrradverleih.android.model.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Live-Implementierung der {@link RadApi}.
 *
 * Verwendet Volley für HTTP-Requests.
 */
class RemoteRadApi(
    private val requestQueue: RequestQueue,
    private val token: String,
) : RadApi {

    // TODO: Base-URL aus Environment lesen
    private val baseUrl: String = "http://10.0.0.50:3000/api"

    override fun login(
        email: String,
        secret: String,
        onSuccess: (result: LoginResult) -> Void,
        onFailure: (e: RadApiException) -> Void
    ) {
        requestQueue.add(RadRequest(
            Request.Method.POST, "$baseUrl/benutzer/login", token,
            JSONObject().put("email", email).put("secret", secret),
            { response -> onSuccess(Json.decodeFromString(response)) },
            { error -> onFailure(RadApiException(error.message, error)) }
        ))
    }

    override fun auth(onSuccess: () -> Void, onFailure: (e: RadApiException) -> Void) {
        requestQueue.add(RadRequest(
            Request.Method.GET, "$baseUrl/benutzer/auth", token, null,
            { onSuccess() },
            { e -> onFailure(RadApiException(e.message, e)) }
        ))
    }

    override fun getBenutzer(
        onSuccess: (result: Benutzer) -> Void,
        onFailure: (e: RadApiException) -> Void
    ) {
        requestQueue.add(RadRequest(
            Request.Method.GET, "$baseUrl/benutzer/details", token, null,
            { response -> onSuccess(Json.decodeFromString(response)) },
            { error -> onFailure(RadApiException(error.message, error)) }
        ))
    }

    override fun setBenutzer(
        benutzer: Benutzer,
        onSuccess: (result: Benutzer) -> Void,
        onFailure: (e: RadApiException) -> Void
    ) {
        requestQueue.add(RadRequest(
            Request.Method.POST, "$baseUrl/benutzer/details", token,
            JSONObject(Json.encodeToString(benutzer)),
            { response -> onSuccess(Json.decodeFromString(response)) },
            { error -> onFailure(RadApiException(error.message, error)) }
        ))
    }

    override fun getAusleihen(
        onSuccess: (result: List<Ausleihe>) -> Void,
        onFailure: (e: RadApiException) -> Void
    ) {
        requestQueue.add(RadRequest(
            Request.Method.GET, "$baseUrl/benutzer/ausleihen", token, null,
            { response -> onSuccess(parseList(response)) },
            { error -> onFailure(RadApiException(error.message, error)) }
        ))
    }

    override fun neueAusleihe(
        radId: String,
        von: LocalDateTime,
        bis: LocalDateTime,
        onSuccess: (result: Ausleihe) -> Void,
        onFailure: (e: RadApiException) -> Void
    ) {
        requestQueue.add(RadRequest(
            Request.Method.POST, "$baseUrl/benutzer/ausleihen/neu", token,
            JSONObject()
                .put("fahrrad", JSONObject().put("id", radId))
                .put("von", von.format(DateTimeFormatter.ISO_DATE_TIME))
                .put("bis", bis.format(DateTimeFormatter.ISO_DATE_TIME)),
            { response -> onSuccess(Json.decodeFromString(response)) },
            { error -> onFailure(RadApiException(error.message, error)) }
        ))
    }

    override fun beendeAusleihe(
        ausleiheId: String,
        stationId: String,
        onSuccess: (result: Ausleihe) -> Void,
        onFailure: (e: RadApiException) -> Void
    ) {
        requestQueue.add(RadRequest(
            Request.Method.POST, "$baseUrl/benutzer/details", token,
            JSONObject()
                .put("ausleihe", JSONObject().put("id", ausleiheId))
                .put("station", JSONObject().put("id", stationId)),
            { response -> onSuccess(Json.decodeFromString(response)) },
            { error -> onFailure(RadApiException(error.message, error)) }
        ))
    }

    override fun getStationen(
        onSuccess: (result: List<Station>) -> Void,
        onFailure: (e: RadApiException) -> Void
    ) {
        requestQueue.add(RadRequest(
            Request.Method.GET, "$baseUrl/stationen", token, null,
            { response -> onSuccess(parseList(response)) },
            { error -> onFailure(RadApiException(error.message, error)) }
        ))
    }

    override fun getRaeder(
        stationId: String,
        onSuccess: (result: List<Fahrrad>) -> Void,
        onFailure: (e: RadApiException) -> Void
    ) {
        requestQueue.add(RadRequest(
            Request.Method.GET, "$baseUrl/stationen/$stationId/raeder", token, null,
            { response -> onSuccess(parseList(response)) },
            { error -> onFailure(RadApiException(error.message, error)) }
        ))
    }

    /**
     * Verarbeitet ein JSON-Array von Elementen T, welches ursprünglich als String vorliegt.
     *
     * Setzt für die Deserialisierung voraus, dass T {@link kotlinx.serialization.Serializable} ist.
     */
    private inline fun <reified T> parseList(input: String): List<T> {
        try {
            // Parsen des JSON-Arrays
            val jsonArray = Json.parseToJsonElement(input)
            if (jsonArray !is JsonArray) throw SerializationException("Unexpected type.")

            // Liste bauen
            val list = ArrayList<T>(jsonArray.size)
            for (c in jsonArray) list.add(Json.decodeFromJsonElement(c))
            return list
        } catch (e: Exception) {
            throw RadApiException("Verarbeiten der Antwort fehlgeschlagen.", e)
        }
    }
}