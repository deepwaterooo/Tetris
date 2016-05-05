package dev.ttetris.util;

public class TextureCube extends Texture {
  // ERROR //
  public TextureCube(java.io.InputStream[] paramArrayOfInputStream, TextureParams paramTextureParams)
  {
      super(0, paramTextureParams);  // left for debug
      
    // Byte code:
    //   0: aload_0
    //   1: ldc 9
    //   3: aload_2
    //   4: invokespecial 12	ru/igsoft/anogl/Texture:<init>	(ILru/igsoft/anogl/TextureParams;)V
    //   7: iconst_0
    //   8: istore_3
    //   9: iload_3
    //   10: bipush 6
    //   12: if_icmplt +21 -> 33
    //   15: aload_2
    //   16: getfield 18	ru/igsoft/anogl/TextureParams:minFilter	Lru/igsoft/anogl/TextureMinFilter;
    //   19: invokevirtual 24	ru/igsoft/anogl/TextureMinFilter:needMipMap	()Z
    //   22: ifeq +10 -> 32
    //   25: aload_0
    //   26: getfield 28	ru/igsoft/anogl/TextureCube:target	I
    //   29: invokestatic 34	android/opengl/GLES20:glGenerateMipmap	(I)V
    //   32: return
    //   33: aload_1
    //   34: iload_3
    //   35: aaload
    //   36: astore 4
    //   38: aload 4
    //   40: invokestatic 40	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;)Landroid/graphics/Bitmap;
    //   43: astore 7
    //   45: aload 4
    //   47: invokevirtual 46	java/io/InputStream:close	()V
    //   50: ldc 47
    //   52: iload_3
    //   53: iadd
    //   54: iconst_0
    //   55: aload 7
    //   57: iconst_0
    //   58: invokestatic 53	android/opengl/GLUtils:texImage2D	(IILandroid/graphics/Bitmap;I)V
    //   61: aload 7
    //   63: invokevirtual 58	android/graphics/Bitmap:recycle	()V
    //   66: iinc 3 1
    //   69: goto -60 -> 9
    //   72: astore 5
    //   74: aload 4
    //   76: invokevirtual 46	java/io/InputStream:close	()V
    //   79: aload 5
    //   81: athrow
    //   82: astore 6
    //   84: goto -5 -> 79
    //   87: astore 8
    //   89: goto -39 -> 50
    //
    // Exception table:
    //   from	to	target	type
    //   38	45	72	finally
    //   74	79	82	java/io/IOException
    //   45	50	87	java/io/IOException
  }
}
