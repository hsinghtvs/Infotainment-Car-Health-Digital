package com.mytvs.infotainmentcarhealthdigital.view.widgetsView

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
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
import com.mytvs.infotainmentcarhealthdigital.R


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun WeatherWidget(
    weather: Int,
    weatherCondition: String,
    modifier: GlanceModifier = GlanceModifier
) {

    Column(
        modifier = modifier
            .cornerRadius(8.dp)
            .background(
                color = Color(0xFF172034),
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = GlanceModifier.padding(top = 25.dp, start = 30.dp),
            text = " WEATHER",
            style = TextStyle(
                fontFamily = FontFamily("rubik"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = ColorProvider(Color.White),
            )
        )
        Spacer(modifier = GlanceModifier.size(32.dp))
        Row(
        ) {
            Image(
                modifier = GlanceModifier.size(100.dp),
                provider = ImageProvider(R.drawable.weather_cloudy),
                contentDescription = ""
            )
            Column(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = weatherCondition,
                    style = TextStyle(
                        fontFamily = FontFamily("rubik"),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color.White),
                    )
                )
                Spacer(modifier = GlanceModifier.size(16.dp))
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = weather.toString(),
                        style = TextStyle(
                            fontFamily = FontFamily("rubik"),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(Color.White),
                        )
                    )
                    Text(
                        text = "o",
                        style = TextStyle(
                            fontFamily = FontFamily("rubik"),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            color = ColorProvider(Color.White),
                        )
                    )
                    Text(
                        text = "c",
                        style = TextStyle(
                            fontFamily = FontFamily("rubik"),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(Color.White),
                        )
                    )
                }
            }
        }
    }
}