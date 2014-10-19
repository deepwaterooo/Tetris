package dev.firstapp;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;

public class Ball   {  
    private int slices = 36; //越大越圆滑  
    private int stacks = 24; //同↑  
    private FloatBuffer[] normalsBuffers;  
    private FloatBuffer[] slicesBuffers;  
    private float yAngle;  
    private float zAngle;  
      
    float radius = 1.3f;  
    public Ball()   {  
        slicesBuffers = new FloatBuffer[slices];  
        normalsBuffers = new FloatBuffer[slices];  
        for (int i = 0; i < slices; i++) {  

            float[] vertexCoords = new float[6 * (stacks + 1)];  
            float[] normalCoords = new float[6 * (stacks + 1)];  
  
            double alpha0 = i * (2 * Math.PI) / slices;  
            double alpha1 = (i + 1) * (2 * Math.PI) / slices;  
              
            float cosAlpha0 = (float) Math.cos(alpha0);  
            float sinAlpha0 = (float) Math.sin(alpha0);  
            float cosAlpha1 = (float) Math.cos(alpha1);  
            float sinAlpha1 = (float) Math.sin(alpha1);  
  
            for (int j = 0; j <= stacks; j++)    {     
                double beta = j * Math.PI / stacks - Math.PI / 2;  
                  
                float cosBeta = (float) Math.cos(beta);  
                float sinBeta = (float) Math.sin(beta);  
                  
                setXYZ(vertexCoords, 6 * j,  
                       radius * cosBeta * cosAlpha1,  
                       radius * sinBeta,  
                       radius * cosBeta * sinAlpha1);  
                setXYZ(vertexCoords, 6 * j + 3,  
                       radius * cosBeta * cosAlpha0,  
                       radius * sinBeta,  
                       radius * cosBeta * sinAlpha0);  
                setXYZ(normalCoords, 6 * j,  
                       cosBeta * cosAlpha1,  
                       sinBeta,  
                       cosBeta * sinAlpha1);  
                setXYZ(normalCoords, 6 * j + 3,  
                       cosBeta * cosAlpha0,  
                       sinBeta,  
                       cosBeta * sinAlpha0);  
            }  
            //slicesBuffers[i] = FloatBuffer.wrap(vertexCoords);  
            //normalsBuffers[i] = FloatBuffer.wrap(normalCoords);
            slicesBuffers[i] = bufferUtil(vertexCoords);
            normalsBuffers[i] = bufferUtil(normalCoords);
        }
    }  
      
    public void setXYZ(float[] vector, int offset, float x, float y, float z) {  
        vector[offset] = x;  
        vector[offset + 1] = y;  
        vector[offset + 2] = z;  
    }  
      
    public void draw(GL10 gl)   {  
        gl.glLoadIdentity();  
        gl.glTranslatef(0.0f, 0.0f, -7.0f);  
        gl.glColor4f(1.0f, 0.2f, 0.3f, 1.0f);  
        gl.glRotatef(yAngle, 1.0f, 0.0f, 0.0f);  
        gl.glRotatef(zAngle, 0.0f, 1.0f, 0.0f);  
          
        for (int i = 0; i < slices; i++) {  
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, slicesBuffers[i]);  
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 2 * (stacks + 1));  
        }  
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
    }  
    public void setyAngle(float yAngle)   {  
        this.yAngle = yAngle;  
    }  
    public void setzAngle(float zAngle)   {  
        this.zAngle = zAngle;  
    }

    public FloatBuffer bufferUtil(float []arr){  
        FloatBuffer buffer;
 
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        qbb.order(ByteOrder.nativeOrder());
 
        buffer = qbb.asFloatBuffer();
        buffer.put(arr);
        buffer.position(0);

        return buffer;
    }
}  
