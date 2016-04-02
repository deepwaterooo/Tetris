package dev.ttetris;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.os.Handler;
import android.util.AttributeSet;
import android.os.Bundle;
import javax.microedition.khronos.opengles.GL10;

class StarGLSurfaceView extends GLSurfaceView {
    private static StarGLSurfaceView mStarGLSurfaceView = null;
    private StarRenderer mStarRenderer; // final?

    private static Handler sHandler; // do I need a handler?

	private ActivityGame activity;
    private Model model;
    public int DELAY = 100;
	private long lastMove = 0;
    
    public StarGLSurfaceView(Context context) {
        super(context);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS); // Turn on error-checking and logging
        setEGLContextClientVersion(2);
        initView();
    }

    public StarGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS); // Turn on error-checking and logging
        setEGLContextClientVersion(2);
        initView();
    }

    private void initView() {
        mStarRenderer = new StarRenderer(); //´´½¨äÖÈ¾Æ÷
        setRenderer(mStarRenderer);  //ÉèÖÃäÖÈ¾Æ÷
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mStarGLSurfaceView = this; // do I really need this one?
        setFocusableInTouchMode(true);
        model = new Model();
    }
    
    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable() {
                public void run() {
                }});
        //requestRender(); // I don't think I need this any more
        return true;
    }
    
    public void setModel(Model model) {
		this.model = model;
	}

	public void setActivity(ActivityGame activity) {
		this.activity = activity;
	}

	public void setGameCommand(Model.Move move) {
		if (null == model || !model.isGameActive()) {
			return;
		}
		if (Model.Move.DOWN.equals(move)) {
			//model.genereteNewField(move);   // I will have to rewrite this method
			invalidate();
			return;
		}
		setGameCommandWithDelay(move);
	}

	public void setGameCommandWithDelay(Model.Move move) {
		long now = System.currentTimeMillis();
		if (now - lastMove > DELAY) {
			//model.genereteNewField(move);      // I will have to rewrite this method
			invalidate();
			lastMove = now;
		} 
		//redrawHandler.sleep(DELAY);
	}

	public void endGame() {
		//messageView.setVisibility(View.VISIBLE);
		//storeHighScoresAndLines();
		//messageView.setText(getApplicationContext().getText(R.string.mode_over));
	}

	public void pauseGame() {
		model.setGamePaused();
		//messageView.setVisibility(View.VISIBLE);
		//messageView.setText(getApplicationContext().getText(R.string.mode_pause));
		//storeHighScoresAndLines();
	}
}
