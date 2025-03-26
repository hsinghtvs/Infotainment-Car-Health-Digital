package com.mytvs.infotainmentcarhealthdigital.data;

import io.realm.RealmObject;


public class MyDataModelClass extends RealmObject {
    public MyDataModelClass() {
    }
    private String data_new;
    public String getData_new() {
        return data_new;
    }

    public void setData_new(String data_new) {
        this.data_new = data_new;
    }
}
