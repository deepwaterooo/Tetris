/**ʹ��GL_OES_EGL_image_external��չ��������ǿGLSL*/
//#extension GL_OES_EGL_image_external : require

precision mediump float;
//uniform samplerExternalOES texture; // ������չ������ȡ���� sampleExternalOES
uniform sampler2D texture; // ������չ������ȡ���� sampleExternalOES
varying vec2 v_TexCoordinate;       // Ƭ����ɫ�����������varying������

void main() {  
    gl_FragColor = texture2D(texture, v_TexCoordinate);
}
