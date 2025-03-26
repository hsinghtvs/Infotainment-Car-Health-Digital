package com.mytvs.infotainmentcarhealthdigital.view.workManager

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mytvs.infotainmentcarhealthdigital.data.DATA_STORE
import com.mytvs.infotainmentcarhealthdigital.data.batteryStatusData
import com.mytvs.infotainmentcarhealthdigital.data.batteryVoltageData
import com.mytvs.infotainmentcarhealthdigital.data.dateAndTimeData
import com.mytvs.infotainmentcarhealthdigital.data.dayData
import com.mytvs.infotainmentcarhealthdigital.data.engineCoolingStatusData
import com.mytvs.infotainmentcarhealthdigital.data.engineStatusData
import com.mytvs.infotainmentcarhealthdigital.data.ignitionStatusData
import com.mytvs.infotainmentcarhealthdigital.data.inTakeAirTempStatusData
import com.mytvs.infotainmentcarhealthdigital.data.mileageData
import com.mytvs.infotainmentcarhealthdigital.data.totalAverageSpeed
import com.mytvs.infotainmentcarhealthdigital.data.totalFuelConsumed
import com.mytvs.infotainmentcarhealthdigital.data.totalKmsDrivenInWeek
import com.mytvs.infotainmentcarhealthdigital.data.totalNoOfAlerts
import com.mytvs.infotainmentcarhealthdigital.data.totalNoOfTrips
import com.mytvs.infotainmentcarhealthdigital.data.totalTimeDriven
import com.mytvs.infotainmentcarhealthdigital.data.weatherConditionData
import com.mytvs.infotainmentcarhealthdigital.data.weatherData
import com.mytvs.infotainmentcarhealthdigital.view.widgetManager.BatteryVoltWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetManager.RsaEmergencyWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetManager.TimeAndDateWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetManager.VehicleHealthWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetManager.VehicleMileageWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetManager.WeeklyTripWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date

class WidgetWorkManager(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val dateFormatter = SimpleDateFormat("EEEE, MMM d")
        val timeFormatter = SimpleDateFormat("hh:mm")
        val timeAmOrPmFormatter = SimpleDateFormat("a")
        val time = timeFormatter.format(Date())
        val timeAmOrPm = timeAmOrPmFormatter.format(Date())
        val currentDay = dateFormatter.format(Date())
        runBlocking(Dispatchers.IO){
            context.DATA_STORE.edit { preferences ->
                preferences[weatherData] = 240
                preferences[weatherConditionData] = "Cloudy"
                preferences[dateAndTimeData] = "$time"
                preferences[dayData] = currentDay
                preferences[mileageData] = 34
                preferences[batteryStatusData] = "BAD"
                preferences[ignitionStatusData] = "BAD"
                preferences[batteryVoltageData] = 16.0
                preferences[engineStatusData] = "GOOD"
                preferences[engineCoolingStatusData] = "GOOD"
                preferences[inTakeAirTempStatusData] = "BAD"
                preferences[totalNoOfTrips] = 3
                preferences[totalKmsDrivenInWeek] = 120
                preferences[totalTimeDriven] = 15
                preferences[totalFuelConsumed] = 20
                preferences[totalNoOfAlerts] = 0
                preferences[totalAverageSpeed] = 50
            }

            // Vehicle Health
            GlanceAppWidgetManager(context).getGlanceIds(VehicleHealthWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { prefs ->
                        val sh = context.getSharedPreferences(
                            "WidgetPref",
                            MODE_PRIVATE
                        )
                        // coolant Temperature
                        val engineCoolantTemperature =
                            sh.getBoolean("engineCoolantTemperatureError", false)
                        prefs[booleanPreferencesKey("engineCoolantTemperatureError")] =
                            engineCoolantTemperature

                        val engineInTakeAirTemperature =
                            sh.getBoolean("engineInTakeAirTemperature", false)
                        prefs[booleanPreferencesKey("engineInTakeAirTemperature")] =
                            engineInTakeAirTemperature
                        val batteryVoltage =
                            sh.getBoolean("BatteryCondition", false)
                        prefs[booleanPreferencesKey("BatteryCondition")] =
                            batteryVoltage
                    }
                }
            VehicleHealthWidget().updateAll(context)

            // Time and Date
            GlanceAppWidgetManager(context).getGlanceIds(TimeAndDateWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { prefs ->
                        prefs[stringPreferencesKey("time")] = mutableStateOf(
                            runBlocking(Dispatchers.IO) {
                                context.DATA_STORE.data.first()[dateAndTimeData]?.let {
                                    it
                                }
                            }
                        ).value.toString() //new value
                        prefs[stringPreferencesKey("date")] = mutableStateOf(
                            runBlocking(Dispatchers.IO) {
                                context.DATA_STORE.data.first()[dayData]?.let {
                                    it
                                }
                            }
                        ).value.toString() //new value
                    }
                }
            TimeAndDateWidget().updateAll(context)

            // Mileage
            GlanceAppWidgetManager(context).getGlanceIds(VehicleMileageWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { prefs ->
                        val sh = context.getSharedPreferences(
                            "WidgetPref",
                            MODE_PRIVATE
                        )
                        val mileage = sh.getString("mileage", "0")
                        prefs[stringPreferencesKey("mileage")] = mileage.toString()

                    }
                }
            VehicleMileageWidget().updateAll(context)

            // Battery Voltage
            GlanceAppWidgetManager(context).getGlanceIds(BatteryVoltWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { prefs ->
                        val sh = context.getSharedPreferences(
                            "WidgetPref",
                            MODE_PRIVATE
                        )
                        val batteryValue = sh.getString("batteryValue", "0")
                        prefs[stringPreferencesKey("batteryValue")] = batteryValue.toString()
                    }
                }
            BatteryVoltWidget().updateAll(context)

            // Rsa Emergency
            val rsaEmergencyManager = GlanceAppWidgetManager(context)
            val rsaEmergencyWidget = RsaEmergencyWidget()
            val rsaEmergencyGlanceIds =
                rsaEmergencyManager.getGlanceIds(rsaEmergencyWidget.javaClass)
            rsaEmergencyGlanceIds.forEach { glanceId ->
                rsaEmergencyWidget.update(context, glanceId)
            }

            // Weekly Trip Summary
            val weeklyTripManager = GlanceAppWidgetManager(context)
            val weeklyTripWidget = WeeklyTripWidget()
            val weeklyTripGlanceIds =
                weeklyTripManager.getGlanceIds(weeklyTripWidget.javaClass)
            weeklyTripGlanceIds.forEach { glanceId ->
                weeklyTripWidget.update(context, glanceId)
            }
        }.toString()
//        }
        return Result.success()
    }
}