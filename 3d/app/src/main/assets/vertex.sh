attribute vec3 vPosition;     // 顶点位置, vTexCoordinate
attribute vec4 vTexCoordinate;
uniform mat4 uMVPMatrix;      // 总变换矩阵: 顶点着色器输入变量
varying vec2 v_TexCoordinate; // 片段着色器输入变量用varying来声明

//attribute vec4 aColor;      // 顶点颜色
//varying  vec4 vColor;       // 用于传递给片元着色器的变量

void main()  {
    v_TexCoordinate = (uMVPMatrix * vTexCoordinate).xy;
    //gl_Position = vPosition;
    gl_Position = vec4(vPosition, 1);
    
//   gl_Position = uMVPMatrix * vec4(vPosition, 1); // 根据总变换矩阵计算此次绘制此顶点位置
//   vColor = aColor;         // 将接收的颜色传递给片元着色器
}     
