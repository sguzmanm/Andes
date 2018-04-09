package com.andes.Mapwize;

import android.content.res.AssetManager;
import android.view.View;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapwizePackage implements ReactPackage {

    private Mapwize mapwize;

    public MapwizePackage(Mapwize mapwize){
        this.mapwize = mapwize;
    }

    //Logic modules
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        //modules.add(new MapwizeModule(reactContext, cacheDir, assets));
        return modules;
    }

    //UI modules
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        List<ViewManager> views = new ArrayList<>();
        views.add(new RCTMapViewManager(mapwize));
        return views;
    }
}
