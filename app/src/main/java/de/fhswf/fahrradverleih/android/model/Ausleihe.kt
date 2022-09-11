package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class Ausleihe(
    val id: String = "",
    val fahrrad: Fahrrad = Fahrrad(),
    val tarif: TarifT = TarifT(),
    @Serializable(with = JsDateSerializer::class)
    val von: LocalDateTime = LocalDateTime.now(),
    @Serializable(with = JsDateSerializer::class)
    val bis: LocalDateTime = LocalDateTime.now(),
)

/**
 * Serialisiert die JSON-Felder mit Datum entsprechend des vom Backend verwendeten
 * Formats nach ISO 8601.
 */
object JsDateSerializer : KSerializer<LocalDateTime> {
    override fun deserialize(decoder: Decoder): LocalDateTime =
        LocalDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_DATE_TIME)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) =
        encoder.encodeString(value.format(DateTimeFormatter.ISO_DATE_TIME))

}
