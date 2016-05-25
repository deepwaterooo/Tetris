precision mediump float;
uniform sampler2D texture;    // 定义扩展的纹理取样器 sampleExternalOES
varying vec2 v_TexCoordinate; // 片段着色器输入变量用varying来声明

void main() {  
    gl_FragColor = texture2D(texture, v_TexCoordinate);
}
