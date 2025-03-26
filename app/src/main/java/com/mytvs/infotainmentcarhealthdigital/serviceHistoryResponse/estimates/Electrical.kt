package com.mytvs.infotainmentcarhealthdigital.data.model.serviceHistoryResponse.estimates

data class Electrical(
    val imageURL: String,
    val parameters: List<Parameter>,
    val subsystemRating: String
)