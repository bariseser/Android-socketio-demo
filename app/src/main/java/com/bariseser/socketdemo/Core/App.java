package com.bariseser.socketdemo.Core;

import android.app.Application;

import com.bariseser.config_manager.ConfigManager;
import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ConfigManager.getInstance().initialize(this, "settings");

        Fresco.initialize(this);
    }
}
