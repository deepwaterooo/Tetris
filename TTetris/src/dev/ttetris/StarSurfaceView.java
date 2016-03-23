package dev.ttetris;  //启动游戏

import dev.ttetris.Model;

import java.io.InputStream;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;  

public class StarSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    public int DELAY = 100;
    private static final int BLOCK_OFFSET = 0;
    private Integer OFFSET = 10;
    private Integer TOP_OFFSET = 36;
    private BlockColor color;
	private int width;
	private int height;
    private int nextSize;
    private Dimension cellSize = null; // maybe I don't need this one at all
    private Dimension frameOffset = null;
	private ActivityGame activity;
	private long lastMove = 0;

    private static float mx;
    private static float my;
    private static float mBgnX;
    private static float mBgnY;
    private static final int SWIPE_MIN_DISTANCE = 5;
    private int lines = 0;

	//SurfaceHolder用来完成对绘制的画布进行裁剪，控制其大小
	SurfaceHolder sHolder = null;
	Canvas canvas = null;
	Paint paint;
	Model model;   

	int face;
    private boolean flag;
	boolean onlyone = true;
	boolean next = false;
    Block activeBlock;
	Block nextBlock;  
    Block tmp; // for temporatory use
	int x;
	int y;
	int cntThree;
    int totalScore;
    private int counter;
    
    public StarSurfaceView(Context context) {
		super(context);
		sHolder = this.getHolder(); //实例化sHolder
		sHolder.addCallback(this);  //addCallback:给SurfaceView添加一个回调函数
		this.setFocusable(true);
		x = -1;
		y = 4;
		cntThree = 0;

		activeBlock = new Block();
		nextBlock = new Block();
		paint = new Paint();
		model = new Model(); // tetrisView
        counter = 0;
        totalScore = 0;
	}

    public enum BlockColor {
		RED(0xffff0000, (byte) 1),
        GREEN(0xff00ff00, (byte) 2),
        BLUE(0xff0000ff, (byte) 3),
        YELLOW(0xffffff00, (byte) 4),
        CYAN(0xff00ffff, (byte) 5),
        //DKGREY(0xff444444, (byte) 6),
        WHITE(0xffffffff, (byte) 6),
        MAGENTA(0xffff00ff, (byte) 7);
		private final int color;
		private final byte value;
		private BlockColor(int color, byte value) {
			this.color = color;
			this.value = value;
		}
	}

	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;

	public void draw() {
		try {    
            canvas = sHolder.lockCanvas();    
            if (canvas != null) {
                drawScoreAndFrames(canvas);
                for (int i = 0; i < 4; i++) 
                    drawNextCell(canvas, nextBlock.aj[i], nextBlock.ai[i], nextBlock.color);
                for (int i = 0; i < Model.ROW; i++) 
                    for (int j = 0; j < Model.COL; j++) 
                        drawGameCell(canvas, i, j);
            }    
        } catch (Exception e) {    
            System.out.println("error");
        } finally {    
            if (canvas!= null)    
                sHolder.unlockCanvasAndPost(canvas);    
        }
	}
    
	public void run() {
		if (onlyone){
            nextBlock.generateBlock(-1);   //随机为下一个方块区域生成一个方块
            activeBlock.generateBlock(-1); //随机为游戏区域生成一个方块
            model.putNextBlock(nextBlock);  //在下一个方块区域产生一个方块
            onlyone = false;
		} 
        
		while (flag){
			if (next) {
				model.deleteNextBlock(nextBlock);  //删除下一个方块区域的方块
				activeBlock.generateBlock(nextBlock.shape);	
                nextBlock.generateBlock(-1); //为下一个方块区域随机生成一个方块
				x = -1;
				y = 4;
				next = false;
				model.putNextBlock(nextBlock);
			}

			try {
                draw();
                Thread.sleep(400 - model.speed);
            } catch (InterruptedException e) {
				e.printStackTrace();
            }
            
			if (left) {
				model.deleteBlock(activeBlock, x, y); //删除游戏区域的方块
                if (activeBlock.canShiftLeft()) 
                    activeBlock = activeBlock.shiftLeft();
                else if (y > 0 && model.canMoveLeft(activeBlock, x, y)) 
					y--;                              //如果方块可以左移，把方块左移
				model.putBlock(activeBlock, x, y);    //重新生成左移后的方块
                draw(); 
				left = false;
                cntThree++;
                if (cntThree < 3) continue;
			} else if (right) {
				model.deleteBlock(activeBlock, x, y);

                if (y >= 0 && y + activeBlock.getWidth() < Model.COL
                       && model.canMoveRight(activeBlock, x, y)) {
					y++;
                    cntThree++;
                } else if (y + activeBlock.getWidth() < Model.COL
                           && activeBlock.canShiftLeft()) {
                    activeBlock = activeBlock.shiftLeft();
                    if (model.canMoveRight(activeBlock, x, y)) {
                        y++;
                        cntThree++;
                    }
                }

                if (y + activeBlock.getWidth() >= Model.COL) cntThree = 3;
                model.putBlock(activeBlock, x, y);
                draw(); 
                right = false;
                if (cntThree < 3) continue;
			} else if (up) { 
				int tv;
                if ( (activeBlock.shape) % 4 == 3) 
					tv = activeBlock.shape - 3;       
				else tv = activeBlock.shape + 1;
                tmp = new Block();
				tmp.generateBlock(tv);
				model.deleteBlock(activeBlock, x, y);

                if (x + tmp.getHeight() < Model.ROW && y + tmp.getWidth() < Model.COL && cntThree < 3) {
					activeBlock = tmp;
					model.putBlock(activeBlock, x, y);
					up = false;
					cntThree++;
					if (cntThree < 3) continue;
                } else {
					model.putBlock(activeBlock, x, y);
					up = false;
				}
                draw(); 
			} else if (down) {
				model.deleteBlock(activeBlock, x, y);
                while (activeBlock.canShiftUp()) 
                    activeBlock = activeBlock.shiftUp();
				while (x + activeBlock.getHeight() < Model.ROW && model.canMoveDown(activeBlock, x, y)) 
					x++;
                model.putBlock(activeBlock, x, y);
                draw();
				down = false;
			}

            cntThree = 0;
			model.deleteBlock(activeBlock, x, y);
            if (x + activeBlock.getHeight() + 1 < Model.ROW
                && y + activeBlock.getWidth() <= Model.COL  
                && model.canMoveDown(activeBlock, x, y)) {
				x++;
				model.putBlock(activeBlock, x, y);
                draw(); 

                lines = model.flood(activeBlock, x, y);
                if (lines > 0)
                    totalScore = model.getUpdatedScore(lines);
                draw(); 
			} else {
                model.putBlock(activeBlock, x, y);
                draw(); 
                lines = model.flood(activeBlock, x, y);
                if (lines > 0)
                    totalScore = model.getUpdatedScore(lines);
                draw();

				if (model.isGameOver(activeBlock, x, y)) {
					Intent intent = new Intent();
                    /*
	        		intent.setClass( ActivityGame.acGame, ActGameover.class);
	        		ActivityGame.acGame.startActivity(intent);
	        		ActivityGame.acGame.finish();
                    */
					break;
				}
				next = true;
			} // else
		} // flag
	}

    public Dimension getCellSize() {
        return this.cellSize;
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

	public int getColor() {
		return color.color;
	}

	public byte getStaticValue() {
		return color.value;
	}

	public static int getColorForStaticValue(byte b) {
		for (BlockColor item : BlockColor.values()) 
			if (b == item.value) 
				return item.color;
		return -1; // color is not found
	}

	private void drawNextCell(Canvas canvas, int x, int y, byte colorFG) {
		float top = TOP_OFFSET + y * nextSize + BLOCK_OFFSET;
		float left = OFFSET + x * nextSize + BLOCK_OFFSET;
		float bottom = TOP_OFFSET + (y + 1) * nextSize - BLOCK_OFFSET;
		float right = OFFSET + (x + 1) * nextSize - BLOCK_OFFSET;
		RectF rect = new RectF(left, top, right, bottom);
        paint.setColor(getColorForStaticValue(colorFG));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
    }

    private void drawGameCell(Canvas canvas, int x, int y) {
		float top = TOP_OFFSET + frameOffset.getHeight() + x * cellSize.getHeight() + BLOCK_OFFSET;
		float left = frameOffset.getWidth() + OFFSET + 4*nextSize + y * cellSize.getWidth() + BLOCK_OFFSET;
        float bottom = frameOffset.getHeight()+ TOP_OFFSET + (x + 1) * cellSize.getHeight() - BLOCK_OFFSET;
        float right = frameOffset.getWidth() + OFFSET + 4*nextSize + (y + 1) * cellSize.getWidth() - BLOCK_OFFSET;
		RectF rect = new RectF(left, top, right, bottom);
        if (model.board[x][y] == 0) {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
        } else {
            paint.setColor(getColorForStaticValue((byte)model.board[x][y]));
            paint.setStyle(Paint.Style.FILL);
        }
        canvas.drawRect(rect, paint);
    }

    private void drawScoreAndFrames(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(40);  
        canvas.drawText("Score: " + totalScore, 2*OFFSET, 3*OFFSET, paint);
        Rect rectNext = new Rect(OFFSET, TOP_OFFSET, OFFSET + nextSize*4, TOP_OFFSET + nextSize*4);
        Rect rect = new Rect(nextSize*4 + 2*OFFSET, TOP_OFFSET, width - OFFSET, height - OFFSET); 
        paint.setColor(Color.LTGRAY);  
        canvas.drawRect(rectNext, paint);
        canvas.drawRect(rect, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setTextSize(80);  
        canvas.drawRect(rectNext, paint);
        canvas.drawRect(rect, paint);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mx = event.getX();
        my = event.getY();
        if (model.isGameOver() || model.isGameBeforeStart()) {
            startNewGame();
            return true;
        } else if (model.isGameActive()) {
            Dimension cellSize = getCellSize();
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
                if (Math.abs(mx - mBgnX) < SWIPE_MIN_DISTANCE && Math.abs(my - mBgnY) < SWIPE_MIN_DISTANCE) {
                    up = true;
                } else if (mx - mBgnX > SWIPE_MIN_DISTANCE) {
                        right = true;
                } else if (mx - mBgnX < -SWIPE_MIN_DISTANCE) {
                        left = true;
                } else if (my - mBgnY > SWIPE_MIN_DISTANCE) { // this step better do BETTER THAN THIS
                    DELAY = 80;
                    down = true;
                }
                break;
            }
            return true;
        } else {
				activateGame();
				return true;
        }
    }

	public void activateGame() {
		//messageView.setVisibility(View.INVISIBLE);
		model.setGameActive();	
	}

	public final void startNewGame() {
		if (!model.isGameActive()) {
			//scoresCounter.reset();
            totalScore = 0;
			model.gameStart();
			setGameCommandWithDelay(Model.Move.DOWN);
		}
	}
    
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
        nextSize = (width - 3 * OFFSET) / (5 * 4);
		int cellWidth = (int)Math.floor((float)((width - 3 * OFFSET) * 0.8 / Model.COL));
		int cellHeight = (int)Math.floor((float)((height - OFFSET - TOP_OFFSET) / Model.ROW));
		int n = Math.min(cellWidth, cellHeight);
		this.cellSize = new Dimension(n, n);
		int offsetX = (w - nextSize*4 - OFFSET - Model.COL * n) / 2;
		int offsetY = (h - TOP_OFFSET - Model.ROW * n) / 2;
		this.frameOffset = new Dimension(offsetX, offsetY);
	}

    public Point getGameTopLeftPoint() {
        int x = OFFSET + nextSize * 4 + OFFSET + frameOffset.getWidth();
        int y = TOP_OFFSET + frameOffset.getHeight();
        return new Point(x, y);
    }
    
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
        //board = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        //canvas = new Canvas(board);
        
        flag = true;
        Thread th = new Thread(this);
        th.start();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
        sHolder.removeCallback(this);
	}
}
