package dev.ttetris;

import dev.ttetris.model.Model;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.Rect;  
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.PixelFormat;

import android.view.Window;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;


//public class ActivityGame extends Activity implements OnSurfacePickedListener {
public class ActivityGame extends Activity {
	//private static final String ICICLE_TAG = "simple-tetris";
	//private static final String PREFS_HIGH_SCORES = "high_scores";
    //private static final String DEBUG_TAG = "Gestures";
    private StarGLSurfaceView mGLSurfaceView;
    private MediaPlayer mp;

    private Model model = new Model();
    private boolean flag = false;
    private int counter = 0;
    private int score;
	Paint paint = null;
    private static float mx;
    private static float my;
    private static float mBgnX;
    private static float mBgnY;
    private static final int SWIPE_MIN_DISTANCE = 5;
    //private Model model = new Model(7, 20, 7);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //mGLSurfaceView = new StarGLSurfaceView(this, this);
        mGLSurfaceView = new StarGLSurfaceView(this);
        mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT); 
        setContentView(mGLSurfaceView); 

		mGLSurfaceView.setModel(model);
		//mGLSurfaceView.setActivity(this);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.theme);
        mp.start();
        mp.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        
		if (null != savedInstanceState) 
			onRestoreInstanceState(savedInstanceState);
	}

    @Override 
    protected void onResume() { 
        super.onResume(); 
        mGLSurfaceView.onResume();
    } 
 
    @Override 
    protected void onPause() { 
        super.onPause(); 
        mGLSurfaceView.onPause(); 
        //mp.release();
    } 
 
    private Handler myHandler = new Handler() { 
            @Override 
            public void handleMessage(Message msg) { 
                Toast.makeText(ActivityGame.this, "selected " + msg.what + " surface", Toast.LENGTH_SHORT).show(); 
            } 
        }; 
    /*
    @Override 
    public void onSurfacePicked(int which) { 
        myHandler.sendEmptyMessage(which); 
    }
    */
}
