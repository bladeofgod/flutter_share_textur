package com.lijiaqi.flutter_share_texture.plugin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lijiaqi.flutter_share_texture.ConstantData;

import java.util.HashMap;
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

    final Activity activity;
    public NativePlugin(Activity activity) {
        this.activity = activity;
    }

    private MethodChannel channel;
    private TextureRegistry textureRegistry;
    private TextureRegistry.SurfaceTextureEntry surfaceTextureEntry;
    private EGLThread eglThread;




    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        Log.i("init ---------", "init");
        channel = new MethodChannel(binding.getBinaryMessenger(), channelName);
        channel.setMethodCallHandler(this);
        textureRegistry = binding.getTextureRegistry();

    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);

    }


    Map<Integer,CustomSimpleTarget> targetBucket = new HashMap<>();

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Log.i("call", "----------");
        Map<String,Integer> args =(Map<String,Integer>) call.arguments;
        switch (call.method) {
            case "create" :
                int width = args.get("width");
                int height = args.get("height");
                surfaceTextureEntry = textureRegistry.createSurfaceTexture();
                SurfaceTexture surfaceTexture = surfaceTextureEntry.surfaceTexture();
                surfaceTexture.setDefaultBufferSize(width, height);
                eglThread = new EGLThread(surfaceTexture, new SimpleRenderer());
                eglThread.start();
                result.success(surfaceTextureEntry.id());
                break;
            case "dispose" :
                eglThread.dispose();
                surfaceTextureEntry.release();
                break;
            case "fetch":
                int itemWidth = args.get("width");
                int itemHeight = args.get("height");
                int itemId = args.get("id");
                final CustomSimpleTarget simpleTarget = new CustomSimpleTarget<Bitmap>(itemId,result);
                targetBucket.put(itemId, simpleTarget);
                Glide.with(activity.getApplicationContext())
                        .asBitmap()
                        .load(ConstantData.imageList.get(itemId))
                        .into(simpleTarget);
                break;
        }

    }

    class CustomSimpleTarget<T> extends  SimpleTarget<T>{
        final MethodChannel.Result result;
        final int itemId;

        CustomSimpleTarget(int itemId,MethodChannel.Result result) {
            this.result = result;
            this.itemId = itemId;
        }

        @Override
        public void onResourceReady(@NonNull T resource, @Nullable Transition<? super T> transition) {

            final Bitmap bitmap = (Bitmap) resource;
            final Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());

            TextureRegistry.SurfaceTextureEntry entry = textureRegistry.createSurfaceTexture();
            long textureId = entry.id();
            SurfaceTexture surfaceTexture = entry.surfaceTexture();
            Surface surface = new Surface(surfaceTexture);
            Canvas canvas = surface.lockCanvas(rect);
            canvas.drawBitmap(bitmap, null,rect,null);
            surface.unlockCanvasAndPost(canvas);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.success(textureId);
                    Glide.with(activity).clear(targetBucket.get(itemId));
                    targetBucket.remove(itemId);
                }
            });

        }

    }


}





















