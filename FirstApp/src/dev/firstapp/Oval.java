package dev.firstapp;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;

public class Oval   {  
    private float[] vertices = new float[720];  
    private FloatBuffer verBuffer ;  
    private float yAngle;  
    private float zAngle;  
    public Oval()   {  
        //初始化圆形数据  
        for (int i = 0; i < 720; i += 2) {  
            // x 坐标  
            vertices[i]   =  (float) (Math.cos(DegToRad(i)) * 1);  
            // y 坐标  
            vertices[i+1] =  (float) (Math.sin(DegToRad(i)) * 1);  
        }     
        //设置圆形顶点数据  
        ByteBuffer qbb = ByteBuffer.allocateDirect(vertices.length * 4);  
        qbb.order(ByteOrder.nativeOrder());  
        verBuffer = qbb.asFloatBuffer();  
        verBuffer.put(vertices);  
        verBuffer.position(0);   
    }  
      
    public float DegToRad(float deg)   {  
        return (float) (3.14159265358979323846 * deg / 180.0);  
    }  
      
    public void draw(GL10 gl)   {  
        //重置投影矩阵  
        gl.glLoadIdentity();  
        // 移动操作，移入屏幕(Z轴)5个像素, x, y , z  
        gl.glTranslatef(0.0f, 0.0f, -5.0f);  
          
        //旋转, angle, x, y , z  
        gl.glRotatef(yAngle, 0.0f, 1.0f, 0.0f);  
        gl.glRotatef(zAngle, 1.0f, 0.0f, 0.0f);  
          
        // 设置当前色为红色, R, G, B, Alpha  
        gl.glColor4f(1.0f, 0.1f, 0.1f, 1.0f);  
          
        //设置顶点类型为浮点坐标    
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verBuffer);  
  
        //打开顶点数组  
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
          
        //向OGL发送实际画图指令  
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 360);  
  
        //画图结束  
        gl.glFinish();  
    }  
  
    public void setyAngle(float yAngle)   {  
        this.yAngle = yAngle;  
    }  
  
    public void setzAngle(float zAngle)   {  
        this.zAngle = zAngle;  
    }  
}  
