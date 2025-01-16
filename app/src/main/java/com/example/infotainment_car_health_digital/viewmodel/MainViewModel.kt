package com.example.infotainment_car_health_digital.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infotainment_car_health_digital.repository.AppSettings
import com.example.infotainment_car_health_digital.repository.MainRepository
import com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates.EstimateResponseDTO
import com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates.Parameter
import com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.inventory.InventoryDTO
import com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.serviceHistory.ServiceHistoryResponseDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository,
    val context: Context,
    val appSettings: AppSettings
) : ViewModel() {
    var serviceHistoryBookings = mutableStateListOf<String>("1004080", "938183", "970661", "993647")
    var serviceHistoryDetail by mutableStateOf<ServiceHistoryResponseDTO?>(null)
    var inventoryServiceHistoryDetail by mutableStateOf<InventoryDTO?>(null)
    var inventoryServiceHistoryLoadingState by mutableStateOf(true)
    var estimateServiceHistoryDetail by mutableStateOf<EstimateResponseDTO?>(null)
    var estimateServiceHistoryLoadingState by mutableStateOf(true)
    var inspectionReportsParameters = listOf<Parameter>()
    var selectedParameter = mutableListOf<Parameter>()
    var errorResponse by mutableStateOf(0)
    var selectedEstimateTab by mutableStateOf(0)
    var gettingReports by mutableStateOf(false)
    var selectedTab by mutableIntStateOf(0)
    var bookingDate by mutableStateOf("Pick a Service Date")
    var bookingTime by mutableStateOf("")
    var bookingReason by mutableStateOf("")
    var bookingType by mutableStateOf("")
    var closeApp by mutableStateOf(false)
    var intentValue by mutableStateOf(false)
    var openDialogPre by mutableStateOf(false)
    var batteryStatus by mutableStateOf("GOOD")
    var engineStatus by mutableStateOf("GOOD")
    var coolingStatus by mutableStateOf("GOOD")
    var tryeStatus by mutableStateOf("GOOD")

    val mainBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF040A1B).copy(alpha = 1f),
            Color(0xFF040A1B).copy(alpha = 1f),
        )
    )

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


    val buttonBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF000000).copy(alpha = 0f),
            Color(0xFF76ADFF).copy(alpha = 0.2f)
        )
    )

    val serviceButtonBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF255AF5).copy(alpha = 1f),
            Color(0xFF0B1112).copy(alpha = 0.1f)
        )
    )

    val itemsBorderGradient = Brush.verticalGradient(
        listOf(
            Color(0xFFFFFFFF).copy(alpha = 0f),
            Color(0xFFFFFFFF).copy(alpha = 0.1f),
            Color(0xFF032B9D).copy(alpha = 0.4f)
        )
    )

    val tabRowBackGroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF2EA7FF).copy(alpha = 0f),
            Color(0xFF2EA7FF).copy(alpha = 0f),
            Color(0xFF2EA7FF).copy(alpha = 0f),
            Color(0xFF2EA7FF).copy(alpha = 0.1f)
        )
    )

    val transparentGradient = Brush.verticalGradient(
        listOf(
            Color.Transparent,
            Color.Transparent
        )
    )
    init {
        getServiceHistoryResponse(serviceHistoryBookings[0])
        appSettings.writeEstimateToken(
            "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MRMs599lWLRZS6SJISaQNnz0HeGW5GMrl_kEeueD5sc97LXMS0QaTPxZ-QcTci0W_gke7QCQdu8d9ujdaHzNuwdlYomjSzEx_z2TbFqnZH0pnSldowBqxbBIqiuB3V_xECyTBLeUuBdMm46r3lgO9Z7CtznbRRQAmRHEt7zD66detDjViHiSkOzAY0hCtiN1etBAKUJtaMz8eE4vbHQfzuoJZkMVY9a7E5q77ucbsUbDPmURKgzAbIWl2xbKMpfYzuTUmOFhG--f54Y1zCaraJBmsk8CYoI5ynFVYO_ipQZuDaDGhWnAMbP1vdFO-FR-JfOTLOLfEZQzjMAWbpoCdw.cjRyMjKItoioT4Erl2tNkQ.hpHvimvs3kZemNjhx9NioAFudr0vvRiS_GkFHKTImjel5SiC4DUvt1Jj73Xo1tQvRob-IArKTZZjl4ZSNDJGUw.dKDBPuuj40FEhDqOObO8fw"
        )
    }

    fun getServiceHistoryResponse(bookingId: String) {
        gettingReports = true
        mainRepository.getServiceHistoryResponse(
            order_id = bookingId,
            token = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MRMs599lWLRZS6SJISaQNnz0HeGW5GMrl_kEeueD5sc97LXMS0QaTPxZ-QcTci0W_gke7QCQdu8d9ujdaHzNuwdlYomjSzEx_z2TbFqnZH0pnSldowBqxbBIqiuB3V_xECyTBLeUuBdMm46r3lgO9Z7CtznbRRQAmRHEt7zD66detDjViHiSkOzAY0hCtiN1etBAKUJtaMz8eE4vbHQfzuoJZkMVY9a7E5q77ucbsUbDPmURKgzAbIWl2xbKMpfYzuTUmOFhG--f54Y1zCaraJBmsk8CYoI5ynFVYO_ipQZuDaDGhWnAMbP1vdFO-FR-JfOTLOLfEZQzjMAWbpoCdw.cjRyMjKItoioT4Erl2tNkQ.hpHvimvs3kZemNjhx9NioAFudr0vvRiS_GkFHKTImjel5SiC4DUvt1Jj73Xo1tQvRob-IArKTZZjl4ZSNDJGUw.dKDBPuuj40FEhDqOObO8fw"
        ).onEach { dataState ->
            dataState.data?.let { response ->
                serviceHistoryDetail = response
                gettingReports = false
//                getEstimateDetail(bookingId, "Estimates")
            }
            dataState.error?.let {
                Log.d("service", "getServiceHistoryResponse: $it")
                if (it == "Unauthorized request!!") {
                    getRefreshToken()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getRefreshToken() {
        mainRepository.getRefreshToken(
            client_code = "MvxW3k482v2o",
            refresh_token = "5c17Kq2mU70SYXEmmrDA9"
        ).onEach { dataState ->
            dataState.data?.let { response ->
                Log.d("service", "getRefreshToken: ${response.result}")
                appSettings.writeEstimateToken(response.result.token)
            }
            dataState.error?.let {
                Log.d("service", "getServiceHistoryResponse: $it ")

            }
        }.launchIn(viewModelScope)
    }

    fun getInventoryDetail(
        bookingId: String,
        type: String,
    ) {
        mainRepository.getInventoryDetail(
            bookingId,
            type,
            token = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MRMs599lWLRZS6SJISaQNnz0HeGW5GMrl_kEeueD5sc97LXMS0QaTPxZ-QcTci0W_gke7QCQdu8d9ujdaHzNuwdlYomjSzEx_z2TbFqnZH0pnSldowBqxbBIqiuB3V_xECyTBLeUuBdMm46r3lgO9Z7CtznbRRQAmRHEt7zD66detDjViHiSkOzAY0hCtiN1etBAKUJtaMz8eE4vbHQfzuoJZkMVY9a7E5q77ucbsUbDPmURKgzAbIWl2xbKMpfYzuTUmOFhG--f54Y1zCaraJBmsk8CYoI5ynFVYO_ipQZuDaDGhWnAMbP1vdFO-FR-JfOTLOLfEZQzjMAWbpoCdw.cjRyMjKItoioT4Erl2tNkQ.hpHvimvs3kZemNjhx9NioAFudr0vvRiS_GkFHKTImjel5SiC4DUvt1Jj73Xo1tQvRob-IArKTZZjl4ZSNDJGUw.dKDBPuuj40FEhDqOObO8fw"
        ).onEach { dataState ->
            dataState.loading.let { loading ->
                inventoryServiceHistoryLoadingState = loading
            }
            dataState.data?.let { response ->
                inventoryServiceHistoryDetail = response
                gettingReports = false
            }
            dataState.error?.let {
            }
        }.launchIn(viewModelScope)
    }

    fun getEstimateDetail(
        bookingId: String,
        type: String
    ) {
        mainRepository.getEstimateDetail(
            bookingId,
            type,
            token = "eyJhbGciOiJSU0EtT0FFUCIsImVuYyI6IkExMjhDQkMtSFMyNTYifQ.MRMs599lWLRZS6SJISaQNnz0HeGW5GMrl_kEeueD5sc97LXMS0QaTPxZ-QcTci0W_gke7QCQdu8d9ujdaHzNuwdlYomjSzEx_z2TbFqnZH0pnSldowBqxbBIqiuB3V_xECyTBLeUuBdMm46r3lgO9Z7CtznbRRQAmRHEt7zD66detDjViHiSkOzAY0hCtiN1etBAKUJtaMz8eE4vbHQfzuoJZkMVY9a7E5q77ucbsUbDPmURKgzAbIWl2xbKMpfYzuTUmOFhG--f54Y1zCaraJBmsk8CYoI5ynFVYO_ipQZuDaDGhWnAMbP1vdFO-FR-JfOTLOLfEZQzjMAWbpoCdw.cjRyMjKItoioT4Erl2tNkQ.hpHvimvs3kZemNjhx9NioAFudr0vvRiS_GkFHKTImjel5SiC4DUvt1Jj73Xo1tQvRob-IArKTZZjl4ZSNDJGUw.dKDBPuuj40FEhDqOObO8fw"
        ).onEach { dataState ->
            dataState.loading.let { loading ->
                estimateServiceHistoryLoadingState = loading
            }
            dataState.data?.let { response ->
                estimateServiceHistoryDetail = response
                if (response.result.inspectionDetails.checklistType != "Minor checklist") {
                    selectedParameter =
                        response.result.inspectionDetails.engine.parameters.toMutableList()
                }
                getInventoryDetail(bookingId, "Inventory")
            }
            dataState.error?.let {
            }
        }.launchIn(viewModelScope)
    }
}