package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResult(
    val ok: Boolean = false,
    val token: String = ""
)
