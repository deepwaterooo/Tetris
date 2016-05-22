attribute vec3 vPosition;     // 顶点位置, vTexCoordinate
attribute vec4 vTexCoordinate;
uniform mat4 uMVPMatrix;      // 总变换矩阵: 顶点着色器输入变量
varying vec2 v_TexCoordinate; // 片段着色器输入变量用varying来声明

void main() {
    v_TexCoordinate = (uMVPMatrix * vTexCoordinate).xy;
    gl_Position = uMVPMatrix * vec4(vPosition, 1);
}     
