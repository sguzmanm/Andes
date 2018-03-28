package com.andes.Mapwize;

import android.view.View;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import io.mapwize.mapwizeformapbox.MapOptions;
import io.mapwize.mapwizeformapbox.MapwizePlugin;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import static com.mikepenz.materialize.util.UIUtils.convertDpToPixel;

public class RCTMapViewManager extends SimpleViewManager<MapView> {

    private MapboxMap mapboxMap;
    private MapView mapView;
    private MapwizePlugin mapwizePlugin;

    @Override
    public String getName() {
        return "MapView";
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext reactContext) {
        Mapbox.getInstance(reactContext, "pk.eyJ1Ijoic2d1em1hbm0iLCJhIjoiY2pleXB3aW45MDkxZDJxcDZzY3FnaTh2ZCJ9.B7iUjwcIAXVEmjQx6I3iEA");
        MapView view = new MapView(reactContext);
        view.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mMap) {
                final MapOptions opts = new MapOptions.Builder()
                        .build();
                mapboxMap = mMap;
                mapwizePlugin = new MapwizePlugin(mapView, mapboxMap, opts);
               // mapwizePlugin.setPreferredLanguage(Locale.getDefault().getLanguage());
                //mapwizePlugin.setTopPadding((int)convertDpToPixel(TOP_PADDING,MapActivity.this));
                //initMapwizePluginListeners();
                //requestLocationPermission();
                //setupSearchEditTexts();
            }
        });
        /*view.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

            }
        });*/
        return view;
    }
}
