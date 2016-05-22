public class VideoTextureSurfaceRenderer extends TextureSurfaceRenderer implements SurfaceTexture.OnFrameAvailableListener {
    public static final String TAG = VideoTextureSurfaceRenderer.class.getSimpleName();
    private static float squareSize = 1.0f;
    private static float squareCoords[] = {
        -squareSize,  squareSize, 0.0f,   // top left
        -squareSize, -squareSize, 0.0f,   // bottom left
        squareSize, -squareSize, 0.0f,    // bottom right
        squareSize,  squareSize, 0.0f     // top right
    };
    private static short drawOrder[] = {0, 1, 2,   0, 2, 3};
    private FloatBuffer textureBuffer;  // 用来缓存纹理坐标，因为纹理都是要在后台被绘制好，然后不断的替换最前面显示的纹理图像
    private float textureCoords[] = {
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f
    };
    private int[] textures = new int[1];
    private int shaderProgram;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawOrderBuffer;
    private float[] videoTextureTransform; // 矩阵来变换纹理坐标，（具体含义下面再解释）
    //private boolean frameAvailable = false;
    private Context context;
    private SurfaceTexture videoTexture；    // 从视频流捕获帧作为Opengl ES 的Texture  

    // texture 从TextureView获取到的SurfaceTexture，目的是为了配置EGL 的native window
    public VideoTextureSurfaceRenderer(Context context, SurfaceTexture texture, int width, int height) {
        super(texture, width, height);  //先调用父类去做EGL初始化工作
        this.context = context;
        videoTextureTransform = new float[16];      
    }
}

public static int[] loadTexture(String path) { 
     int[] textureId = new int[1]; 
    GLES20.glGenTextures(1, textureId, 0); // Generate a texture object 
    int[] result = null; 

    if (textureId[0] != 0) { 
        InputStream is = Tools.readFromAsserts(path); 
        Bitmap bitmap; 
        try { 
            bitmap = BitmapFactory.decodeStream(is); 
        } finally { 
            try { 
                is.close(); 
            } catch (IOException e) { 
                throw new RuntimeException("Error loading Bitmap."); 
            } 
        } 
        result = new int[3]; 
        result[TEXTURE_ID] = textureId[0];           // TEXTURE_ID 
        result[TEXTURE_WIDTH] = bitmap.getWidth();   // TEXTURE_WIDTH 
        result[TEXTURE_HEIGHT] = bitmap.getHeight(); // TEXTURE_HEIGHT 
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]); // Bind to the texture in OpenGL 

        // Set filtering 
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR); 
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST); 
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE); 
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE); 

        // Load the bitmap into the bound texture. 
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0); 

        // Recycle the bitmap, since its data has been loaded into OpenGL. 
        bitmap.recycle(); 
    } else { 
        throw new RuntimeException("Error loading texture."); 
    } 
    return result; 
} 

private static float squareSize = 1.0f;
private static float squareCoords[] = {
    -squareSize,  squareSize, 0.0f,   // top left
    -squareSize, -squareSize, 0.0f,   // bottom left
    squareSize, -squareSize, 0.0f,    // bottom right
    squareSize,  squareSize, 0.0f     // top right
};
private float textureCoords[] = {
    0.0f, 1.0f, 0.0f, 1.0f,  //左上
    0.0f, 0.0f, 0.0f, 1.0f,  //左下
    1.0f, 0.0f, 0.0f, 1.0f,  //右下
    1.0f, 1.0f, 0.0f, 1.0f   //右上
};

@Override
protected void initGLComponents() {
    setupVertexBuffer();
    setupTexture();
    loadShaders();
}
private void setupVertexBuffer() {
    ByteBuffer orderByteBuffer = ByteBuffer.allocateDirect(drawOrder. length * 2); 
    orderByteBuffer.order(ByteOrder.nativeOrder());  //Modifies this buffer's byte order
    drawOrderBuffer = orderByteBuffer.asShortBuffer();  //创建此缓冲区的视图，作为一个short缓冲区.
    drawOrderBuffer.put(drawOrder);
    drawOrderBuffer.position(0); //下一个要被读或写的元素的索引，从0 开始

    ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
    bb.order(ByteOrder.nativeOrder());
    vertexBuffer = bb.asFloatBuffer();
    vertexBuffer.put(squareCoords);
    vertexBuffer.position(0);
}
/**接着初始化纹理*/
private void setupTexture() {
    ByteBuffer texturebb = ByteBuffer.allocateDirect(textureCoords.length * 4);
    texturebb.order(ByteOrder.nativeOrder());
    textureBuffer = texturebb.asFloatBuffer();
    textureBuffer.put(textureCoords);
    textureBuffer.position(0);

    // 启用纹理
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    //生成纹理对象textures（用于存储纹理数据）
    GLES20.glGenTextures(1, textures, 0);
    // 将绑定纹理(texuture[0]表示指针指向纹理数据的初始位置)
    GLES20.glBindTexture(GLES11Ext.GL_BLEND_EQUATION_RGB_OES, textures[0]);

    videoTexture = new SurfaceTexture(textures[0]);
    videoTexture.setOnFrameAvailableListener(this);
}

private void loadShaders() {
    final String vertexShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.vetext_sharder);
    final String fragmentShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.fragment_sharder);

    final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
    final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
    shaderProgram = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                                                      new String[]{"texture","vPosition","vTexCoordinate","textureTransform"});
}

private void drawTexture() {
    GLES20.glUseProgram(shaderProgram); //绘制时使用着色程序
    int textureParamHandle = GLES20.glGetUniformLocation(shaderProgram, "texture"); //返回一个于着色器程序中变量名为"texture"相关联的索引
    int textureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "vTexCoordinate");
    int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
    int textureTransformHandle = GLES20.glGetUniformLocation(shaderProgram, "textureTransform");
    
    GLES20.glEnableVertexAttribArray(positionHandle); // 在用VertexAttribArray前必须先激活它    
    //指定positionHandle的数据值可以在什么地方访问。 vertexBuffer在内部（NDK）是个指针，指向数组的第一组值的内存
    GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);               
    GLES20.glBindTexture(GLES20.GL_TEXTURE0, textures[0]);
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    //指定一个当前的textureParamHandle对象为一个全局的uniform 变量
    GLES20.glUniform1i(textureParamHandle, 0);

    GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
    GLES20.glVertexAttribPointer(textureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, textureBuffer);

    GLES20.glUniformMatrix4fv(textureTransformHandle, 1, false, videoTextureTransform, 0);
    //GLES20.GL_TRIANGLES（以无数小三角行的模式）去绘制出这个纹理图像
    GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawOrderBuffer);

    GLES20.glDisableVertexAttribArray(positionHandle);
    GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
}

attribute vec4 vPosition;    //顶点着色器输入变量由attribute来声明
attribute vec4 vTexCoordinate;
//uniform表示一个变量的值由应用程序在着色器执行之前指定，并且在图元处理过程中不会发生任何变化。mat4表示一个4x4矩阵
uniform mat4 textureTransform; 
varying vec2 v_TexCoordinate;    //片段着色器输入变量用arying来声明
void main () {
    v_TexCoordinate = (textureTransform * vTexCoordinate).xy;
    gl_Position = vPosition;
}


/**使用GL_OES_EGL_image_external扩展处理，来增强GLSL*/
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES texture; //定义扩展的的纹理取样器amplerExternalOES
varying vec2 v_TexCoordinate;
void main () {
    vec4 color = texture2D(texture, v_TexCoordinate);
    gl_FragColor = color;
}

@Override
protected boolean draw() {
    synchronized (this) {
        if (frameAvailable) {
            videoTexture .updateTexImage();
            videoTexture .getTransformMatrix(videoTextureTransform);
            frameAvailable = false;
        } else 
            return false;
    }
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    GLES20.glViewport(0, 0, width, height);
    this.drawTexture();
    return true;
}
