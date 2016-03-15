package dev.tetris;

import dev.tetris.game.Model;
import dev.tetris.game.ScoresCounter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageButton;
import android.support.v4.view.GestureDetectorCompat;

public class TetrisGame extends Activity {

	private static final String ICICLE_TAG = "simple-tetris";
	private static final String PREFS_HIGH_LINES = "high_lines";
	private static final String PREFS_HIGH_SCORES = "high_scores";

	private TextView scoresView = null;
	private TextView highScoresView = null;
	private TextView messageView = null;
	private ScoresCounter scoresCounter = null;
	private Model model = new Model();
	private TetrisView tetrisView = null;

    private ImageButton leftBtn = null;
    private ImageButton downBtn = null;
    private ImageButton rightBtn = null;
    private ImageButton rttleftBtn = null;
    private ImageButton rttRightBtn = null;

	private int highLines = 0;
	private int highScores = 0;

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    private static float mx;
    private static float my;
    private static float mBgnX;
    private static float mBgnY;
    private static final int SWIPE_MIN_DISTANCE = 5;
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //int action = MotionEventCompat.getActionMasked(event);
        mx = event.getX();
        my = event.getY();
        if (model.isGameOver() || model.isGameBeforeStart()) {
            startNewGame();
            return true;
        } else if (model.isGameActive()) {
            Dimension cellSize = tetrisView.getCellSize();
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
                tetrisView.DELAY = 80;
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
                    tetrisView.DELAY = 10;
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

    /*
	private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (model.isGameOver() || model.isGameBeforeStart()) {
				startNewGame();
				return true;
			} else if (model.isGameActive()) {
				int direction = getDirection(v, event);
				switch (direction) {
				case 0: // left
					doMove(Model.Move.LEFT);
					break;
				case 1: // rotate
					doMove(Model.Move.ROTATE);
					break;
				case 2: // down
					doMove(Model.Move.DOWN);
					break;
				case 3: // right
					doMove(Model.Move.RIGHT);
					break;
				}
				return true;
			} else {
				// Paused state
				activateGame();
				return true;
			}
		}
	};
    */

    public void doMove(Model.Move move) {
		if (model.isGameActive()) {
			tetrisView.setGameCommand(move);
			scoresView.invalidate();   
		}
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameview);

		tetrisView = TetrisView.class.cast(findViewById(R.id.tetrisview));
		tetrisView.setModel(model);
		tetrisView.setActivity(this);
		//tetrisView.setOnTouchListener(onTouchListener);

		scoresView = TextView.class.cast(findViewById(R.id.scores));
		scoresCounter = new ScoresCounter(scoresView, getString(R.string.scores_format));
		model.setCounter(scoresCounter);
		highScoresView = TextView.class.cast(findViewById(R.id.high_scores));

		loadHighScoresAndLines();
		messageView = TextView.class.cast(findViewById(R.id.message));

        //mDetector = new GestureDetectorCompat(this, this);
        //mDetector.setOnTouchListener(this);
        
		// Restore the state:
		if (null != savedInstanceState) {
			onRestoreInstanceState(savedInstanceState);
		} else {
			messageView.setText(getApplicationContext().getString(R.string.mode_ready));
		}
		
		// Assign font:
		Typeface tf = Typeface.createFromAsset(getAssets(), "Callie-Mae.ttf");
		scoresView.setTypeface(tf);
		highScoresView.setTypeface(tf);
		messageView.setTypeface(tf);
	}

    // this is NOT any good method at all, needs more of modifications
	private int getDirection(View v, MotionEvent event) {
		// Normalize x,y between 0 and 1
		float x = event.getX() / v.getWidth();
		float y = event.getY() / v.getHeight();
		int direction;
		if (y > x) {
			direction = (x > 1 - y) ? 2 : 0;
		} else {
			direction = (x > 1 - y) ? 3 : 1;
		}
		return direction;
	}
    /*
	public void doMove(Model.Move move) {
		if (model.isGameActive()) {
			tetrisView.setGameCommand(move);
			scoresView.invalidate();
		}
	}
    */
	public final void startNewGame() {
		if (!model.isGameActive()) {
			messageView.setVisibility(View.INVISIBLE);
			scoresCounter.reset();
			model.gameStart();
			tetrisView.setGameCommandWithDelay(Model.Move.DOWN);
		}
	}

	public void endGame() {
		messageView.setVisibility(View.VISIBLE);
		storeHighScoresAndLines();
		messageView
				.setText(getApplicationContext().getText(R.string.mode_over));
	}

	public void pauseGame() {
		model.setGamePaused();
		messageView.setVisibility(View.VISIBLE);
		messageView.setText(getApplicationContext()
				.getText(R.string.mode_pause));
		storeHighScoresAndLines();
	}
	
	public void activateGame() {
		messageView.setVisibility(View.INVISIBLE);
		model.setGameActive();	
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
		scoresCounter.storeTo(bundle);
		outState.putBundle(ICICLE_TAG, bundle);
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onSaveInstanceState(inState);
		Bundle bundle = inState.getBundle(ICICLE_TAG);
		if (null != bundle) {
			model.restoreFrom(bundle);
			scoresCounter.restoreFrom(bundle);
		}
		pauseGame();

	}

	private void loadHighScoresAndLines() {
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		highLines = prefs.getInt(PREFS_HIGH_LINES, 0);
		highScores = prefs.getInt(PREFS_HIGH_SCORES, 0);
		updateHighScoresView();
	}

	private void updateHighScoresView() {
		highScoresView.setText(String.format(
				getString(R.string.high_scores_format), highLines, highScores));
	}

	private void storeHighScoresAndLines() {
		if (highScores < scoresCounter.getScores()) {
			highScores = scoresCounter.getScores();
		}
		if (highLines < scoresCounter.getLines()) {
			highLines = scoresCounter.getLines();
		}

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PREFS_HIGH_LINES, highLines);
		editor.putInt(PREFS_HIGH_SCORES, highScores);

		editor.commit();
		updateHighScoresView();
	}
}
