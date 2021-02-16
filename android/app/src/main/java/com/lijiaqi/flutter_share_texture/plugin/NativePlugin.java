package com.lijiaqi.flutter_share_texture.plugin;

import android.graphics.SurfaceTexture;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.TextureRegistry;

/**
 * @author LiJiaqi
 * @date 2021/2/16
 * Description:
 */
public class NativePlugin implements FlutterPlugin , MethodChannel.MethodCallHandler {

    static private final String channelName = "egl_plugin_alpha";

    private MethodChannel channel;
    private TextureRegistry textureRegistry;
    private TextureRegistry.SurfaceTextureEntry surfaceTextureEntry;
    private EGLThread eglThread;


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), channelName);
        channel.setMethodCallHandler(this);
        textureRegistry = binding.getTextureRegistry();

    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Map<String,Integer> args =(Map<String,Integer>) call.arguments;
        switch (call.method) {
            case "create" -> {
                int width = args.get("width");
                int height = args.get("height");
                surfaceTextureEntry = textureRegistry.createSurfaceTexture();
                SurfaceTexture surfaceTexture = surfaceTextureEntry.surfaceTexture();
                surfaceTexture.setDefaultBufferSize(width, height);
                eglThread = new EGLThread(surfaceTexture, new SimpleRenderer());
                eglThread.start();
                result.success(surfaceTextureEntry.id());
            }
            case "dispose" -> {
                eglThread.dispose();
                surfaceTextureEntry.release();
            }
        }

    }
}





















