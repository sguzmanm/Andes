package io.mapwize.app;

import android.app.Application;

import io.mapwize.mapwizeformapbox.AccountManager;


public class MapwizeApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Default of mapwize
        AccountManager.start(this, "cfda71828f4384bd6d6f00f97db6cae6");
        //My own

    }

}
