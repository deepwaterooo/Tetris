package dev.ttetris.util;

import dev.ttetris.model.Constant;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VertexArray {
    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData) {
        this.floatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constant.BYTE_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
    }

    public void setVertextAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride){
        this.floatBuffer.position(dataOffset);
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, false, stride, floatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        this.floatBuffer.position(0);
    }
}
