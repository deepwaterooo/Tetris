package dev.ttetris.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SceneView extends GLSurfaceView {
    private static final TextureParams DEFAULT_TEXTURE_PARAMS = new TextureParams(TextureMinFilter.LINEAR_MIPMAP_LINEAR, TextureMagFilter.LINEAR, TextureWrap.REPEAT, TextureWrap.REPEAT);
    private Scene activeScene;
    private AssetManager assetManager;
    protected Queue<Runnable> postDrawEvents = new LinkedList();
    protected Queue<Runnable> preDrawEvents = new LinkedList();

    public SceneView(Context paramContext) {
        this(paramContext, null);
    }

    public SceneView(Context paramContext, final OnAssetManagerCreated paramOnAssetManagerCreated) {
        super(paramContext);
        if (detectOpenGLES20()) {
            setEGLContextClientVersion(2);
            setRenderer(new GLSurfaceView.Renderer() {
                    public void onDrawFrame(GL10 paramAnonymousGL10) {
                        SceneView.this.executePreDrawEvents();
                        if (SceneView.this.activeScene != null)
                            SceneView.this.activeScene.drawFrame(SceneView.this.assetManager, SceneView.this.getWidth(), SceneView.this.getHeight(), 10);  // problem here
                        while (true) {
                            SceneView.this.executePostDrawEvents();
                            //return;
                            GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
                            GLES20.glColorMask(true, true, true, true);
                            GLES20.glClear(16384);
                        }
                    }

                    public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2) {
                        GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
                    }

                    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig) {
                        SceneView.this.activeScene = null;
                        SceneView.this.assetManager = new AssetManager(SceneView.this.getContext(), SceneView.DEFAULT_TEXTURE_PARAMS);
                        if (paramOnAssetManagerCreated != null)
                            paramOnAssetManagerCreated.onCreated(SceneView.this.assetManager);
                    }
                });
            return;
        }
        setRenderer(new GLSurfaceView.Renderer() {
                public void onDrawFrame(GL10 paramAnonymousGL10) {
                    SceneView.this.executePreDrawEvents();
                    paramAnonymousGL10.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
                    paramAnonymousGL10.glClear(16640);
                    if (SceneView.this.activeScene != null)
                        SceneView.this.activeScene.drawFrame(SceneView.this.assetManager, SceneView.this.getWidth(), SceneView.this.getHeight(), 10);
                    SceneView.this.executePostDrawEvents();
                }

                public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2) {
                    paramAnonymousGL10.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
                }

                public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig) {
                    SceneView.this.activeScene = null;
                    SceneView.this.assetManager = new AssetManager(SceneView.this.getContext(), SceneView.DEFAULT_TEXTURE_PARAMS);
                    if (paramOnAssetManagerCreated != null)
                        paramOnAssetManagerCreated.onCreated(SceneView.this.assetManager);
                }
            });
    }

    private boolean detectOpenGLES20() {
        return ((ActivityManager)getContext().getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 131072;
    }

    private void executePostDrawEvents() {
        synchronized (this.postDrawEvents) {
            if (this.postDrawEvents.peek() == null)
                return;
            ((Runnable)this.postDrawEvents.poll()).run();
        }
    }

    private void executePreDrawEvents() {
        synchronized (this.preDrawEvents) {
            if (this.preDrawEvents.peek() == null)
                return;
            ((Runnable)this.preDrawEvents.poll()).run();
        }
    }

    @Deprecated
    public void queueEvent(Runnable paramRunnable) {}

    public void queuePostDrawEvent(Runnable paramRunnable) {
        synchronized (this.postDrawEvents) {
            this.postDrawEvents.offer(paramRunnable);
            return;
        }
    }

    public void queuePreDrawEvent(Runnable paramRunnable) {
        synchronized (this.preDrawEvents) {
            this.preDrawEvents.offer(paramRunnable);
            return;
        }
    }

    public void setScene(final Scene paramScene) {
        queuePreDrawEvent(new Runnable() {
                public void run() {
                    SceneView.this.activeScene = paramScene;
                }
            });
    }

    public static abstract interface OnAssetManagerCreated {
        public abstract void onCreated(AssetManager paramAssetManager);
    }
}
