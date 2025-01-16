package com.example.infotainment_car_health_digital.services

import android.app.Activity
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infotainment_car_health_digital.R
import com.example.infotainment_car_health_digital.viewmodel.MainViewModel
import java.util.Calendar
import java.util.Date


@Composable
fun BookService(viewModel: MainViewModel) {
    val pickUpOption = listOf("Pick Up", "Self Drop")
    val timingOption = listOf("09:00-11:00", "11:00-13:00", "13:00-15:00", "15:00-17:00")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(pickUpOption[0]) }
    val (timingOptionSelected, onTimingOptionSelected) = remember { mutableStateOf(timingOption[0]) }
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)


    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()
    val mDatePickerDialogFromDate = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            viewModel.bookingDate = "$mDayOfMonth-${mMonth + 1}-$mYear"
        }, mYear, mMonth, mDay
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = viewModel.mainBackGroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp)
                .border(
                    0.4.dp,
                    brush = viewModel.borderGradient,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
                .background(viewModel.mainBackGroundGradient),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            Text(
//                text = "Please select Booking Date", style = TextStyle(
//                    color = Color.White,
//                    fontSize = 16.sp, fontFamily = FontFamily(
//                        Font(R.font.manrope_regular)
//                    )
//                )
//            )
            Spacer(modifier = Modifier.size(10.dp))

            Box(modifier = Modifier.clickable {
                mDatePickerDialogFromDate.show()
            }) {
                Text(
                    modifier = Modifier
                        .background(
                            brush = viewModel.buttonBackGroundGradient,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(10.dp),
                    text = viewModel.bookingDate,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.manrope_bold))
                    )
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
//            Text(
//                text = "Please select Booking Type ", style = TextStyle(
//                    color = Color.White,
//                    fontSize = 16.sp, fontFamily = FontFamily(
//                        Font(R.font.manrope_regular)
//                    )
//                )
//            )
//            Spacer(modifier = Modifier.size(10.dp))
//            Row(
//            ) {
//                pickUpOption.forEach { option ->
//                    Row(
//                        modifier = Modifier
//                            .selectable(
//                                selected = (option == selectedOption),
//                                onClick = {
//                                    onOptionSelected(option)
//                                }
//                            ),
//                        horizontalArrangement = Arrangement.Start,
//                        verticalAlignment = Alignment.CenterVertically
//
//                    ) {
//
//                        RadioButton(
//                            selected = (option == selectedOption),
//                            onClick = {
//                                onOptionSelected(option)
//                            },
//                            modifier = Modifier.padding(8.dp),
//                            enabled = true,
//                            colors = RadioButtonDefaults.colors(
//                                Color.Blue,
//                                unselectedColor = Color.Blue
//                            ),
//                            interactionSource = remember { MutableInteractionSource() }
//                        )
//                        Text(
//                            text = option,
//                            modifier = Modifier.padding(start = 16.dp),
//                            fontWeight = FontWeight.Bold,
//                            style = TextStyle(
//                                color = Color.White,
//                                fontSize = 16.sp,
//                                fontFamily = FontFamily(Font(R.font.manrope_regular))
//                            )
//                        )
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.size(10.dp))
//            Text(
//                text = "Please select Booking Time", style = TextStyle(
//                    color = Color.White,
//                    fontSize = 16.sp, fontFamily = FontFamily(
//                        Font(R.font.manrope_regular)
//                    )
//                )
//            )
//            Spacer(modifier = Modifier.size(10.dp))
//            Row{
//                timingOption.forEach { timing ->
//                    Row(
//                        modifier = Modifier
//                            .selectable(
//                                selected = (timing == timingOptionSelected),
//                                onClick = {
//                                    onTimingOptionSelected(timing)
//                                }
//                            ),
//                        horizontalArrangement = Arrangement.Start,
//                        verticalAlignment = Alignment.CenterVertically
//
//                    ) {
//
//                        RadioButton(
//                            selected = (timing == timingOptionSelected),
//                            onClick = {
//                                onTimingOptionSelected(timing)
//                            },
//                            modifier = Modifier.padding(8.dp),
//                            enabled = true,
//                            colors = RadioButtonDefaults.colors(
//                                Color.Blue,
//                                Color.Blue
//                            ),
//                            interactionSource = remember { MutableInteractionSource() }
//                        )
//                        Text(
//                            text = timing,
//                            modifier = Modifier.padding(start = 16.dp),
//                            fontWeight = FontWeight.Bold,
//                            style = TextStyle(
//                                color = Color.White,
//                                fontSize = 16.sp,
//                                fontFamily = FontFamily(Font(R.font.manrope_regular))
//                            )
//                        )
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.size(10.dp))

            Spacer(modifier = Modifier.size(10.dp))
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    viewModel.bookingReason = ""
                    if (viewModel.intentValue) {
                        viewModel.intentValue = false
                        activity?.finish()
                    }
                }) {
                Text(
                    modifier = Modifier
                        .width(180.dp)
                        .background(
                            brush = viewModel.serviceButtonBackGroundGradient,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(10.dp),
                    text = "Book Service",
                    style = TextStyle(
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.manrope_bold))
                    )
                )
            }
        }
        Box(modifier = Modifier.padding(top = 30.dp).align(Alignment.TopCenter).clickable {
        }) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                text = "Booking ${viewModel.bookingReason}",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.manrope_extrabold))
                )
            )
        }
    }
}

//@Preview
//@Composable
//fun PreviewBookService() {
//    BookService()
//}