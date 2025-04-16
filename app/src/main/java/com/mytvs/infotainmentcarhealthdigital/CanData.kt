package com.mytvs.infotainmentcarhealthdigital

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class CanData(val _canMap: Map<String, String>) : Parcelable {

    private var canMap: Map<String, String> = mutableMapOf();

    private companion object : Parceler<CanData> {
        override fun create(parcel: Parcel): CanData {
            var localCanMap: Map<String, String> = mutableMapOf();
            parcel.writeMap(localCanMap);
            return CanData(localCanMap);
        }

        override fun CanData.write(dest: Parcel, flags: Int) {
            dest.writeMap(canMap);
        }

    }

    fun setCanMap(canValueMap: Map<String, String>) {
        canMap = canValueMap;
    }

    fun getCanMap(): Map<String, String> {
        return canMap;
    }

}