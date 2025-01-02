package com.example.infotainment_car_health_digital.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val context: Context
) : ViewModel() {
    var serviceHistoryBookings = mutableStateListOf<String>("1004080", "938183", "970661", "993647")
    var serviceHistoryDetail by mutableStateOf<ServiceHistoryResponseDTO?>(null)
    var inventoryServiceHistoryDetail by mutableStateOf<InventoryDTO?>(null)
    var inventoryServiceHistoryLoadingState by mutableStateOf(true)
    var estimateServiceHistoryDetail by mutableStateOf<EstimateResponseDTO?>(null)
    var estimateServiceHistoryLoadingState by mutableStateOf(true)
    var inspectionReportsParameters = listOf<Parameter>()

    init {
        getServiceHistoryResponse(serviceHistoryBookings[0])
    }

    fun getServiceHistoryResponse(bookingId: String) {
        mainRepository.getServiceHistoryResponse(bookingId).onEach { dataState ->
            dataState.data?.let { response ->
                serviceHistoryDetail = response
            }
            dataState.error?.let {
                Log.d("service", "getServiceHistoryResponse: $it ")
            }
        }.launchIn(viewModelScope)
    }

    fun getInventoryDetail(
        bookingId: String,
        type: String
    ) {
        mainRepository.getInventoryDetail(bookingId, type).onEach { dataState ->
            dataState.loading.let { loading ->
                inventoryServiceHistoryLoadingState = loading
            }
            dataState.data?.let { response ->
                inventoryServiceHistoryDetail = response
            }
            dataState.error?.let {
            }
        }.launchIn(viewModelScope)
    }

    fun getEstimateDetail(
        bookingId: String,
        type: String
    ) {
        mainRepository.getEstimateDetail(bookingId, type).onEach { dataState ->
            dataState.loading.let { loading ->
                estimateServiceHistoryLoadingState = loading
            }
            dataState.data?.let { response ->
                estimateServiceHistoryDetail = response
            }
            dataState.error?.let {
            }
        }.launchIn(viewModelScope)
    }
}