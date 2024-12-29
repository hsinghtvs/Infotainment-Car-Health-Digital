package com.example.infotainment_car_health_digital.services

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.R


var selectedPastService by mutableIntStateOf(0)

@Composable
fun PastServices() {
    Row(
        modifier = Modifier.padding(bottom = 85.dp)
    ) {
        BookingInformation(Modifier.weight(1f))
        Spacer(modifier = Modifier.size(10.dp))
        ServiceDetail(Modifier.weight(1f))
        Spacer(modifier = Modifier.size(10.dp))
        EstimateReport(Modifier.weight(2.5f))
    }
}

@Composable
fun ServiceDetail(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Service Details",
            style = TextStyle(color = Color.White, fontSize = 14.sp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn() {
            items(6) {
                when (it) {
                    0 -> {
                        ServiceAudit(
                            "Service Booking Received",
                            "10 : 15 Am , 7th Jan",
                            R.drawable.service
                        )
                    }

                    1 -> {
                        ServiceAudit(
                            "Inventory inspection complete",
                            "10 : 20 Am , 7th Jan",
                            R.drawable.inspection
                        )
                    }

                    2 -> {
                        ServiceAudit(
                            "Vehicle inspection complete",
                            "10 : 25 Am , 7th Jan",
                            R.drawable.vehicle_inspection
                        )
                    }

                    3 -> {
                        ServiceAudit(
                            "Estimated Approved",
                            "10 : 58 Am , 7th Jan",
                            R.drawable.estimation
                        )
                    }

                    4 -> {
                        ServiceAudit(
                            "Repair work in progress",
                            "12 : 15 Am , 7th Jan",
                            R.drawable.workinprogress
                        )
                    }

                    5 -> {
                        ServiceAudit(
                            "Final health check complete",
                            "04 : 15 Pm , 7th Jan",
                            R.drawable.finalhealthcheckup
                        )
                    }
                }
                Spacer(modifier = Modifier.size(5.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(1.dp)
                        .background(color = Color.White.copy(alpha = 0.1f))
                )
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
    }
}

@Composable
fun ServiceAudit(serviceName: String, serviceDate: String, image: Int) {
    Row {
        Image(
            modifier = Modifier
                .size(30.dp)
                .border(2.dp, color = Color(0xFF172034), shape = CircleShape)
                .padding(10.dp),
            painter = painterResource(id = image),
            contentDescription = ""
        )
        Column(
            modifier = Modifier.padding(start = 5.dp)
        ) {
            Text(
                text = serviceName,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.size(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.check), contentDescription = "")
                Text(
                    text = serviceDate,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
fun BookingInformation(modifier: Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(2) {
            if (it == 0) {
                BookingInfoBox(
                    name = "Periodic Maintenance Service",
                    bookingId = "123456",
                    bookingDate = "12 Dec 2024",
                    index = 0
                )
            } else {
                BookingInfoBox(
                    name = "Engine Overheating",
                    bookingId = "123456",
                    bookingDate = "12 Dec 2024",
                    index = 1
                )
            }
        }
    }
}

@Composable
fun BookingInfoBox(name: String, bookingId: String, bookingDate: String, index: Int) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {
                selectedPastService = index
            }
            .background(color = Color(0xFF1D3354), shape = RoundedCornerShape(size = 10.dp))
            .border(
                width = 1.dp,
                color = if (selectedPastService == index) {
                    Color(0xFF1F57E7)
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(10.dp)
            ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = name,
                style = Typography().labelSmall.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = "Booking Id $bookingId",
                style = Typography().labelSmall.copy(color = Color.Gray)
            )
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bookingDate,
                    style = Typography().labelSmall.copy(color = Color.Gray)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier.size(10.dp),
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = "my TvsGunidy",
                    style = Typography().labelSmall.copy(color = Color.Gray)
                )
            }

        }
    }

}