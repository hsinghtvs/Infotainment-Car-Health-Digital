package com.mytvs.infotainmentcarhealthdigital.view.widgetManager

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.mytvs.infotainmentcarhealthdigital.MainActivity
import com.mytvs.infotainmentcarhealthdigital.data.DATA_STORE
import com.mytvs.infotainmentcarhealthdigital.data.engineStatusData
import com.mytvs.infotainmentcarhealthdigital.data.ignitionStatusData
import com.mytvs.infotainmentcarhealthdigital.data.totalAverageSpeed
import com.mytvs.infotainmentcarhealthdigital.data.totalFuelConsumed
import com.mytvs.infotainmentcarhealthdigital.data.totalKmsDrivenInWeek
import com.mytvs.infotainmentcarhealthdigital.data.totalNoOfAlerts
import com.mytvs.infotainmentcarhealthdigital.data.totalNoOfTrips
import com.mytvs.infotainmentcarhealthdigital.data.totalTimeDriven
import com.mytvs.infotainmentcarhealthdigital.view.widgetsView.BatteryVoltageWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetsView.MileageWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetsView.RoadSideAssistanceWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetsView.TimeAndDate
import com.mytvs.infotainmentcarhealthdigital.view.widgetsView.VehicleHealthWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetsView.VehicleStatusWidget
import com.mytvs.infotainmentcarhealthdigital.view.widgetsView.WeeklyTripSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


private fun getScreenResolutionWidth(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val metrics = DisplayMetrics()
    display.getMetrics(metrics)
    val width = metrics.widthPixels
    return width
}

private fun getScreenResolutionHeight(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val metrics = DisplayMetrics()
    display.getMetrics(metrics)
    val height = metrics.heightPixels
    return height
}


class TimeAndDateWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TimeAndDateWidget()
}

class TimeAndDateWidget : GlanceAppWidget() {
    override val stateDefinition = PreferencesGlanceStateDefinition

    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<Preferences>()
            val time = state[stringPreferencesKey("time")] ?: "10:20"
            val date = state[stringPreferencesKey("date")] ?: "October 10th"

            val context = LocalContext.current
            val width = getScreenResolutionWidth(context).div(3).minus(20)
            val height = getScreenResolutionHeight(context).minus(149).div(2)
            // create your AppWidget here
            time.let {
                date.let { date ->
                    TimeAndDate(
                        modifier = GlanceModifier.width(width.dp).height(height.dp),
                        time = time,
                        date = date
                    )
                }
            }
        }
    }
}

class MileageWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = VehicleMileageWidget()
}

class VehicleMileageWidget : GlanceAppWidget() {
    override val stateDefinition = PreferencesGlanceStateDefinition

    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<Preferences>()
            val mileage = state[stringPreferencesKey("mileage")] ?: "2.0"

            val context = LocalContext.current
            val width = getScreenResolutionWidth(context).div(3).minus(20)
            val height = getScreenResolutionHeight(context).minus(151)
            // create your AppWidget here
            mileage.let {
                MileageWidget(
                    modifier = GlanceModifier.width(width.dp).height(height = height.dp),
                    mileage = mileage
                )
            }
        }
    }
}

class RsaEmergencyWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RsaEmergencyWidget()
}

class RsaEmergencyWidget : GlanceAppWidget() {

    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<Preferences>()
            val battery =
                if (state[booleanPreferencesKey("BatteryCondition")] == false) {
                    "BAD"
                } else {
                    "GOOD"
                }

            val iginationStatus = remember {
                mutableStateOf(runBlocking(Dispatchers.IO) {
                    context.DATA_STORE.data.first()[ignitionStatusData]
                }).value
            }

            val context = LocalContext.current
            val width = getScreenResolutionWidth(context).div(3).minus(20)
            val height = getScreenResolutionHeight(context).minus(149).div(2)
            // create your AppWidget here
            battery?.let { battery ->
                iginationStatus?.let { ignition ->
                    VehicleStatusWidget(modifier = GlanceModifier.width(width.dp)
                        .height(height = height.dp),
                        batteryStatus = battery,
                        ignitionStatus = ignition,
                        onRsaEmergencyClick = {
                            actionStartActivity<MainActivity>()
                        })
                }
            }
        }
    }
}

class VoltageWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = BatteryVoltWidget()
}

class BatteryVoltWidget : GlanceAppWidget() {

    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<Preferences>()
            val battery = state[stringPreferencesKey("batteryValue")] ?: "0"

            val context = LocalContext.current
            val width = getScreenResolutionWidth(context).div(3).minus(20)
            val height = getScreenResolutionHeight(context).minus(149).div(2)
            // create your AppWidget here
            battery?.let {
                BatteryVoltageWidget(modifier = GlanceModifier.width(width.dp).height(height.dp),
                    batteryVoltage = it,
                    checked = true,
                    onCheck = {

                    },
                    onClick = {
                        actionStartActivity<MainActivity>(
                            actionParametersOf(destinationKey to "0")
                        )
                    })
            }
        }
    }
}

class RSACallReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RSACallWidget()
}

class RSACallWidget : GlanceAppWidget() {


    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            val context = LocalContext.current
            val width = getScreenResolutionWidth(context).div(3).minus(20)
            val height = getScreenResolutionHeight(context).minus(149).div(2)

            RoadSideAssistanceWidget(modifier = GlanceModifier.width(width.dp).height(height.dp),
                onCheck = {

                },
                checked = true,
                onClickOfRoadAssistance = {
                    actionStartActivity<MainActivity>(
                        actionParametersOf(destinationKey to "RSA")
                    )
                })
        }
    }
}

class VehicleHealthWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = VehicleHealthWidget()
}

class VehicleHealthWidget : GlanceAppWidget() {
    override val stateDefinition = PreferencesGlanceStateDefinition

    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<Preferences>()
            val engineCooling =
                if (state[booleanPreferencesKey("engineCoolantTemperatureError")] == true) {
                    "BAD"
                } else {
                    "GOOD"
                }

            val intakeAirTemp =
                if (state[booleanPreferencesKey("engineInTakeAirTemperature")] == true) {
                    "BAD"
                } else {
                    "GOOD"
                }

            val battery =
                if (state[booleanPreferencesKey("BatteryCondition")] == true) {
                    "BAD"
                } else {
                    "GOOD"
                }

            val engineStatus = remember {
                mutableStateOf(runBlocking(Dispatchers.IO) {
                    context.DATA_STORE.data.first()[engineStatusData]?.let {
                        it
                    }
                }).value
            }

            val context = LocalContext.current
            val width = getScreenResolutionWidth(context).div(3).minus(20)
            val height = getScreenResolutionHeight(context).minus(149)

            VehicleHealthWidget(modifier = GlanceModifier.width(width.dp).height(height.dp),
                engineStatus = engineStatus.toString(),
                engineCoolingStatus = engineCooling.toString(),
                batteryStatus = battery.toString(),
                intakeAirTempStatus = intakeAirTemp.toString(),
                checked = true,
                onCheck = {

                },
                onClick = {
                    actionStartActivity<MainActivity>(
                        actionParametersOf(destinationKey to "0")
                    )
                })
        }
    }
}

class WeeklyTripWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeeklyTripWidget()
}

class WeeklyTripWidget : GlanceAppWidget() {

    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            val noOfTrips = remember {
                mutableStateOf(runBlocking(Dispatchers.IO) {
                    context.DATA_STORE.data.first()[totalNoOfTrips]?.let {
                        it
                    }
                }).value
            }
            val kmsInADriven = remember {
                mutableStateOf(runBlocking(Dispatchers.IO) {
                    context.DATA_STORE.data.first()[totalKmsDrivenInWeek]?.let {
                        it
                    }
                }).value
            }
            val timeDriven = remember {
                mutableStateOf(runBlocking(Dispatchers.IO) {
                    context.DATA_STORE.data.first()[totalTimeDriven]?.let {
                        it
                    }
                }).value
            }
            val fuelConsumed = remember {
                mutableStateOf(runBlocking(Dispatchers.IO) {
                    context.DATA_STORE.data.first()[totalFuelConsumed]?.let {
                        it
                    }
                }).value
            }
            val noOfAlerts = remember {
                mutableStateOf(runBlocking(Dispatchers.IO) {
                    context.DATA_STORE.data.first()[totalNoOfAlerts]?.let {
                        it
                    }
                }).value
            }
            val averageSpeed = remember {
                mutableStateOf(runBlocking(Dispatchers.IO) {
                    context.DATA_STORE.data.first()[totalAverageSpeed]?.let {
                        it
                    }
                }).value
            }
            val context = LocalContext.current
            val width = getScreenResolutionWidth(context).div(3).minus(20)
            val height = getScreenResolutionHeight(context).minus(151)
            noOfTrips?.let {
                WeeklyTripSummary(modifier = GlanceModifier.width(width.dp)
                    .height(height = height.dp),
                    numberOfTrips = it,
                    totalKms = kmsInADriven!!,
                    totalTimeDriven = timeDriven!!,
                    totalFuelConsumed = fuelConsumed!!,
                    noOfAlerts = noOfAlerts!!,
                    averageSpeed = averageSpeed!!,
                    checked = true,
                    onCheck = {

                    },
                    onTripClick = {
                        actionStartActivity<MainActivity>(
                            actionParametersOf(destinationKey to "2")
                        )
                    })
            }
        }
    }
}

public val destinationKey = ActionParameters.Key<String>(
    MainActivity().LAUNCH_KEY_DESTINATION
)
