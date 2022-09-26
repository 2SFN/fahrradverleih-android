package de.fhswf.fahrradverleih.android.api.remote;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.fhswf.fahrradverleih.android.api.OnFailure;
import de.fhswf.fahrradverleih.android.api.OnSuccess;
import de.fhswf.fahrradverleih.android.api.RadApi;
import de.fhswf.fahrradverleih.android.api.RadApiException;
import de.fhswf.fahrradverleih.android.model.Ausleihe;
import de.fhswf.fahrradverleih.android.model.Benutzer;
import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.LoginResult;
import de.fhswf.fahrradverleih.android.model.Station;
import de.fhswf.fahrradverleih.android.util.Prefs;

public class RemoteRadApi implements RadApi {
    // TODO: Base-URL aus .env lesen
    private static final String BASE_URL = "http://10.0.0.50:3000/api";

    // Wrapper für kürzeren Zugriff auf Methoden
    private static final int POST = Request.Method.POST;
    private static final int GET = Request.Method.GET;

    @NonNull
    private final Context context;

    @Nullable
    private String token;

    private final Moshi moshi;
    private final RequestQueue requestQueue;

    public RemoteRadApi(@NonNull Context context) {
        this.context = context;

        // Token aus Prefs lesen
        this.token = Prefs.getString(context, Prefs.KEY_TOKEN);

        // JSON Serialisierung vorbereiten
        this.moshi = new Moshi.Builder()
                .add(LocalDateTime.class, new DateTimeAdapter())
                .build();

        // Volley Request-Queue
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void login(@NonNull String email, @NonNull String secret,
                      @NonNull OnSuccess<LoginResult> onSuccess,
                      @NonNull OnFailure onFailure) {
        try {
            post(LoginResult.class, "/benutzer/login",
                    new JSONObject()
                            .put("email", email)
                            .put("secret", secret),
                    onSuccess, onFailure);
        } catch (JSONException ignored) {
        }
    }

    @Override
    public void auth(@NonNull OnSuccess<Void> onSuccess,
                     @NonNull OnFailure onFailure) {
        get(Void.class, "/benutzer/auth", new JSONObject(), onSuccess, onFailure);
    }

    @Override
    public void getBenutzer(@NonNull OnSuccess<Benutzer> onSuccess,
                            @NonNull OnFailure onFailure) {
        get(Benutzer.class, "/benutzer/details", new JSONObject(), onSuccess, onFailure);
    }

    @Override
    public void setBenutzer(@NonNull Benutzer benutzer,
                            @NonNull OnSuccess<Benutzer> onSuccess,
                            @NonNull OnFailure onFailure) {
        try {
            post(Benutzer.class, "/benutzer/details",
                    new JSONObject(moshi.adapter(Benutzer.class).toJson(benutzer)),
                    onSuccess, onFailure);
        } catch (JSONException ignored) {
        }
    }

    @Override
    public void getAusleihen(@NonNull OnSuccess<List<Ausleihe>> onSuccess,
                             @NonNull OnFailure onFailure) {
        get(Types.newParameterizedType(List.class, Ausleihe.class),
                "/benutzer/ausleihen", new JSONObject(), onSuccess, onFailure);
    }

    @Override
    public void neueAusleihe(@NonNull String radId, @NonNull LocalDateTime von,
                             @NonNull LocalDateTime bis,
                             @NonNull OnSuccess<Ausleihe> onSuccess,
                             @NonNull OnFailure onFailure) {
        try {
            post(Ausleihe.class, "benutzer/ausleihen/neu",
                    new JSONObject()
                            .put("fahrrad", new JSONObject().put("id", radId))
                            .put("von", von.format(DateTimeFormatter.ISO_DATE_TIME))
                            .put("bis", bis.format(DateTimeFormatter.ISO_DATE_TIME)),
                    onSuccess, onFailure);
        } catch (JSONException ignored) {
        }
    }

    @Override
    public void beendeAusleihe(@NonNull String ausleiheId, @NonNull String stationId,
                               @NonNull OnSuccess<Ausleihe> onSuccess,
                               @NonNull OnFailure onFailure) {
        try {
            post(Ausleihe.class, "/benutzer/ausleihen/ende",
                    new JSONObject()
                            .put("ausleihe", new JSONObject().put("id", ausleiheId))
                            .put("station", new JSONObject().put("id", stationId))
            , onSuccess, onFailure);
        } catch (JSONException ignored) {
        }
    }

    @Override
    public void getStationen(@NonNull OnSuccess<List<Station>> onSuccess,
                             @NonNull OnFailure onFailure) {
        get(Types.newParameterizedType(List.class, Station.class), "/stationen",
                new JSONObject(), onSuccess, onFailure);
    }

    @Override
    public void getRaeder(@NonNull String stationId,
                          @NonNull OnSuccess<List<Fahrrad>> onSuccess,
                          @NonNull OnFailure onFailure) {
        get(Types.newParameterizedType(List.class, Fahrrad.class),
                String.format("/stationen/%s/raeder", stationId),
                new JSONObject(), onSuccess, onFailure);
    }

    /**
     * Aktualisiert das verwendete Token und speichert es mit {@link Prefs}.
     *
     * @param token Neues Token. <code>null</code> löscht das gespeicherte Token.
     */
    public void updateToken(@Nullable String token) {
        this.token = token;

        if (token != null) Prefs.setString(context, Prefs.KEY_TOKEN, token);
        else Prefs.remove(context, Prefs.KEY_TOKEN);
    }

    private <T> void get(@NonNull Type type,
                         @NonNull String path, @NonNull JSONObject args,
                         @NonNull OnSuccess<T> onSuccess,
                         @NonNull OnFailure onFailure) {
        requestQueue.add(new RadRequest(
                GET, BASE_URL + path, token, args,
                response -> process(type, response, onSuccess, onFailure),
                new RadApiFailureListener(onFailure)
        ));
    }

    private <T> void post(@NonNull Type type,
                          @NonNull String path, @NonNull JSONObject args,
                          @NonNull OnSuccess<T> onSuccess,
                          @NonNull OnFailure onFailure) {
        requestQueue.add(new RadRequest(
                POST, BASE_URL + path, token, args,
                response -> process(type, response, onSuccess, onFailure),
                new RadApiFailureListener(onFailure)
        ));
    }

    private <T> void process(@NonNull Type type, String in,
                             @NonNull OnSuccess<T> onSuccess,
                             @NonNull OnFailure onFailure) {
        try {
            if(type == Void.class) {
                onSuccess.onSuccess(null);
            } else {
                onSuccess.onSuccess(moshi.<T>adapter(type).fromJson(in));
            }
        } catch (Exception e) {
            onFailure.onFailure(new RadApiException("Ungültige Antwort.", e));
        }
    }

    /**
     * Adapter für {@link Moshi}, um Timestamps die in String-Form vorliegen
     * in Objekte zu konvertieren.
     */
    static class DateTimeAdapter extends JsonAdapter<LocalDateTime> {

        @Override
        public LocalDateTime fromJson(JsonReader reader) throws IOException {
            if(reader.peek() == null) return reader.nextNull();
            String value = reader.nextString();
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
        }

        @Override
        public void toJson(@NonNull JsonWriter writer, LocalDateTime value) throws IOException {
            if(value == null) writer.nullValue();
            else writer.value(value.format(DateTimeFormatter.ISO_DATE_TIME));
        }
    }
}
