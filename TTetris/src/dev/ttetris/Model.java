package dev.ttetris;

import android.os.Bundle;

public class Model {
    public enum GameStatus {
		BEFORE_START {}, ACTIVE {}, PAUSED {}, OVER {};
	}

	public enum Move {
		LEFT, RIGHT, ROTATE, DOWN
	}

	private GameStatus gameStatus = GameStatus.BEFORE_START;
	private static final String TAG_DATA = "data";
	private static final String TAG_ACTIVE_BLOCK = "active-block";
    public static final int ROW = 20;
    public static final int COL = 10;

    public int[][] next = null;  
    public int[][] board = null; 
    private int score;    
    public int speed;

    public Model() {
        score = 0;
        speed = 100;
        next = new int[4][4];
        board = new int[ROW][COL];
    }

    // @param x: the number of rows full and destroyed
    public int getUpdatedScore(int x) {
        return score += x * x * 10;
    }
    
    // put generated Block into Next area
    public void putNextBlock(Block b) {
        for (int i = 0; i < 4; i++) 
            next[b.ai[i]][b.aj[i]] = b.color;
    }

    // delete Block in next area
    public void deleteNextBlock(Block c) { 
        for (int i = 0; i < 4; i++) 
            next[c.ai[i]][c.aj[i]] = 0;
    }
    /*
    // get Down projection position row, can not put here cause I need a model
    public int getDownProjectionPos(Block a, int x, int y) {
        while (a.canShiftUp())
            a.shiftUp();
        while (x + a.getHeight() < ROW && canMoveDown(a, x, y)) {
            x++;
            System.out.println("getProj x: " + x);
            System.out.println("canMoveDown(a, x, y): " + canMoveDown(a, x, y));

        }
        return x;
    }
    */
    public void putCurrBlockProjection(Block a, int x, int y) {
            for (int i = 0; i < 4; i++)
                board[x + a.ai[i]][y + a.aj[i]] = (byte)8;
    }

    public void deleteCurrBlockProjection(Block a, int x, int y) {
            for (int i = 0; i < 4; i++)
                board[x + a.ai[i]][y + a.aj[i]] = 0;
    }
    
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
            if (x + 1 + a.ai[i] < ROW
                && y + a.aj[i] >= 0 && y + a.aj[i] < COL
                && (board[x + a.ai[i] + 1][y + a.aj[i]] != 0
                    || board[x + a.ai[i] + 1][y + a.aj[i]] != 8))
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

    // check if the at most 4 rows are full and need to be removed
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

    // remove a row from board
    public void remove(int x) {
        for (int j = 0; j < 10; j++) {
            for (int i = x; i >= 1; i--) 
                board[i][j] = board[i - 1][j];
            board[0][j] = 0; 
        }
    }

    public boolean isGameActive() {
		return GameStatus.ACTIVE.equals(gameStatus);
	}

	public boolean isGameOver() {
		return GameStatus.OVER.equals(gameStatus);
	}
	
	public boolean isGameBeforeStart() {
		return GameStatus.BEFORE_START.equals(gameStatus);
	}	

	public void reset() {
		reset(false); // call the inner method - reset the all data
	}

    /**
	 * Reset the field data:
	 * @param true - clear only dynamic data, false - clear all the data
	 */
	private final void reset(boolean bDynamicDataOnly) {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				if (!bDynamicDataOnly || board[i][j] == 8) 
					board[i][j] = 0;
			}
		}
	}

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

	public void setCellStatus(int nRow, int nCol, byte nStatus) {
		board[nRow][nCol] = nStatus;
	}
}
