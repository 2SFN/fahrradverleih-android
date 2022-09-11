package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class TarifT(
    val preis: CurrencyT = CurrencyT(),
    val taktung: Int = 1) {

//    fun toJson(): JSONObject {
//        return JSONObject()
//            .put("preis", preis.toJson())
//            .put("taktung", taktung)
//    }
//
//    companion object {
//        fun fromJson(obj: JSONObject): TarifT {
//            return TarifT(
//                preis = CurrencyT.fromJson(obj.getJSONObject("preis")),
//                taktung = obj.optInt("taktung", 1))
//        }
//    }

}
