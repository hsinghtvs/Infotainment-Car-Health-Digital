package com.mytvs.infotainmentcarhealthdigital.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.runBlocking

val Context.DATA_STORE by preferencesDataStore("widgets")
val weatherData = intPreferencesKey("weather")
val weatherConditionData = stringPreferencesKey("weather_condition")
val dateAndTimeData = stringPreferencesKey("date_and_time")
val dayData = stringPreferencesKey("day")
val mileageData = intPreferencesKey("mileage")
val batteryStatusData = stringPreferencesKey("batteryStatusData")
val ignitionStatusData = stringPreferencesKey("ignitionStatusData")
val engineStatusData = stringPreferencesKey("engineStatusData")
val engineCoolingStatusData = stringPreferencesKey("engineCoolingStatusData")
val inTakeAirTempStatusData = stringPreferencesKey("inTakeAirTempStatusData")
val batteryVoltageData = doublePreferencesKey("batteryVoltageData")
val totalNoOfTrips = intPreferencesKey("totalNoOfTrips")
val totalKmsDrivenInWeek = intPreferencesKey("totalKmsDrivenInWeek")
val totalTimeDriven = intPreferencesKey("totalTimeDriven")
val totalFuelConsumed = intPreferencesKey("totalFuelConsumed")
val totalNoOfAlerts = intPreferencesKey("totalNoOfAlerts")
val totalAverageSpeed = intPreferencesKey("totalAverageSpeed")


class AppDataWidgets(
    private val context: Context
) {

    fun updateWeatherData(
        temperature: Int,
        condition: String
    ) {
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[weatherData] = temperature
                preferences[weatherConditionData] = condition
            }
        }
    }

    fun updateDateAndTime(
        dateAndTime: String,
        day: String
    ) {
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[dateAndTimeData] = dateAndTime
                preferences[dayData] = day
            }
        }
    }

    fun mileage(
        mileage: Int
    ) {
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[mileageData] = mileage
            }
        }
    }

    fun rsaEmergency(
        batteryStatus: String,
        ignitionStatus: String
    ) {
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[batteryStatusData] = batteryStatus
                preferences[ignitionStatusData] = ignitionStatus
            }
        }
    }

    fun batteryVoltage(
        voltage: Double
    ) {
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[batteryVoltageData] = voltage
            }
        }
    }

    fun engineComponentsStatus(
        battery : String,
        engine : String,
        engineCooling : String,
        inTakeAirTemp : String
    ){
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[engineStatusData] = engine
                preferences[batteryStatusData] = battery
                preferences[engineCoolingStatusData] = engineCooling
                preferences[inTakeAirTempStatusData] = inTakeAirTemp
            }
        }
    }

    fun weeklyTripUpdate(
        noOfTrips: Int,
        noOfKmsInAWeek : Int,
        timeDriven : Int,
        fuelConsumed : Int,
        noOfAlerts : Int,
        averageSpeed : Int
    ){
        runBlocking {
            context.DATA_STORE.edit { preferences ->
                preferences[totalNoOfTrips] = noOfTrips
                preferences[totalKmsDrivenInWeek] = noOfKmsInAWeek
                preferences[totalTimeDriven] = timeDriven
                preferences[totalFuelConsumed] = fuelConsumed
                preferences[totalNoOfAlerts] = noOfAlerts
                preferences[totalAverageSpeed] = averageSpeed
            }
        }
    }
}