package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests, welche die Serialisierung und Deserialisierung des Datenmodells testen.
 */
class AusleiheTest {

    /**
     * Erzeugt ein {@link Ausleihe} Objekt und prüft, ob das Ergebnis einer Serialisierung und
     * anschließenden Deserialisierung identisch ist.
     */
    @Test
    fun ausleihe_serialization_internal() {
        val ausleihe = Ausleihe(
            "ID",
            Fahrrad(
                "F-ID",
                GeopositionT(1.0, 1.0),
                FahrradTyp("Typ", TarifT(CurrencyT(3, "USD"), 6))
            )
        )

        // Serialisiertes JSON
        val json = Json.encodeToString(ausleihe)

        // Zu Objekt deserialisieren
        val result: Ausleihe = Json.decodeFromString(json)

        assertTrue(result == ausleihe)
    }

    /**
     * Prüft oberflächlich, ob ein vorgegebener String des Backends deserialisiert werden kann.
     */
    @Test
    fun ausleihe_deserialize_api_string() {
        val input = "{\"id\":\"A-U0003-1662736714645\",\"fahrrad\":{\"id\":\"R0002\",\"position\":{\"breite\":0,\"laenge\":0},\"typ\":{\"bezeichnung\":\"E-Bike\",\"tarif\":{\"preis\":{\"betrag\":7,\"iso4217\":\"EUR\"},\"taktung\":3}}},\"tarif\":{\"preis\":{\"betrag\":7,\"iso4217\":\"EUR\"},\"taktung\":3},\"von\":\"2022-09-09T15:18:33.873Z\",\"bis\":\"2022-09-09T18:18:33.873Z\"}"
        val ausleihe: Ausleihe = Json.decodeFromString(input)

        assertTrue(ausleihe.id == "A-U0003-1662736714645")
    }

}