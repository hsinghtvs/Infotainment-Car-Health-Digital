package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates

data class Exterior(
    val imageURL: String,
    val parameters: List<Parameter>,
    val subsystemRating: String
)