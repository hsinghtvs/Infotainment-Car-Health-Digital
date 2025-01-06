package com.example.infotainment_car_health_digital

import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.carHealth.CarHealthScreen
import com.example.infotainment_car_health_digital.home.VehicleHealth
import com.example.infotainment_car_health_digital.services.Services
import com.example.infotainment_car_health_digital.ui.theme.InfotainmentCarHealthDigitalTheme
import com.example.infotainment_car_health_digital.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


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
                    val backGroundGradient = Brush.verticalGradient(
                        listOf(
                            Color(0xFF040A1B).copy(alpha = 1f),
                            Color(0xFF040A1B).copy(alpha = 1f),
                        )
                    )
                    Box(
                        modifier = Modifier.background(brush = backGroundGradient)
                    ) {
                        when (viewModel.selectedTab) {
                            0 -> {
                                Row(
                                    modifier = Modifier.padding(bottom = 80.dp)
                                ) {
                                    CarHealth(modifier = Modifier.weight(2f))
                                    Spacer(modifier = Modifier.weight(0.1f))
                                    NextServiceDue(Modifier.weight(1f))

                                }
                            }
                            1 -> {
                                Services(viewModel)
                            }
                            2 -> {
                                CarHealthScreen()
                            }
                        }
                        BottomTab(modifier = Modifier.align(Alignment.BottomCenter), viewModel)
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
            Color(0xFF032B9D).copy(alpha = 0f),
            Color(0xFF040A1B).copy(alpha = 1f),
        )
    )
    val borderGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFDDE4FF).copy(alpha = 1f),
            Color(0xFF040A1B).copy(alpha = 0.4f)
        )
    )
    Column(
        modifier = modifier
            .padding(10.dp)
            .background(brush = backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                0.4.dp,
                brush = borderGradient,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(top =10.dp, start =10.dp, end = 10.dp)
    ) {
        Text(
            text = "CAR HEALTH",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                color = Color.White,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.size(10.dp))
        VehicleHealth(
            batteryStatus = "GOOD",
            engineStatus = "GOOD",
            coolingStatus = "GOOD",
            airIntakeStatus = "GOOD"
        )
    }
}

@Composable
private fun NextServiceDue(modifier: Modifier) {
    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF032B9D).copy(alpha = 0f),
            Color(0xFF040A1B).copy(alpha = 1f),
        )
    )

    val borderGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFDDE4FF).copy(alpha = 1f),
            Color(0xFF040A1B).copy(alpha = 0.4f)
        )
    )
    Column(
        modifier = modifier
            .padding(10.dp)
            .background(brush = backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                1.dp,
                brush = borderGradient,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Text(
            text = "NEXT SERVICE DUE",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                color = Color.White,
                fontSize = 14.sp
            )
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
    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF000000).copy(alpha = 0f),
            Color(0xFF76ADFF).copy(alpha = 0.2f)
        )
    )
    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .clickable {

            }
            .background(brush = backGroundGradient, shape = RoundedCornerShape(size = 10.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = name,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = date,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    color = Color(0xFF56AAFF)
                )
            )
        }
    }
}

@Composable
fun BottomTab(modifier: Modifier,viewModel : MainViewModel) {
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
                },
            if (viewModel.selectedTab == 2) {
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
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = image),
            contentDescription = name,
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = name, style = TextStyle(color = Color.White))
    }
}