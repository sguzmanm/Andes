package com.andes.Mapwize;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;
import com.andes.resources.PixelLocation;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.io.File;
import java.util.HashMap;

import io.mapwize.mapwizeformapbox.MapwizePlugin;

public class MapwizeModule extends ReactContextBaseJavaModule {

    private Mapwize mapwize;
    private File cacheDir;
    private AssetManager assets;

    public MapwizeModule(ReactApplicationContext reactContext, File cacheDir, AssetManager assets){
        super(reactContext);
        this.cacheDir = cacheDir;
        this.assets = assets;
        mapwize = new Mapwize(reactContext, cacheDir, assets);
    }

    @Override
    public String getName() {
        return "Mapwize";
    }
}
