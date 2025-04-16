// PushDataInterface.aidl
package com.mytvs.infotainmentcarhealthdigital;
//Import statements
import com.mytvs.infotainmentcarhealthdigital.PushDataCallback;

interface PushDataInterface {

    void pushData(PushDataCallback pushDataCallback);

}