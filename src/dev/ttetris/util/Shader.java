package dev.ttetris.util;

import android.opengl.GLES20;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shader {
    private static final String TAG = "Shader";
    private int program;

    public Shader(InputStream paramInputStream) {
        createProgram(paramInputStream);
    }

    public Shader(String paramString1, String paramString2) {
        createProgram(paramString1, paramString2);
    }

    private void createProgram(InputStream paramInputStream) {
        StringBuilder localStringBuilder1 = new StringBuilder();
        StringBuilder localStringBuilder2 = new StringBuilder();
        StringBuilder localStringBuilder3 = localStringBuilder1;
        try {
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
            while (true) {
                String str = localBufferedReader.readLine();
                if (str == null) {
                    if (localStringBuilder3 == localStringBuilder2)
                        break;
                    throw new RuntimeException("Split marker between vertex and fragment shaders not found (#SplitMarker)");
                }
                if (str.contains("#SplitMarker"))
                    localStringBuilder3 = localStringBuilder2;
                else
                    localStringBuilder3.append(str).append("\n");
            }
        }
        catch (IOException localIOException) {
            throw new RuntimeException("Could not read shader source from stream", localIOException);
        }
        createProgram(localStringBuilder1.toString(), localStringBuilder2.toString());
    }

    private void createProgram(String paramString1, String paramString2) {
        int i = loadShader(35633, paramString1);
        int j = loadShader(35632, paramString2);
        this.program = GLES20.glCreateProgram();
        if (this.program == 0)
            Log.e("Shader", "Could not create program");
        int[] arrayOfInt;
        do {
            //return;
            GLES20.glAttachShader(this.program, i);
            GLES20.glAttachShader(this.program, j);
            GLES20.glLinkProgram(this.program);
            arrayOfInt = new int[1];
            GLES20.glGetProgramiv(this.program, 35714, arrayOfInt, 0);
        }
        while (arrayOfInt[0] == 1);
        Log.e("Shader", "Could not link program: \n" + GLES20.glGetProgramInfoLog(this.program));
    }

    private int loadShader(int paramInt, String paramString) {
        int i = GLES20.glCreateShader(paramInt);
        if (i == 0) {
            Log.e("Shader", "Could not create shader " + paramInt);
            i = 0;
        }
        int[] arrayOfInt;
        do {
            //return i;
            GLES20.glShaderSource(i, paramString);
            GLES20.glCompileShader(i);
            arrayOfInt = new int[1];
            GLES20.glGetShaderiv(i, 35713, arrayOfInt, 0);
        }
        while (arrayOfInt[0] != 0);
        Log.e("Shader", "Could not compile shader " + paramInt + ":\n" + GLES20.glGetShaderInfoLog(i));
        Log.e("Shader", "Shader source:\n" + paramString);
        return i;
    }

    public int getAttributeHandle(String paramString) {
        int i = 0;
        if (this.program == 0)
            i = -1;
        do {
            //return i;
            i = GLES20.glGetAttribLocation(this.program, paramString);
        }
        while (i != -1);
        Log.e("Shader", "Could not get attrib location for " + paramString);
        return i;
    }

    public AttributInfo[] getAttributes() {
        AttributInfo[] arrayOfAttributInfo = null;
        if (this.program == 0)
            arrayOfAttributInfo = new AttributInfo[0];
        while (true) {
            //return arrayOfAttributInfo;
            int[] arrayOfInt1 = new int[1];
            GLES20.glGetProgramiv(this.program, 35721, arrayOfInt1, 0);
            int[] arrayOfInt2 = new int[1];
            int[] arrayOfInt3 = new int[1];
            int[] arrayOfInt4 = new int[1];
            byte[] arrayOfByte = new byte[64];
            arrayOfAttributInfo = new AttributInfo[arrayOfInt1[0]];
            for (int i = 0; i < arrayOfInt1[0]; i++) {
                GLES20.glGetActiveAttrib(this.program, i, arrayOfByte.length, arrayOfInt2, 0, arrayOfInt3, 0, arrayOfInt4, 0, arrayOfByte, 0);
                arrayOfAttributInfo[i] = new AttributInfo(new String(arrayOfByte, 0, arrayOfInt2[0]), arrayOfInt3[0], arrayOfInt4[0]);
            }
        }
    }

    public int getProgram() {
        return this.program;
    }

    public int getUniformHandle(String paramString) {
        int i = 0;
        if (this.program == 0)
            i = -1;
        do {
            //return i;
            i = GLES20.glGetUniformLocation(this.program, paramString);
        }
        while (i != -1);
        Log.e("Shader", "Could not get attrib location for " + paramString);
        return i;
    }

    public UniformInfo[] getUniforms() {
        UniformInfo[] arrayOfUniformInfo = null;
        if (this.program == 0)
            arrayOfUniformInfo = new UniformInfo[0];
        while (true) {
            //return arrayOfUniformInfo;
            int[] arrayOfInt1 = new int[1];
            GLES20.glGetProgramiv(this.program, 35718, arrayOfInt1, 0);
            int[] arrayOfInt2 = new int[1];
            int[] arrayOfInt3 = new int[1];
            int[] arrayOfInt4 = new int[1];
            byte[] arrayOfByte = new byte[64];
            arrayOfUniformInfo = new UniformInfo[arrayOfInt1[0]];
            for (int i = 0; i < arrayOfInt1[0]; i++) {
                GLES20.glGetActiveUniform(this.program, i, arrayOfByte.length, arrayOfInt2, 0, arrayOfInt3, 0, arrayOfInt4, 0, arrayOfByte, 0);
                arrayOfUniformInfo[i] = new UniformInfo(new String(arrayOfByte, 0, arrayOfInt2[0]), arrayOfInt3[0], arrayOfInt4[0]);
            }
        }
    }

    public boolean useProgram() {
        if (this.program == 0)
            return false;
        GLES20.glUseProgram(this.program);
        return true;
    }
}
