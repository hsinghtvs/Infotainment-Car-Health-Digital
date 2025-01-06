package com.example.infotainment_car_health_digital.carHealth

import android.content.ComponentName
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.R
import com.example.infotainment_car_health_digital.home.VehicleHealth

@Composable
fun CarHealthScreen() {
    Row(
        modifier = Modifier.padding(bottom = 80.dp)
    ) {
        CarHealth(modifier = Modifier.weight(2f))
        Spacer(modifier = Modifier.weight(0.1f))
        NextServiceDue(Modifier.weight(1f))

    }
}

@Composable
private fun CarHealth(modifier: Modifier) {
    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF032B9D).copy(alpha = 0f),
            Color(0xFF040A1B).copy(alpha = 1f),
        )
    )
    val borderGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFDDE4FF).copy(alpha = 1f),
            Color(0xFF040A1B).copy(alpha = 0.4f)
        )
    )
    Column(
        modifier = modifier
            .padding(10.dp)
            .background(brush = backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                0.4.dp,
                brush = borderGradient,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(top =10.dp, start =10.dp, end = 10.dp)
    ) {
        Text(
            text = "CAR HEALTH",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                color = Color.White,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.size(10.dp))
        VehicleHealth(
            batteryStatus = "GOOD",
            engineStatus = "GOOD",
            coolingStatus = "GOOD",
            airIntakeStatus = "GOOD"
        )
    }
}

@Composable
private fun NextServiceDue(modifier: Modifier) {
    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF032B9D).copy(alpha = 0f),
            Color(0xFF040A1B).copy(alpha = 1f),
        )
    )
    val borderGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFDDE4FF).copy(alpha = 1f),
            Color(0xFF040A1B).copy(alpha = 0.4f)
        )
    )
    Column(
        modifier = modifier
            .padding(10.dp)
            .background(brush = backGroundGradient, shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .border(
                0.4.dp,
                brush = borderGradient,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Text(
            text = "HEALTH STATUS",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.manrope_bold)),
                color = Color.White,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn() {
            items(1) {
                ServiceBox(critical = "high", name = "Battery Needs Replacement", bookRsa = true)
                ServiceBox(critical = "high", name = "Engine Coolant Low", bookRsa = true)
                ServiceBox(
                    critical = "low",
                    name = "Suspension are weak fix it in next service",
                    bookRsa = false
                )
            }
        }
    }
}

@Composable
private fun ServiceBox(critical: String, name: String, bookRsa: Boolean) {

    val context = LocalContext.current
    val buttonBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF255AF5).copy(alpha = 1f),
            Color(0xFF0B1112).copy(alpha = 0.1f)
        )
    )

    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF000000).copy(alpha = 0f),
            Color(0xFF76ADFF).copy(alpha = 0.2f)
        )
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {

            }
            .background(brush = backGroundGradient, shape = RoundedCornerShape(size = 10.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = if (critical == "low") R.drawable.info_circle else R.drawable.warning),
                contentDescription = critical
            )
            Spacer(modifier = Modifier.size(15.dp))
            Text(
                modifier = Modifier.weight(3f),
                text = name,
                style = Typography().labelSmall.copy(color = Color.White)
            )
            if (bookRsa) {
                Box(modifier = Modifier
                    .weight(1f)
                    .clickable {
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.action = Intent.ACTION_SEND
                        intent.component =
                            ComponentName(
                                "com.example.infotainment_rsa",
                                "com.example.infotainment_rsa.MainActivity"
                            )
                        intent.putExtra("service", name)
                        intent.type = "text/plain"
                        context.startActivity(intent)
                    }) {
                    Text(
                        modifier = Modifier
                            .background(
                                brush = buttonBackGroundGradient,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        text = "BOOK RSA",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(color = Color.White, fontSize = 10.sp)
                    )
                }
            }
        }
    }
}