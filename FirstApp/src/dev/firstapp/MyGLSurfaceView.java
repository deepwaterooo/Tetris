package dev.firstapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView{
    /* 方法一： 画圆/椭圆    */
    MyRenderer myRenderer;
    //GLRender myRenderer;// 自定义的渲染器  
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// 角度缩放比例  
    private float mPreviousY;// 上次的触控位置Y坐标  
    private float mPreviousX;// 上次的触控位置X坐标  
    float yAngle = 0;// 绕y轴旋转的角度  
    float zAngle = 0;// 绕z轴旋转的角度  
  
    public MyGLSurfaceView(Context context)   {  
        super(context);  
        //        myRenderer = new MyRenderer();// 创建渲染器
        setEGLContextClientVersion(2);  // added by me here
        setRenderer(new MyRenderer());// 设置渲染器  
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        //this.setRenderer(myRenderer);// 设置渲染器
        //this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// 设置渲染模式
    }
    
    /* 方法二：
    @Override  
    public boolean onTouchEvent(MotionEvent e)   {   // 触摸事件的回调方法  
        float x = e.getX();// 得到x坐标  
        float y = e.getY();// 得到y坐标  
        switch (e.getAction())   {  
        case MotionEvent.ACTION_MOVE:// 触控笔移动  
            float dy = y - mPreviousY;// 计算触控笔Y位移  
            float dx = x - mPreviousX;// 计算触控笔X位移  
            yAngle += dx * TOUCH_SCALE_FACTOR;// 设置沿y轴旋转角度  
            zAngle += dy * TOUCH_SCALE_FACTOR;// 设置沿z轴旋转角度  
            myRenderer.setyAngle(yAngle);  
            myRenderer.setzAngle(zAngle);     
            requestRender();// 重绘画面  
        }  
        mPreviousY = y;// 记录触控笔位置  
        mPreviousX = x;// 记录触控笔位置  
        return true;// 返回true  
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
