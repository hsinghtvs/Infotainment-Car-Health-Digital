package com.example.infotainment_car_health_digital.services

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.infotainment_car_health_digital.R
import com.example.infotainment_car_health_digital.TabItem
import com.example.infotainment_car_health_digital.Tabs
import com.example.infotainment_car_health_digital.viewmodel.MainViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun EstimateReport(
    modifier: Modifier,
    viewModel: MainViewModel
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
    viewModel.estimateServiceHistoryDetail?.result?.estimationDetails?.labour?.let { labour ->
        for (i in labour.indices) {
            if (labour[i].approvalStatus == "2" ||
                labour[i].approvalStatus == "1"
            ) {
                grandTotal += labour[i].totalPrice
            }
        }
    }
    viewModel.estimateServiceHistoryDetail?.result?.estimationDetails?.parts?.let { parts ->
        for (i in parts.indices) {
            if (parts[i].approvalStatus == "2" ||
                parts[i].approvalStatus == "1"
            ) {
                grandTotal += parts[i].totalPrice
            }
        }
    }

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
                    viewModel.selectedEstimateTab = it.id
                },
                selectedIndex = viewModel.selectedEstimateTab,
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
                    viewModel.estimateServiceHistoryDetail?.result?.inspectionDetails?.overallRating?.let {
                        Text(
                            modifier = Modifier
                                .background(
                                    brush = transparentGradient,
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .padding(vertical = 10.dp),
                            text = it,
                            style = TextStyle(color = Color(0xFF059C05), fontSize = 14.sp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(18.dp))
        when (viewModel.selectedEstimateTab) {
            0 -> {
                ServiceEstimate(
                    grandTotal = grandTotal,
                    viewModel
                )
            }

            1 -> InspectionReports(
                viewModel
            )
            2 -> viewModel.inventoryServiceHistoryDetail?.result?.inventoryPhotos?.let { VehicleInventoryPicture(images = it) }
        }
    }
}

@Composable
fun VehicleInventoryPicture(
    images: List<String>
) {
    var showDialogue by remember { mutableStateOf(false) }
    var showDialogueIndex by remember { mutableIntStateOf(0) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        itemsIndexed(images) { index, item ->
            AsyncImage(
                modifier = Modifier
                    .clickable {
                        showDialogue = true
                        showDialogueIndex = index
                    }
                    .padding(10.dp)
                    .height(height = 130.dp),
                model = item,
                contentDescription = ""
            )
        }
    }
    if (showDialogue) {
        Dialog(
            onDismissRequest = {
                showDialogue = false
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp)
                            .clickable { showDialogue = false },
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.clickable {
                            if (showDialogueIndex > 0) {
                                showDialogueIndex--
                            }
                        },
                        painter = painterResource(id = R.drawable.left_arrow),
                        contentDescription = "",
                        colorFilter = if (showDialogueIndex > 0) {
                            ColorFilter.tint(color = Color.White)
                        } else {
                            ColorFilter.tint(color = Color.White.copy(alpha = 0.25f))
                        }
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    AsyncImage(
                        modifier = Modifier.size(width = 400.dp, height = 400.dp),
                        model = images[showDialogueIndex],
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Image(
                        modifier = Modifier.clickable {
                            if (showDialogueIndex < (images.size - 1)) {
                                showDialogueIndex++
                            }
                        },
                        painter = painterResource(id = R.drawable.right_arrow),
                        contentDescription = "",
                        colorFilter = if (showDialogueIndex < (images.size - 1)) {
                            ColorFilter.tint(color = Color.White)
                        } else {
                            ColorFilter.tint(color = Color.White.copy(alpha = 0.25f))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InspectionReports(viewModel: MainViewModel) {
    var selected by remember { mutableStateOf("Engine") }
    Column(

    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            viewModel.estimateServiceHistoryDetail?.result?.inspectionDetails?.let {
                if (it.checklistType == "Minor checklist"){
                    LazyColumn() {
                        item {
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = "${it.checklistType} Summary",
                                color = Color.White.copy(alpha = 0.96f),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.rubik)),
                                )
                            )
                        }
                        items(it.parameters) {
                            InspectionIndividuals(
                                image = it.imageURL,
                                title = it.parameterName,
                                performance = it.parameterRating,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        item {
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = it.checklistType,
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
                                image = it.engine.imageURL,
                                performance = it.engine.subsystemRating,
                                selected = selected == "Engine",
                                modifier = Modifier.clickable {
                                    selected = "Engine"
                                    viewModel.selectedParameter =
                                        it.engine.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.battery.imageURL,
                                title = "Battery",
                                performance = it.battery.subsystemRating,
                                selected = selected == "Battery",
                                modifier = Modifier.clickable {
                                    selected = "Battery"
                                    viewModel.selectedParameter =
                                        it.battery.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.suspension.imageURL,
                                title = "Suspension",
                                performance = it.suspension.subsystemRating,
                                selected = selected == "Suspension",
                                modifier = Modifier.clickable {
                                    selected = "Suspension"
                                    viewModel.selectedParameter =
                                        it.suspension.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.tire.imageURL,
                                title = "Tyre",
                                performance = it.tire.subsystemRating,
                                selected = selected == "Tires",
                                modifier = Modifier.clickable {
                                    selected = "Tires"
                                    viewModel.selectedParameter = it.tire.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.electrical.imageURL,
                                title = "Electrical",
                                performance = it.electrical.subsystemRating,
                                selected = selected == "Electrical",
                                modifier = Modifier.clickable {
                                    selected = "Electrical"
                                    viewModel.selectedParameter =
                                        it.electrical.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.trans.imageURL,
                                title = "Transmission",
                                performance = it.trans.subsystemRating,
                                selected = selected == "Transmission",
                                modifier = Modifier.clickable {
                                    selected = "Transmission"
                                    viewModel.selectedParameter =
                                        it.trans.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.interior.imageURL,
                                title = "Interior",
                                performance = it.interior.subsystemRating,
                                selected = selected == "Interior",
                                modifier = Modifier.clickable {
                                    selected = "Interior"
                                    viewModel.selectedParameter =
                                        it.interior.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.exterior.imageURL,
                                title = "Exterior",
                                performance = it.exterior.subsystemRating,
                                selected = selected == "Exterior",
                                modifier = Modifier.clickable {
                                    selected = "Exterior"
                                    viewModel.selectedParameter =
                                        it.exterior.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.brakes.imageURL,
                                title = "Brakes",
                                performance = it.brakes.subsystemRating,
                                selected = selected == "Brakes",
                                modifier = Modifier.clickable {
                                    selected = "Brakes"
                                    viewModel.selectedParameter =
                                        it.brakes.parameters.toMutableList()
                                }
                            )
                        }
                        item {
                            InspectionIndividuals(
                                image = it.steering.imageURL,
                                title = "Steering",
                                performance = it.steering.subsystemRating,
                                selected = selected == "Steering",
                                modifier = Modifier.clickable {
                                    selected = "Steering"
                                    viewModel.selectedParameter =
                                        it.steering.parameters.toMutableList()
                                }
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
                                text = "$selected Status",
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 10.sp
                                )
                            )
                        }
                        viewModel.selectedParameter?.let { parameter ->
                            itemsIndexed(parameter) { index, item ->
                                item?.let { it1 ->
                                    PerformanceData(
                                        title = it1.parameterName,
                                        performance = it1.parameterRating,
                                        description = if (it1.ratingReasonDesc != "" && it1.ratingReasonDesc != null) it1.ratingReasonDesc else it1.ratingReasonRemarks,
                                    )
                                }
                            }
                        }
                    }
                }
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
    onClick: () -> Unit = {},
) {
    val selectedGradientColors = Brush.verticalGradient(
        listOf(
            Color.Transparent,
            Color.Transparent
        )
    )
    val nonSelectedGradient = Brush.verticalGradient(
        listOf(
            Color.Transparent,
            Color.Transparent

        )
    )
    Column(
        modifier = modifier
    ) {
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
fun PerformanceData(
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
    grandTotal: Double,
    viewModel: MainViewModel
) {
    if(viewModel.gettingReports){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            CircularProgressIndicator()
        }
    } else {
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

            viewModel.estimateServiceHistoryDetail?.result?.estimationDetails?.labour?.let {
                items(it) {
                    ServiceCard(
                        title = it.description,
                        qty = "${it.quantity} Nos",
                        rate = "₹${it.totalPrice / it.quantity}",
                        totalAmount = "₹${it.totalPrice}",
                        backgroundColor = Color.Transparent
                    )
                }
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

            viewModel.estimateServiceHistoryDetail?.result?.estimationDetails?.parts?.let {
                items(it) {
                    ServiceCard(
                        title = it.description,
                        qty = "${it.quantity} Nos",
                        rate = "₹${it.totalPrice / it.quantity}",
                        totalAmount = "₹${it.totalPrice}",
                        backgroundColor = Color.Transparent
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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