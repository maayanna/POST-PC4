package com.example.todoboom;

import android.app.Application;

public class MyAwesomeApp extends Application {

    TdLstStore tdLst;

    public void onCreate(){

        super.onCreate();
        tdLst = new TdLstStore(this);

    }

}
