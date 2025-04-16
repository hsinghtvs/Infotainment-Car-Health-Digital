package com.mytvs.infotainmentcarhealthdigital.data;

import com.google.gson.annotations.SerializedName


data class DBCParam(

    @SerializedName("key") var key: String,
    @SerializedName("canId") var canId: String,
    @SerializedName("name") var name: String,
    @SerializedName("label") var label: String,
    @SerializedName("startBit") var startBit: Int,
    @SerializedName("bitLength") var bitLength: Int,
    @SerializedName("isLittleEndian") var isLittleEndian: Boolean,
    @SerializedName("isSigned") var isSigned: Boolean,
    @SerializedName("factor") var factor: Float,
    @SerializedName("offset") var offset: Float,
    @SerializedName("dataType") var dataType: String,
    @SerializedName("min") var min: Double? = null,
    @SerializedName("max") var max: Double? = null,
    @SerializedName("choking") var choking: Boolean? = null,
    @SerializedName("visibility") var visibility: Boolean? = null,
    @SerializedName("interval") var interval: Int? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("lineInDbc") var lineInDbc: Int? = null,
    @SerializedName("problems") var problems: ArrayList<String> = arrayListOf()
)