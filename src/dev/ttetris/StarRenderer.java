package dev.ttetris;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import java.nio.ByteBuffer;  
import java.nio.ByteOrder;  
import java.nio.FloatBuffer;  

public class StarRenderer implements GLSurfaceView.Renderer {
    private StarGLSurfaceView mView;

    public StarRenderer() {
        super();
    }
    
    @Override  
    public void onSurfaceCreated(GL10 gl,EGLConfig config){  
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);   
        gl.glShadeModel(GL10.GL_SMOOTH);  
        gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_FASTEST);   
        gl.glClearDepthf(1.0f);  
        gl.glDepthFunc(GL10.GL_LEQUAL);  
        gl.glEnable(GL10.GL_DEPTH_TEST);     
    }  
   
    @Override  
    public void onSurfaceChanged(GL10 gl, int w, int h){  
        gl.glViewport(0, 0, w, h);  
        float ratio;  
        ratio = (float)w/h;  
        gl.glMatrixMode(GL10.GL_PROJECTION);  
        gl.glLoadIdentity();  
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);    
    }  
   
    @Override  
    public void onDrawFrame(GL10 gl){    
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);   
        gl.glMatrixMode(GL10.GL_MODELVIEW);  
        gl.glLoadIdentity();    
        GLU.gluLookAt(gl, 0, 0, 5, 0, 0, 0, 0, 1, 0);  
        gl.glFrontFace(GL10.GL_CCW);  
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
        // 随圆形
        drawEllipse( gl, 32, 3.0f, 1.0f, 0.0f, -4.0f, 0.0f,true );  
        // 圆形
        drawEllipse( gl, 24, 1.5f, 1.5f, 0.0f, 3.0f, 0.0f,false );  
    }  
   
    private void drawEllipse(GL10 gl,int seg,float w,float h,float px,float py,float  
                             pz,boolean filled){  
        gl.glTranslatef(px, py, pz);  
        float vertices[];  
        vertices = new float[seg*2];  
        int count=0;  
        for(float i=0; i<360.0f; i+=(360.0f/seg)){  
            vertices[count++] = (float)Math.cos( degressToRadian(i) )*w;  
            vertices[count++] = (float)Math.sin( degressToRadian(i) )*h;   
        }  
        gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, getFloatBuffer(vertices));  
        gl.glDrawArrays((filled)?GL10.GL_TRIANGLE_FAN:GL10.GL_LINE_LOOP, 0, seg);  
    }  
   
    private double degressToRadian(float deg){  
        double rad = 0;  
        rad = (deg * Math.PI)/180.0f;  
        return rad;  
    }  
   
    private FloatBuffer getFloatBuffer(float[] table ){  
        ByteBuffer bb = ByteBuffer.allocateDirect(table.length * 4);  
        bb.order(ByteOrder.nativeOrder());  
        FloatBuffer fb = bb.asFloatBuffer();  
        fb.put(table);  
        fb.position(0);  
        return fb;  
    }  

    /*
    // 方法一： 圆 与 椭圆 
    public class MyRenderer implements GLSurfaceView.Renderer{
    Oval o = new Oval();  
    Ball b = new Ball();  
    //度到弧度的转换  
  
    @Override  
    public void onDrawFrame(GL10 gl) {  
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);  
    gl.glMatrixMode(GL10.GL_MODELVIEW);  
    gl.glLoadIdentity();          
    gl.glEnable(GL10.GL_CULL_FACE);  
    gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_FALSE);  
    //o.draw(gl);                 <span style="color:#ff0000;">//------画圆</span>  
    b.draw(gl); //<span style="color:#ff0000;">//------画球</span>  
    }  
       
    public void setyAngle(float yAngle)   {  
    b.setyAngle(yAngle);  
    o.setyAngle(yAngle);  
    }  
  
    public void setzAngle(float zAngle)   {  
    b.setzAngle(zAngle);  
    o.setzAngle(zAngle);  
    }  
  
    @Override  
    public void onSurfaceChanged(GL10 gl, int width, int height) {  
    if (height == 0) height = 1;  
    gl.glViewport(0, 0, width, height);  
    gl.glMatrixMode(GL10.GL_PROJECTION);  
    gl.glLoadIdentity();  
    pp  GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 1.0f, 100.0f);  
    }  
  
    @Override  
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {  
    gl.glShadeModel(GL10.GL_SMOOTH);  
    gl.glClearColor(0, 0, 0, 0);  
    gl.glClearDepthf(1.0f);  
    gl.glEnable(GL10.GL_DEPTH_TEST);  
    gl.glDepthFunc(GL10.GL_LEQUAL);  
    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);   
    gl.glCullFace(GL10.GL_BACK);  
    gl.glEnable(GL10.GL_LIGHT0);  
    }
    */
    /*
      public void onSurfaceCreated(GL10 unused, EGLConfig config){
      //set background color
      GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
      }

      public void onDrawFrame(GL10 unused){
      //redraw background color
      GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
      }
	
      public void onSurfaceChanged(GL10 unused, int width, int height){
      GLES20.glViewport(0, 0, width, height);
      }
    */
}
