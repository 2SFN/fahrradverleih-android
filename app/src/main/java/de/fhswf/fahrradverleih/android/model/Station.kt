package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable

@Serializable
data class Station(
    val id: String = "",
    val bezeichnung: String = "",
    val position: GeopositionT = GeopositionT(),
    val verfuegbar: Int = 0)
