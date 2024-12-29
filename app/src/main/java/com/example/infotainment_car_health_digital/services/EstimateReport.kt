package com.example.infotainment_car_health_digital.services

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.infotainment_car_health_digital.R
import com.example.infotainment_car_health_digital.TabItem
import com.example.infotainment_car_health_digital.Tabs

@SuppressLint("UnrememberedMutableState")
@Composable
fun EstimateReport(
    modifier: Modifier
) {
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

    var grandTotal by mutableDoubleStateOf(0.0)
    val tabs = listOf(
        TabItem(0, "Service Estimate"),
        TabItem(1, "Inspection Report"),
        TabItem(2, "Inspection Pictures")
    )
    var currentTabSelect by remember { mutableStateOf(0) }
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Tabs(
                tabs = tabs,
                onTabSelect = {
                    currentTabSelect = it.id
                },
                selectedIndex = currentTabSelect,
                modifier = Modifier.weight(2f)
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .background(
                        brush = rowBackGroundGradient,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Box(modifier = Modifier) {
                    Text(
                        modifier = Modifier
                            .background(
                                brush = transparentGradient,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(10.dp),
                        text = "Over All Rating",
                        style = TextStyle(color = Color.White, fontSize = 14.sp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Box(modifier = Modifier) {
                    Text(
                        modifier = Modifier
                            .background(
                                brush = transparentGradient,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(vertical = 10.dp),
                        text = "Good",
                        style = TextStyle(color = Color(0xFF059C05), fontSize = 14.sp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(18.dp))
        when (currentTabSelect) {
            0 -> ServiceEstimate(
                grandTotal = grandTotal
            )

            1 -> InspectionReports(

            )
        }
    }
}

@Composable
fun InspectionReports(
) {
    var selected by remember { mutableStateOf("Engine") }
    Column(

    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = "Major CheckList Summary",
                        color = Color.White.copy(alpha = 0.96f),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.rubik)),
                        )
                    )
                }
                item {
                    InspectionIndividuals(
                        title = "Engine",
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        performance = "Good",
//                        parameter = result.inspectionDetails.engine.parameters,
                        selected = selected == "Engine",
                        modifier = Modifier
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Battery",
                        performance = "Average",
                        selected = selected == "Battery",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.battery.parameters
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Suspension",
                        performance = "Good",
                        selected = selected == "Suspension",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.suspension.parameters
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Tyre",
                        performance = "Bad",
                        selected = selected == "Tires",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.tire.parameters
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Electrical",
                        performance = "Good",
                        selected = selected == "Electrical",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.electrical.parameters
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Transmission",
                        performance = "Good",
                        selected = selected == "Transmission",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.trans.parameters
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Interior",
                        performance = "Good",
                        selected = selected == "Interior",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.interior.parameters
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Exterior",
                        performance = "Good",
                        selected = selected == "Exterior",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.exterior.parameters
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Brakes",
                        performance = "Good",
                        selected = selected == "Brakes",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.brakes.parameters
                    )
                }
                item {
                    InspectionIndividuals(
                        image = "https://spn-sta.spinny.com/blog/20220228143219/ezgif.com-gif-maker-2021-11-24T204220.732.jpg?compress=true&quality=80&w=800&dpr=2.6",
                        title = "Steering",
                        performance = "Good",
                        selected = selected == "Steering",
                        modifier = Modifier
//                        parameter = result.inspectionDetails.steering.parameters
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = "Engine Status",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 10.sp
                        )
                    )
                }
//                parameter?.let { parameter ->
                items(8) {
                    when (it) {
                        1 -> {
                            PerformanceData(
                                title = "Radiator",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        2 -> {
                            PerformanceData(
                                title = "Hose Pipe",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        3 -> {
                            PerformanceData(
                                title = "Spark Plug",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        4 -> {
                            PerformanceData(
                                title = "Ignition Coil/Ignitor",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        5 -> {
                            PerformanceData(
                                title = "High Tension Cable",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        6 -> {
                            PerformanceData(
                                title = "Brake Oil",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        7 -> {
                            PerformanceData(
                                title = "Coolant",
                                performance = "Average",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Black in Color"
                            )
                        }

                    }
                }
//                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionIndividuals(
    image: String,
    title: String,
    performance: String,
    selected: Boolean = false,
    modifier: Modifier = Modifier,
//    parameter: List<Parameter>
) {
    var showDialogue by remember { mutableStateOf(false) }
    val selectedGradientColors = Brush.verticalGradient(
        listOf(
//            Color(red = 0.40f, green = 0.56f, blue = 0.92f, alpha = 1f),
//            Color(red = 0f, green = 0f, blue = 0f, alpha = 0f),
            Color.Transparent,
            Color.Transparent

        )
    )
    val nonSelectedGradient = Brush.verticalGradient(
        listOf(
//            Color(red = 0f, green = 0f, blue = 0f, alpha = 0f),
//            Color(red = 0f, green = 0f, blue = 0f, alpha = 0f),
            Color.Transparent,
            Color.Transparent

        )
    )
    Column {
        Box(
            modifier = modifier
                .background(
                    brush = if (selected) {
                        selectedGradientColors
                    } else {
                        nonSelectedGradient
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (selected) {
                        Color.Transparent
                    } else {
                        Color.Transparent
                    }
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    modifier = Modifier.size(20.dp),
                    model = image,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color(0xFFF36F21))
                )
                Spacer(modifier = Modifier.size(21.dp))
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = performance,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        fontWeight = FontWeight(600),
                        color = when (performance) {
                            "Good" -> Color(0xFF059C05)
                            "Average" -> Color(0xFFEFAF24)
                            else -> Color.Red
                        },
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.size(5.dp))
                if (performance == "Good") {
                    repeat(3) {
                        Performance(color = Color(0xFF76B283))
                    }
                } else if (performance == "Average") {
                    repeat(2) {
                        Performance(color = Color(0xFFEFAF24))
                    }
                    Performance(color = Color.Black)
                } else {
                    repeat(3) {
                        Performance(color = Color.Red)
                    }
                }
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(color = Color.Gray.copy(alpha = 0.5f))
                .fillMaxWidth()
        )
    }

    if (showDialogue) {
        ModalBottomSheet(
            containerColor = Color.White,
            onDismissRequest = {
                showDialogue = false
            }) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = Color.White)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = title, color = Color(0xFF213F99))
                        Image(
                            modifier = Modifier.clickable {
                                showDialogue = false
                            },
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = ""
                        )
                    }
                }
                item {
                    Canvas(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    ) {

                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }
                }
//                parameter?.let { parameter ->
                items(7) {
                    when (it) {
                        1 -> {
                            PerformanceData(
                                title = "Radiator",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        2 -> {
                            PerformanceData(
                                title = "Hose Pipe",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        3 -> {
                            PerformanceData(
                                title = "Spark Plug",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        4 -> {
                            PerformanceData(
                                title = "Ignition Coil/Ignitor",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        5 -> {
                            PerformanceData(
                                title = "High Tension Cable",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        6 -> {
                            PerformanceData(
                                title = "Brake Oil",
                                performance = "Good",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Good"
                            )
                        }

                        7 -> {
                            PerformanceData(
                                title = "Coolant",
                                performance = "Average",
//                            description = if (item.ratingReasonDesc != "" && item.ratingReasonDesc != null) item.ratingReasonDesc else item.ratingReasonRemarks
                                description = "Black in Color"
                            )
                        }

                    }
                }
//                }
            }
        }
    }
}

@Composable
fun Performance(
    color: Color
) {
    Box(
        modifier = Modifier
            .padding(0.5.dp)
            .width(15.dp)
            .height(6.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(0.dp)
            )
    )
}

@Composable
private fun PerformanceData(
    title: String,
    performance: String,
    description: String
) {
    Column {
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    maxLines = 1,
                    text = title,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        fontWeight = FontWeight(100),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                )
                if (description != "") {
                    Text(
                        modifier = Modifier,
                        maxLines = 1,
                        text = description,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.rubik)),
                            fontWeight = FontWeight(400),
                            color = Color.Gray.copy(alpha = 0.65f),
                            fontSize = 12.sp
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = performance,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.rubik)),
                    fontWeight = FontWeight(600),
                    color = Color.Gray,
                    fontSize = 12.sp
                ),
                color = when (performance) {
                    "Good" -> Color(0xFF059C05)
                    "Average" -> Color(0xFFF36F21)
                    else -> Color.Red
                }
            )
        }
        Spacer(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .height(1.dp)
                .background(color = Color.Gray.copy(alpha = 0.5f))
                .fillMaxWidth()
        )
    }
}

@Composable
fun ServiceEstimate(
    grandTotal: Double
) {
    LazyColumn() {
        item {
            ServiceCard(
                title = "Labour Details",
                qty = "Qty",
                rate = "Rate",
                totalAmount = "TotalAmount",
                backgroundColor = Color(0xFF6F8EFB).copy(alpha = 0.10f)
            )
        }

        items(1) {
            ServiceCard(
                title = "Renew Window Regulator",
                qty = "1 Nos",
                rate = "₹690.00",
                totalAmount = "₹810.00",
                backgroundColor = Color.Transparent
            )
        }

        item {
            ServiceCard(
                title = "Parts Details",
                qty = "Qty",
                rate = "Rate",
                totalAmount = "TotalAmount",
                backgroundColor = Color(0xFF6F8EFB).copy(alpha = 0.10f)
            )
        }

        items(3) {
            if (it == 0) {
                ServiceCard(
                    title = "Bumper Front (Black)",
                    qty = "1 Nos",
                    rate = "₹690.00",
                    totalAmount = "₹810.00",
                    backgroundColor = Color.Transparent
                )

            } else if (it == 1) {
                ServiceCard(
                    title = "Regulator Assy, Front Window R",
                    qty = "1 Nos",
                    rate = "₹690.00",
                    totalAmount = "₹810.00",
                    backgroundColor = Color.Transparent
                )
            } else if (it == 2) {
                ServiceCard(
                    title = "Cover",
                    qty = "1 Nos",
                    rate = "₹690.00",
                    totalAmount = "₹810.00",
                    backgroundColor = Color.Transparent
                )
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .align(Alignment.End
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 7.dp),
                    text = "Grand Total",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        fontSize = 12.sp
                    )
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 7.dp),
                    text = "₹3240.00",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF89F38D),
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
fun ServiceCard(
    title: String,
    qty: String,
    rate: String,
    totalAmount: String,
    backgroundColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 7.dp)
                .width(180.dp),
            text = title,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.rubik)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                fontSize = 12.sp
            )
        )
        Text(
            modifier = Modifier
                .width(60.dp)
                .padding(horizontal = 5.dp, vertical = 7.dp),
            text = qty,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.rubik)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                fontSize = 12.sp
            )
        )
        Text(
            modifier = Modifier
                .width(100.dp)
                .padding(horizontal = 5.dp, vertical = 7.dp),
            text = rate,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.rubik)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                fontSize = 12.sp
            )
        )
        Text(
            modifier = Modifier
                .width(100.dp)
                .padding(horizontal = 5.dp, vertical = 7.dp),
            text = totalAmount,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.rubik)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                fontSize = 12.sp
            )
        )
    }
}