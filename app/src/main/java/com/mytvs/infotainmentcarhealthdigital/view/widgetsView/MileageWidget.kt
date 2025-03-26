package com.mytvs.infotainmentcarhealthdigital.view.widgetsView

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
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
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.mytvs.infotainmentcarhealthdigital.R


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MileageWidget(
    mileage: String,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier
            .cornerRadius(8.dp)
            .background(
                color = Color(0xFF172034),
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mileage.toString(),
            style = TextStyle(
                fontSize = 80.sp,
                fontFamily = FontFamily("rubik"),
                fontWeight = FontWeight.Normal,
                color = ColorProvider(Color.White),
            )
        )
        Text(
            text = "KMPL",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily("rubik"),
                fontWeight = FontWeight.Normal,
                color = ColorProvider(Color.White),
            )
        )
        MileageProgressBar(mileage.take(1).toInt() * 10)
        Text(
            text = "INSTANT MILEAGE",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily("rubik"),
                fontWeight = FontWeight.Normal,
                color = ColorProvider(Color.White),
                textAlign = TextAlign.Center,
            )
        )
        Image(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(186.dp),
            provider = ImageProvider(R.drawable.car_milleage),
            contentDescription = ""
        )
    }
}

@Composable
private fun MileageProgressBar(
    percent: Int
) {
    val gradientProgress =
        Brush.horizontalGradient(listOf(Color.Green.copy(alpha = 0.5f), Color.Green))

    Column(
        modifier = GlanceModifier.width(218.dp),
    ) {
        Image(
            modifier = GlanceModifier.padding(
                start = if (percent < 30) {
                    (percent * 6.7).dp
                } else {
                    200.dp
                }
            ),
            provider = ImageProvider(R.drawable.polygon_2),
            contentDescription = ""
        )
        Box(
            modifier = GlanceModifier
                .cornerRadius(5.dp)
                .background(Color(0xFF080C14))
                .height(10.dp)
                .width(218.dp)
        ) {
            Box(
                modifier = GlanceModifier
                    .cornerRadius(5.dp)
                    .background(color = Color.Green.copy(alpha = 0.5f))
                    .height(10.dp)
                    .width(218.dp * percent / 30)
            ) {

            }
        }
        Spacer(modifier = GlanceModifier.size(5.dp))
        Row(
            modifier = GlanceModifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "0",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily("rubik"),
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(Color.White),
                )
            )
            Spacer(GlanceModifier.size(90.dp))
            Text(
                text = "15",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily("rubik"),
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(Color.White),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(GlanceModifier.size(73.dp))
            Row {
                Text(
                    text = "30",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily("rubik"),
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color.White),
                        textAlign = TextAlign.Right,
                    )
                )
                Text(
                    text = "+",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = FontFamily("rubik"),
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color.White),
                        textAlign = TextAlign.Right,
                    )
                )
            }
        }
    }
}