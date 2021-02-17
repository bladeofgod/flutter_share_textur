package com.lijiaqi.flutter_share_texture;

import android.util.Log;

import androidx.annotation.NonNull;

import com.lijiaqi.flutter_share_texture.plugin.NativePlugin;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterActivity {
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        flutterEngine.getPlugins().add(new NativePlugin(this));
    }
}
