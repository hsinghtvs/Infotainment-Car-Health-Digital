package com.mytvs.infotainmentcarhealthdigital.data.model.serviceHistoryResponse.estimates

data class Result(
    val estimationDetails: EstimationDetails,
    val inspectionDetails: InspectionDetails,
    val inspectionPictures: List<Any>,
    val orderDetails: OrderDetails
)