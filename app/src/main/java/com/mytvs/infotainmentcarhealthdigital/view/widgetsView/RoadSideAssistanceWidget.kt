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
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.mytvs.infotainmentcarhealthdigital.R


@Composable
fun RoadSideAssistanceWidget(
    onCheck: (Boolean) -> Unit,
    checked: Boolean,
    modifier: GlanceModifier = GlanceModifier,
    onClickOfRoadAssistance: () -> Action
) {

    Column(
        modifier = modifier
            .cornerRadius(15.dp)
            .background(color = Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = GlanceModifier.padding(start = 25.dp, top = 10.dp),
                text = "Road Side Assistance",
                style = TextStyle(
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
                            onCheck(false)
                        }
                        .padding(top = 25.dp, end = 25.dp),
                    provider = ImageProvider(R.drawable.uncheck),
                    contentDescription = ""
                )
            }
        }
        Spacer(modifier = GlanceModifier.size(5.dp))
        Row(
            modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                modifier = GlanceModifier.size(width = 201.dp, height = 105.dp),
                provider = ImageProvider(R.drawable.rsa),
                contentDescription = ""
            )
            Spacer(modifier = GlanceModifier.size(10.dp))
            Image(
                modifier = GlanceModifier.size(37.dp),
                provider = ImageProvider(R.drawable.rsacall),
                contentDescription = ""
            )
        }
        Spacer(modifier = GlanceModifier.size(5.dp))
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp)
                .height(38.dp)
                .cornerRadius(8.dp)
                .clickable(
                    onClick = onClickOfRoadAssistance()
                )
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "View Ticket Status",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily("rubik"),
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(Color(0xFFFF2525)),
                )
            )
        }
    }
}