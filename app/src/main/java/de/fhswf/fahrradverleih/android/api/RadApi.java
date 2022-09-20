package de.fhswf.fahrradverleih.android.api;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.List;

import de.fhswf.fahrradverleih.android.model.Ausleihe;
import de.fhswf.fahrradverleih.android.model.Benutzer;
import de.fhswf.fahrradverleih.android.model.Fahrrad;
import de.fhswf.fahrradverleih.android.model.LoginResult;
import de.fhswf.fahrradverleih.android.model.Station;

/**
 * Definitionen von Schnittstellen zur Rad-API.
 */
public interface RadApi {

    void login(@NonNull String email, @NonNull String secret,
               @NonNull OnSuccess<LoginResult> onSuccess,
               @NonNull OnFailure onFailure);

    void auth(@NonNull OnSuccess<Void> onSuccess,
              @NonNull OnFailure onFailure);

    void getBenutzer(@NonNull OnSuccess<Benutzer> onSuccess,
                     @NonNull OnFailure onFailure);

    void setBenutzer(@NonNull Benutzer benutzer,
                     @NonNull OnSuccess<Benutzer> onSuccess,
                     @NonNull OnFailure onFailure);

    void getAusleihen(@NonNull OnSuccess<List<Ausleihe>> onSuccess,
                      @NonNull OnFailure onFailure);

    void neueAusleihe(@NonNull String radId, @NonNull LocalDateTime von,
                      @NonNull LocalDateTime bis,
                      @NonNull OnSuccess<Ausleihe> onSuccess,
                      @NonNull OnFailure onFailure);

    void beendeAusleihe(@NonNull String ausleiheId, @NonNull String stationId,
                        @NonNull OnSuccess<Ausleihe> onSuccess,
                        @NonNull OnFailure onFailure);

    void getStationen(@NonNull OnSuccess<List<Station>> onSuccess,
                      @NonNull OnFailure onFailure);

    void getRaeder(@NonNull String stationId,
                   @NonNull OnSuccess<List<Fahrrad>> onSuccess,
                   @NonNull OnFailure onFailure);

}
