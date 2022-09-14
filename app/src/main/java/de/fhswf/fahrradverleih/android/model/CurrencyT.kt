package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyT(
    val betrag: Int = 0,
    val iso4217: String = "EUR")
