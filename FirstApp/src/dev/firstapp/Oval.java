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
        //��ʼ��Բ������  
        for (int i = 0; i < 720; i += 2) {  
            // x ����  
            vertices[i]   =  (float) (Math.cos(DegToRad(i)) * 1);  
            // y ����  
            vertices[i+1] =  (float) (Math.sin(DegToRad(i)) * 1);  
        }     
        //����Բ�ζ�������  
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
        //����ͶӰ����  
        gl.glLoadIdentity();  
        // �ƶ�������������Ļ(Z��)5������, x, y , z  
        gl.glTranslatef(0.0f, 0.0f, -5.0f);  
          
        //��ת, angle, x, y , z  
        gl.glRotatef(yAngle, 0.0f, 1.0f, 0.0f);  
        gl.glRotatef(zAngle, 1.0f, 0.0f, 0.0f);  
          
        // ���õ�ǰɫΪ��ɫ, R, G, B, Alpha  
        gl.glColor4f(1.0f, 0.1f, 0.1f, 1.0f);  
          
        //���ö�������Ϊ��������    
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verBuffer);  
  
        //�򿪶�������  
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
          
        //��OGL����ʵ�ʻ�ͼָ��  
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 360);  
  
        //��ͼ����  
        gl.glFinish();  
    }  
  
    public void setyAngle(float yAngle)   {  
        this.yAngle = yAngle;  
    }  
  
    public void setzAngle(float zAngle)   {  
        this.zAngle = zAngle;  
    }  
}  
