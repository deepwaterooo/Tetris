package dev.firstapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.view.View;

public class ClassWorkView extends View{
	
	public ClassWorkView(Context context){
		super(context);
		
	}
	
	public void onDraw(Canvas canvas){
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.white));
		canvas.drawRect(0, 0, getWidth(), getHeight(),	background);
		
		Path circle = new Path();
		circle.addCircle(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/4, Direction.CW);
		
		Paint fillCircle = new Paint();
		fillCircle.setColor(getResources().getColor(R.color.magenta));
		fillCircle.setStyle(Paint.Style.FILL);
		canvas.drawPath(circle, fillCircle);
	}

}
