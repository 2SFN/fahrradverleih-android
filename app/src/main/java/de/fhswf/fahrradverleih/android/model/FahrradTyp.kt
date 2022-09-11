package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable

@Serializable
data class FahrradTyp(
    val bezeichnung: String = "",
    val tarif: TarifT = TarifT(),
)
