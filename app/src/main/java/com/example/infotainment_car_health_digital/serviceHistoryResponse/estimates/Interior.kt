package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates

data class Interior(
    val imageURL: String,
    val parameters: List<Parameter>,
    val subsystemRating: String
)