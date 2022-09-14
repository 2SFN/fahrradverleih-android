package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable

@Serializable
data class TarifT(
    val preis: CurrencyT = CurrencyT(),
    val taktung: Int = 1)
