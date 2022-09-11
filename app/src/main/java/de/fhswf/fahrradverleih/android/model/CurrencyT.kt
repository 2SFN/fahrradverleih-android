package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class CurrencyT(
    val betrag: Int = 0,
    val iso4217: String = "EUR") {

//    fun toJson(): JSONObject {
//        return JSONObject()
//            .put("betrag", betrag)
//            .put("iso4217", iso4217)
//    }
//
//    companion object {
//        fun fromJson(obj: JSONObject): CurrencyT {
//            return CurrencyT(
//                betrag = obj.optInt("betrag", 0),
//                iso4217 = obj.optString("iso4217", "EUR"))
//        }
//    }

}
