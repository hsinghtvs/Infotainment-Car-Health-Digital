package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.estimates

data class Parameter(
    val parameterName: String,
    val parameterRating: String,
    val ratingReasonCode: String,
    val ratingReasonDesc: String,
    val ratingReasonRemarks: String,
    val imageURL : String
)