attribute vec3 vPosition;     // ����λ��, vTexCoordinate
attribute vec4 vTexCoordinate;
uniform mat4 uMVPMatrix;      // �ܱ任����: ������ɫ���������
varying vec2 v_TexCoordinate; // Ƭ����ɫ�����������varying������

//attribute vec4 aColor;      // ������ɫ
//varying  vec4 vColor;       // ���ڴ��ݸ�ƬԪ��ɫ���ı���

void main()  {
    v_TexCoordinate = (uMVPMatrix * vTexCoordinate).xy;
    //gl_Position = vPosition;
    gl_Position = vec4(vPosition, 1);
    
//   gl_Position = uMVPMatrix * vec4(vPosition, 1); // �����ܱ任�������˴λ��ƴ˶���λ��
//   vColor = aColor;         // �����յ���ɫ���ݸ�ƬԪ��ɫ��
}     
