package dev.tetris;

import dev.tetris.game.Block;
import dev.tetris.game.Model;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;

public class TetrisView extends View {
	//private static final int DELAY = 100;
    public int DELAY = 80;
	private RedrawHandler redrawHandler = new RedrawHandler(this);
	//private static final int BLOCK_OFFSET = 2;
    private static final int BLOCK_OFFSET = 0;
	private static final int FRAME_OFFSET_BASE = 5;
	private final Paint paint = new Paint();
	private int width;
	private int height;
	private Dimension cellSize = null;
    //public Dimension cellSize = null;   // this is NOT a good habit
    
	private Dimension frameOffset = null;
	private Model model = null;
	private long lastMove = 0;
    private long lastDown = 0;
	private TetrisGame activity;

    public Dimension getCellSize() {
        return this.cellSize;
    }

	public TetrisView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TetrisView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setActivity(TetrisGame activity) {
		this.activity = activity;
	}

	public void setGameCommand(Model.Move move) {
		if (null == model || !model.isGameActive()) {
			return;
		}
		if (Model.Move.DOWN.equals(move)) {
			model.genereteNewField(move);
			invalidate();
			return;
		}
		setGameCommandWithDelay(move);
	}

	public void setGameCommandWithDelay(Model.Move move) {
		long now = System.currentTimeMillis();
		if (now - lastMove > DELAY) {
        //if (move != Model.Move.DOWN && now - lastMove > DELAY) {  // this maybe unnecessary
			model.genereteNewField(move);
			invalidate();
			lastMove = now;
		} /*  else if (move == Model.Move.DOWN && now - lastDown >= 4 * DELAY) {
            model.genereteNewField(move);
            invalidate();
            lastDown = now;
            } */
		redrawHandler.sleep(DELAY);
	}

	private void drawCell(Canvas canvas, int row, int col) {
		byte nStatus = model.getCellStatus(row, col);
        /*
		if (Block.CELL_EMPTY != nStatus) {
			int color = Block.CELL_DYNAMIC == nStatus ? model.getActiveBlockColor() :
                Block.getColorForStaticValue(nStatus);
			//drawCell(canvas, col, row, color);
            drawCell(canvas, col, row, color, nStatus);
            }*/
        int color = 0;
        if (Block.CELL_DYNAMIC == nStatus) {
            color = model.getActiveBlockColor();
        } else if (nStatus != Block.CELL_EMPTY) {
            color = Block.getColorForStaticValue(nStatus);
        }
        drawCell(canvas, col, row, color, nStatus);
	}
    
	private void drawCell(Canvas canvas, int x, int y, int colorFG, byte nStatus) {
		//paint.setColor(colorFG);
		float top = frameOffset.getHeight() + y * cellSize.getHeight()
				+ BLOCK_OFFSET;
		float left = frameOffset.getWidth() + x * cellSize.getWidth()
				+ BLOCK_OFFSET;
		float bottom = frameOffset.getHeight() + (y + 1) * cellSize.getHeight()
				- BLOCK_OFFSET;
		float right = frameOffset.getWidth() + (x + 1) * cellSize.getWidth()
				- BLOCK_OFFSET;
		RectF rect = new RectF(left, top, right, bottom);
        if (nStatus == Block.CELL_EMPTY) {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            //canvas.drawRoundRect(rect, 4, 4, paint);
            canvas.drawRect(rect, paint);
        } else {
            paint.setColor(colorFG);
            paint.setStyle(Paint.Style.FILL);
            //canvas.drawRoundRect(rect, 4, 4, paint);
            canvas.drawRect(rect, paint);
        }
	}

	private void drawCell(Canvas canvas, int x, int y, int colorFG) {
		paint.setColor(colorFG);
		float top = frameOffset.getHeight() + y * cellSize.getHeight()
				+ BLOCK_OFFSET;
		float left = frameOffset.getWidth() + x * cellSize.getWidth()
				+ BLOCK_OFFSET;
		float bottom = frameOffset.getHeight() + (y + 1) * cellSize.getHeight()
				- BLOCK_OFFSET;
		float right = frameOffset.getWidth() + (x + 1) * cellSize.getWidth()
				- BLOCK_OFFSET;
		RectF rect = new RectF(left, top, right, bottom);
		canvas.drawRoundRect(rect, 4, 4, paint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawFrame(canvas);
		if (null == model) {
			return;
		}
		// draw all the cells:
		for (int i = 0; i < Model.NUM_ROWS; i++) {
			for (int j = 0; j < Model.NUM_COLS; j++) {
				drawCell(canvas, i, j);
			}
		}
	}

	private void drawFrame(Canvas canvas) {
		try {
			InputStream input = activity.getAssets().open("frame.png");
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			canvas.drawBitmap(bitmap, 0, 0, paint);
		} catch (IOException ex) {
			Log.e("asset", "can't open asset bitmap", ex);
		}
		paint.setColor(Color.LTGRAY);
		//paint.setColor(0xffffffcc); // light_yellow
		canvas.drawRect(frameOffset.getWidth(), frameOffset.getHeight(),
                        width - frameOffset.getWidth(), height - frameOffset.getHeight(), paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		int cellWidth = (width - 2 * FRAME_OFFSET_BASE) / Model.NUM_COLS;
		int cellHeight = (height - 2 * FRAME_OFFSET_BASE) / Model.NUM_ROWS;
		int n = Math.min(cellWidth, cellHeight);
		this.cellSize = new Dimension(n, n);
		int offsetX = (w - Model.NUM_COLS * n) / 2;
		int offsetY = (h - Model.NUM_ROWS * n) / 2;
		this.frameOffset = new Dimension(offsetX, offsetY);
	}

	static class RedrawHandler extends Handler {
		private final TetrisView owner;
		private RedrawHandler(TetrisView owner) {
			this.owner = owner;
		}

		@Override
		public void handleMessage(Message msg) {
			if (null == owner.model) {
				return;
			}
			if (owner.model.isGameOver()) {
				owner.activity.endGame();
			}
			if (owner.model.isGameActive()) {
				owner.setGameCommandWithDelay(Model.Move.DOWN);
			}
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	}
}
