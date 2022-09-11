package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable

@Serializable
data class Fahrrad(
    val id: String = "",
    val position: GeopositionT = GeopositionT(),
    val typ: FahrradTyp = FahrradTyp(),
)
