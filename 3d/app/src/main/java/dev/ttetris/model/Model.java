package dev.ttetris.model;

import dev.ttetris.model.Cube;
import dev.ttetris.model.Block;
import android.os.Bundle;
import android.content.Context;

public class Model {
    public enum GameStatus {
		BEFORE_START {}, ACTIVE {}, PAUSED {}, OVER {};
	}
	private static GameStatus gameStatus = GameStatus.BEFORE_START;
    public static final int ROW = 5;  // x
    public static final int COL = 5;  // y
    public static final int HGT = 10; // z
    public static int[][][] board;    // 0 - empty, 1-7, 8 for projection
    private static int score;    
    private static int speed;                 // could control for the game later on
    private static boolean dropFast;
    
    // maybe I don't need this much, as far as an angle, 3 angles are passed in?
    public static boolean [] isFrameZRotating = new boolean[2]; // 0 - false, [1] - clockwise, [0] - anticlockwise 
    public static boolean [] isFrameXRotating = new boolean[2]; // 0 - false, [1] - clockwise, [0] - anticlockwise come up with some better ideas for this one
    public static boolean [] isBlockXRotating = new boolean[2]; // 0 - false, [1] - clockwise, [0] - anticlockwise 
    public static boolean [] isBlockYRotating = new boolean[2]; // 0 - false, [1] - clockwise, [0] - anticlockwise 
    public static boolean [] isBlockZRotating = new boolean[2]; // 0 - false, [1] - clockwise, [0] - anticlockwise 
    
    public static void init(Context context) {
        if (board == null)
            board = new int[ROW][COL][HGT];
        else resetBoard();
        score = 0;
        speed = 100;
        dropFast = false;
    }

    public static void onSwipeRight() {
        isFrameZRotating[0] = true;  // --> anticlock
        isFrameZRotating[1] = false; // <-- clock
        isFrameXRotating[0] = false; // ���� anti
        isFrameXRotating[1] = false; // ���� ^|^ clock-wise
    }
    public static void onSwipeLeft() {
        isFrameZRotating[0] = false;  // --> anticlock
        isFrameZRotating[1] = true;   // <-- clock
        isFrameXRotating[0] = false; // ���� anti
        isFrameXRotating[1] = false; // ���� ^|^ clock-wise
    }
    public static void onSwipeBottom() {
        isFrameZRotating[0] = false;
        isFrameZRotating[1] = false;
        isFrameXRotating[0] = true;   // ���� anti
        isFrameXRotating[1] = false;  // ���� ^|^ clock-wise
    }
    public static void onSwipeTop() {
        isFrameZRotating[0] = false;
        isFrameZRotating[1] = false;
        isFrameXRotating[0] = false;   // ���� anti
        isFrameXRotating[1] = true;  // ���� ^|^ clock-wise
    }
    
    public static void resetBoard() {
        for (int k = 0; k < HGT; k++) 
            for (int j = 0; j < COL; j++) 
                for (int i = 0; i < ROW; i++) 
                    board[i][j][k] = 0;
    }
    
    /*
    // upload main board block
    public void putBlock(Block a, int x, int y) {
        for (int i = 0; i < 4; i++)
            if (x + a.ai[i] >= 0 && x + a.ai[i] < ROW && y + a.aj[i] >= 0 && y + a.aj[i] < COL) 
                board[x + a.ai[i]][y + a.aj[i]] = a.color;
    }

    public void deleteBlock(Block a, int x, int y) {
        for (int i = 0; i < 4; i++) 
            if (x + a.ai[i] >= 0 && x + a.ai[i] < ROW
                && y + a.aj[i] >= 0 && y + a.aj[i] < COL) 
                board[x + a.ai[i]][y + a.aj[i]] = 0;
    }

    // actually clean up left behind garbages
    public void shiftUp(Block a, int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (x + a.ai[i] >= 0 && x + a.ai[i] < ROW // for a bug
                && y + a.aj[i] >= 0 && y + a.aj[i] < COL)
                board[x + a.ai[i]][y + a.aj[i]] = 0;
            a.ai[i] -= 1;
        }
    }

    // actually clean up left backwards garbages
    public void shiftLeft(Block a, int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (x + a.ai[i] >= 0 && x + a.ai[i] < ROW // for a bug
                && y + a.aj[i] >= 0 && y + a.aj[i] < COL)
                board[x + a.ai[i]][y + a.aj[i]] = 0;
            a.aj[i] -= 1;
        }
    }

    public boolean canMoveLeft(Block a, int x, int y) { 
        for (int i = 0; i < 4; i++) {
            if (y + a.aj[i] - 1 >= 0 && y + a.aj[i] < COL
                && x + a.ai[i] >= 0 && x + a.ai[i] < ROW
                && board[x + a.ai[i]][y + a.aj[i] - 1] != 0) 
                return false;
            if (y + a.aj[i] - 1 < 0) return false; 
        }
        return true;
    }

    public boolean canMoveRight(Block a, int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (y + a.aj[i] >= 0 && y + a.aj[i] + 1 < COL
                && x + a.ai[i] >= 0 && x + a.ai[i] < ROW
                && board[x + a.ai[i]][y + a.aj[i] + 1] != 0)
                return false;
            if (y + a.aj[i] + 1 >= COL) return false; 
        }
        return true;
    }
    
    public boolean canMoveDown(Block a, int x, int y) { 
        for (int i = 0; i < 4; i++) {
            if (x + 1 + a.ai[i] < ROW && x + 1 + a.ai[i] >= 0
                && y + a.aj[i] >= 0 && y + a.aj[i] < COL
                && board[x + a.ai[i] + 1][y + a.aj[i]] != 0
                && board[x + a.ai[i] + 1][y + a.aj[i]] != 8)
                return false;
            if (x + a.ai[i] + 1 >= ROW) return false; 
        }
        return true;
    }

    public boolean isGameOver(Block a, int x, int y) { 
        for (int i = 0; i < 4; i++)
            if (x + a.ai[i] < 1)
                return true;
        return false;
    }

    // check if the at most 4 layers are full and need to be removed
    public int flood(Block a, int x, int y) {
        int gridCnt, lineCnt = 0;
        for (int i = 0; i < 4; i++) {
            gridCnt = 0;
            for (int j = 0; j < COL; j++)
                if (board[x + a.ai[i]][j] != 0 && board[x + a.ai[i]][j] != 8)
                    gridCnt++;
            if (gridCnt == 10) {
                lineCnt++;
                remove(a.ai[i] + x);
            } 
        }
        return lineCnt;  
    }
*/
    // remove a z = x layer from the board
    public void remove(int x) {
        for (int i = 0; i < ROW; i++) 
            for (int j = 0; j < COL; j++) {
                for (int k = x; k < HGT - 1; k++)  // z = x
                    board[i][j][k] = board[i][j][k + 1];
                board[i][j][HGT - 1] = 0;
            }
    }

    public boolean isGameActive() { return GameStatus.ACTIVE.equals(gameStatus); }
	public boolean isGameOver() { return GameStatus.OVER.equals(gameStatus); }
	public boolean isGameBeforeStart() { return GameStatus.BEFORE_START.equals(gameStatus); }
    /*
    public void reset() { reset(false);  }
    private final void reset(boolean bDynamicDataOnly) {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				//if (!bDynamicDataOnly || board[i][j] == 8) 
				//	board[i][j] = 0;
			}
		}
        } */
    /*
	public void gameStart() { // Start the game:
		if (isGameActive()) {
			return;
		}
		setGameActive();
		//activeBlock = Block.createBlock();
	}
    
	public void setGameActive() {
		setGameStatus(GameStatus.ACTIVE);
	}
	
	public synchronized void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setGamePaused() {
		setGameStatus(GameStatus.PAUSED);
	}

	public boolean isGamePaused() {
		return GameStatus.PAUSED.equals(gameStatus);
	}	

	public void storeTo(Bundle bundle) {
		//bundle.putSerializable(TAG_ACTIVE_BLOCK, activeBlock);
		bundle.putIntArray(TAG_DATA, getIntArrayFromData());
	}

	public void restoreFrom(Bundle bundle) {
		//activeBlock = Block.class.cast( bundle.getSerializable(TAG_ACTIVE_BLOCK));
		restoreDataFromIntArray( bundle.getIntArray(TAG_DATA));
	}

	private void restoreDataFromIntArray(int[] src) {
		if (null == src ) {
			return;
		}
		for( int k = 0; k < src.length; k++ ) {
			int i = k / COL;
			int j = k % COL;
			board[i][j] = src[k];
		}
	}

	private int[] getIntArrayFromData() {
		int[] result = new int[ COL * ROW ];
		for( int i = 0; i < ROW; i++ ) {
			for( int j = 0; j<COL; j++ ) {
				result[ COL * i + j ] = board[i][j];
			}
		}
		return result;
	}
    
	public int getCellStatus(int nRow, int nCol) {
		return board[nRow][nCol];
	}
    */
	public void setCellStatus(int nRow, int nCol, int nHig, byte nStatus) {
		board[nRow][nCol][nHig] = nStatus;
	}

    // @param x: the number of rows full and destroyed
    public int getUpdatedScore(int x) {
        return score += x * x * 10;
    }
}
