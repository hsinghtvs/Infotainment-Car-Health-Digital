package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates

data class InspectionDetails(
    val battery: Battery,
    val brakes: Brakes,
    val checklistType: String,
    val electrical: Electrical,
    val engine: Engine,
    val exterior: Exterior,
    val interior: Interior,
    val overallRating: String,
    val steering: Steering,
    val suspension: Suspension,
    val tire: Tire,
    val trans: Trans,
    val parameters: List<Parameter>,
)