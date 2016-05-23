package dev.ttetris;

import dev.ttetris.model.Model;
import dev.ttetris.control.SwipeControls;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.Window;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class ActivityGame extends Activity implements OnSurfacePickedListener {
    private StarGLSurfaceView mGLSurfaceView;
    private MediaPlayer mp;
    //private boolean flag = false;
    //private int counter = 0;
    //private int score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mGLSurfaceView = new StarGLSurfaceView(this, this);
        setContentView(mGLSurfaceView);
        
        mGLSurfaceView.setOnTouchListener(new SwipeControls(this));
        Model.init(this);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.theme);
        //mp.start(); // for tmp for quiet
        mp.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
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
    } 
 
    private Handler myHandler = new Handler() { 
            @Override 
            public void handleMessage(Message msg) { 
                Toast.makeText(ActivityGame.this, "selected " + msg.what + " surface", Toast.LENGTH_SHORT).show(); 
            } 
        }; 

    @Override 
    public void onSurfacePicked(int which) { 
        myHandler.sendEmptyMessage(which); 
    }
}
