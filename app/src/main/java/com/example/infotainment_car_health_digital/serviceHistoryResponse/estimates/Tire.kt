package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates

data class Tire(
    val imageURL: String,
    val parameters: List<Parameter>,
    val subsystemRating: String
)