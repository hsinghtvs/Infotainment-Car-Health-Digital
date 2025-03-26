package com.mytvs.infotainmentcarhealthdigital.data.model.serviceHistoryResponse.estimates

data class Interior(
    val imageURL: String,
    val parameters: List<Parameter>,
    val subsystemRating: String
)