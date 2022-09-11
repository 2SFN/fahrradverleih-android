package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class GeopositionT(
    val breite: Double = 0.0,
    val laenge: Double = 0.0) {

//    fun toJson(): JSONObject {
//        return JSONObject()
//            .put("breite", breite)
//            .put("laenge", laenge)
//    }
//
//    companion object {
//        fun fromJson(obj: JSONObject): GeopositionT {
//            return GeopositionT(
//                breite = obj.optDouble("breite", 0.0),
//                laenge = obj.optDouble("laenge", 0.0)
//            )
//        }
//    }

}
