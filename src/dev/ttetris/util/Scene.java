package dev.ttetris.util;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Scene {
    //private static final float DEFAULT_FPS = 30.0F;
    //private static final String TAG = "Scene";
    protected List<FrameBuffer> frameBuffers = new ArrayList();
    protected Queue<Runnable> events = new LinkedList();
    protected GeometryComparator geometryComparator = new GeometryComparator();
    protected List<Geometry> traversedGeometries = new ArrayList();
    protected RenderParams renderParams;
    protected float previousFps;
    protected float currentFps;
    protected float averageFps;
    protected float fov = 45.0F;  // Frustr
    protected float aspectRatio = 1.5F;
    protected float near = 1.0F;
    protected float far = 100.0F;

    protected int height; // y
    protected int width;  // x
    protected int depth;  // z
    protected Node rootNode;
    protected boolean created;
    protected boolean logFps;
    protected long lastTime;
    protected long updateInterval;
    protected float[] worldMatrix = new float[16];

    public Scene() { setFps(30.0F); }
    public void addFrameBuffer(FrameBuffer paramFrameBuffer) { this.frameBuffers.add(paramFrameBuffer); }
    public float getAverageFps() { return this.averageFps; }
    public float getCurrentFps() { return this.currentFps; }
    protected void onCreate(AssetManager paramAssetManager) {}
    protected void onResize() {}
    protected void onUpdate(float paramFloat) {}
    
    public void drawFrame(AssetManager paramAssetManager, int paramInt1, int paramInt2, int paramInt3) {
        if (!this.created) {
            this.rootNode = new Node();
            this.renderParams = new RenderParams();
            //this.renderParams.setLookAt(0.0F, 0.0F, 10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
            this.renderParams.setLookAt(4.8f, 2.2f, 4.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f); 
            Matrix.setIdentityM(this.worldMatrix, 0);
            onCreate(paramAssetManager);  
            this.created = true;
        }
        // 1 width COL z; 2 height HEIGHT y; 3 row ROW z;
        if ((this.width != paramInt1) || (this.height != paramInt2) || this.depth != paramInt3) {
            this.width = paramInt1; // x
            this.depth = paramInt2; // z
            this.height = paramInt3;// y
            this.aspectRatio = (float)(paramInt1 / paramInt2);
            updateFrustrum();
            onResize();
        }
        long l1 = SystemClock.uptimeMillis();
        if ((this.lastTime == 0L) || (l1 < this.lastTime))
            this.lastTime = l1;
        long l2 = l1 - this.lastTime;
        float f1 = 0f;
        if (l2 >= this.updateInterval) {
            this.lastTime = l1;
            f1 = 0.001F * (float)l2;
            //if (l2 != 0L);  //break label314;
            for (float f2 = 0.0F; ; f2 = 1.0F / f1) {
                this.currentFps = f2;
                this.averageFps = (0.5F * (this.currentFps + this.previousFps));
                this.previousFps = this.currentFps;
                if (this.logFps)
                    Log.v("Scene", "averageFps=" + this.averageFps + ", currentFps=" + this.currentFps);
                executeEvents();
                onUpdate(f1);
                this.rootNode.update(f1);
                render();
                int i = GLES20.glGetError();
                if (i != 0)
                    Log.e("Scene", ErrorInfo.getDesc(i));
                //return;
                executeEvents();
                Thread.yield();
                l1 = SystemClock.uptimeMillis();
                break;
            } 
            
        }/*
        label314: for (float f2 = 0.0F; ; f2 = 1.0F / f1) {
            this.currentFps = f2;
            this.averageFps = (0.5F * (this.currentFps + this.previousFps));
            this.previousFps = this.currentFps;
            if (this.logFps)
                Log.v("Scene", "averageFps=" + this.averageFps + ", currentFps=" + this.currentFps);
            executeEvents();
            onUpdate(f1);
            this.rootNode.update(f1);
            render();
            int i = GLES20.glGetError();
            if (i != 0)
                Log.e("Scene", ErrorInfo.getDesc(i));
            //return;
            executeEvents();
            Thread.yield();
            l1 = SystemClock.uptimeMillis();
            break;
            } */
    }

    protected void executeEvents() {
        synchronized (this.events) {
            if (this.events.peek() == null)
                return;
            ((Runnable)this.events.poll()).run();
        }
    }

    protected void queueEvent(Runnable paramRunnable) {
        synchronized (this.events) {
            this.events.offer(paramRunnable);
            return;
        }
    }

    public void removeFrameBuffer(FrameBuffer paramFrameBuffer) {
        this.frameBuffers.remove(paramFrameBuffer);
    }

    protected void render() {
        if (!this.created)
            throw new RuntimeException("Scene not created");
        this.traversedGeometries.clear();
        this.rootNode.traverseSceneGraph(this.traversedGeometries, this.worldMatrix);
        Collections.sort(this.traversedGeometries, this.geometryComparator);
        this.renderParams.update();
        Iterator localIterator1 = this.frameBuffers.iterator();
        Iterator localIterator2 = null;
        if (!localIterator1.hasNext()) {
            GLES20.glBindFramebuffer(36160, 0);
            GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
            GLES20.glClearDepthf(1.0F);
            GLES20.glColorMask(true, true, true, true);
            GLES20.glDepthMask(true);
            GLES20.glClear(16640);
            localIterator2 = this.traversedGeometries.iterator();
        }
        while (true) {
            if (!localIterator2.hasNext()) {
                //return;
                ((FrameBuffer)localIterator1.next()).render(this.traversedGeometries, this.renderParams);
                break;
            }
            ((Geometry)localIterator2.next()).render(this.renderParams);
        }
    }

    public void setFps(float paramFloat) {
        this.updateInterval = Math.round(1000.0F / paramFloat);
    }

    protected void setFrustrum(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.fov = paramFloat1;
        this.near = paramFloat2;
        this.far = paramFloat3;
        updateFrustrum();
    }

    public void setLogFps(boolean paramBoolean) {
        this.logFps = paramBoolean;
    }

    protected void updateFrustrum() {
        this.renderParams.makeFrustum(this.fov, this.aspectRatio, this.near, this.far);
    }
}
