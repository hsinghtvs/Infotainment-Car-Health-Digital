package com.mytvs.mytvs4wvehicleapp.data.model.serviceHistoryResponse.serviceHistory

data class Result(
    val auditTrail: AuditTrail,
    val dmsStatus: Any,
    val fitStatus: String,
    val totalInvoice: Any
)