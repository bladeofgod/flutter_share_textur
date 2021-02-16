package com.lijiaqi.flutter_share_texture.plugin;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.TextureRegistry;

/**
 * @author LiJiaqi
 * @date 2021/2/16
 * Description:
 */
class NativePlugin implements FlutterPlugin , MethodChannel.MethodCallHandler {

    private MethodChannel channel;
    private TextureRegistry textureRegistry;
    private TextureRegistry.SurfaceTextureEntry surfaceTextureEntry;


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {

    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {

    }
}
