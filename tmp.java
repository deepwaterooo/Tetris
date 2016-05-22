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
    private FloatBuffer textureBuffer;  // ���������������꣬��Ϊ������Ҫ�ں�̨�����ƺã�Ȼ�󲻶ϵ��滻��ǰ����ʾ������ͼ��
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
    private float[] videoTextureTransform; // �������任�������꣬�����庬�������ٽ��ͣ�
    //private boolean frameAvailable = false;
    private Context context;
    private SurfaceTexture videoTexture��    // ����Ƶ������֡��ΪOpengl ES ��Texture  

    // texture ��TextureView��ȡ����SurfaceTexture��Ŀ����Ϊ������EGL ��native window
    public VideoTextureSurfaceRenderer(Context context, SurfaceTexture texture, int width, int height) {
        super(texture, width, height);  //�ȵ��ø���ȥ��EGL��ʼ������
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
    0.0f, 1.0f, 0.0f, 1.0f,  //����
    0.0f, 0.0f, 0.0f, 1.0f,  //����
    1.0f, 0.0f, 0.0f, 1.0f,  //����
    1.0f, 1.0f, 0.0f, 1.0f   //����
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
    drawOrderBuffer = orderByteBuffer.asShortBuffer();  //�����˻���������ͼ����Ϊһ��short������.
    drawOrderBuffer.put(drawOrder);
    drawOrderBuffer.position(0); //��һ��Ҫ������д��Ԫ�ص���������0 ��ʼ

    ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
    bb.order(ByteOrder.nativeOrder());
    vertexBuffer = bb.asFloatBuffer();
    vertexBuffer.put(squareCoords);
    vertexBuffer.position(0);
}
/**���ų�ʼ������*/
private void setupTexture() {
    ByteBuffer texturebb = ByteBuffer.allocateDirect(textureCoords.length * 4);
    texturebb.order(ByteOrder.nativeOrder());
    textureBuffer = texturebb.asFloatBuffer();
    textureBuffer.put(textureCoords);
    textureBuffer.position(0);

    // ��������
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    //�����������textures�����ڴ洢�������ݣ�
    GLES20.glGenTextures(1, textures, 0);
    // ��������(texuture[0]��ʾָ��ָ���������ݵĳ�ʼλ��)
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
    GLES20.glUseProgram(shaderProgram); //����ʱʹ����ɫ����
    int textureParamHandle = GLES20.glGetUniformLocation(shaderProgram, "texture"); //����һ������ɫ�������б�����Ϊ"texture"�����������
    int textureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "vTexCoordinate");
    int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
    int textureTransformHandle = GLES20.glGetUniformLocation(shaderProgram, "textureTransform");
    
    GLES20.glEnableVertexAttribArray(positionHandle); // ����VertexAttribArrayǰ�����ȼ�����    
    //ָ��positionHandle������ֵ������ʲô�ط����ʡ� vertexBuffer���ڲ���NDK���Ǹ�ָ�룬ָ������ĵ�һ��ֵ���ڴ�
    GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);               
    GLES20.glBindTexture(GLES20.GL_TEXTURE0, textures[0]);
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    //ָ��һ����ǰ��textureParamHandle����Ϊһ��ȫ�ֵ�uniform ����
    GLES20.glUniform1i(textureParamHandle, 0);

    GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
    GLES20.glVertexAttribPointer(textureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, textureBuffer);

    GLES20.glUniformMatrix4fv(textureTransformHandle, 1, false, videoTextureTransform, 0);
    //GLES20.GL_TRIANGLES��������С�����е�ģʽ��ȥ���Ƴ��������ͼ��
    GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawOrderBuffer);

    GLES20.glDisableVertexAttribArray(positionHandle);
    GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
}

attribute vec4 vPosition;    //������ɫ�����������attribute������
attribute vec4 vTexCoordinate;
//uniform��ʾһ��������ֵ��Ӧ�ó�������ɫ��ִ��֮ǰָ����������ͼԪ��������в��ᷢ���κα仯��mat4��ʾһ��4x4����
uniform mat4 textureTransform; 
varying vec2 v_TexCoordinate;    //Ƭ����ɫ�����������arying������
void main () {
    v_TexCoordinate = (textureTransform * vTexCoordinate).xy;
    gl_Position = vPosition;
}


/**ʹ��GL_OES_EGL_image_external��չ��������ǿGLSL*/
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES texture; //������չ�ĵ�����ȡ����amplerExternalOES
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
