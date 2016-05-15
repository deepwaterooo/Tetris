package dev.ttetris.control;

import dev.ttetris.model.Model;
import dev.ttetris.StarGLSurfaceView;
import dev.ttetris.util.AppConfig;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

public class SwipeControls implements OnTouchListener {
	public SwipeControls(Context _c) { }
	private boolean isMultiTouch = false;
	private Integer fingersCount = 0;
	private float mPreviousX = 0, mPreviousY = 0;
	private float x = 0, y = 0;
	private long time = 0;
    // don't forget threads

	//public void onSwipeRight()  { Model.setCurrentXPositionPos(); }
    public void onSwipeRight()  { // test: 1 finger --> anticlockwize; <-- clockwise
        //Model.setCurrentXPositionPos();
        Model.onSwipeRight();
    }
	public void onSwipeLeft()   { Model.onSwipeLeft(); }
	public void onSwipeTop()    { Model.onSwipeTop(); }
	public void onSwipeBottom() { Model.onSwipeBottom(); }
        
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        AppConfig.setTouchPosition(x, y);

		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
        case MotionEvent.ACTION_DOWN:
            AppConfig.gbNeedPick = false;

            mPreviousX = event.getX();
            mPreviousY = event.getY();
            fingersCount = event.getPointerCount();
            time = System.currentTimeMillis();
            break;
        case MotionEvent.ACTION_MOVE:  // work on this part
            // 经过中心点的手势方向逆时针旋转90°后的坐标 
            float dx = y - mPreviousY; 
            float dy = x - mPreviousX; 
            // 手势距离 
            float d = (float) (Math.sqrt(dx * dx + dy * dy)); // current center: (2.5, 2.5, 5)
            // 旋转轴单位向量的x,y值（z=0） 
            Model.setMfAngleX(dx);
            Model.setMfAngleY(dy);
            Model.setGesDistance(d); // 手势距离 
            AppConfig.gbNeedPick = false;
            break;
        case MotionEvent.ACTION_POINTER_DOWN: 
            isMultiTouch = true;
            fingersCount = event.getPointerCount();
            if (fingersCount == 3)
                //Model.getCurrentObject().rotate('z'); // check activeBlock?   // comment for temp app run
            break;
        case MotionEvent.ACTION_POINTER_UP: 
            // fingersCount = event.getPointerCount();
            break;
        case MotionEvent.ACTION_UP: 
            AppConfig.gbNeedPick = true; 

            if((System.currentTimeMillis() - time) < 90) {
                Model.setDropFast(); // ???
                return true;
            }				
            x = event.getX();
            y = event.getY();
            move(mPreviousX, mPreviousY, x, y, fingersCount);
            isMultiTouch = false;
            fingersCount = 0;
            mPreviousX = x;
            mPreviousY = y;
            break;
        case MotionEvent.ACTION_CANCEL: 
            AppConfig.gbNeedPick = false; 
            break; 
		} // needs better control on mPreviousX mPreviousY
        //mPreviousX = x;
        //mPreviousY = y;
		return true;
	}  

	private void move(float xFirst, float yFirst, float xSecond, float ySecond, int fCount) {
		switch (fCount) {
        case 1:
            switch (detectDirection(xFirst, yFirst, xSecond, ySecond)) {
            case 1: onSwipeRight();  break;
            case 2: onSwipeLeft();   break; 
            case 3: onSwipeBottom(); break;
            case 4: onSwipeTop();    break;
            }
            break;
        case 2:
        case 3: /*
            switch (detectDirection(xFirst, yFirst, xSecond, ySecond)) {
            case 1: Model.getCurrentObject().rotate('x'); break;
            case 2: Model.getCurrentObject().rotate('x'); break;
            case 3: Model.getCurrentObject().rotate('y'); break;
            case 4: Model.getCurrentObject().rotate('y'); break;
            } */
            break;
		}
	}

    // this definition is to bad for user game experience, need to update this one
	public int detectDirection(float xFirst, float yFirst, float xSecond, float ySecond) {
		int rez = 0;
		int limit = 50;
		try {
			float diffY = ySecond - yFirst;
			float diffX = xSecond - xFirst;
			if (Math.abs(diffX) > Math.abs(diffY)) {
				if (Math.abs(diffX) < limit)
					return 0;
				if (diffX > 0) // -->
					return 1;
				else           // <--
					return 2;
			} else {
				if (Math.abs(diffY) < limit)
					return 0;
				if (diffY > 0) 
					return 3;
				else 
					return 4;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return rez;
	}
}
