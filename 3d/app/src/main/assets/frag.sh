precision mediump float;
uniform sampler2D texture;    // ������չ������ȡ���� sampleExternalOES
varying vec2 v_TexCoordinate; // Ƭ����ɫ�����������varying������

void main() {  
    gl_FragColor = texture2D(texture, v_TexCoordinate);
}
