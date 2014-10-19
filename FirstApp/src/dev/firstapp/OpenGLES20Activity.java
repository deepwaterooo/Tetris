package dev.firstapp;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import android.view.Menu;   // could this be all right?

public class OpenGLES20Activity extends Activity{
    /* 
    //public class AndroidGLDemo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLSurfaceView glView = new GLSurfaceView(this);
        AndroidGLDemoRenderer renderer = new AndroidGLDemoRenderer();
        glView.setRenderer(renderer);
        setContentView(glView);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */

    //  ·½·¨Ò»£º 	
	private GLSurfaceView mGLView;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);
	}
    
}
