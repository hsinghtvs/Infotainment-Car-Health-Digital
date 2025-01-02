package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.serviceHistory

data class IntialEstimationPending(
    val detailsMessage: String,
    val isComplete: Boolean,
    val isCurrentStatus: Boolean,
    val name: String,
    val reportKey: String,
    val showDetails: Boolean,
    val timestamp: String
)