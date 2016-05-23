package dev.ttetris.util;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
	private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = GLES20.glCreateShader(type);
        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) 
                Log.w(TAG, "Counld not create new shader");
            return 0;
        }
        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (LoggerConfig.ON) 
            Log.v(TAG, GLES20.glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId);
            if (LoggerConfig.ON) 
                Log.w(TAG, "Compilation of shader failed");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = GLES20.glCreateProgram();
        if (programObjectId == 0) {
            if (LoggerConfig.ON) 
                Log.w(TAG, "Could not create new Program");
            return 0;
        }
        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);
        GLES20.glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (LoggerConfig.ON) 
            Log.w(TAG, GLES20.glGetProgramInfoLog(programObjectId));
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId);
            if (LoggerConfig.ON) 
                Log.w(TAG, "linking of program failed");
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, GLES20.glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;
        int vertextShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);
        program = linkProgram(vertextShader, fragmentShader);

        if (LoggerConfig.ON)
            validateProgram(program);
        return program;
    }
    
	/** 
	 * Helper function to compile a shader.
	 * 
	 * @param shaderType The shader type.
	 * @param shaderSource The shader source code.
	 * @return An OpenGL handle to the shader.
	 */
	/*public static int compileShader(final int shaderType, final String shaderSource) {
		int shaderHandle = GLES20.glCreateShader(shaderType);
		if (shaderHandle != 0)  {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)  {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }
		if (shaderHandle == 0) 		
            throw new RuntimeException("Error creating shader.");
		return shaderHandle;
        }*/
	
	/**
	 * Helper function to compile and link a program.
	 * 
	 * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
	 * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
	 * @param attributes Attributes that need to be bound to the program.
	 * @return An OpenGL handle to the program.
	 */
	public static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) {
		int programHandle = GLES20.glCreateProgram();
		if (programHandle != 0)  {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);   // Bind the vertex shader to the program.			
            GLES20.glAttachShader(programHandle, fragmentShaderHandle); // Bind the fragment shader to the program.
			
            // Bind attributes
            if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
            }
			
            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)  {				
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
		if (programHandle == 0) 
            throw new RuntimeException("Error creating program.");
		return programHandle;
	}
}
