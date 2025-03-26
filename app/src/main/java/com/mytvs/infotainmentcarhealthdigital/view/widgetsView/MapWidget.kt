package com.mytvs.infotainmentcarhealthdigital.view.widgetsView

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
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
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.mytvs.infotainmentcarhealthdigital.R

@SuppressLint("MissingPermission")
@Composable
fun MapWidget(
    checked: Boolean,
    onCheck: (Boolean) -> Unit,
    onClick: () -> Unit,
    cameraPositionState: CameraPositionState,
    currentLocationValues: LatLng
) {
    Column(
        modifier = GlanceModifier
            .width(400.dp)
            .height(250.dp)
            .cornerRadius(15.dp)
            .background(
                color = Color(0xFF172034),
            )
            .clickable(
//                onClick = onClick()
            ) {
                onClick()
            }
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = GlanceModifier.padding(start = 25.dp, top = 25.dp),
                text = "Map",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily("rubik"),
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(Color(0xFFFFFFFF)),
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

        Box(
            modifier = GlanceModifier
                .padding(0.dp)
                .width(353.dp)
                .height(156.dp)
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth(),
                cameraPositionState = cameraPositionState,
            ) {
                Marker(
                    state = MarkerState(position = currentLocationValues),
                    title = "Current Location",
                    snippet = "Marker in Current Location"
                )
            }
        }
    }
}