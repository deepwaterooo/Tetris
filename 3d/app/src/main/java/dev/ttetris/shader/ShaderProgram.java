package dev.ttetris.shader;

import dev.ttetris.util.ShaderHelper;
import dev.ttetris.util.TextResourceReader;
import android.content.Context;
import android.opengl.GLES20;

public class ShaderProgram {
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int framgentShaderResourceId) {
        this.program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                                                 TextResourceReader.readTextFileFromResource(context, framgentShaderResourceId));
    }

    public void useProgram(){
        GLES20.glUseProgram(program);
    }
}
