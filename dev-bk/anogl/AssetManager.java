package dev.anogl;

import android.content.Context;
import android.content.res.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import dev.anogl.utils.MeshLoader;
import dev.anogl.utils.MeshPlyLoader;

public class AssetManager {
    private static final String[] CUBEMAP_FACES = { "RT", "LF", "UP", "DN", "FR", "BK" };
    private static final String CUBEMAP_FACE_PATTERN = ".*_(RT|LF|UP|DN|FR|BK)(\\.\\w*)?$";
    private static final TextureParams CUBEMAP_TEXTURE_PARAMS = new TextureParams(TextureMinFilter.LINEAR, TextureMagFilter.LINEAR, TextureWrap.CLAMP_TO_EDGE, TextureWrap.CLAMP_TO_EDGE);
    private static final String TAG = "AssetManager";
    private static final boolean WRITE_LOG;
    private Context context;
    private TextureParams defaultTextureParams;
    private Map<String, Material> materials = new HashMap();
    private Map<String, MeshLoader> meshLoaders = new HashMap();
    private Map<String, Mesh> meshes = new HashMap();
    private Map<String, Shader> shadersByFilePath = new HashMap();
    private Map<Integer, Shader> shadersById = new HashMap();
    private Map<String, Texture> texturesByFilePath = new HashMap();
    private Map<Integer, Texture> texturesById = new HashMap();

    public AssetManager(Context paramContext, TextureParams paramTextureParams) {
        this.context = paramContext;
        this.defaultTextureParams = paramTextureParams;
        this.meshLoaders.put("ply", new MeshPlyLoader());
    }

    private String getCubemapFileByFace(String paramString) {
        String[] arrayOfString1 = splitFilename(paramString);
        String[] arrayOfString2 = CUBEMAP_FACES;
        int i = arrayOfString2.length;
        for (int j = 0; ; j++) {
            String str3;
            if (j >= i)
                str3 = paramString;
            do {
                return str3;
                String str1 = arrayOfString2[j];
                String str2 = "_" + str1;
                if (!arrayOfString1[0].endsWith(str2))
                    break;
                str3 = arrayOfString1[0].substring(0, arrayOfString1[0].length() - str2.length());
            }
            while (arrayOfString1[1] == null);
            return str3 + arrayOfString1[1];
        }
    }

    private boolean isCubemapFaceFile(String paramString) {
        return paramString.matches(".*_(RT|LF|UP|DN|FR|BK)(\\.\\w*)?$");
    }

    private Mesh loadMesh(String paramString) {
        String[] arrayOfString = splitFilename(paramString);
        if (arrayOfString[1] != null) {
            MeshLoader localMeshLoader = (MeshLoader)this.meshLoaders.get(arrayOfString[1].substring(1));
            if (localMeshLoader != null)
                try {
                    Mesh localMesh = localMeshLoader.loadMesh(this.context.getAssets().open(paramString));
                    return localMesh;
                }
                catch (IOException localIOException) {
                    throw new RuntimeException("Cannot load mesh file: " + paramString);
                }
        }
        return null;
    }

    private String[] splitFilename(String paramString) {
        String str1 = paramString;
        int i = paramString.lastIndexOf('.');
        String str2 = null;
        if (i != -1) {
            str2 = paramString.substring(i);
            if (!str2.contains("/"))
                break label51;
            str2 = null;
        }
        while (true) {
            return new String[] { str1, str2 };
            label51: str1 = paramString.substring(0, i);
        }
    }

    public Texture getCubeTexture(String paramString) {
        Object localObject = (Texture)this.texturesByFilePath.get(paramString);
        String[] arrayOfString;
        InputStream[] arrayOfInputStream;
        int i;
        if (localObject == null) {
            arrayOfString = splitFilename(paramString);
            arrayOfInputStream = new InputStream[6];
            i = 0;
        }
        String str;
        while (true) {
            if (i >= arrayOfInputStream.length) {
                localObject = new TextureCube(arrayOfInputStream, CUBEMAP_TEXTURE_PARAMS);
                this.texturesByFilePath.put(paramString, localObject);
                return localObject;
            }
            str = arrayOfString[0] + "_" + CUBEMAP_FACES[i];
            if (arrayOfString[1] != null)
                str = str + arrayOfString[1];
            try {
                arrayOfInputStream[i] = this.context.getAssets().open(str);
                i++;
            }
            catch (IOException localIOException) {
            }
        }
        throw new RuntimeException("Cannot open cubemap face texture file: " + str);
    }

    public Material getMaterial(String paramString) {
        return (Material)this.materials.get(paramString);
    }

    public Mesh getMesh(String paramString) {
        Mesh localMesh = (Mesh)this.meshes.get(paramString);
        if (localMesh == null) {
            localMesh = loadMesh(paramString);
            if (localMesh != null)
                this.meshes.put(paramString, localMesh);
        }
        return localMesh;
    }

    public Shader getShader(int paramInt) {
        Shader localShader = (Shader)this.shadersById.get(Integer.valueOf(paramInt));
        if (localShader == null) {
            localShader = new Shader(this.context.getResources().openRawResource(paramInt));
            this.shadersById.put(Integer.valueOf(paramInt), localShader);
        }
        return localShader;
    }

    // ERROR //
    public Shader getShader(String paramString) {
        // Byte code:
        //   0: aload_0
        //   1: getfield 86	ru/igsoft/anogl/AssetManager:shadersByFilePath	Ljava/util/Map;
        //   4: aload_1
        //   5: invokeinterface 165 2 0
        //   10: checkcast 228	ru/igsoft/anogl/Shader
        //   13: astore_2
        //   14: aload_2
        //   15: ifnonnull +36 -> 51
        //   18: new 228	ru/igsoft/anogl/Shader
        //   21: dup
        //   22: aload_0
        //   23: getfield 100	ru/igsoft/anogl/AssetManager:context	Landroid/content/Context;
        //   26: invokevirtual 173	android/content/Context:getAssets	()Landroid/content/res/AssetManager;
        //   29: aload_1
        //   30: invokevirtual 179	android/content/res/AssetManager:open	(Ljava/lang/String;)Ljava/io/InputStream;
        //   33: invokespecial 241	ru/igsoft/anogl/Shader:<init>	(Ljava/io/InputStream;)V
        //   36: astore_3
        //   37: aload_0
        //   38: getfield 86	ru/igsoft/anogl/AssetManager:shadersByFilePath	Ljava/util/Map;
        //   41: aload_1
        //   42: aload_3
        //   43: invokeinterface 113 3 0
        //   48: pop
        //   49: aload_3
        //   50: astore_2
        //   51: aload_2
        //   52: areturn
        //   53: astore 6
        //   55: new 184	java/lang/RuntimeException
        //   58: dup
        //   59: new 121	java/lang/StringBuilder
        //   62: dup
        //   63: ldc 244
        //   65: invokespecial 126	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
        //   68: aload_1
        //   69: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   72: invokevirtual 134	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   75: invokespecial 187	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
        //   78: athrow
        //   79: astore 4
        //   81: goto -26 -> 55
        //
        // Exception table:
        //   from	to	target	type
        //   18	37	53	java/io/IOException
        //   37	49	79	java/io/IOException
    }

    public Texture getTexture(int paramInt) {
        return getTexture(paramInt, this.defaultTextureParams);
    }

    public Texture getTexture(int paramInt, TextureParams paramTextureParams) {
        Object localObject = (Texture)this.texturesById.get(Integer.valueOf(paramInt));
        if (localObject == null) {
            localObject = new Texture2D(this.context.getResources().openRawResource(paramInt), paramTextureParams);
            this.texturesById.put(Integer.valueOf(paramInt), localObject);
        }
        return localObject;
    }

    public Texture getTexture(String paramString) {
        return getTexture(paramString, this.defaultTextureParams);
    }

    // ERROR //
    public Texture getTexture(String paramString, TextureParams paramTextureParams) {
        // Byte code:
        //   0: aload_0
        //   1: getfield 90	ru/igsoft/anogl/AssetManager:texturesByFilePath	Ljava/util/Map;
        //   4: aload_1
        //   5: invokeinterface 165 2 0
        //   10: checkcast 201	ru/igsoft/anogl/Texture
        //   13: astore_3
        //   14: aload_3
        //   15: ifnonnull +40 -> 55
        //   18: new 251	ru/igsoft/anogl/Texture2D
        //   21: dup
        //   22: aload_0
        //   23: getfield 100	ru/igsoft/anogl/AssetManager:context	Landroid/content/Context;
        //   26: invokevirtual 173	android/content/Context:getAssets	()Landroid/content/res/AssetManager;
        //   29: aload_1
        //   30: invokevirtual 179	android/content/res/AssetManager:open	(Ljava/lang/String;)Ljava/io/InputStream;
        //   33: aload_2
        //   34: invokespecial 254	ru/igsoft/anogl/Texture2D:<init>	(Ljava/io/InputStream;Lru/igsoft/anogl/TextureParams;)V
        //   37: astore 4
        //   39: aload_0
        //   40: getfield 90	ru/igsoft/anogl/AssetManager:texturesByFilePath	Ljava/util/Map;
        //   43: aload_1
        //   44: aload 4
        //   46: invokeinterface 113 3 0
        //   51: pop
        //   52: aload 4
        //   54: astore_3
        //   55: aload_3
        //   56: areturn
        //   57: astore 7
        //   59: new 184	java/lang/RuntimeException
        //   62: dup
        //   63: new 121	java/lang/StringBuilder
        //   66: dup
        //   67: ldc_w 259
        //   70: invokespecial 126	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
        //   73: aload_1
        //   74: invokevirtual 130	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   77: invokevirtual 134	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   80: invokespecial 187	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
        //   83: athrow
        //   84: astore 5
        //   86: goto -27 -> 59
        //
        // Exception table:
        //   from	to	target	type
        //   18	39	57	java/io/IOException
        //   39	52	84	java/io/IOException
    }

    public void loadMeshes(String paramString) {
        int i;
        int j;
        do
            try {
                String[] arrayOfString = this.context.getAssets().list(paramString);
                i = arrayOfString.length;
                j = 0;
                continue;
                String str = arrayOfString[j];
                getMesh(paramString + "/" + str);
                j++;
            }
            catch (IOException localIOException) {
                throw new RuntimeException("Cannot load meshes from dir: " + paramString, localIOException);
            }
        while (j < i);
    }

    public void loadShaders(String paramString) {
        int i;
        int j;
        do
            try {
                String[] arrayOfString = this.context.getAssets().list(paramString);
                i = arrayOfString.length;
                j = 0;
                continue;
                String str = arrayOfString[j];
                getShader(paramString + "/" + str);
                j++;
            }
            catch (IOException localIOException) {
                throw new RuntimeException("Cannot load shaders from dir: " + paramString, localIOException);
            }
        while (j < i);
    }

    public void loadTextures(String paramString) {
        loadTextures(paramString, this.defaultTextureParams);
    }

    public void loadTextures(String paramString, TextureParams paramTextureParams) {
        while (true) {
            int j;
            Iterator localIterator;
            try {
                HashSet localHashSet = new HashSet();
                String[] arrayOfString = this.context.getAssets().list(paramString);
                int i = arrayOfString.length;
                j = 0;
                if (j >= i) {
                    localIterator = localHashSet.iterator();
                    if (localIterator.hasNext());
                }
                else {
                    String str = arrayOfString[j];
                    if (isCubemapFaceFile(str))
                        localHashSet.add(paramString + "/" + getCubemapFileByFace(str));
                    else
                        getTexture(paramString + "/" + str);
                }
            }
            catch (IOException localIOException) {
                throw new RuntimeException("Cannot load textures from dir: " + paramString, localIOException);
            }
            getCubeTexture((String)localIterator.next());
            continue;
            j++;
        }
    }

    public void putMaterial(String paramString, Material paramMaterial) {
        this.materials.put(paramString, paramMaterial);
    }

    public void putMesh(String paramString, Mesh paramMesh) {
        this.meshes.put(paramString, paramMesh);
    }
}
