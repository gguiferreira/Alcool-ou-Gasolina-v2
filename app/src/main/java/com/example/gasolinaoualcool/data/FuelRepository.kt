package com.example.gasolinaoualcool.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.gasolinaoualcool.utils.LocationHelper

class FuelRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("FuelData", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveStations(stations: List<FuelStation>) {
        val json = gson.toJson(stations)
        sharedPreferences.edit().putString("STATIONS", json).apply()
    }

    fun getStations(): List<FuelStation> {
        val json = sharedPreferences.getString("STATIONS", null)
        return if (json != null) {
            val type = object : TypeToken<List<FuelStation>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun getStationById(stationId: String): FuelStation? {
        return getStations().find { it.id == stationId }
    }

    fun addStation(station: FuelStation, context: Context) {
        val stations = getStations().toMutableList()
        val location = LocationHelper.getCurrentLocation(context)

        val updatedStation = station.copy(
            latitude = location?.first,
            longitude = location?.second
        )

        stations.add(updatedStation)
        saveStations(stations)
    }


    fun deleteStation(stationId: String) {
        val stations = getStations().filter { it.id != stationId }
        saveStations(stations)
    }

    fun updateStation(updatedStation: FuelStation) {
        val stations = getStations().map {
            if (it.id == updatedStation.id) updatedStation else it
        }
        saveStations(stations)
    }


}
