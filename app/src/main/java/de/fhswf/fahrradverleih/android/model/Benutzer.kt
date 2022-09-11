package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable

@Serializable
data class Benutzer(
    val id: String = "",
    val name: String = "",
    val vorname: String = "",
    val email: String = "",
)
