package dev.ttetris.shader;

import dev.ttetris.R;
import android.content.Context;
import android.opengl.GLES20;

public class ColorShaderProgram extends ShaderProgram{
    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        this.uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        this.aPositionLocation = GLES20.glGetAttribLocation(program,  A_POSITION);
        this.aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
    }

    public void setUniforms(float[] matrix){
        GLES20.glUniformMatrix4fv(uMatrixLocation , 1, false, matrix, 0);
    }

    public int getaPositionLocation() {
        return aPositionLocation;
    }

    public int getaColorLocation() {
        return aColorLocation;
    }
}
