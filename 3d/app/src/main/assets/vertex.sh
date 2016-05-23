attribute vec3 vPosition;     // ����λ��, vTexCoordinate
attribute vec2 vTexCoordinate;
uniform mat4 uMVPMatrix;      // �ܱ任����: ������ɫ���������
varying vec2 v_TexCoordinate; // Ƭ����ɫ�����������varying������

void main() {
    //v_TexCoordinate = (uMVPMatrix * vTexCoordinate).xy;
    v_TexCoordinate = vTexCoordinate;

    gl_Position = uMVPMatrix * vec4(vPosition, 1);
}     
