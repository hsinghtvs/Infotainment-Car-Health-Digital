package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.inventory

data class Result(
    val covid: List<Covid>,
    val dentScratch: List<DentScratch>,
    val fuelLevel: String,
    val inventory: List<Inventory>,
    val inventoryPhotos: List<String>,
    val odometerReading: String,
    val signaturePhotos: List<String>
)