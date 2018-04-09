package com.andes;

import android.app.Application;
import android.util.Log;

import com.andes.Mapwize.Mapwize;
import com.andes.Mapwize.MapwizePackage;
import com.facebook.react.ReactApplication;
import com.BV.LinearGradient.LinearGradientPackage;
import com.oblador.vectoricons.VectorIconsPackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainApplication extends Application implements ReactApplication {

  private Mapwize mapwize;

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
              new LinearGradientPackage(),
              new VectorIconsPackage(),
              new MapwizePackage(mapwize),
              new AnExamplePackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    this.mapwize = new Mapwize(this, getCacheDir(), getAssets());
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    //this.mapwize = new Mapwize(this, getCacheDir(), getAssets());
  }
}
