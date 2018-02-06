package io.mapwize.app;

import android.content.Context;
import android.os.Handler;

import io.indoorlocation.core.IndoorLocation;
import io.indoorlocation.core.IndoorLocationProvider;
import io.indoorlocation.core.IndoorLocationProviderListener;
import io.indoorlocation.gps.GPSIndoorLocationProvider;

public class MapwizeLocationProvider extends IndoorLocationProvider implements IndoorLocationProviderListener{

    private GPSIndoorLocationProvider mGpsIndoorLocationProvider;
    private boolean mStarted;
    private boolean mLocationLocked;
    private Handler mHandler;

    MapwizeLocationProvider(Context context) {
        super();
        mGpsIndoorLocationProvider = new GPSIndoorLocationProvider(context);
        mGpsIndoorLocationProvider.addListener(this);
    }

    @Override
    public boolean supportsFloor() {
        return true;
    }

    @Override
    public void start() {
        if (!mStarted) {
            mGpsIndoorLocationProvider.start();
            mStarted = true;
        }
    }

    @Override
    public void stop() {
        if (mStarted) {
            mGpsIndoorLocationProvider.stop();
            mStarted = false;
        }
    }

    @Override
    public boolean isStarted() {
        return mStarted;
    }

    void defineLocation(IndoorLocation indoorLocation) {
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
        if (!mLocationLocked) {
            this.dispatchIndoorLocationChange(indoorLocation);
        }
    }
}
