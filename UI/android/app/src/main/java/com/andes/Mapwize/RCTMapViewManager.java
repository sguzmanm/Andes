package com.andes.Mapwize;

import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import io.mapwize.mapwizeformapbox.MapOptions;
import io.mapwize.mapwizeformapbox.MapwizePlugin;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.io.File;

import static com.mikepenz.materialize.util.UIUtils.convertDpToPixel;

public class RCTMapViewManager extends SimpleViewManager<MapView> {

    private Mapwize mapwize;

    @Override
    public String getName() {
        return "MapView";
    }

    public RCTMapViewManager(Mapwize mapwize) {
        super();
        this.mapwize = mapwize;
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext reactContext) {
        Log.d("PRUEBA", "Se llamo el CVI");
        String chimbita =  mapwize.mapView.toString();
        Log.d("PRUEBAA", "Peguelo hp: " + chimbita.length() + "  ");

        return mapwize.mapView;
    }
}
