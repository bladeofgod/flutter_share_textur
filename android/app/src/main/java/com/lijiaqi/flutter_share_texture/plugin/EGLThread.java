package com.lijiaqi.flutter_share_texture.plugin;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * @author LiJiaqi
 * @date 2021/2/16
 * Description:
 */
class EGLThread implements Runnable{

    static final String EGLThreadTag = "Egl information";

    final private SurfaceTexture surfaceTexture;
    final private Renderer renderer;

    EGLThread(SurfaceTexture surfaceTexture, Renderer renderer) {
        this.surfaceTexture = surfaceTexture;
        this.renderer = renderer;
    }


    private boolean running = true;

    private EGL10 egl10;
    private EGLDisplay eglDisplay;
    private EGLContext eglContext;
    private EGLSurface eglSurface;
    private int[] config = new int[]{
            EGL10.EGL_RENDERABLE_TYPE, 4,
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_DEPTH_SIZE, 16,
            EGL10.EGL_STENCIL_SIZE, 0,
            EGL10.EGL_SAMPLE_BUFFERS, 1,
            EGL10.EGL_SAMPLES, 4,
            EGL10.EGL_NONE
    };



    public void start(){
        new Thread(this).start();
    }

    public void dispose(){
        running = false;
    }


    @Override
    public void run() {
        initEGL();
        renderer.onCreate();
        Log.i(EGLThreadTag, "init ok");
        while(running){
            long loopStart = System.currentTimeMillis();
            if(renderer.onDraw()){
                //后台绘制的内容 显示在前台
                if(!egl10.eglSwapBuffers(eglDisplay, eglSurface)){
                    Log.i(EGLThreadTag, egl10.eglGetError() + "");
                }
            }
            // fps 2
            long waitDelta = 500 - (System.currentTimeMillis() - loopStart);
            if(waitDelta > 0){
                try {
                    Thread.sleep(waitDelta);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        renderer.onDispose();
        destroyEGL();

    }

    private void initEGL(){
        egl10 = (EGL10) EGLContext.getEGL();
        //获取默认显示设备
        eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if(eglDisplay == EGL10.EGL_NO_DISPLAY){
            throw new RuntimeException("egl10 get display failed!");
        }
        int[] version = new int[2];
        if(!egl10.eglInitialize(eglDisplay, version)){
            throw new RuntimeException("egl initialize failed!");
        }
        EGLConfig eglConfig = chooseEglConfig();
        eglContext = createContext(egl10, eglDisplay, eglConfig);
        eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig,surfaceTexture , null);
        if(eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE){
            throw  new RuntimeException("gl error !");
        }
        //绑定三者
        if(!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)){
            throw new RuntimeException("gl make current error : " + GLUtils.getEGLErrorString(egl10.eglGetError()));
        }

    }

    private void destroyEGL() {
        //解绑
        egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl10.eglDestroySurface(eglDisplay, eglSurface);
        egl10.eglDestroyContext(eglDisplay, eglContext);
        egl10.eglTerminate(eglDisplay);
        Log.i(EGLThreadTag, "egl destroy done!");

    }

    private EGLContext createContext(EGL10 egl10,EGLDisplay eglDisplay,EGLConfig eglConfig){
        int[] attributeList = new int[]{EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
        return egl10.eglCreateContext(eglDisplay, eglConfig, eglContext, attributeList);
    }


    private EGLConfig chooseEglConfig(){
        int[] configCount = new int[1];
        EGLConfig[] configs = new EGLConfig[]{null};
        boolean chooseResult = egl10.eglChooseConfig(eglDisplay, config, configs, 1, configCount);
        if(!chooseResult){
            throw  new RuntimeException("choose config failed");
        }
        return configCount[0] > 0 ? configs[0] : null;
    }

    interface Renderer{
        void onCreate();
        boolean onDraw();
        void onDispose();
    }

}




















