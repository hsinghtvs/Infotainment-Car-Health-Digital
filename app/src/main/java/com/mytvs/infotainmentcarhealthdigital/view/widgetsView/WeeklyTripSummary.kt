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
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.mytvs.infotainmentcarhealthdigital.R


@Composable
fun WeeklyTripSummary(
    modifier: GlanceModifier = GlanceModifier,
    numberOfTrips: Int,
    totalKms: Int,
    totalTimeDriven: Int,
    totalFuelConsumed: Int,
    noOfAlerts: Int,
    averageSpeed: Int,
    checked: Boolean,
    onCheck: (Boolean) -> Unit,
    onTripClick: () -> Action
) {
    Column(
        modifier = modifier
            .cornerRadius(15.dp)
            .background(color = Color(0xFF172034))
            .clickable(
                onClick = onTripClick()
            )
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = GlanceModifier.padding(start = 25.dp, top = 25.dp),
                text = "Weekly Trip Summary",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily("ruibik"),
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(Color.White)
                )
            )
            Spacer(GlanceModifier.defaultWeight())
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
        Spacer(modifier = GlanceModifier.size(10.dp))
        Components(
            image = R.drawable.totaltrips,
            title = "Total No of Trips",
            value = "$numberOfTrips"
        )
        Components(
            image = R.drawable.totalkms,
            title = "Total Kms driven \n in week",
            value = "$totalKms kms"
        )
        Components(
            image = R.drawable.totaltime,
            title = "Total TIME DRIVEN",
            value = "$totalTimeDriven hr "
        )
        Components(
            image = R.drawable.totalfuelconsumed,
            title = "Total fuel consumed",
            value = "$totalFuelConsumed ltr"
        )
        Components(
            image = R.drawable.noofalerts,
            title = "No of alertss",
            value = "$noOfAlerts"
        )
        Components(
            image = R.drawable.avgspeed,
            title = "Average Speed",
            value = "$averageSpeed kms"
        )
    }
}

@Composable
private fun Components(
    image: Int,
    title: String,
    value: String
) {

    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 12.dp),
    ) {
        Image(
            modifier = GlanceModifier.size(12.dp),
            provider = ImageProvider(image),
            contentDescription = ""
        )
        Spacer(modifier = GlanceModifier.size(22.dp))
        Text(
            text = title,
            style = TextStyle(
                fontFamily = FontFamily("ruibik"),
                fontWeight = FontWeight.Medium,
                color = ColorProvider(Color.White)
            )
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Text(
            text = value,
            style = TextStyle(
                fontFamily = FontFamily("ruibik"),
                fontWeight = FontWeight.Medium,
                color = ColorProvider(Color.White),
                textAlign = TextAlign.End
            )
        )
    }
}