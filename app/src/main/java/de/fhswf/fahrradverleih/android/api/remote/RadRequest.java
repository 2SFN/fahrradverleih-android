package de.fhswf.fahrradverleih.android.api.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import kotlin.text.Charsets;

/**
 * Basis-Klasse für Requests an die Rad-API.
 *
 * Basiert auf Volley's {@link JsonRequest}.
 * Das Ergebnis der Anfrage wird hier lediglich als String verarbeitet, später können damit
 * als Antwort einzelne Objekte oder Listen/Arrays abgehandelt werden.
 */
public class RadRequest extends JsonRequest<String> {

    @Nullable
    private final String token;

    public RadRequest(int method, String url, @Nullable String token,
                      @NonNull JSONObject requestBody,
                      Response.Listener<String> listener,
                      @Nullable Response.ErrorListener errorListener) {
        super(method, url, requestBody.toString(), listener, errorListener);
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() {
        var headers = new HashMap<String, String>(2);
        headers.put("Content-Type", "application/json; charset=utf-8");
        if (token != null) headers.put("Token", token);

        return headers;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (response == null) {
            return Response.error(new VolleyError("Netzwerk-Fehler"));
        }

        var data = new String(response.data,
                Charset.forName(HttpHeaderParser.parseCharset(
                        response.headers, Charsets.UTF_8.name())));
        return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
    }
}
