package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates

data class Result(
    val estimationDetails: EstimationDetails,
    val inspectionDetails: InspectionDetails,
    val inspectionPictures: List<Any>,
    val orderDetails: OrderDetails
)