package dev.ttetris; 

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
import android.media.AudioManager;
import android.media.SoundPool;

public class StarSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    public int DELAY = 100;
    private Integer OFFSET = 12;
    private Integer TOP_OFFSET = 36;
    private BlockColor color;
	private int width;
	private int height;
    private int nextSize;
    private Dimension cellSize = null; 
    private Dimension frameOffset = null;
	private ActivityGame activity;
	private long lastMove = 0;
    private static float mx;
    private static float my;
    private static float mBgnX;
    private static float mBgnY;
    private static float mLeftRightX;
    private static float mLeftRightY;
    private static final int SWIPE_MIN_DISTANCE = 5;
    //private static final float ALPHA = 0.5f;
    private int lines = 0;

	//SurfaceHolder用来完成对绘制的画布进行裁剪，控制其大小
	SurfaceHolder sHolder = null;
	Canvas canvas = null;
	Paint paint;
	Model model;   
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
    private SoundPool sounds;
    private int sndTurn;
    private int sndKillLine;
    private int sndTouch;
    private int sndAccDown;
    private int sndLevelUp;
    
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
        totalScore = 0;
        /*// for sounds effects
        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        sndTurn = sounds.load(context, R.raw.turn, 1);
        sndKillLine = sounds.load(context, dev.ttetris.R.raw.killLine, 1);
        sndTouch = sounds.load(context, dev.ttetris.R.raw.touch, 1);
        sndLevelUp = sounds.load(context, dev.ttetris.R.raw.levelUp, 1);
        sndAccDown = sounds.load(context, dev.ttetris.R.raw.accDown, 1); */
	}
    public enum BlockColor {
		RED(0xffff0000, (byte) 1),
        GREEN(0xff00ff00, (byte) 2),
        BLUE(0xff0000ff, (byte) 3),
        YELLOW(0xffffff00, (byte) 4),
        CYAN(0xff00ffff, (byte) 5),
        WHITE(0xffffffff, (byte) 6),
        MAGENTA(0xffff00ff, (byte) 7),
        TRANSPARENT(0x20320617, (byte) 8);
		private final int color;
		private final byte value;
		private BlockColor(int color, byte value) {
			this.color = color;
			this.value = value;
		}
	}

	public void draw() {
		try {    
            canvas = sHolder.lockCanvas();    
            if (canvas != null) {
                drawScoreAndFrames(canvas);
                for (int i = 0; i < 4; i++) 
                    drawNextCell(canvas, nextBlock.ai[i], nextBlock.aj[i], nextBlock.color);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mx = event.getX();
        my = event.getY();
        if (model.isGameOver() || model.isGameBeforeStart()) {
            startNewGame();
            return true;
        } else if (model.isGameActive()) { 
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBgnX = mx;
                mBgnY = my;
                mLeftRightX = mx;
                mLeftRightY = my;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mx - mLeftRightX >= cellSize.getWidth()) {
                    right = true;
                    mLeftRightX = mx;
                    mLeftRightY = my;
                } else if (mx - mLeftRightX <= -cellSize.getWidth()) {
                    left = true;
                    mLeftRightX = mx;
                    mLeftRightY = my;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mx - mBgnX) < cellSize.getWidth() && Math.abs(my - mBgnY) < cellSize.getWidth())
                    up = true;
                else if (my - mBgnY > cellSize.getWidth() && Math.abs(mx - mBgnX) < cellSize.getWidth()) 
                    down = true;
                break;
            }
            return true;
        } else {
				activateGame();
				return true;
        }
    }
    
	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;
    int proj = Model.ROW - 1;
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
                deleteCurrBlockProjection(activeBlock, proj, y);
				model.deleteBlock(activeBlock, x, y); 

                if (activeBlock.canShiftLeft()) {
                    model.shiftLeft(activeBlock, x, y); 
                    cntThree++;
                } else if (y > 0 && model.canMoveLeft(activeBlock, x, y)) {
					--y;
                    cntThree++;
                } 
                proj = getDownProjectionPos(activeBlock, x, y);
                System.out.println("Left proj: " + proj);
                System.out.println("Left x: " + x);
                System.out.println("Left y: " + y);
                System.out.println("Left activeBlock.shape: " + activeBlock.shape);
                putCurrBlockProjection(activeBlock, proj, y);
				model.putBlock(activeBlock, x, y);     
                draw(); 
                left = false;
                if (cntThree < 3) continue;   
            } 

            if (right) {
                deleteCurrBlockProjection(activeBlock, proj, y);
				model.deleteBlock(activeBlock, x, y); 

                if (y >= 0 && y + activeBlock.getWidth() < Model.COL
                       && model.canMoveRight(activeBlock, x, y)) {
					y++;
                    cntThree++;
                } else if (y + activeBlock.getWidth() < Model.COL
                           && activeBlock.canShiftLeft()) {
                    model.shiftLeft(activeBlock, x, y);  // 
                    y++;
                    cntThree++;
                } 
                proj = getDownProjectionPos(activeBlock, x, y);
                System.out.println("Right proj: " + proj);
                System.out.println("Right x: " + x);
                System.out.println("Right y: " + y);
                System.out.println("Right activeBlock.shape: " + activeBlock.shape);
                putCurrBlockProjection(activeBlock, proj, y);
				model.putBlock(activeBlock, x, y);    
                right = false;
                if (y + activeBlock.getWidth() >= Model.COL) cntThree = 3;
                if (cntThree < 3) continue;
                draw(); 
			}
            
            if (up) {
				int tv;
                if ( (activeBlock.shape) % 4 == 3) 
					tv = activeBlock.shape - 3;       
				else tv = activeBlock.shape + 1;
                tmp = new Block();
				tmp.generateBlock(tv);

                deleteCurrBlockProjection(activeBlock, proj, y);
				model.deleteBlock(activeBlock, x, y);
                if (x + tmp.getHeight() < Model.ROW && y + tmp.getWidth() < Model.COL && cntThree < 3) {
					activeBlock = tmp;
                    System.out.println("Up proj: " + proj);
                    System.out.println("Up x: " + x);
                    System.out.println("Up y: " + y);
                    System.out.println("Up activeBlock.shape: " + activeBlock.shape);

                    proj = getDownProjectionPos(activeBlock, x, y);
                    putCurrBlockProjection(activeBlock, proj, y);
					model.putBlock(activeBlock, x, y);
					cntThree++;
                } else {
                    putCurrBlockProjection(activeBlock, proj, y);
					model.putBlock(activeBlock, x, y);
				}
                draw();
                up = false;
                if (cntThree < 3) continue;
			} 

            if (down) {
				model.deleteBlock(activeBlock, x, y);
                while (activeBlock.canShiftUp())
                    model.shiftUp(activeBlock, x, y);
				while (x + activeBlock.getHeight() < Model.ROW && model.canMoveDown(activeBlock, x, y)) 
					x++;
                model.putBlock(activeBlock, x, y);
				down = false;
                draw();
			}

            cntThree = 0;
            proj = getDownProjectionPos(activeBlock, x, y);
			model.deleteBlock(activeBlock, x, y);
            deleteCurrBlockProjection(activeBlock, proj, y); // added
            System.out.println("x: " + x);
            System.out.println("y: " + y);
            System.out.println("model.canMoveDown(activeBlock, x, y): " + model.canMoveDown(activeBlock, x, y));

            if (x + activeBlock.getHeight() < Model.ROW //  + 1 < Model.ROW
                && y + activeBlock.getWidth() <= Model.COL  
                && model.canMoveDown(activeBlock, x, y)) {
				x++;
                if (x + activeBlock.getMaxRowIdx() >= Model.ROW)
                    model.shiftLeft(activeBlock, x, y);
                
                proj = getDownProjectionPos(activeBlock, x, y);
                System.out.println("If proj: " + proj);
                System.out.println("If x: " + x);
                System.out.println("If y: " + y);
                System.out.println("If activeBlock.shape: " + activeBlock.shape);
                putCurrBlockProjection(activeBlock, proj, y);
				model.putBlock(activeBlock, x, y);
                draw();
			} else {
                System.out.println("Else proj: " + proj);
                System.out.println("Else x: " + x);
                System.out.println("Else y: " + y);
                System.out.println("Else activeBlock.shape: " + activeBlock.shape);
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
			}
		} 
	}

    public int getDownProjectionPos(Block a, int x, int y) {
        Block tmp = new Block();
        tmp = a;
        while (tmp.canShiftUp())
            tmp.shiftUp();
        while (x + tmp.getHeight() < Model.ROW && model.canMoveDown(tmp, x, y)) 
            x++;
        return x;
    }

    public void putCurrBlockProjection(Block a, int x, int y) {
        Block tmp = new Block();
        tmp = a;
        while (tmp.canShiftUp())
            tmp.shiftUp();
        for (int i = 0; i < 4; i++)
            if (x + tmp.ai[i] >= 0 && x + tmp.ai[i] < Model.ROW && y + tmp.aj[i] >= 0 && y + tmp.aj[i] < Model.COL) 
                model.board[x + tmp.ai[i]][y + tmp.aj[i]] = (byte)8;
    }

    public void deleteCurrBlockProjection(Block a, int x, int y) {
        for (int i = 0; i < 4; i++)
            if (x + a.ai[i] >= 0 && x + a.ai[i] < Model.ROW
                && y + a.aj[i] >= 0 && y + a.aj[i] < Model.COL) 
                model.board[x + a.ai[i]][y + a.aj[i]] = 0;
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
		float top = TOP_OFFSET + frameOffset.getHeight() + x * nextSize;// + BLOCK_OFFSET;
		float left = frameOffset.getWidth() + y * nextSize;// + BLOCK_OFFSET;
		float bottom = TOP_OFFSET + frameOffset.getHeight() + (x + 1) * nextSize;// - BLOCK_OFFSET;
		float right = frameOffset.getWidth() + (y + 1) * nextSize;// - BLOCK_OFFSET;
		RectF rect = new RectF(left, top, right, bottom);
        paint.setStrokeWidth(1);
        paint.setColor(getColorForStaticValue(colorFG));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect, paint);
    }

    private void drawGameCell(Canvas canvas, int x, int y) {
		float top = TOP_OFFSET + frameOffset.getHeight() + x * cellSize.getHeight();// + BLOCK_OFFSET;
		float left = frameOffset.getWidth() + OFFSET + 4*nextSize + y * cellSize.getWidth();// + BLOCK_OFFSET;
        float bottom = frameOffset.getHeight()+ TOP_OFFSET + (x + 1) * cellSize.getHeight();// - BLOCK_OFFSET;
        float right = frameOffset.getWidth() + OFFSET + 4*nextSize + (y + 1) * cellSize.getWidth();// - BLOCK_OFFSET;

        RectF rect = new RectF(left, top, right, bottom);
        paint.setStrokeWidth(1);
        if (model.board[x][y] == 0) {
            paint.setColor(Color.LTGRAY);
            paint.setStyle(Paint.Style.FILL);
        } else {
            paint.setColor(getColorForStaticValue((byte)model.board[x][y]));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rect, paint);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawRect(rect, paint);
    }

    private void drawScoreAndFrames(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(40);  
        canvas.drawText("Score: " + totalScore, 2*OFFSET, 3*OFFSET, paint);
        Rect rectNext = new Rect(frameOffset.getWidth() - 1, TOP_OFFSET + frameOffset.getHeight() - 1,
                                 frameOffset.getWidth() + nextSize*4 + 1, TOP_OFFSET + nextSize*4 + 1);
        Rect rect = new Rect(nextSize*4 + frameOffset.getWidth() + OFFSET - 1,
                             TOP_OFFSET + frameOffset.getHeight() - 1,
                             width - frameOffset.getWidth() + 1,
                             height - frameOffset.getHeight() + 1); 
        paint.setColor(Color.LTGRAY);  
        canvas.drawRect(rectNext, paint);
        canvas.drawRect(rect, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(4);
        canvas.drawRect(rectNext, paint);
        canvas.drawRect(rect, paint);
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
        nextSize = (width - OFFSET) / (5 * 4);
		int cellWidth = (int)Math.floor((float)((width - OFFSET) * 0.8 / Model.COL));
		int cellHeight = (int)Math.floor((float)((height - TOP_OFFSET) / Model.ROW));
		int n = Math.min(cellWidth, cellHeight);
		this.cellSize = new Dimension(n, n);
		int offsetX = (w - nextSize*4 - OFFSET - Model.COL * n) / 2;
		int offsetY = (h - TOP_OFFSET - Model.ROW * n) / 2;
		this.frameOffset = new Dimension(offsetX, offsetY);
	}
    
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
        flag = true;
        Thread th = new Thread(this);
        th.start();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
        sHolder.removeCallback(this);
	}
}
