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
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.wear.tiles.border
import com.mytvs.infotainmentcarhealthdigital.R


@Composable
fun VehicleStatusWidget(
    batteryStatus: String,
    ignitionStatus: String,
    modifier: GlanceModifier = GlanceModifier,
    onRsaEmergencyClick: () -> Action
) {
    Column(
        modifier = modifier
            .background(color = Color(0xFF172034))
    ) {
        Text(
            modifier = GlanceModifier.padding(top = 25.dp, start = 30.dp),
            text = "VEHICLE STATUS",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily("rubik"),
                fontWeight = FontWeight.Normal,
                color = ColorProvider(Color.White),
            )
        )
        Row(
            modifier = GlanceModifier.padding(start = 25.dp, end = 25.dp),
        ) {
            StatusComponent(
                componentImage = if (batteryStatus == "GOOD") {
                    R.drawable.vehiclebattery
                } else {
                    R.drawable.vehiclebatteryerror
                },
                componentName = "Battery",
                componentStatus = batteryStatus,
                modifier = GlanceModifier.defaultWeight()
            )
            Spacer(modifier = GlanceModifier.size(24.dp))
            StatusComponent(
                componentImage = if (ignitionStatus == "GOOD") {
                    R.drawable.vehicleignition
                } else {
                    R.drawable.vehicleignitionerror
                },
                componentName = "Ignition",
                componentStatus = ignitionStatus,
                modifier = GlanceModifier.defaultWeight()
            )
        }
        Row(
            modifier = GlanceModifier
                .cornerRadius(8.dp)
                .clickable(
                    onClick = onRsaEmergencyClick()
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp)
                    .background(ColorProvider(Color.Red))
                    .cornerRadius(8.dp)
                    .clickable(
                        onClick = onRsaEmergencyClick()
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = GlanceModifier.size(30.dp),
                    provider = ImageProvider(R.drawable.vehicletoving),
                    contentDescription = ""
                )
                Spacer(modifier = GlanceModifier.size(20.dp))
                Text(
                    text = "RSA EMERGENCY",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily("rubik"),
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color.White),
                    )
                )
                Spacer(modifier = GlanceModifier.size(65.dp))
                Image(
                    modifier = GlanceModifier.size(30.dp),
                    provider = ImageProvider(R.drawable.call),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun StatusComponent(
    componentImage: Int,
    componentName: String,
    componentStatus: String,
    gradientColor : Color = Color.Transparent,
    borderColor: Color = Color.Transparent,
    modifier : GlanceModifier = GlanceModifier
) {
    Box(
        modifier
            .border(width = 1.dp, color = ColorProvider(borderColor))
            .height(110.dp)
            .cornerRadius(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (gradientColor == Color.Transparent) {
            Image(
                modifier = GlanceModifier.fillMaxSize(),
                provider = ImageProvider(R.drawable.vehicle_status_background),
                contentDescription = ""
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = GlanceModifier
                    .size(50.dp)
                    .padding(start = 6.dp),
                provider = ImageProvider(componentImage),
                contentDescription = ""
            )
            Column(
                modifier = GlanceModifier.padding(start = 6.dp),
            ) {
                Text(
                    text = componentName,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily("rubik"),
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color.White),
                    )
                )
                Spacer(modifier = GlanceModifier.size(8.dp))
                Text(
                    text = componentStatus,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily("rubik"),
                        fontWeight = FontWeight.Normal,
                        color = if (componentStatus == "GOOD") {
                            ColorProvider(Color.Green)
                        } else {
                            ColorProvider(Color.Red)
                        },
                    )
                )
                Spacer(modifier = GlanceModifier.size(5.dp))
            }
        }
    }
}