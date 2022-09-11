package de.fhswf.fahrradverleih.android.model

import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class Station(
    val id: String = "",
    val bezeichnung: String = "",
    val position: GeopositionT = GeopositionT(),
    val verfuegbar: Int = 0) {

//    fun toJson(): JSONObject {
//        return JSONObject()
//            .put("id", id)
//            .put("bezeichnung", bezeichnung)
//            .put("position", position.toJson())
//            .put("verfuegbar", verfuegbar)
//    }
//
//    companion object {
//        fun fromJson(obj: JSONObject): Station {
//            return Station(
//                id = obj.optString("id"),
//                bezeichnung = obj.optString("bezeichnung"),
//                position = GeopositionT.fromJson(obj.getJSONObject("position")),
//                verfuegbar = obj.optInt("verfuegbar")
//            )
//        }
//    }

}
