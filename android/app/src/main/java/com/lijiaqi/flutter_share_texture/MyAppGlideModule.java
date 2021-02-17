package com.lijiaqi.flutter_share_texture;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

/**
 * @author LiJiaqi
 * @date 2021/2/17
 * Description:
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDiskCache(new DiskLruCacheFactory(context.getExternalCacheDir().getAbsolutePath(),240*1024*1024));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
