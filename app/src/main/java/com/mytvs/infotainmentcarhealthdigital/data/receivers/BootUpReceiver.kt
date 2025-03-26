package com.mytvs.infotainmentcarhealthdigital.data.receivers


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.mytvs.infotainmentcarhealthdigital.MainActivity
import com.mytvs.infotainmentcarhealthdigital.R
import com.mytvs.infotainmentcarhealthdigital.DataFromDevice
import com.mytvs.infotainmentcarhealthdigital.calculatedMAF
import com.mytvs.infotainmentcarhealthdigital.caluclatedMAFFuelMileage
import com.mytvs.infotainmentcarhealthdigital.convertDecimalToBinaryForDtc
import com.mytvs.infotainmentcarhealthdigital.serviceKit.Constants
import com.mytvs.infotainmentcarhealthdigital.serviceKit.CustomProber
import com.mytvs.infotainmentcarhealthdigital.serviceKit.TextUtil
import com.mytvs.infotainmentcarhealthdigital.vehicleMAFFuelMileage
import com.mytvs.infotainmentcarhealthdigital.view.workManager.WidgetWorkManager
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Calendar
import java.util.Objects
import kotlin.random.Random


var fuelMileage: String = "0"
var vehicleFuelMileage = "0" // For Testing
var calculatedMAFFuelMileage = "0" // For Testing
var engineCoolantTemperature by mutableStateOf(0)
var intakeAirTemperature by mutableStateOf(0)
var absolutePressure by mutableStateOf(0)
var engineSpeed by mutableStateOf(0)
var vehicleSpeed by mutableStateOf(0)
var airFlowRate by mutableStateOf(0)
var DTCCode = mutableStateListOf<String>()
var DTCCodeErrorsList = mutableStateListOf<String>("")
var DTCCodeError by mutableStateOf("")
var textView by mutableStateOf("")
var battery by mutableStateOf("")
var deviceconnected by mutableStateOf("Not Connected")


class MyService : Service() {


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            while (true) {
                val context = this
                saveToFile() // Can remove once done with the testing
                // Important of calling API in service class from Here - 1

//                val client = OkHttpClient()
//                val JSON: MediaType? = "application/json".toMediaTypeOrNull()
//                val actualData : JSONObject = JSONObject()
//                try {
//                    actualData.put("userName","neveralone301@gmail.com")
//                    actualData.put("password","Neveralone@301")
//                } catch (e : Exception){
//
//                }
//
//                val body = RequestBody.create(JSON,actualData.toString())
//                val newReq = Request.Builder()
//                    .url("https://insight1.nesh.live/authenticate")
//                    .post(body)
//                    .build()
//
//                var response = client.newCall(newReq)
//
//
//                response.enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        Log.e("HttpService", "onFailure() Request was: $e")
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        val json = response.body?.string()
//                        val obj = JSONObject(json)
//                        accessToken = obj.getString("accessToken")
//                    }
//                })
//
//                val actualDataTrips : JSONObject = JSONObject()
//                try {
//                    actualDataTrips.put("deviceID","106")
//                    actualDataTrips.put("startDate","2023-10-01")
//                    actualDataTrips.put("endDate","2023-10-04")
//                    actualDataTrips.put("status","all")
//                } catch (e : Exception){
//
//                }
//
//                val bodyTrips = RequestBody.create(JSON,actualDataTrips.toString())
//                val newReqTrips = Request.Builder()
//                    .header("AUTHORIZATION", "Bearer $accessToken")
//                    .url("https://insight1.nesh.live/api/v1/getAllCompletedTripDetails")
//                    .post(bodyTrips)
//                    .build()
//
//                var responseTrips = client.newCall(newReqTrips)
//
//
//                responseTrips.enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        Log.e("HttpService", "onFailure() Request was: $e")
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        Log.d("TAG", "onResponse: ${response.body?.string()} accessToken ${accessToken} ")
//                    }
//                })


                // Till Here - 1

                // Work Manager Starting
//                val work  = PeriodicWorkRequestBuilder<WidgetWorkManager>(15, TimeUnit.MINUTES).setConstraints(
//                    androidx.work.Constraints.Builder().setRequiresCharging(true).build()
//                ).build()
                val backupWorkRequest = OneTimeWorkRequestBuilder<WidgetWorkManager>().build()
                WorkManager.getInstance(context).enqueue(backupWorkRequest)


                // Usb Service
                val portListItems = ArrayList<MainActivity.PortListItem>()
                var broadcastReceiver: BroadcastReceiver? = null
                val usbManager = this.getSystemService(USB_SERVICE) as UsbManager
                val usbDefaultProber = UsbSerialProber.getDefaultProber()
                val usbCustomProber = CustomProber.getCustomProber()
                portListItems.clear()
                for (device in usbManager.deviceList.values) {
                    var driver = usbDefaultProber.probeDevice(device)
                    if (driver == null) {
                        driver = usbCustomProber.probeDevice(device)
                    }
                    if (driver != null) {
                        for (port in driver.ports.indices) portListItems.add(
                            MainActivity.PortListItem(
                                device, port, driver
                            )
                        )
                    } else {
                        portListItems.add(MainActivity.PortListItem(device, 0, null))
                    }
                }

                // Loading Usb Service Data
                if (portListItems.isNotEmpty()) {
                    // DTCCodeERROR Notification
                    if (!DTCCodeError.equals("")) {
                        alertNotificationChannel(context = context)
                        engineErrorWidget(context = context)
                    }


                    // Widgets Updating
                    fuelMileageUpdateWidget(context = context)
                    engineCoolantTemperatureWidget(context)
                    engineInTakeAirTemperatureWidget(context)
                    if (battery.isNotEmpty() && battery != "") {
                        batteryCheck(context)
                    }
                    deviceconnected = getString(R.string.connected) // For Testing
                    Handler(Looper.getMainLooper()).post {
                        // write your code here
                        portListItems.get(0).let {
                            val sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            val myEdit = sh?.edit()
                            myEdit?.putInt("device", it.device.deviceId)
                            myEdit?.putInt("port", it.port)
                            myEdit?.putInt("baud", 38400)
                            myEdit?.apply()
                            val newClass: DataFromDevice = DataFromDevice.getInstance(context)
                            broadcastReceiver = object : BroadcastReceiver() {
                                override fun onReceive(context: Context, intent: Intent) {
                                    if (Constants.INTENT_ACTION_GRANT_USB == intent.action) {
                                        val granted = intent.getBooleanExtra(
                                            UsbManager.EXTRA_PERMISSION_GRANTED, false
                                        )
                                        newClass.connect(granted)
                                        newClass.StartService()
                                    }
                                }
                            }
                            newClass.StartService()
                        }
                        LocalBroadcastManager.getInstance(context.applicationContext)
                            .registerReceiver(mRecover, IntentFilter("USBData"))
                    }
                } else {
                    deviceconnected = getString(R.string.not_connected) // For Testing
                }
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationAndStartForeGround()
        } else {
            startForeground(1, Notification())
        }
        return START_STICKY
    }

    /**
     * Saving the deatils to the File
     * this below one runs in background thread
     */
    private fun saveToFile() {
        /* Create a Directory*/
        val time = Calendar.getInstance().time
        val usbData =
            " timeSTamp = $time ,engineCoolantTemperature = ${engineCoolantTemperature.toString()} , intakeAirTemperature =  ${intakeAirTemperature.toString()} , absolutePressure = ${absolutePressure.toString()} , engineSpeed = ${engineSpeed.toString()} , vehicleSpeed = ${vehicleSpeed.toString()} , airFlowRate = ${airFlowRate.toString()} , DTCCode Error = ${DTCCodeError.toString()}, fuel Mileage = $fuelMileage  , vehicleFuelMilleage = $vehicleFuelMileage , MAFFuelMilleage = $calculatedMAFFuelMileage  Calculated MAF = $calculatedMAF ,  Battery =$battery , $deviceconnected"
        try {
            val directory: File =
                File(applicationContext.getExternalFilesDir(null), "Usb Reading Data");
            if (!directory.exists()) {
                directory.mkdir()
            }

            /* Create a File inside the directory */
            val file = File(directory, "usbData.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            val fileOutputStream: FileOutputStream = FileOutputStream(file, true)
            val writer = OutputStreamWriter(fileOutputStream)
            writer.append("$usbData \n");
            writer.close()
            val intent = Intent("com.xyz.service")
            intent.putExtra("data", "write done")
            sendBroadcast(intent)
        } catch (e: Exception) {
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun engineCoolantTemperatureWidget(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("WidgetPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        if (engineCoolantTemperature > 96) {
            myEdit.putBoolean("engineCoolantTemperatureError", true)
            myEdit.apply()
        } else {
            myEdit.putBoolean("engineCoolantTemperatureError", false)
            myEdit.apply()
        }
    }

    private fun batteryCheck(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("WidgetPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        if (battery < "12") {
            myEdit.putString("batteryValue", battery)
            myEdit.putBoolean("BatteryCondition", false)
            myEdit.apply()
        } else {
            myEdit.putString("batteryValue", battery)
            myEdit.putBoolean("BatteryCondition", true)
            myEdit.apply()
        }
    }

    private fun engineInTakeAirTemperatureWidget(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("WidgetPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        if (intakeAirTemperature < -30 || intakeAirTemperature > 70) {
            myEdit.putBoolean("engineInTakeAirTemperature", true)
            myEdit.apply()
        } else {
            myEdit.putBoolean("engineInTakeAirTemperature", false)
            myEdit.apply()
        }
    }

    private fun engineErrorWidget(context: Context) {
        val engineError = DTCCodeError != ""
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("WidgetPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putBoolean("engineError", engineError)
        myEdit.apply()
    }

    private fun fuelMileageUpdateWidget(context: Context) {
        if (engineSpeed != 0) {
            fuelMileage = if (engineSpeed > 0 && airFlowRate > 0) {
                vehicleMAFFuelMileage(
                    maf = airFlowRate,
                    airFuelRatio = 14.7,
                    fuelDensity = 715,
                    vehicleSpeed = vehicleSpeed
                ).toString().take(4)
            } else if (engineSpeed > 0 && airFlowRate == 0 && absolutePressure > 0 && intakeAirTemperature > 0) {
                caluclatedMAFFuelMileage(
                    rpm = engineSpeed,
                    absolutePressure = absolutePressure,
                    airTemperature = intakeAirTemperature,
                    engineDisplacement = 1.8,
                    airFuelRatio = 14.7,
                    fuelDensity = 715,
                    vehicleSpeed = vehicleSpeed
                )
            } else {
                "0"
            }
        }


        if (fuelMileage != "0.0") {
            val calculatedFuel = fuelMileage
//            val fuelBeforePoint = calculatedFuel[0]
            val fuelMileageData = calculatedFuel
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("WidgetPref", MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()
            myEdit.putString("mileage", fuelMileage)
            myEdit.apply()
        } else {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("WidgetPref", MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()
            myEdit.putString("mileage", "0")
            myEdit.apply()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun alertNotificationChannel(context: Context) {
        val notificationChannel = NotificationChannel(
            "alert", "engine Alerts", NotificationManager.IMPORTANCE_HIGH
        )
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        notificationIntent.putExtra("DTCCode", "1")
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("DTCPrefs", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("DTCCode", DTCCodeError)
        myEdit.apply()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        notificationChannel.description = "Please check the alert to know more"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        val notification =
            NotificationCompat.Builder(context, "alert").setContentTitle("Alerts")
                .setContentText(DTCCodeError).setSmallIcon(R.drawable.alert)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true).build()
        notificationManager.notify(
            Random.nextInt(), notification
        )
        DTCCodeError = ""
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showNotificationAndStartForeGround() {
        createChannel()
        var notificationBuilder: NotificationCompat.Builder? = null
        notificationBuilder =
            NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Incoming Voice Call")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        var notification: Notification? = null
        notification = notificationBuilder.build()
        startForeground(120, notification)
    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID", "4Wheeler Vehicle App", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Call Notifications"
            Objects.requireNonNull(this.getSystemService(NotificationManager::class.java))
                .createNotificationChannel(channel)
        }
    }

}

// Updating the view Model Values
public val mRecover: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        textView = intent.getStringExtra("data_new").toString() // For Testing
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


