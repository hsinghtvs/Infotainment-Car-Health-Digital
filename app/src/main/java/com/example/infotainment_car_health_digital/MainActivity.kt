package com.example.infotainment_car_health_digital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.carHealth.CarHealthScreen
import com.example.infotainment_car_health_digital.home.VehicleHealth
import com.example.infotainment_car_health_digital.services.Services
import com.example.infotainment_car_health_digital.ui.theme.InfotainmentCarHealthDigitalTheme

var selectedTab by mutableIntStateOf(0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfotainmentCarHealthDigitalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.background(color = Color(0xFF0B1112))
                    ) {
                        when (selectedTab) {
                            0 -> {
                                Row(
                                    modifier = Modifier.padding(bottom = 100.dp)
                                ) {
                                    CarHealth(modifier = Modifier.weight(2f))
                                    Spacer(modifier = Modifier.weight(0.1f))
                                    NextServiceDue(Modifier.weight(1f))

                                }
                            }
                            1 -> {
                                Services()
                            }
                            2 -> {
                                CarHealthScreen()
                            }
                        }
                        BottomTab(modifier = Modifier.align(Alignment.BottomCenter), selectedTab)
                    }
                }
            }
        }
    }
}

@Composable
private fun CarHealth(modifier: Modifier) {
    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0B1112).copy(alpha = 1f),
            Color(0xFF16345B).copy(alpha = 0.4f),
        )
    )
    Column(
        modifier = modifier
            .padding(10.dp)
            .background(brush = backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                1.dp,
                color = Color.Gray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Text(
            text = "CAR HEALTH",
            style = TextStyle(color = Color.White, fontSize = 14.sp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        VehicleHealth(
            batteryStatus = "GOOD",
            engineStatus = "GOOD",
            coolingStatus = "GOOD",
            airIntakeStatus = "BAD"
        )
    }
}

@Composable
private fun NextServiceDue(modifier: Modifier) {
    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0B1112).copy(alpha = 1f),
            Color(0xFF16345B).copy(alpha = 0.4f),
        )
    )
    Column(
        modifier = modifier
            .padding(10.dp)
            .background(brush = backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                1.dp,
                color = Color.Gray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Text(
            text = "NEXT SERVICE DUE",
            style = TextStyle(color = Color.White, fontSize = 14.sp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn() {
            items(3) {
                ServiceBox(name = "Periodic Maintenance Service", date = "14 Jan")
                ServiceBox(name = "Brake Checkup", date = "18 Jan")
                ServiceBox(name = "AC Checkup", date = "22 Jan")
            }
        }
    }
}

@Composable
private fun ServiceBox(name: String, date: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {

            }
            .background(color = Color(0xFF1D3354), shape = RoundedCornerShape(size = 10.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(15.dp))
            Text(
                text = name,
                style = Typography().labelSmall.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = date,
                style = Typography().labelSmall.copy(color = Color(0xFF3DED4F))
            )
        }
    }
}

@Composable
fun BottomTab(modifier: Modifier,selectedTabValue : Int) {
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
                    selectedTab = 0
                },
            if (selectedTab == 0) {
                R.drawable.home_active
            } else {
                R.drawable.home_inactive
            }, "Home"
        )
        IndividualTab(
            Modifier
                .weight(1f)
                .clickable {
                    selectedTab = 1
                },
            if (selectedTab == 1) {
                R.drawable.services_active
            } else {
                R.drawable.services_inactive
            }, "Services"
        )
        IndividualTab(
            Modifier
                .weight(1f)
                .clickable {
                    selectedTab = 2
                },
            if (selectedTab == 2) {
                R.drawable.car_health_active
            } else {
                R.drawable.car_health_inactive
            },
            "Car Health/Status"
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
        Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = image),
            contentDescription = name,
            tint = Color(0xFF255AF5)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = name, style = TextStyle(color = Color.White))
    }
}