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
        onSuccess: (result: LoginResult) -> Unit,
        onFailure: (e: RadApiException) -> Unit
    )

    fun auth(onSuccess: () -> Unit, onFailure: (e: RadApiException) -> Unit)

    fun getBenutzer(onSuccess: (result: Benutzer) -> Unit, onFailure: (e: RadApiException) -> Unit)

    fun setBenutzer(
        benutzer: Benutzer,
        onSuccess: (result: Benutzer) -> Unit,
        onFailure: (e: RadApiException) -> Unit
    )

    fun getAusleihen(
        onSuccess: (result: List<Ausleihe>) -> Unit,
        onFailure: (e: RadApiException) -> Unit
    )

    fun neueAusleihe(
        radId: String,
        von: LocalDateTime,
        bis: LocalDateTime,
        onSuccess: (result: Ausleihe) -> Unit,
        onFailure: (e: RadApiException) -> Unit
    )

    fun beendeAusleihe(
        ausleiheId: String,
        stationId: String,
        onSuccess: (result: Ausleihe) -> Unit,
        onFailure: (e: RadApiException) -> Unit
    )

    fun getStationen(
        onSuccess: (result: List<Station>) -> Unit,
        onFailure: (e: RadApiException) -> Unit
    )

    fun getRaeder(
        stationId: String,
        onSuccess: (result: List<Fahrrad>) -> Unit,
        onFailure: (e: RadApiException) -> Unit
    )

}
