package dev.firstapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView{
    /* ����һ�� ��Բ/��Բ    */
    MyRenderer myRenderer;
    //GLRender myRenderer;// �Զ������Ⱦ��  
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���  
    private float mPreviousY;// �ϴεĴ���λ��Y����  
    private float mPreviousX;// �ϴεĴ���λ��X����  
    float yAngle = 0;// ��y����ת�ĽǶ�  
    float zAngle = 0;// ��z����ת�ĽǶ�  
  
    public MyGLSurfaceView(Context context)   {  
        super(context);  
        //        myRenderer = new MyRenderer();// ������Ⱦ��
        setEGLContextClientVersion(2);  // added by me here
        setRenderer(new MyRenderer());// ������Ⱦ��  
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        //this.setRenderer(myRenderer);// ������Ⱦ��
        //this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������Ⱦģʽ
    }
    
    /* ��������
    @Override  
    public boolean onTouchEvent(MotionEvent e)   {   // �����¼��Ļص�����  
        float x = e.getX();// �õ�x����  
        float y = e.getY();// �õ�y����  
        switch (e.getAction())   {  
        case MotionEvent.ACTION_MOVE:// ���ر��ƶ�  
            float dy = y - mPreviousY;// ���㴥�ر�Yλ��  
            float dx = x - mPreviousX;// ���㴥�ر�Xλ��  
            yAngle += dx * TOUCH_SCALE_FACTOR;// ������y����ת�Ƕ�  
            zAngle += dy * TOUCH_SCALE_FACTOR;// ������z����ת�Ƕ�  
            myRenderer.setyAngle(yAngle);  
            myRenderer.setzAngle(zAngle);     
            requestRender();// �ػ滭��  
        }  
        mPreviousY = y;// ��¼���ر�λ��  
        mPreviousX = x;// ��¼���ر�λ��  
        return true;// ����true  
    }  
*/
	/*
      public MyGLSurfaceView(Context context){
      super(context);

      setEGLContextClientVersion(2);
      //setEGLConfigChooser(8, 8, 8, 8, 64, 0);  // don't need this one yet;
      setRenderer(new MyRenderer());
      setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
      }
    */
}
