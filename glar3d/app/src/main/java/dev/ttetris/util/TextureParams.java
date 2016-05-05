package dev.ttetris.util;

public class TextureParams {
    public final TextureMagFilter magFilter;
    public final TextureMinFilter minFilter;
    public final TextureWrap wrapS;
    public final TextureWrap wrapT;

    public TextureParams(TextureMinFilter paramTextureMinFilter, TextureMagFilter paramTextureMagFilter, TextureWrap paramTextureWrap1, TextureWrap paramTextureWrap2) {
        this.minFilter = paramTextureMinFilter;
        this.magFilter = paramTextureMagFilter;
        this.wrapS = paramTextureWrap1;
        this.wrapT = paramTextureWrap2;
    }
}
