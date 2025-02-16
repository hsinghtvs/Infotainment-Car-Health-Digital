package com.example.infotainment_car_health_digital.services

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.R
import com.example.infotainment_car_health_digital.viewmodel.MainViewModel

var selectedService by mutableIntStateOf(0)

@Composable
fun Services(viewModel: MainViewModel) {

    if (viewModel.bookingReason.isEmpty()) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .padding(bottom = 70.dp)
                .fillMaxHeight()
                .background(brush = viewModel.backGroundGradient, shape = RoundedCornerShape(8.dp))
                .border(1.dp, brush = viewModel.borderGradient, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .background(
                                brush = viewModel.tabRowBackGroundGradient, shape = CircleShape
                            )
                            .border(
                                1.dp,
                                brush = viewModel.itemsBorderGradient,
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        Box(modifier = Modifier.clickable {
                            selectedService = 0
                        }) {
                            Text(
                                modifier = Modifier
                                    .background(
                                        brush = if (selectedService == 0) viewModel.serviceButtonBackGroundGradient else viewModel.transparentGradient,
                                        shape = RoundedCornerShape(30.dp)
                                    )
                                    .padding(10.dp), text = "Due Service", style = TextStyle(
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope_bold))
                                )
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Box(modifier = Modifier.clickable {
                            selectedService = 1
                        }) {
                            Text(
                                modifier = Modifier
                                    .background(
                                        brush = if (selectedService == 1) viewModel.serviceButtonBackGroundGradient else viewModel.transparentGradient,
                                        shape = RoundedCornerShape(30.dp)
                                    )
                                    .padding(10.dp), text = "Past Service", style = TextStyle(
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.manrope_bold))
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                if (selectedService == 1) {
                    Text(
                        modifier = Modifier.weight(1f), text = "Service Details", style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.manrope_bold))
                        )
                    )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    val tabs = listOf(
//                        TabItem(0, "Service Estimate"),
//                        TabItem(1, "Inspection Report"),
//                        TabItem(2, "Inspection Pictures")
//                    )
//                    var currentTabSelect by remember { mutableStateOf(0) }
//                    Tabs(
//                        tabs = tabs,
//                        onTabSelect = {
//                            currentTabSelect = it.id
//                            viewModel.selectedEstimateTab = it.id
//                        },
//                        selectedIndex = viewModel.selectedEstimateTab,
//                        modifier = Modifier.weight(2f)
//                    )
//                    Row(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(10.dp)
//                            .background(
//                                brush = rowBackGroundGradient,
//                                shape = RoundedCornerShape(10.dp)
//                            )
//                    ) {
//                        Box(
//                            modifier = Modifier.weight(2f)
//                        ) {
//                            Text(
//                                modifier = Modifier
//                                    .background(
//                                        brush = transparentGradient,
//                                        shape = RoundedCornerShape(30.dp)
//                                    )
//                                    .padding(10.dp),
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis,
//                                text = "Over All Rating",
//                                style = TextStyle(
//                                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
//                                    color = Color.White,
//                                    fontSize = 14.sp
//                                )
//                            )
//                        }
//                        Spacer(modifier = Modifier.size(10.dp))
//                        Box(modifier = Modifier.weight(0.5f)) {
//                            viewModel.estimateServiceHistoryDetail?.result?.inspectionDetails?.overallRating?.let {
//                                Text(
//                                    modifier = Modifier
//                                        .background(
//                                            brush = transparentGradient,
//                                            shape = RoundedCornerShape(30.dp)
//                                        )
//                                        .padding(vertical = 10.dp),
//                                    text = it,
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis,
//                                    style = TextStyle(
//                                        fontFamily = FontFamily(Font(R.font.manrope_medium)),
//                                        color = Color(0xFF059C05),
//                                        fontSize = 14.sp
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
                }
            }



            Spacer(modifier = Modifier.size(10.dp))

            if (selectedService == 0) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxWidth(), columns = StaggeredGridCells.Fixed(3)
                ) {
                    items(5) {
                        if (it == 2) {
                            ServiceBox(
                                name = "Periodic Maintenance Service", date = "July 2025", viewModel
                            ) { type ->
                                viewModel.bookingReason = type
                            }
                        } else if (it == 1) {
                            ServiceBox(
                                name = "Brake Checkup",
                                date = "Mar 2025",
                                viewModel
                            ) { type ->
                                viewModel.bookingReason = type
                            }
                        } else if (it == 0) {
                            ServiceBox(name = "AC Checkup", date = "Mar 2025", viewModel) { type ->
                                viewModel.bookingReason = type
                            }
                        } else if (it == 3) {
                            ServiceBox(
                                name = "Tyre Replacement",
                                date = "Dec 2025",
                                viewModel
                            ) { type ->
                                viewModel.bookingReason = type
                            }
                        }
                    }
                }
            } else {
                PastServices(viewModel)
            }
        }
    } else {
        BookService(viewModel)
    }

    BackHandler {
        if (!viewModel.bookingReason.isEmpty()) {
            viewModel.bookingReason = ""
        } else {
            viewModel.closeApp = true
        }
    }
}

@Composable
private fun ServiceBox(
    name: String,
    date: String,
    viewModel: MainViewModel,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {
                onClick(name)
            }
            .background(brush = viewModel.buttonBackGroundGradient, shape = RoundedCornerShape(size = 6.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(15.dp))
            Text(
                modifier = Modifier.weight(2f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = name,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = date,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.manrope_bold)),
                    color = Color(0xFF56AAFF)
                )
            )
        }
    }
}