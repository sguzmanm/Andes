package io.mapwize.app;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import io.indoorlocation.core.IndoorLocation;
import io.indoorlocation.core.IndoorLocationProvider;
import io.indoorlocation.core.IndoorLocationProviderListener;
import io.indoorlocation.gps.GPSIndoorLocationProvider;
import io.indoorlocation.providerselector.SelectorIndoorLocationProvider;
import io.indoorlocation.socketlocationprovider.SocketIndoorLocationProvider;

public class MapwizeLocationProvider extends IndoorLocationProvider implements IndoorLocationProviderListener{

    private GPSIndoorLocationProvider mGpsIndoorLocationProvider;
    private SocketIndoorLocationProvider socketIndoorLocationProvider;
    private SelectorIndoorLocationProvider ILSelector;
    private boolean mStarted;
    private boolean mLocationLocked;
    private Handler mHandler;

    MapwizeLocationProvider(Context context) {
        super();
        mGpsIndoorLocationProvider = new GPSIndoorLocationProvider(context);
        socketIndoorLocationProvider = new SocketIndoorLocationProvider(context, "http://192.168.128.11:3003");
        socketIndoorLocationProvider.addListener(this);
        ILSelector = new SelectorIndoorLocationProvider(60*1000);
        ILSelector.addIndoorLocationProvider(mGpsIndoorLocationProvider);
        ILSelector.addIndoorLocationProvider(socketIndoorLocationProvider);
        ILSelector.addListener(this);

    }

    @Override
    public boolean supportsFloor() {
        return true;
    }

    @Override
    public void start() {
        if (!mStarted) {
            socketIndoorLocationProvider.start();
            mStarted = true;
        }
    }

    @Override
    public void stop() {
        if (mStarted) {
            socketIndoorLocationProvider.stop();
            mStarted = false;
        }
    }

    @Override
    public boolean isStarted() {
        return mStarted;
    }

    void defineLocation(IndoorLocation indoorLocation) {
        Log.d("MY TAG","MA1");

        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable,120000);
        mLocationLocked = true;
        this.dispatchIndoorLocationChange(indoorLocation);
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            mLocationLocked = false;
        }
    };

    /*
        GPSIndoorLocationProviderListener
     */

    @Override
    public void onProviderStarted() {

    }

    @Override
    public void onProviderStopped() {

    }

    @Override
    public void onProviderError(Error error) {

    }

    @Override
    public void onIndoorLocationChange(IndoorLocation indoorLocation) {
        Log.d("MY TAG","MA2");
        if (!mLocationLocked) {
            this.dispatchIndoorLocationChange(indoorLocation);
        }
    }
}
