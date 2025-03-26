package com.mytvs.infotainmentcarhealthdigital.view.widgetsView

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.text.SimpleDateFormat
import java.util.Date


@SuppressLint("SimpleDateFormat")
@Composable
fun TimeAndDate(
    time: String,
    date: String,
    modifier: GlanceModifier = GlanceModifier
) {
    val dateFormatter = SimpleDateFormat("EEEE, MMM d")
    val timeFormatter = SimpleDateFormat("hh:mm")
    val timeAmOrPmFormatter = SimpleDateFormat("a")
    val timeAmOrPm = timeAmOrPmFormatter.format(Date())
    Column(
        modifier = modifier
            .cornerRadius(5.dp)
            .background(
                color = Color(0xFF172034),
            )
    ) {
        Row(
            modifier = GlanceModifier.padding(start = 66.dp, end = 16.dp, top = 45.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = time,
                style = TextStyle(
                    fontFamily = FontFamily("rubik"),
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color.White),
                )
            )
            Spacer(modifier = GlanceModifier.size(7.dp))
            Text(
                text = timeAmOrPm,
                style = TextStyle(
                    fontFamily = FontFamily("rubik"),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(Color.White),
                )
            )
        }
        Spacer(modifier = GlanceModifier.size(8.dp))
        Text(
            modifier = GlanceModifier.padding(start = 66.dp, end = 66.dp, bottom = 45.dp),
            text = date,
            style = TextStyle(
                fontFamily = FontFamily("rubik"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = ColorProvider(Color.White),
            )
        )
    }
}