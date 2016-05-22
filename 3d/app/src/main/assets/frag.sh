/**ʹ��GL_OES_EGL_image_external��չ��������ǿGLSL*/
#extension GL_OES_EGL_image_external : require

precision mediump float;
uniform samplerExternalOES texture; // ������չ������ȡ���� sampleExternalOES
varying vec2 v_TexCoordinate;      // Ƭ����ɫ�����������varying������

void main() {  
    vec4 color = texture2D(texture, v_TexCoordinate);
    gl_FragColor = color;
}
