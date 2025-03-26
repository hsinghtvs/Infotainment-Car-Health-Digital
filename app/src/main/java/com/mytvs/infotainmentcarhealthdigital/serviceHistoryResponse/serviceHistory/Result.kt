package com.mytvs.infotainmentcarhealthdigital.data.model.serviceHistoryResponse.serviceHistory

data class Result(
    val auditTrail: AuditTrail,
    val dmsStatus: Any,
    val fitStatus: String,
    val totalInvoice: Any
)