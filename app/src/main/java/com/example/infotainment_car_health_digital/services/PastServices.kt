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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.R
import com.example.infotainment_car_health_digital.viewmodel.MainViewModel


var selectedPastService by mutableIntStateOf(0)
var remarkType by mutableStateOf("")

@Composable
fun PastServices(viewModel: MainViewModel) {
    Row(
        modifier = Modifier
    ) {
        BookingInformation(Modifier.weight(1f), viewModel)
        Spacer(modifier = Modifier.size(10.dp))
        ServiceDetail(Modifier.weight(1f), viewModel)
        Spacer(modifier = Modifier.size(10.dp))
        EstimateReport(Modifier.weight(2.5f), viewModel)

    }
}

@Composable
fun ServiceDetail(modifier: Modifier, viewModel: MainViewModel) {
    var serviceHistoryDetail = viewModel.serviceHistoryDetail
    var bookingId by remember { mutableStateOf(viewModel.serviceHistoryBookings[1]) }

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        if (viewModel.gettingReports == true) {
            CircularProgressIndicator()
        } else {
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
                                        .fillMaxWidth()
                                        .background(color = Color.White.copy(alpha = 0.20f)),
                                    thickness = 1.dp,
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
                                        .fillMaxWidth()
                                        .background(color = Color.White.copy(alpha = 0.20f)),
                                    thickness = 1.dp,
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
                                        .fillMaxWidth()
                                        .background(color = Color.White.copy(alpha = 0.20f)),
                                    thickness = 1.dp,
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
                                        .fillMaxWidth()
                                        .background(color = Color.White.copy(alpha = 0.20f)),
                                    thickness = 1.dp,
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
                                        .fillMaxWidth()
                                        .background(color = Color.White.copy(alpha = 0.20f)),
                                    thickness = 1.dp,
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
                                        .fillMaxWidth()
                                        .background(color = Color.White.copy(alpha = 0.20f)),
                                    thickness = 1.dp,
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
        }
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

    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFFFFFFF).copy(alpha = 0f),
            Color(0xFFFFFFFF).copy(alpha = 0.2f)
        )
    )
    Row(
        modifier = modifier.padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(brush = backGroundGradient, shape = CircleShape)
                .border(1.dp, color = Color(0xFF007AEC).copy(alpha = 0.5f), shape = CircleShape)
                .padding(8.dp)
        ) {
            Image(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = image),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.size(5.dp))
        Column(

        ) {
            Text(
                text = description,
                fontFamily = FontFamily(Font(R.font.manrope_medium)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.manrope_medium)),
                    color = Color.White,
                    fontSize = 14.sp
                )
            )
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (completed) {
                    Image(
                        modifier = Modifier
                            .size(10.dp),
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
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontFamily = FontFamily(Font(R.font.manrope_regular)),
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
fun BookingInformation(modifier: Modifier, viewModel: MainViewModel) {

    LazyColumn(
        modifier = modifier
    ) {
        items(3) {
            when (it) {
                0 -> {
                    BookingInfoBox(
                        modifier = Modifier.clickable {
                        },
                        name = "Periodic Maintenance Service",
                        bookingId = "1004080",
                        bookingDate = "12 Dec 2024",
                        index = 0,
                        viewModel = viewModel
                    )
                }

                1 -> {
                    BookingInfoBox(
                        modifier = Modifier.clickable {
                        },
                        name = "Engine Overheating",
                        bookingId = "969314",
                        bookingDate = "12 Dec 2024",
                        index = 1,
                        viewModel = viewModel
                    )
                }

                2 -> {
                    BookingInfoBox(
                        modifier = Modifier.clickable {
                        },
                        name = "Battery Error",
                        bookingId = "867507",
                        bookingDate = "12 Dec 2024",
                        index = 2,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun BookingInfoBox(
    modifier: Modifier,
    name: String,
    bookingId: String,
    bookingDate: String,
    index: Int,
    viewModel: MainViewModel
) {
    val backGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF000000).copy(alpha = 0.2f),
            Color(0xFF76ADFF).copy(alpha = 0.2f)
        )
    )

    Box(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {
                when (index) {
                    0 -> {
                        viewModel.selectedEstimateTab = 0
                        viewModel.getServiceHistoryResponse("1004080")
                    }

                    1 -> {
                        viewModel.selectedEstimateTab = 0
                        viewModel.getServiceHistoryResponse("969314")
                    }

                    2 -> {
                        viewModel.selectedEstimateTab = 0
                        viewModel.getServiceHistoryResponse("867507")
                    }
                }
                selectedPastService = index
            }
            .background(brush = backGroundGradient, shape = RoundedCornerShape(size = 10.dp))
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = name,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.manrope_extrabold)),color = Color.White)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = "Booking Id $bookingId",
                style = TextStyle(fontFamily = FontFamily(Font(R.font.manrope_medium)), color = Color.White)
            )
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1.2f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = bookingDate,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.manrope_medium)), color = Color.White)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier.size(10.dp),
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = "my TvsGunidy",
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.manrope_medium)), color = Color.White)
                )
            }

        }
    }

}