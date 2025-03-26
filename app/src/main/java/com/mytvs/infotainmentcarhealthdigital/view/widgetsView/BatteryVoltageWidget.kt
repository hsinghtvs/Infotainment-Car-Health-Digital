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
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.mytvs.infotainmentcarhealthdigital.R


@Composable
fun BatteryVoltageWidget(
    batteryVoltage: String,
    checked: Boolean,
    modifier: GlanceModifier = GlanceModifier,
    onCheck: (Boolean) -> Unit,
    onClick : () -> Action
) {
    Column(
        modifier = modifier
            .cornerRadius(15.dp)
            .clickable(
                onClick = onClick()
            )
            .background(
                color = Color(0xFF172034),
            )
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = GlanceModifier.padding(start = 25.dp, top = 25.dp),
                text = "Battery Voltage",
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
        Spacer(modifier = GlanceModifier.size(25.dp))
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = GlanceModifier
                    .width(128.dp)
                    .height(96.dp)
                    .padding(6.dp),
                provider = ImageProvider(R.drawable.batteryicon),
                contentDescription = ""
            )
            Spacer(modifier = GlanceModifier.size(30.dp))
            Text(
                text = "$batteryVoltage volts",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily("rubik"),
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color(0xFFFFFFFF)),
                )
            )
        }
        Spacer(modifier = GlanceModifier.size(10.dp))
    }
}