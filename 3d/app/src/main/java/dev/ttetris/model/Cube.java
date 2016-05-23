package dev.ttetris.model;

import dev.ttetris.model.CubeColor;
import dev.ttetris.util.VertexArray;
import dev.ttetris.shader.TextureShaderProgram;
import java.io.Serializable;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLUtils;
import android.opengl.GLES11Ext;

public class Cube implements Cloneable, Comparable<Cube>, Serializable {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 2;
	public static float[] mVMatrix = new float[16];
	public static float[] mProjMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
    public static float[] mMMatrix = new float[16]; 
    private ShortBuffer drawListBuffer;
    private final float size = 0.5f; 
    private final float one = 1f;
    public float xAngle = 0f;
    private float[] texCoords = new float[] { one, 0, 0, 0, 0, one, one, one,  0, 0, 0, one, one, one, one, 0,
                                              one, one, one, 0, 0, 0, 0, one,  0, one, one, one, one, 0, 0, 0,
                                              0, 0, 0, one, one, one, one, 0,  one, 0, 0, 0, 0, one, one, one}; 
    private CubeColor color; 
    public float [] coords;
    private float x;
    private float y;
    private float z;
    private int colorIdx;
    public CubeColor getColor() { return this.color; }
    public void setX(float paramFloat) { this.x = paramFloat; }
    public void setY(float paramFloat) { this.y = paramFloat; }
    public void setZ(float paramFloat) { this.z = paramFloat; }
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getZ() { return this.z; }

    public Cube(CubeColor paramCubeColor, int paramInt1, int paramInt2, int paramInt3) {
        this.color = paramCubeColor;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        coords = new float[72]; 
        setCoordinates();
        initVertexData(); // preare for drawOrder data
        this.textureArr = new VertexArray(texCoords);
    }
    
    public void setCoordinates() { 
        float [] res = {x-size, y+size, z-size, // 0 
                        x+size, y+size, z-size, // 1
                        x+size, y+size, z+size, // 2
                        x-size, y+size, z+size, // 3
                        x-size, y-size, z-size, // 4
                        x-size, y-size, z+size, // 5
                        x+size, y-size, z+size, // 6
                        x+size, y-size, z-size};
        float [] fin = new float[72];
        int j = 0;
        for (int i = 0; i < drawOrder0.length; i++) {
            fin[j++] = res[drawOrder0[i] * 3];
            fin[j++] = res[drawOrder0[i] * 3 + 1];
            fin[j++] = res[drawOrder0[i] * 3 + 2];
        }
        this.coords = fin;
        this.verticeArr = new VertexArray(this.coords);
    }

    private boolean isActiveFlag; // for activeBlock
    public void setActiveFlag(boolean v) { this.isActiveFlag = v; }
    public boolean getActiveFlag() { return this.isActiveFlag; }

    private static final short drawOrder[] = {0, 1, 3, 2,  4, 5, 7, 6,  8, 9, 11, 10, 12, 13, 15, 14, 16, 17, 19, 18, 20, 21, 23, 22};
    private static final short drawOrder0[] = {4, 7, 6, 5, 0, 3, 2, 1, 3, 5, 6, 2, 0, 1, 7, 4 ,1, 2, 6, 7, 0, 4, 5, 3};
    
    private VertexArray verticeArr;
    private VertexArray textureArr;
    public void draw() { 
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 24);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        this.verticeArr.setVertextAttribPointer(0, textureProgram.getaPositionLocation(), POSITION_COMPONENT_COUNT, POSITION_COMPONENT_COUNT * 4);
        this.textureArr.setVertextAttribPointer(0, textureProgram.getaTextureCoordinatesLocation(), COLOR_COMPONENT_COUNT, COLOR_COMPONENT_COUNT * 4);

		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);           // ³õÊ¼»¯±ä»»¾ØÕó
        if (getActiveFlag()) {
            if (Model.isFrameZRotating[0]) {       // anti-
                Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);
            } else if (Model.isFrameZRotating[1]) { // clock-wise
                Matrix.rotateM(mMMatrix, 0, -xAngle, 0, 0, 1);
            } else if (Model.isFrameXRotating[0]) {                // minor bug here for X rotating
                Matrix.translateM(mMMatrix, 0, 0, -2.5f, -5f);
                Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
                Matrix.translateM(mMMatrix, 0, 0, 2.5f, 5f);
            } else if (Model.isFrameXRotating[1]) {
                Matrix.translateM(mMMatrix, 0, 0, -2.5f, -5f);
                Matrix.rotateM(mMMatrix, 0, -xAngle, 1, 0, 0);
                Matrix.translateM(mMMatrix, 0, 0, 2.5f, 5f);
            }
            Matrix.translateM(mMMatrix, 0, 2f, 2f, 0.5f);
        } else 
            Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);    

        Matrix.translateM(mMMatrix, 0, -0.5f, -0.5f, -0.5f); 
        if (Model.isFrameZRotating[0]) {       // anti-
            Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);
        } else if (Model.isFrameZRotating[1]) { // clock-wise
            Matrix.rotateM(mMMatrix, 0, -xAngle, 0, 0, 1);
        } else if (Model.isFrameXRotating[0]) {
            Matrix.translateM(mMMatrix, 0, 0, -2.5f, -5f);
            Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
            Matrix.translateM(mMMatrix, 0, 0, 2.5f, 5f);
        } else if (Model.isFrameXRotating[1]) {
            Matrix.translateM(mMMatrix, 0, 0, -2.5f, -5f);
            Matrix.rotateM(mMMatrix, 0, -xAngle, 1, 0, 0);
            Matrix.translateM(mMMatrix, 0, 0, 2.5f, 5f);
        }
        Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);    
    }

    public Cube clone() {
        try {
            Cube localCube = (Cube)super.clone();
            return localCube;
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
        }
        return null;
    }
    public int compareTo(Cube paramCube) { return Math.abs(this.x - paramCube.x) < 0.00000001f ? 1 : 0; }    
    public void setColor(CubeColor color) {
        this.color = color;
        switch(color) {
        case Amethyst:    this.colorIdx = 0; return;
        case Anchient:    this.colorIdx = 1; return;
        case Brass:       this.colorIdx = 2; return;
        case LapisLazuli: this.colorIdx = 3; return;
        case Marble:      this.colorIdx = 4; return;
        case MarbleRough: this.colorIdx = 5; return;
        case Oak:         this.colorIdx = 6; return;
        case WhiteMarble: this.colorIdx = 7; return;
        }
    }
    
    public static float[] getFinalMatrix(float [] spec) {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0); // mMMatrix --> spec
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        for (int i = 0; i < 16; i++) 
            System.out.println("mMVPMatrix[i]: " + mMVPMatrix[i]);
		return mMVPMatrix;
	}

    public void initVertexData() {
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
}
