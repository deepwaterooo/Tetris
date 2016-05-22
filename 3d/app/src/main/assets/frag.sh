/**使用GL_OES_EGL_image_external扩展处理，来增强GLSL*/
#extension GL_OES_EGL_image_external : require

precision mediump float;
uniform samplerExternalOES texture; // 定义扩展的纹理取样器 sampleExternalOES
varying vec2 v_TexCoordinate;      // 片段着色器输入变量用varying来声明

void main() {  
    vec4 color = texture2D(texture, v_TexCoordinate);
    gl_FragColor = color;
}
