package com.example.infotainment_car_health_digital.services

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var selectedService by mutableIntStateOf(1)

@Composable
fun Services() {

    val buttonBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF255AF5).copy(alpha = 1f),
            Color(0xFF0B1112).copy(alpha = 0.1f)
        )
    )

    val rowBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF111841).copy(alpha = 1f),
            Color(0xFF050812).copy(alpha = 1f)
        )
    )

    val transparentGradient = Brush.verticalGradient(
        listOf(
            Color.Transparent,
            Color.Transparent
        )
    )
    Column {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .background(
                    brush = rowBackGroundGradient,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Box(modifier = Modifier.clickable {
                selectedService = 0
            }) {
                Text(
                    modifier = Modifier
                        .background(
                            brush = if (selectedService == 0) buttonBackGroundGradient else transparentGradient,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(10.dp),
                    text = "Due Service",
                    style = TextStyle(color = Color.White, fontSize = 14.sp)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier.clickable {
                selectedService = 1
            }) {
                Text(
                    modifier = Modifier
                        .background(
                            brush = if (selectedService == 1) buttonBackGroundGradient else transparentGradient,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(10.dp),
                    text = "Paid Service",
                    style = TextStyle(color = Color.White, fontSize = 14.sp)
                )
            }
        }

        Spacer(modifier = Modifier.size(10.dp))

        if (selectedService == 0) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = StaggeredGridCells.Fixed(3)
            ) {
                items(3) {
                    if (it == 0) {
                        ServiceBox(name = "Periodic Maintenance Service", date = "14 Jan")
                    } else if (it == 1) {
                        ServiceBox(name = "Brake Checkup", date = "18 Jan")
                    } else if (it == 2) {
                        ServiceBox(name = "AC Checkup", date = "22 Jan")
                    }
                }
            }
        } else {
            PastServices()
        }
    }
}

@Composable
private fun ServiceBox(name: String, date: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {

            }
            .background(color = Color(0xFF1D3354), shape = RoundedCornerShape(size = 10.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(15.dp))
            Text(
                text = name,
                style = Typography().labelSmall.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = date,
                style = Typography().labelSmall.copy(color = Color(0xFF3DED4F))
            )
        }
    }
}