package dev.ttetris.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import java.io.IOException;
import java.io.InputStream;

public class Texture2D extends Texture {
    public Texture2D(int paramInt1, int paramInt2, TextureParams paramTextureParams) {
        super(3553, paramTextureParams);
        GLES20.glTexImage2D(this.target, 0, 6407, paramInt1, paramInt2, 0, 6407, 33635, null);
    }

    public Texture2D(InputStream paramInputStream, TextureParams paramTextureParams) {
        super(3553, paramTextureParams);
        try {
            Bitmap localBitmap = BitmapFactory.decodeStream(paramInputStream);
        }
        finally {
            try {
                Bitmap localBitmap = null;
                paramInputStream.close();
                label18: GLUtils.texImage2D(this.target, 0, localBitmap, 0);
                localBitmap.recycle();
                /*
                if (paramTextureParams.minFilter.needMipMap())
                    GLES20.glGenerateMipmap(this.target);
                    return;  */
                IOException localObject = null; // = finally;
                try {
                    paramInputStream.close();
                    label57: throw localObject;
                }
                catch (IOException localIOException1) {
                    //break label57;
                }
            }
            catch (IOException localIOException2) {
                //break label18;
            }
        }
    }
}
