package com.example.infotainment_car_health_digital.carHealth

import android.content.ComponentName
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.R
import com.example.infotainment_car_health_digital.home.VehicleHealth
import com.example.infotainment_car_health_digital.viewmodel.MainViewModel

@Composable
fun VehicleHealthScreen(viewModel: MainViewModel) {
    Row(
        modifier = Modifier.padding(bottom = 80.dp)
    ) {
        CarHealth(modifier = Modifier.weight(2f), viewModel)
        Spacer(modifier = Modifier.weight(0.1f))
        NextServiceDue(Modifier.weight(1f), viewModel)

    }
}

@Composable
private fun CarHealth(modifier: Modifier, viewModel: MainViewModel) {

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
            .background(brush =viewModel.backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                0.4.dp,
                brush = viewModel.borderGradient,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = "HEALTH STATUS",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                color = Color.White,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            items(1) {
                if (viewModel.engineStatus == "BAD") {
                    ServiceBox(critical = "high", name = "Engine Issue", bookRsa = true,viewModel)
                } else if (viewModel.batteryStatus == "BAD") {
                    ServiceBox(critical = "high", name = "Battery Issue", bookRsa = true,viewModel)
                } else if (viewModel.coolingStatus == "BAD") {
                    ServiceBox(critical = "high", name = "Engine Cooling Issue", bookRsa = true,viewModel)
                } else if (viewModel.tryeStatus == "BAD") {
                    ServiceBox(critical = "high", name = "Tyre Issue", bookRsa = true,viewModel)
                }
                ServiceBox(
                    critical = "low",
                    name = "Suspension are weak fix it in next service",
                    bookRsa = false,
                    viewModel
                )
            }
        }
    }
}

@Composable
private fun ServiceBox(critical: String, name: String, bookRsa: Boolean,viewModel: MainViewModel) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
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
            Image(
                painterResource(id = if (critical == "low") R.drawable.info_circle else R.drawable.warning),
                modifier = Modifier.size(20.dp),
                contentDescription = critical
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                modifier = Modifier.weight(2f),
                text = name,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        Font(R.font.manrope_regular)
                    )
                )
            )
            if (bookRsa) {
                Box(modifier = Modifier
                    .weight(1.2f)
                    .clickable {
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.action = Intent.ACTION_SEND
                        intent.component =
                            ComponentName(
                                "com.example.infotainment_rsa",
                                "com.example.infotainment_rsa.MainActivity"
                            )
                        intent.putExtra("service", name)
                        intent.type = "text/plain"
                        context.startActivity(intent)
                    }) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                val intent = Intent(Intent.ACTION_MAIN)
                                intent.action = Intent.ACTION_SEND
                                intent.component =
                                    ComponentName(
                                        "com.example.infotainment_rsa",
                                        "com.example.infotainment_rsa.MainActivity"
                                    )
                                intent.putExtra("service", name)
                                intent.type = "text/plain"
                                context.startActivity(intent)
                            }
                            .background(
                                brush = viewModel.serviceButtonBackGroundGradient,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        text = "Request Help",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(
                                Font(R.font.manrope_regular)
                            )
                        )
                    )
                }
            }
        }
    }
}