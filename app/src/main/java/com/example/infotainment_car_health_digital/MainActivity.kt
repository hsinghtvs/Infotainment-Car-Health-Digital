package com.example.infotainment_car_health_digital

import android.os.Bundle
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
import com.example.infotainment_car_health_digital.carHealth.VehicleHealthScreen
import com.example.infotainment_car_health_digital.home.VehicleHealth
import com.example.infotainment_car_health_digital.services.BookService
import com.example.infotainment_car_health_digital.services.Services
import com.example.infotainment_car_health_digital.ui.theme.InfotainmentCarHealthDigitalTheme
import com.example.infotainment_car_health_digital.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.ably.lib.realtime.AblyRealtime
import io.ably.lib.realtime.Channel
import io.ably.lib.realtime.ConnectionState
import io.ably.lib.realtime.ConnectionStateListener
import io.ably.lib.types.Message


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                        NextServiceDue(Modifier.weight(1f),viewModel)

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
private fun NextServiceDue(modifier: Modifier,viewModel: MainViewModel) {
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
                ServiceBox(name = "AC Checkup", date = "Mar 2025",viewModel)
                ServiceBox(name = "Brake Checkup", date = "Mar 2025",viewModel)
                ServiceBox(name = "Periodic Maintenance Service", date = "July 2025",viewModel)
                ServiceBox(name = "Tyre Replacement", date = "Dec 2025",viewModel)
            }
        }
    }
}

@Composable
private fun ServiceBox(name: String, date: String,viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .clickable {

            }
            .background(brush = viewModel.buttonBackGroundGradient, shape = RoundedCornerShape(size = 10.dp)),
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