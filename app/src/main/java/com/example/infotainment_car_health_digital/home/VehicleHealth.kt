package com.example.infotainment_car_health_digital.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.example.infotainment_car_health_digital.R

var widthOfImage by mutableIntStateOf(0)
var heightOfImage by mutableIntStateOf(0)


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VehicleHealth(
    batteryStatus: String,
    engineStatus: String,
    coolingStatus: String,
    airIntakeStatus: String
) {
    var selectedComponent by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            modifier = Modifier
        ) {
            Box(modifier = Modifier
                .onGloballyPositioned {
                    widthOfImage = it.size.width
                    heightOfImage = it.size.height
                }
                .weight(2f)
            ) {
                Image(
                    modifier = Modifier
//                        .fillMaxSize()
                        .align(Alignment.Center)
                        .size(width = 793.dp, height = 470.dp),
                    painter = painterResource(id = R.drawable.car_image),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
                if (selectedComponent == "ENGINE") {
                    Engine(
                        engineStatus = engineStatus,
                        modifier = Modifier
                    )
                } else if (selectedComponent.isEmpty()) {
                    Engine(
                        engineStatus = engineStatus,
                        modifier = Modifier
                    )
                }
                if (selectedComponent == "BATTERY") {
                    Battery(
                        batteryStatus = batteryStatus,
                        modifier = Modifier
                    )
                } else if (selectedComponent.isEmpty()) {
                    Battery(
                        batteryStatus = batteryStatus,
                        modifier = Modifier
                    )
                }
                if (selectedComponent == "INTAKE AIR TEMP") {
                    AirInTake(
                        airIntakeStatus = airIntakeStatus,
                        modifier = Modifier
                    )
                } else if (selectedComponent.isEmpty()) {
                    AirInTake(
                        airIntakeStatus = airIntakeStatus,
                        modifier = Modifier
                    )
                }
                if (selectedComponent == "ENGINE COOLING") {
                    Cooling(
                        coolingStatus = coolingStatus,
                        modifier = Modifier
                    )
                } else if (selectedComponent.isEmpty()) {
                    Cooling(
                        coolingStatus = coolingStatus,
                        modifier = Modifier
                    )
                }
            }
            VehicleComponentStatus(
                batteryStatus = batteryStatus,
                engineStatus = engineStatus,
                coolingStatus = coolingStatus,
                airIntakeStatus = airIntakeStatus,
                componentSelect = selectedComponent,
                modifier = Modifier.weight(2f)
            ) {
                selectedComponent = it
            }
        }
    }
}


@Composable
private fun Cooling(
    coolingStatus: String,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .padding(top = 65.dp)
            .size(width = 793.dp, height = 470.dp),
        painter = painterResource(
            id = if (coolingStatus == "GOOD") {
                R.drawable.cooling_good
            } else {
                R.drawable.cooling_bad
            }
        ),
        contentDescription = ""
    )
}


@Composable
private fun AirInTake(
    airIntakeStatus: String,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .padding(top = 65.dp)
            .size(width = 793.dp, height = 470.dp),
        painter = painterResource(
            id = if (airIntakeStatus == "GOOD") {
                R.drawable.air_intake_good
            } else {
                R.drawable.air_intake_bad
            }
        ),
        contentDescription = ""
    )
}


@Composable
private fun Battery(
    batteryStatus: String,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .padding(top = 65.dp)
            .size(width = 793.dp, height = 470.dp),
        painter = painterResource(
            id = if (batteryStatus == "GOOD") {
                R.drawable.battery_good_old
            } else {
                R.drawable.battery_bad_old
            }
        ),
        contentDescription = ""
    )
}

@Composable
private fun Engine(
    engineStatus: String,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .padding(top = 65.dp)
            .size(width = 793.dp, height = 470.dp),
        painter = painterResource(
            id = if (engineStatus == "GOOD") {
                R.drawable.engine_good
            } else {
                R.drawable.engine_bad
            }
        ),
        contentDescription = ""
    )
}

@Composable
fun VehicleComponentStatus(
    batteryStatus: String,
    engineStatus: String,
    coolingStatus: String,
    airIntakeStatus: String,
    componentSelect: String,
    modifier: Modifier,
    onClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
    ) {
        item {
            VehicleHealthIndividualComponent(
                title = "ENGINE",
                statusImage = R.drawable.engine,
                componentSelect = componentSelect,
                value = engineStatus,
                onClick = {
                    onClick(it)
                }
            )
            VehicleHealthIndividualComponent(
                title = "BATTERY",
                statusImage = R.drawable.battery,
                componentSelect = componentSelect,
                value = batteryStatus,
                onClick = {
                    onClick(it)
                }
            )
            VehicleHealthIndividualComponent(
                title = "ENGINE COOLING",
                statusImage = R.drawable.engine_collant,
                componentSelect = componentSelect,
                value = coolingStatus,
                onClick = {
                    onClick(it)
                }
            )
            VehicleHealthIndividualComponent(
                title = "TYRES",
                statusImage = R.drawable.tyres,
                componentSelect = componentSelect,
                value = airIntakeStatus,
                onClick = {
                    onClick(it)
                }
            )
        }
    }
}

@Composable
private fun VehicleHealthIndividualComponent(
    title: String,
    statusImage: Int,
    value:String,
    componentSelect: String,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {
                if (componentSelect == title) {
                    onClick("")
                } else {
                    onClick(title)
                }
            }
            .background(color = Color(0xFF1D3354), shape = RoundedCornerShape(size = 10.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = statusImage),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.size(15.dp))
            Text(
                text = title,
                style = Typography().labelSmall.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                style = Typography().labelSmall.copy(color =if(value == "GOOD")  Color(0xFF3DED4F) else Color.Red)
            )
        }
        if (componentSelect == title) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
                    .size(width = 6.dp, height = 48.dp)
                    .background(
                        color = Color(0xFFEA920E),
                        shape = RoundedCornerShape(size = 4.dp)
                    )
            )
        }
    }
}