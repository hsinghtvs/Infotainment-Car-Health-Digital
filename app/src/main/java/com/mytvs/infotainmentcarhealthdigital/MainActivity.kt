package com.mytvs.infotainmentcarhealthdigital

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.mytvs.infotainmentcarhealthdigital.carHealth.VehicleHealthScreen
import com.mytvs.infotainmentcarhealthdigital.data.receivers.DTCCode
import com.mytvs.infotainmentcarhealthdigital.data.receivers.DTCCodeError
import com.mytvs.infotainmentcarhealthdigital.data.receivers.DTCCodeErrorsList
import com.mytvs.infotainmentcarhealthdigital.data.receivers.MyService
import com.mytvs.infotainmentcarhealthdigital.data.receivers.absolutePressure
import com.mytvs.infotainmentcarhealthdigital.data.receivers.airFlowRate
import com.mytvs.infotainmentcarhealthdigital.data.receivers.battery
import com.mytvs.infotainmentcarhealthdigital.data.receivers.engineCoolantTemperature
import com.mytvs.infotainmentcarhealthdigital.data.receivers.engineSpeed
import com.mytvs.infotainmentcarhealthdigital.data.receivers.intakeAirTemperature
import com.mytvs.infotainmentcarhealthdigital.data.receivers.vehicleSpeed
import com.mytvs.infotainmentcarhealthdigital.home.VehicleHealth
import com.mytvs.infotainmentcarhealthdigital.serviceKit.Constants
import com.mytvs.infotainmentcarhealthdigital.serviceKit.CustomProber
import com.mytvs.infotainmentcarhealthdigital.serviceKit.TextUtil
import com.mytvs.infotainmentcarhealthdigital.services.BookService
import com.mytvs.infotainmentcarhealthdigital.services.Services
import com.mytvs.infotainmentcarhealthdigital.ui.theme.InfotainmentCarHealthDigitalTheme
import com.mytvs.infotainmentcarhealthdigital.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.ably.lib.realtime.AblyRealtime
import io.ably.lib.realtime.Channel
import io.ably.lib.realtime.ConnectionState
import io.ably.lib.realtime.ConnectionStateListener
import io.ably.lib.types.Message

var calculatedMAF by mutableStateOf(0.0) // Not Required Only to Test


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val LAUNCH_KEY_DESTINATION: String = ""
    val viewModel: MainViewModel by viewModels<MainViewModel>()
    private var dataInterface: DataInterface? = null;

    class PortListItem(
        var device: UsbDevice,
        var port: Int,
        var driver: UsbSerialDriver?
    )

    var broadcastReceiver: BroadcastReceiver? = null
    val portPortListItems = ArrayList<PortListItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        setContent {
            InfotainmentCarHealthDigitalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var deviceId by remember { mutableStateOf("") }
                    val sharedPreferences = getSharedPreferences("Health", MODE_PRIVATE)
                    val addDeviceId = sharedPreferences.edit()
                    val deviceIdPref = sharedPreferences.getString("deviceId", null)
                    if (deviceIdPref == null) {
                        viewModel.openDialogPre = true
                        if (viewModel.openDialogPre) {
                            Dialog(onDismissRequest = {
                                addDeviceId.putString("deviceId", deviceId).apply()
                                viewModel.openDialogPre = false
                            }) {
                                Column {
                                    Box(
                                        Modifier
                                            .padding(10.dp)
                                            .background(Color.White)
                                            .clip(RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp)
                                        ) {
                                            TextField(
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    keyboardType = KeyboardType.Number
                                                ),
                                                label = {
                                                    Text(text = "Device ID")
                                                },
                                                value = deviceId,
                                                onValueChange = {
                                                    deviceId = it
                                                })
                                            Spacer(modifier = Modifier.size(10.dp))
                                            Button(onClick = {
                                                addDeviceId.putString("deviceId", deviceId)
                                                viewModel.openDialogPre = false
                                            }) {
                                                Text(text = "Submit")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    hasLocationPermission(this);

                    val ablyRealtime =
                        AblyRealtime("QXuRaw.FDmgDA:DDyKIC14kCxFW0TQ1lY1WmnVLQvDh9sC5Zl4ZraFMXg")
                    ablyRealtime.connection.on(object : ConnectionStateListener {
                        override fun onConnectionStateChanged(state: ConnectionStateListener.ConnectionStateChange?) {
                            Log.d("ABLY", "New state is " + (state?.current?.name ?: "null"));
                            when (state?.current) {
                                ConnectionState.connected -> {
                                    Log.d("ABLY", "Connected to Ably!")
                                }

                                ConnectionState.failed -> {
                                    Log.d("ABLY", "Connected to Ably Failed!")
                                }

                                else -> {
                                    Log.d("ABLY", "Connected to Ably Error!")
                                }
                            }
                        }
                    })

                    val channel: Channel = ablyRealtime.channels.get("get-started")
                    channel.subscribe("${deviceIdPref}Health", object : Channel.MessageListener {
                        override fun onMessage(message: Message) {
                            if (message.data.toString() == "Engine") {
                                viewModel.engineStatus = "BAD"
                                viewModel.batteryStatus = "GOOD"
                                viewModel.coolingStatus = "GOOD"
                                viewModel.tryeStatus = "GOOD"
                            } else if (message.data.toString() == "Battery Low") {
                                viewModel.engineStatus = "GOOD"
                                viewModel.batteryStatus = "BAD"
                                viewModel.coolingStatus = "GOOD"
                                viewModel.tryeStatus = "GOOD"
                            } else if (message.data.toString() == "Coolant high") {
                                viewModel.engineStatus = "GOOD"
                                viewModel.batteryStatus = "GOOD"
                                viewModel.coolingStatus = "BAD"
                                viewModel.tryeStatus = "GOOD"
                            } else if (message.data.toString() == "Tyres worn") {
                                viewModel.engineStatus = "GOOD"
                                viewModel.batteryStatus = "GOOD"
                                viewModel.coolingStatus = "GOOD"
                                viewModel.tryeStatus = "BAD"
                            } else if (message.data.toString() == "All Good") {
                                viewModel.engineStatus = "GOOD"
                                viewModel.batteryStatus = "GOOD"
                                viewModel.coolingStatus = "GOOD"
                                viewModel.tryeStatus = "GOOD"
                            }
                        }
                    })

                    if (intent.getStringExtra("Book Service") != null) {
                        viewModel.bookingReason = intent.getStringExtra("Book Service").toString()
                        viewModel.intentValue = true
                    } else {
                        viewModel.intentValue = false
                    }
                    Box(
                        modifier = Modifier.background(brush = viewModel.mainBackGroundGradient)
                    ) {
                        if (viewModel.bookingReason.isEmpty()) {
                            when (viewModel.selectedTab) {
                                0 -> {
                                    Row(
                                        modifier = Modifier.padding(bottom = 80.dp)
                                    ) {
                                        VehicleHealth(
                                            modifier = Modifier.weight(2f),
                                            viewModel = viewModel
                                        )
                                        Spacer(modifier = Modifier.weight(0.1f))
                                        NextServiceDue(Modifier.weight(1f), viewModel)

                                    }
                                }

                                1 -> {
                                    Services(viewModel)
                                }

                                2 -> {
                                    VehicleHealthScreen(viewModel)
                                }
                            }

                        } else {
                            BookService(viewModel = viewModel)
                        }
                        BottomTab(modifier = Modifier.align(Alignment.BottomCenter), viewModel)
                    }

                    val serviceIntent = Intent(
                        this@MainActivity,
                        MyService::class.java
                    )
                    startForegroundService(serviceIntent)
                    UsbConnectionRefresh()
                    if (portPortListItems.isNotEmpty()) {
                        portPortListItems.get(0).let {
                            val context = applicationContext
                            val sh =
                                context.getSharedPreferences(
                                    "MySharedPref",
                                    MODE_PRIVATE
                                )
                            val myEdit = sh.edit()
                            myEdit.putInt("device", it.device.deviceId)
                            myEdit.putInt("port", it.port)
                            myEdit.putInt("baud", 500)
                            myEdit.apply()

                            val deviceData: DataFromDevice = DataFromDevice.getInstance(context)
                            broadcastReceiver = object : BroadcastReceiver() {
                                override fun onReceive(
                                    context: Context,
                                    intent: Intent
                                ) {
                                    if (Constants.INTENT_ACTION_GRANT_USB == intent.action) {
                                        val granted = intent.getBooleanExtra(
                                            UsbManager.EXTRA_PERMISSION_GRANTED,
                                            false
                                        )
                                        deviceData.connect(granted)
                                        deviceData.StartService()
                                    }
                                }
                            }
                            deviceData.StartService()
                        }
                        LocalBroadcastManager.getInstance(DataFromDevice.context)
                            .registerReceiver(mRecover, IntentFilter("USBData"))
                    }
                }
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (viewModel.closeApp) {
            finish()
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun UsbConnectionRefresh() {
        val usbManager = this.getSystemService(USB_SERVICE) as UsbManager
        val usbDefaultProber = UsbSerialProber.getDefaultProber()
        val usbCustomProber = CustomProber.getCustomProber()
        portPortListItems.clear()
        for (device in usbManager.deviceList.values) {
            var driver = usbDefaultProber.probeDevice(device)
            if (driver == null) {
                driver = usbCustomProber.probeDevice(device)
            }
            if (driver != null) {
                for (port in driver.ports.indices) portPortListItems.add(
                    PortListItem(
                        device,
                        port,
                        driver
                    )
                )
            } else {
                portPortListItems.add(PortListItem(device, 0, null))
            }
        }
    }



}

@Composable
private fun VehicleHealth(modifier: Modifier, viewModel: MainViewModel) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .background(brush = viewModel.backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                0.4.dp,
                brush = viewModel.borderGradient,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.vehicle_health_icon_br),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = "VEHICLE HEALTH",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    color = Color.White,
                    fontSize = 14.sp
                )
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        VehicleHealth(
            batteryStatus = viewModel.batteryStatus,
            engineStatus = viewModel.engineStatus,
            coolingStatus = viewModel.coolingStatus,
            tyre = viewModel.tryeStatus
        )
    }
}

@Composable
private fun NextServiceDue(modifier: Modifier, viewModel: MainViewModel) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .background(brush = viewModel.backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                1.dp,
                brush = viewModel.borderGradient,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = "NEXT SERVICE DUE",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                color = Color.White,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center
        ) {
            items(1) {
                ServiceBox(name = "AC Checkup", date = "Mar 2025", viewModel)
                ServiceBox(name = "Brake Checkup", date = "Mar 2025", viewModel)
                ServiceBox(name = "Periodic Maintenance Service", date = "July 2025", viewModel)
                ServiceBox(name = "Tyre Replacement", date = "Dec 2025", viewModel)
            }
        }
    }
}

@Composable
private fun ServiceBox(name: String, date: String, viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .clickable {

            }
            .background(
                brush = viewModel.buttonBackGroundGradient,
                shape = RoundedCornerShape(size = 10.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                modifier = Modifier.weight(2f),
                text = name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                modifier = Modifier.weight(0.7f),
                text = date,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    color = Color(0xFF56AAFF)
                )
            )
        }
    }
}

@Composable
fun BottomTab(modifier: Modifier, viewModel: MainViewModel) {
    Row(
        modifier = modifier
            .padding(horizontal = 30.dp)
            .background(
                color = Color(0xFF14171D), shape = RoundedCornerShape(
                    topStart = 100.dp,
                    topEnd = 100.dp
                )
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IndividualTab(
            Modifier
                .weight(1f)
                .clickable {
                    viewModel.selectedTab = 0
                    viewModel.bookingReason = ""
                },
            if (viewModel.selectedTab == 0) {
                R.drawable.home_active
            } else {
                R.drawable.home_inactive
            }, "Home"
        )
        IndividualTab(
            Modifier
                .weight(1f)
                .clickable {
                    viewModel.selectedTab = 1
                    viewModel.bookingReason = ""
                },
            if (viewModel.selectedTab == 1) {
                R.drawable.services_active
            } else {
                R.drawable.services_inactive
            }, "Services"
        )
        IndividualTab(
            Modifier
                .weight(1f)
                .clickable {
                    viewModel.selectedTab = 2
                    viewModel.bookingReason = ""
                },
            if (viewModel.selectedTab == 2) {
                R.drawable.car_health_active
            } else {
                R.drawable.car_health_inactive
            },
            "Vehicle Health/Status"
        )
    }
}

@Composable
fun IndividualTab(modifier: Modifier, image: Int, name: String) {
    Row(
        modifier = modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = image),
            contentDescription = name,
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = name,
            style = TextStyle(
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.manrope_bold))
            )
        )
    }
}

fun convertDecimalToBinaryForDtc(code: String): String {
    val DTCCode = code
    var number = DTCCode.substring(0..0).toInt()
    var int = 0
    var binaryNumber = ""
    if (number == 0) {
        binaryNumber = "0000"
    } else {
        while (number > 0) {
            if (int == 0) {
                if (number >= 8) {
                    number %= 8
                    binaryNumber += "1"
                    int++
                } else {
                    binaryNumber += "0"
                    int++
                }
            }
            if (int == 1) {
                if (number >= 4) {
                    number %= 4
                    binaryNumber += "1"
                    int++
                } else {
                    binaryNumber += "0"
                    int++
                }
            }
            if (int == 2) {
                if (number >= 2) {
                    number %= 2
                    binaryNumber += "1"
                    int++
                } else {
                    binaryNumber += "0"
                    int++
                }
            }
            if (int == 3) {
                if (number == 1) {
                    number %= 1
                    binaryNumber += "1"
                    int++
                } else {
                    binaryNumber += "0"
                }
            }
        }
    }

    var errorValue = ""
    when (binaryNumber.substring(0..1)) {
        "00" -> {
            errorValue = "P"
        }

        "01" -> {
            errorValue = "C"
        }

        "10" -> {
            errorValue = "B"
        }

        "11" -> {
            errorValue = "U"
        }
    }
    errorValue += Integer.parseInt(binaryNumber.substring(2..3), 2)
    errorValue += DTCCode.substring(1 until DTCCode.length)
    return errorValue
}

fun vehicleMAFFuelMileage(
    maf: Int,
    airFuelRatio: Double,
    fuelDensity: Int,
    vehicleSpeed: Int
): Double {
    return if (airFuelRatio > 0 && fuelDensity > 0 && vehicleSpeed > 0) {
        val fuelFlow = (maf * 3600) / (airFuelRatio * fuelDensity)
        val mileage = vehicleSpeed / fuelFlow
        mileage.toDouble()
    } else {
        0.0
    }
}

fun caluclatedMAFFuelMileage(
    rpm: Int,
    absolutePressure: Int,
    airTemperature: Int,
    engineDisplacement: Double,
    airFuelRatio: Double,
    fuelDensity: Int,
    vehicleSpeed: Int
): String {
    return if (absolutePressure > 0 && airTemperature > 0 && engineDisplacement > 0 && airFuelRatio > 0 && fuelDensity > 0 && vehicleSpeed > 0) {
        val intakeAbsolutePressure = (rpm * absolutePressure) / ((airTemperature + 273) * 2)
        val massAirFlow =
            (intakeAbsolutePressure / 60) * (0.8) * engineDisplacement * (28.97 / 8.314)
        calculatedMAF = massAirFlow // Not Required only to Test
        val fuelFlow = (massAirFlow * 3600) / (airFuelRatio * fuelDensity)
        val mileage = vehicleSpeed / fuelFlow
        mileage.toString()
    } else {
        "0"
    }
}

// Updating the view Model Values
public val mRecover: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        textView = intent.getStringExtra("data_new").toString() // For Testing
        val data_string = intent.getStringExtra("Response_data")
        Log.d("TAG", "onReceive: DTCCode  data_string = ${data_string}") // For Testing
        val value = data_string?.replace(" ", "")
        if ((value?.length ?: 0) > 5) {
            if (value?.substring(2..3) == "41") {
                val splitPid = value.substring(4..5)
                when (splitPid) {
                    "05" -> {
                        val a: Int = TextUtil.hexToDec(value.substring(6..7))
                        engineCoolantTemperature = a - 40
                    }

                    "0F" -> {
                        val a: Int = TextUtil.hexToDec(value.substring(6..7))
                        intakeAirTemperature = a - 40
                    }

                    "0B" -> {
                        val a: Int = TextUtil.hexToDec(value.substring(6..7))
                        absolutePressure = a
                    }

                    "0C" -> {
                        val a: Int = TextUtil.hexToDec(value.substring(6..7))
                        val b = TextUtil.hexToDec(value.substring(8..9))
                        engineSpeed = (((256 * a) + b) / 4)
                    }

                    "0D" -> {
                        val a: Int = TextUtil.hexToDec(value.substring(6..7))
                        vehicleSpeed = a
                    }

                    "10" -> {
                        val a: Int = TextUtil.hexToDec(value.substring(6..7))
                        val b = TextUtil.hexToDec(value.substring(8..9))
                        airFlowRate = (((256 * a + b)) / 100)
                    }

                    "BB" -> {
                        battery = value.substring(6..10)
                    }
                }
            } else if (value?.substring(1..2)?.contains("43") == true) {
                val data_new = data_string.replace(">", "")
                val data = data_string.replace(" ", "")
                // Adding Engine failure Alerts ( As of Dummy )
                data.let { DTCCode.add(it) }
                for (i in 0 until DTCCode.size) {
                    DTCCodeErrorsList.add(
                        convertDecimalToBinaryForDtc(
                            DTCCode[i].substring(
                                2..5
                            )
                        )
                    )
                    if (DTCCode[i].length >= 10) {
                        DTCCodeErrorsList.add(
                            convertDecimalToBinaryForDtc(
                                DTCCode[i].substring(
                                    6..9
                                )
                            )
                        )
                    }
                }
                DTCCodeError = DTCCodeErrorsList.last()
//                viewModel.alertData()
                // Till here
            }
        }
    }
}