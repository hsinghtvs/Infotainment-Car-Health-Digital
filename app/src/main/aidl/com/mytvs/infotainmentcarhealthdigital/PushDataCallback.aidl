// PushDataCallback.aidl
package com.mytvs.infotainmentcarhealthdigital;

import com.mytvs.infotainmentcarhealthdigital.CanData;

interface PushDataCallback {

    void sendData(in CanData canData);

}