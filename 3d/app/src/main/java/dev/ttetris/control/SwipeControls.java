package dev.ttetris.control;

import dev.ttetris.model.Model;
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
	private float x1 = 0, y1 = 0;
	private float x2 = 0, y2 = 0;
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
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
        case MotionEvent.ACTION_DOWN: 
            x1 = event.getX();
            y1 = event.getY();
            fingersCount = event.getPointerCount();
            time = System.currentTimeMillis();
            break;
        case MotionEvent.ACTION_MOVE: 
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
            if((System.currentTimeMillis() - time) < 90) {
                //Model.setDropFast();    // comment for temp app run, this function is necessary, by setting dropping speed fast?
                return true;
            }				
            // Log.d("Kruno", "Action up1");
            x2 = event.getX();
            y2 = event.getY();
            move(x1, y1, x2, y2, fingersCount);
            isMultiTouch = false;
            fingersCount = 0;
            break;
		}
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
