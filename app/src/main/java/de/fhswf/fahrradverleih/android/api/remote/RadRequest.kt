package de.fhswf.fahrradverleih.android.api.remote

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * Basis-Klasse für Requests an die Rad-API.
 *
 * Basiert auf Volley's {@link JsonRequest}.
 * Das Ergebnis der Anfrage wird hier lediglich als String verarbeitet, später können damit
 * als Antwort einzelne Objekte oder Listen/Arrays abgehandelt werden.
 */
class RadRequest(
    method: Int,
    url: String?,
    private val token: String,
    requestBody: JSONObject?,
    listener: Response.Listener<String>?,
    errorListener: Response.ErrorListener?
) : JsonRequest<String>(method, url, requestBody?.toString(), listener, errorListener) {

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json; charset=utf-8"
        headers["Token"] = token

        return headers
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        return if (response == null) {
            Response.error(VolleyError("Netzwerk-Fehler"))
        } else {
            val data = String(response.data, Charset.forName(
                HttpHeaderParser.parseCharset(
                    response.headers, Charsets.UTF_8.name())))
            Response.success(data, HttpHeaderParser.parseCacheHeaders(response))
        }
    }
}