package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable

@Serializable
data class GeopositionT(
    val breite: Double = 0.0,
    val laenge: Double = 0.0)
