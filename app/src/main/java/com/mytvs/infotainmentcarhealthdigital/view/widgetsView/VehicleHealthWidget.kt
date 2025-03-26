package com.mytvs.infotainmentcarhealthdigital.view.widgetsView

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.mytvs.infotainmentcarhealthdigital.R

@Composable
fun VehicleHealthWidget(
    engineStatus: String,
    engineCoolingStatus: String,
    batteryStatus: String,
    intakeAirTempStatus: String,
    checked: Boolean,
    modifier: GlanceModifier = GlanceModifier,
    onCheck: (Boolean) -> Unit,
    onClick: () -> Action
) {
    Column(
        modifier = modifier
            .cornerRadius(15.dp)
            .background(
                color = Color(0xFF172034),
            )
            .clickable(
                onClick = onClick()
            ),
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = GlanceModifier.padding(start = 25.dp, top = 10.dp),
                text = "Vehicle Health",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily("rubik"),
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(Color(0xFFFFFFFF)),
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            if (checked) {
                Image(
                    modifier = GlanceModifier
                        .clickable {
                            onCheck(false)
                        }
                        .padding(top = 25.dp, end = 25.dp),
                    provider = ImageProvider(R.drawable.check),
                    contentDescription = ""
                )
            } else {
                Image(
                    modifier = GlanceModifier
                        .clickable {
                            onCheck(true)
                        }
                        .padding(top = 25.dp, end = 25.dp),
                    provider = ImageProvider(R.drawable.uncheck),
                    contentDescription = ""
                )
            }
        }
        Spacer(modifier = GlanceModifier.size(30.dp))
        Image(
            modifier = GlanceModifier
                .padding(start = 100.dp),
            provider = ImageProvider(R.drawable.vehicleconditions),
            contentDescription = ""
        )
        Spacer(modifier = GlanceModifier.size(30.dp))
        Row(
            modifier = GlanceModifier.padding(start = 25.dp, end = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StatusComponent(
                componentImage = if (engineStatus == "GOOD") {
                    R.drawable.enginehealth
                } else {
                    R.drawable.engineerror
                },
                componentName = "ENGINE",
                gradientColor = Color(0xFF141A29),
                componentStatus = engineStatus,
                borderColor = Color(0xFF374F74),
                modifier = GlanceModifier.defaultWeight()
            )
            Spacer(modifier = GlanceModifier.size(22.dp))
            StatusComponent(
                componentImage = if (batteryStatus == "GOOD") {
                    R.drawable.vehiclebattery
                } else {
                    R.drawable.vehiclebatteryerror
                },
                componentName = "BATTERY",
                componentStatus = batteryStatus,
                gradientColor = Color(0xFF141A29),
                borderColor = Color(0xFF374F74),
                modifier = GlanceModifier.defaultWeight()
            )
        }
        Spacer(modifier = GlanceModifier.size(10.dp))
        Row(
            modifier = GlanceModifier.padding(start = 25.dp, end = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StatusComponent(
                componentImage = R.drawable.vehiclecooling,
                componentName = "ENG COOLING",
                componentStatus = engineCoolingStatus,
                gradientColor = Color(0xFF141A29),
                borderColor = Color(0xFF374F74),
                modifier = GlanceModifier.defaultWeight()
            )
            Spacer(modifier = GlanceModifier.size(20.dp))
            StatusComponent(
                componentImage = if (intakeAirTempStatus == "GOOD") {
                    R.drawable.intakeairtemp
                } else {
                    R.drawable.intakeerror
                },
                componentName = "INTAKE AIR TEMP",
                componentStatus = intakeAirTempStatus,
                gradientColor = Color(0xFF141A29),
                borderColor = Color(0xFF374F74),
                modifier = GlanceModifier.defaultWeight()
            )
        }
    }
}