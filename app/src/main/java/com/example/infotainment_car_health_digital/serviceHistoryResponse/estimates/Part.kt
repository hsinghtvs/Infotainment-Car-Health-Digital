package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates

data class Part(
    var approvalStatus: String,
    val description: String,
    val estimateId: String,
    val estimatonType: String,
    val quantity: Int,
    val totalPrice: Double
)