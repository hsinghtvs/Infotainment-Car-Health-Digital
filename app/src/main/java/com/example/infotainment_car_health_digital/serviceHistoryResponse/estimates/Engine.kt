package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates

data class Engine(
    val imageURL: String,
    val parameters: List<Parameter>,
    val subsystemRating: String
)