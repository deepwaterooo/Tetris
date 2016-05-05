package dev.ttetris.util;

import android.opengl.GLES20;
import java.util.Iterator;
import java.util.List;

public final class FrameBuffer {
    private int bufferId;
    private FrameBufferListener listener;

    public FrameBuffer() {
        int[] arrayOfInt = new int[1];
        GLES20.glGenFramebuffers(1, arrayOfInt, 0);
        this.bufferId = arrayOfInt[0];
    }

    public void attachTexture2D(Texture2D paramTexture2D, TextureAttachment paramTextureAttachment) {
        bind();
        GLES20.glFramebufferTexture2D(36160, paramTextureAttachment.getValue(), paramTexture2D.target, paramTexture2D.textureId, 0);
    }

    public void bind() {
        GLES20.glBindFramebuffer(36160, this.bufferId);
    }

    public void destroy() {
        try {
            if (this.bufferId != 0) {
                int[] arrayOfInt = new int[1];
                arrayOfInt[0] = this.bufferId;
                GLES20.glDeleteFramebuffers(1, arrayOfInt, 0);
                this.bufferId = 0;
            }
            return;
        }
        finally {
            //localObject = finally;
            //throw localObject;
        }
    }

    protected void finalize() {
        destroy();
    }

    public boolean isComplete() {
        bind();
        return GLES20.glCheckFramebufferStatus(36160) == 36053;
    }

    public void render(List<Geometry> paramList, RenderParams paramRenderParams) {
        if (!isComplete());
        while (true) {
            //return;
            bind();
            GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
            GLES20.glClearDepthf(1.0F);
            GLES20.glColorMask(true, true, true, true);
            GLES20.glDepthMask(true);
            GLES20.glClear(16640);
            RenderParams localRenderParams = paramRenderParams.clone();
            if (this.listener != null)
                this.listener.prepareRenderParams(localRenderParams);
            Iterator localIterator = paramList.iterator();
            while (localIterator.hasNext())
                ((Geometry)localIterator.next()).render(localRenderParams);
        }
    }

    public void setListener(FrameBufferListener paramFrameBufferListener) {
        this.listener = paramFrameBufferListener;
    }
}
