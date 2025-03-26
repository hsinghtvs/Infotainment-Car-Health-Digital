package com.mytvs.infotainmentcarhealthdigital.data.model.serviceHistoryResponse.serviceHistory

data class AuditTrail(
    val customerWalkin: CustomerWalkin? = null,
    val delivered: Delivered? = null,
    val estimation: Estimation? = null,
    val inspectionCompleted: InspectionCompleted?=null,
    val intialEstimationPending: IntialEstimationPending? = null,
    val postInspectionCompleted: PostInspectionCompleted? = null,
    val workInProgress: WorkInProgress? = null
)