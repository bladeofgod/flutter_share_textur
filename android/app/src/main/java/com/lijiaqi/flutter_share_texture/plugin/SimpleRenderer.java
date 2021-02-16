package com.lijiaqi.flutter_share_texture.plugin;

import android.opengl.GLES20;

import java.util.Random;

/**
 * @author LiJiaqi
 * @date 2021/2/16
 * Description:
 */
class SimpleRenderer implements EGLThread.Renderer {

    private final Random random = new Random();
    @Override
    public void onCreate() {

    }

    @Override
    public boolean onDraw() {
        GLES20.glClearColor(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        return true;
    }

    @Override
    public void onDispose() {

    }
}
