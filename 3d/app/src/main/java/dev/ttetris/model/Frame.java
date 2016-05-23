package dev.ttetris.model;

import dev.ttetris.shader.ColorShaderProgram;
import dev.ttetris.util.VertexArray;
import java.io.Serializable;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Frame {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 4;
    public static float[] mMMatrix = new float[16]; // 具体物体的移动旋转矩阵，旋转、平移
	public static float[] mVMatrix = new float[16];
	public static float[] mProjMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
    private VertexArray vertexArr;
    private VertexArray colorArr;
	private static float vertices[] = { 0.0f, 0.0f, 0.0f, // 0
                                        0.0f, 0.0f, 0.0f, // 1
                                        0.0f, 0.0f, 0.0f, // 0
                                        0.0f, 0.0f, 0.0f, // 2
                                        0.0f, 0.0f, 0.0f, // 0
                                        0.0f, 0.0f, 0.0f};// 3 
	private static float colors[] = { 0.5f, 0.5f, 0.5f, 0.5f,
                                      1f, 0f, 0f, 1f,  // red   x
                                      0.5f, 0.5f, 0.5f, 0.5f,
                                      1f, 1f, 0f, 1f,  // yellow y
                                      0.5f, 0.5f, 0.5f, 0.5f,
                                      0f, 0f, 1f, 1f}; // blue  z
    public float xAngle = 0f;
    
	public Frame() { // default: 5 5 10		
		vertices[3] = 5.0f;
		vertices[11] = 5.0f;
		vertices[17] = 10.0f;
        this.vertexArr = new VertexArray(vertices);
        this.colorArr = new VertexArray(colors);
	}

    public Frame(int n, int h) {
		vertices[3] = n; 
		vertices[11] = n;
		vertices[17] = h;
        this.vertexArr = new VertexArray(vertices);
        this.colorArr = new VertexArray(colors);
	}

    public void bindData(ColorShaderProgram colorProgram) { 
        this.vertexArr.setVertextAttribPointer(0, colorProgram.getaPositionLocation(), POSITION_COMPONENT_COUNT, POSITION_COMPONENT_COUNT * 4);
        this.colorArr.setVertextAttribPointer(0, colorProgram.getaColorLocation(), COLOR_COMPONENT_COUNT, COLOR_COMPONENT_COUNT * 4);

		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);          // 初始化变换矩阵
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
    }

    public void draw() {
        GLES20.glLineWidth(3.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 6); // 6, why it's 12 ?
    }
    
	public static float[] getFinalMatrix() {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}
} 
