package dev.ttetris;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;  
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageButton;

public class ActivityGame extends Activity {
    //private Integer OFFSET = 10;
    //private Integer TOP_OFFSET = 40;
    //private SurfaceHolder sHolder = null;
    private  StarSurfaceView view = null;
    private Model model = new Model();
    private boolean flag = false;
    private int counter = 0;
    private int score;
	Paint paint = null;
    
	private static final String ICICLE_TAG = "simple-tetris";
	private static final String PREFS_HIGH_SCORES = "high_scores";
	//private int highScores = 0;
    private static final String DEBUG_TAG = "Gestures";
    //private GestureDetectorCompat mDetector;
    private static float mx;
    private static float my;
    private static float mBgnX;
    private static float mBgnY;
    private static final int SWIPE_MIN_DISTANCE = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new StarSurfaceView(this);
        setContentView(view);
		view.setModel(model);
		view.setActivity(this);
        
		// Restore the state:
		if (null != savedInstanceState) {
			onRestoreInstanceState(savedInstanceState);
		}
	}
    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //int action = MotionEventCompat.getActionMasked(event);
        mx = event.getX();
        my = event.getY();
        if (model.isGameOver() || model.isGameBeforeStart()) {
            startNewGame();
            return true;
        } else if (model.isGameActive()) {
            Dimension cellSize = view.getCellSize();
            int width = cellSize.getWidth();
            int height = cellSize.getHeight();
            int cnt = 0;
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBgnX = mx;
                mBgnY = my;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                view.DELAY = 80;
                if (Math.abs(mx - mBgnX) < SWIPE_MIN_DISTANCE && Math.abs(my - mBgnY) < SWIPE_MIN_DISTANCE) {
                    doMove(Model.Move.ROTATE);
                } else if (mx - mBgnX > SWIPE_MIN_DISTANCE) {
                    cnt = Math.round((mx - mBgnX) / width);
                    while (cnt >= 1) {
                        System.out.println("cnt: " + cnt);
                        doMove(Model.Move.RIGHT);
                        cnt--;
                    }
                } else if (mx - mBgnX < -SWIPE_MIN_DISTANCE) {
                    cnt = Math.round((mBgnX - mx) / width);
                    while (cnt >= 1) {
                        doMove(Model.Move.LEFT);
                        cnt--;
                    }
                } else if (my - mBgnY > SWIPE_MIN_DISTANCE) { // this step better do BETTER THAN THIS
                    view.DELAY = 10;
                    doMove(Model.Move.DOWN);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_OUTSIDE:
                break;
            default:
                break;
            }
            return true;
        } else {
				// Paused state
				activateGame();
				return true;
        }
    }
    */
    public void doMove(Model.Move move) {
		if (model.isGameActive()) {
			view.setGameCommand(move);
			//scoresView.invalidate(); // how to update this one?
		}
	}
    /*  
	public final void startNewGame() {
		if (!model.isGameActive()) {
			//scoresCounter.reset();
            score = 0;
			model.gameStart();
			view.setGameCommandWithDelay(Model.Move.DOWN);
		}
	}
*/
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
	
	@Override
	public void onBackPressed() {
		if( model.isGameOver() || model.isGameBeforeStart() || model.isGamePaused() ) {
			finish();
			return;
		}
		if( model.isGameActive() ) {
			pauseGame();
			return;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		pauseGame();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Bundle bundle = new Bundle();
		model.storeTo(bundle);
		//scoresCounter.storeTo(bundle);   // how to save this one?
		outState.putBundle(ICICLE_TAG, bundle);
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onSaveInstanceState(inState);
		Bundle bundle = inState.getBundle(ICICLE_TAG);
		if (null != bundle) {
			model.restoreFrom(bundle);
			//scoresCounter.restoreFrom(bundle);  // how to restoret this one?
		}
		pauseGame();
	}
}
