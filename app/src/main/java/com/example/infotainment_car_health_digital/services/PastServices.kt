package com.example.infotainment_car_health_digital.services

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.R
import com.example.infotainment_car_health_digital.viewmodel.MainViewModel


var selectedPastService by mutableIntStateOf(0)
var remarkType by mutableStateOf("")

@Composable
fun PastServices(viewModel: MainViewModel) {
    Row(
        modifier = Modifier.padding(bottom = 85.dp)
    ) {
        BookingInformation(Modifier.weight(1f))
        Spacer(modifier = Modifier.size(10.dp))
        ServiceDetail(Modifier.weight(1f),viewModel)
        Spacer(modifier = Modifier.size(10.dp))
        EstimateReport(Modifier.weight(2.5f))
    }
}

@Composable
fun ServiceDetail(modifier: Modifier, viewModel: MainViewModel) {
    var serviceHistoryDetail = viewModel.serviceHistoryDetail
    var bookingId by remember { mutableStateOf(viewModel.serviceHistoryBookings[1]) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Service Details",
            style = TextStyle(color = Color.White, fontSize = 14.sp)
        )
        Spacer(modifier = Modifier.size(10.dp))

        Row {
            serviceHistoryDetail?.let { serviceHistory ->
                LazyColumn(
                    modifier = Modifier.padding(10.dp)
                ) {
                    item {
                        serviceHistory.result.auditTrail.customerWalkin?.let { customerWalkin ->
                            ServiceDetailsList(
                                image = R.drawable.service,
                                description = customerWalkin.name,
                                date = customerWalkin.timestamp,
                                completed = customerWalkin.isComplete,
                                reportKey = customerWalkin.reportKey != "",
                                detailMessage = customerWalkin.detailsMessage
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Divider(
                                modifier = Modifier
                                    .width(230.dp)
                                    .background(color = Color.White.copy(alpha = 0.20f)),
                                thickness = 2.dp,
                                color = Color.White.copy(alpha = 0.20f)
                            )
                        }
                    }
                    item {
                        serviceHistory.result.auditTrail.intialEstimationPending?.let { intialEstimationPending ->
                            ServiceDetailsList(
                                modifier = Modifier.clickable {
                                    if (intialEstimationPending.isComplete) {
                                        remarkType = intialEstimationPending.reportKey
                                        viewModel.getInventoryDetail(
                                            bookingId = bookingId,
                                            type = intialEstimationPending.reportKey
                                        )
                                    }
                                },
                                image = R.drawable.inspection,
                                description = intialEstimationPending.name,
                                date = intialEstimationPending.timestamp,
                                completed = intialEstimationPending.isComplete,
                                reportKey = intialEstimationPending.reportKey != "",
                                detailMessage = intialEstimationPending.detailsMessage
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Divider(
                                modifier = Modifier
                                    .width(230.dp)
                                    .background(color = Color.White.copy(alpha = 0.20f)),
                                thickness = 2.dp,
                                color = Color.White.copy(alpha = 0.20f)
                            )
                        }
                    }
                    item {
                        serviceHistory.result.auditTrail.inspectionCompleted?.let { inspectionCompleted ->
                            ServiceDetailsList(
                                modifier = Modifier.clickable {
                                    if (serviceHistory.result.auditTrail.estimation?.isComplete == true) {
                                        remarkType =
                                            serviceHistory.result.auditTrail.estimation.reportKey
                                        viewModel.getEstimateDetail(
                                            bookingId = bookingId,
                                            type = serviceHistory.result.auditTrail.estimation.reportKey
                                        )
                                    }
                                },
                                image = R.drawable.vehicle_inspection,
                                description = inspectionCompleted.name,
                                date = inspectionCompleted.timestamp,
                                completed = inspectionCompleted.isComplete,
                                reportKey = inspectionCompleted.reportKey != "",
                                detailMessage = inspectionCompleted.detailsMessage

                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Divider(
                                modifier = Modifier
                                    .width(230.dp)
                                    .background(color = Color.White.copy(alpha = 0.20f)),
                                thickness = 2.dp,
                                color = Color.White.copy(alpha = 0.20f)
                            )
                        }
                    }
                    item {
                        serviceHistory.result.auditTrail.estimation?.let { estimation ->
                            ServiceDetailsList(
                                modifier = Modifier.clickable {
                                    if (estimation.isComplete) {
                                        remarkType =
                                            estimation.reportKey
                                        viewModel.getEstimateDetail(
                                            bookingId = bookingId,
                                            type = estimation.reportKey
                                        )
                                    }
                                },
                                image = R.drawable.estimation,
                                description = estimation.name,
                                date = estimation.timestamp,
                                completed = estimation.isComplete,
                                reportKey = estimation.reportKey != "",
                                detailMessage = estimation.detailsMessage
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Divider(
                                modifier = Modifier
                                    .width(230.dp)
                                    .background(color = Color.White.copy(alpha = 0.20f)),
                                thickness = 2.dp,
                                color = Color.White.copy(alpha = 0.20f)
                            )
                        }
                    }
                    item {
                        serviceHistory.result.auditTrail.workInProgress?.let { workInProgress ->
                            ServiceDetailsList(
                                image = R.drawable.workinprogress,
                                description = workInProgress.name,
                                date = workInProgress.timestamp,
                                completed = workInProgress.isComplete,
                                reportKey = workInProgress.reportKey != "",
                                detailMessage = workInProgress.detailsMessage
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Divider(
                                modifier = Modifier
                                    .width(230.dp)
                                    .background(color = Color.White.copy(alpha = 0.20f)),
                                thickness = 2.dp,
                                color = Color.White.copy(alpha = 0.20f)
                            )
                        }
                    }
                    item {
                        serviceHistory.result.auditTrail.postInspectionCompleted?.let { postInspectionCompleted ->
                            ServiceDetailsList(
                                image = R.drawable.finalhealthcheckup,
                                description = postInspectionCompleted.name,
                                date = postInspectionCompleted.timestamp,
                                completed = postInspectionCompleted.isComplete,
                                reportKey = postInspectionCompleted.reportKey != "",
                                detailMessage = postInspectionCompleted.detailsMessage
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Divider(
                                modifier = Modifier
                                    .width(230.dp)
                                    .background(color = Color.White.copy(alpha = 0.20f)),
                                thickness = 2.dp,
                                color = Color.White.copy(alpha = 0.20f)
                            )
                        }
                    }
                    item {
                        serviceHistory.result.auditTrail.delivered?.let { delivered ->
                            ServiceDetailsList(
                                image = R.drawable.service_vehicle_delivered,
                                description = delivered.name,
                                date = delivered.timestamp,
                                completed = delivered.isComplete,
                                reportKey = delivered.reportKey != "",
                                detailMessage = delivered.detailsMessage
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
        }
//        LazyColumn() {
////            items(6) {
////                when (it) {
////                    0 -> {
////                        ServiceAudit(
////                            "Service Booking Received",
////                            "10 : 15 Am , 7th Jan",
////                            R.drawable.service
////                        )
////                    }
////
////                    1 -> {
////                        ServiceAudit(
////                            "Inventory inspection complete",
////                            "10 : 20 Am , 7th Jan",
////                            R.drawable.inspection
////                        )
////                    }
////
////                    2 -> {
////                        ServiceAudit(
////                            "Vehicle inspection complete",
////                            "10 : 25 Am , 7th Jan",
////                            R.drawable.vehicle_inspection
////                        )
////                    }
////
////                    3 -> {
////                        ServiceAudit(
////                            "Estimated Approved",
////                            "10 : 58 Am , 7th Jan",
////                            R.drawable.estimation
////                        )
////                    }
////
////                    4 -> {
////                        ServiceAudit(
////                            "Repair work in progress",
////                            "12 : 15 Am , 7th Jan",
////                            R.drawable.workinprogress
////                        )
////                    }
////
////                    5 -> {
////                        ServiceAudit(
////                            "Final health check complete",
////                            "04 : 15 Pm , 7th Jan",
////                            R.drawable.finalhealthcheckup
////                        )
////                    }
////                }
////                Spacer(modifier = Modifier.size(5.dp))
////                Spacer(
////                    modifier = Modifier
////                        .fillMaxSize()
////                        .height(1.dp)
////                        .background(color = Color.White.copy(alpha = 0.1f))
////                )
////                Spacer(modifier = Modifier.size(5.dp))
////            }
//        }
    }
}

@Composable
fun ServiceDetailsList(
    modifier: Modifier = Modifier,
    image: Int,
    description: String,
    date: String,
    completed: Boolean,
    reportKey: Boolean,
    detailMessage: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(12.dp),
            painter = painterResource(id = image),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.size(5.dp))
        Column(

        ) {
            Text(
                text = description,
                fontFamily = FontFamily(Font(R.font.rubik)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                fontSize = 12.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (completed) {
                    Image(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .size(16.dp),
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = ""
                    )
                } else {
                    Image(
                        colorFilter = ColorFilter.tint(color = Color(0xFF808080)),
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .size(16.dp),
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    modifier = Modifier,
                    text = if (completed) date else "Not yet Started",
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        fontWeight = FontWeight(400),
                        color = if (completed) Color(0xFFFFFFFF) else Color(0xFF808080),
                    )
                )
                if (completed && reportKey) {
                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 2.dp),
                        painter = painterResource(id = R.drawable.file_open),
                        contentDescription = ""
                    )
                }
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