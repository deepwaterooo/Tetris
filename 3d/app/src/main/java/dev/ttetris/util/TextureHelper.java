package dev.ttetris.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureHelper {
    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, int resourceId){
        final int [] textureId = new int[1]; // �������� ID
        GLES20.glGenTextures(1, textureId, 0);

        if (textureId[0] == 0) {
            Log.w("Cube: ", "Could not generate a new OpenGL texture object.");
            return  0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureId, 0);
            return 0;
        }
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]); // ������ ID
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT); // check here if looks better
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        // ����������Դ�                       // 0: ����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0); // last 0: ����߿�ߴ�
        
        bitmap.recycle(); // ������سɹ����ͷ��ڴ��е�����ͼ
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureId[0];
    }
}
