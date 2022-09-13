package de.fhswf.fahrradverleih.android.api

import de.fhswf.fahrradverleih.android.model.*
import java.time.LocalDateTime

/**
 * Definitionen von Schnittstellen zur Rad-API.
 */
interface RadApi {

    fun login(
        email: String,
        secret: String,
        onSuccess: (result: LoginResult) -> Void,
        onFailure: (e: RadApiException) -> Void
    )

    fun auth(onSuccess: () -> Void, onFailure: (e: RadApiException) -> Void)

    fun getBenutzer(onSuccess: (result: Benutzer) -> Void, onFailure: (e: RadApiException) -> Void)

    fun setBenutzer(
        benutzer: Benutzer,
        onSuccess: (result: Benutzer) -> Void,
        onFailure: (e: RadApiException) -> Void
    )

    fun getAusleihen(
        onSuccess: (result: List<Ausleihe>) -> Void,
        onFailure: (e: RadApiException) -> Void
    )

    fun neueAusleihe(
        radId: String,
        von: LocalDateTime,
        bis: LocalDateTime,
        onSuccess: (result: Ausleihe) -> Void,
        onFailure: (e: RadApiException) -> Void
    )

    fun beendeAusleihe(
        ausleiheId: String,
        stationId: String,
        onSuccess: (result: Ausleihe) -> Void,
        onFailure: (e: RadApiException) -> Void
    )

    fun getStationen(
        onSuccess: (result: List<Station>) -> Void,
        onFailure: (e: RadApiException) -> Void
    )

    fun getRaeder(
        stationId: String,
        onSuccess: (result: List<Fahrrad>) -> Void,
        onFailure: (e: RadApiException) -> Void
    )

}
