package com.example.infotainment_car_health_digital.api

import com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates.EstimateResponseDTO
import com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.inventory.InventoryDTO
import com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.serviceHistory.ServiceHistoryResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface VehicleDetailApiResponse {

    @GET("service/fleet/timelines")
    suspend fun getServiceHistoryResponse(
        @Header("client_code") client_code: String,
        @Header("token") token: String,
        @Query("order_id") order_id: String
    ): Response<ServiceHistoryResponseDTO>

    @GET("service/fleet/{order_id}/details")
    suspend fun getInventoryDetail(
        @Header("client_code") client_code: String,
        @Header("token") token: String,
        @Path("order_id") order_id: String,
        @Query("type") type: String
    ): Response<InventoryDTO>

    @GET("service/fleet/{order_id}/details")
    suspend fun getEstimateDetail(
        @Header("client_code") client_code: String,
        @Header("token") token: String,
        @Path("order_id") order_id: String,
        @Query("type") type: String
    ): Response<EstimateResponseDTO>
}
