package com.andes.Mapwize;

import android.view.View;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class RCTMapViewManager extends SimpleViewManager<MapView> {
    @Override
    public String getName() {
        return "MapView";
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext reactContext) {
        MapView view = new MapView(reactContext);
        /*view.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

            }
        });*/
        return view;
    }
}
