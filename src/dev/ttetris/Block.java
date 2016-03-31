package dev.ttetris;

public class Block {
    public int shape; 
    public byte color;
    public int[] ai;
    public int[] aj;

    public Block() {
        ai = new int[4];
        aj = new int[4];
        color = 0;
        shape = -1;
    }

    // generate blocks randomly for next board, and generate fixed as previous next for the main game board
    public void generateBlock(int x) { 
        if (x == -1) 
            shape = (int)(Math.random() * 28);
        else shape = x;
        int k = 0;
        color = (byte)(shape / 4 + 1);
        for (int i = 0; i < 4; i++) 
            for (int j = 0; j < 4; j++) 
                if (block[shape][i][j] != 0) {
                    ai[k] = i; 
                    aj[k] = j;
                    k++;
                }
    }

    public int getWidth() {
        int res = 0;
        int s = shape / 4;
        int mod = shape % 4;
        switch(s) {
        case 0:
            if (mod == 0 || mod == 2) res = 1;
            else res = 4;
            break;
        case 1:
            res = 2;
            break;
        case 2:
        case 3:
            if (mod == 0 || mod == 2) res = 2;
            else res = 3;
            break;
        case 4:
            if (mod == 0 || mod == 2) res = 3;
            else res = 2;
            break;
        case 5:
        case 6:
            if (mod == 0 || mod == 2) res = 3; 
            else res = 2;
            break;
        }
        return res;
    }
    
    public int getHeight() {
        int res = 0;
        int s = shape / 4;
        int mod = shape % 4;
        switch(s) {
        case 0:
            if (mod == 0 || mod == 2) res = 4;
            else res = 1;
            break;
        case 1:
            res = 2;
            break;
        case 2:
        case 3:
            if (mod == 0 || mod == 2) res = 3;
            else res = 2;
            break;
        case 4:
            if (mod == 0 || mod == 2) res = 2;
            else res = 3;
            break;
        case 5:
        case 6:
            if (mod == 0 || mod == 2) res = 2; 
            else res = 3;
            break;
        }
        return res;
    }
    
    public boolean canShiftLeft() {
        for (int i = 0; i < 4; i++) 
            if (aj[i] == 0) return false;
        return true;
    }

    public boolean canShiftUp() {
        for (int i = 0; i < 4; i++) 
            if (ai[i] == 0) return false;
        return true;
    }

    // the two methods I rewrote them because phone memory related issues?
    public void shiftLeft() { 
        for (int i = 0; i < 4; i++)
            aj[i] -= 1;
    }

    public void shiftUp() {    
        for (int i = 0; i < 4; i++)
            ai[i] -= 1;
    }
    
    public int getMaxRowIdx() {
        int res = ai[0];
        for (int i = 1; i < 4; i++) 
            if (ai[i] > res) res = ai[i];
        return res;
    }
    
    public static int[][][] block = new int[][][] {
        { { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 } },
        { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 } },
        { { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 } },
        { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 1, 1, 1, 0 }, { 1, 0, 0, 0 }, { 0, 0, 0, 0 } },
        { { 0, 1, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 1, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 1, 0, 0, 0 }, { 1, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 1, 1, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 1, 1, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 1, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 1, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 0, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 1, 1, 1, 0 }, { 0, 1, 0, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 1, 1, 0, 0 }, { 0, 0, 0, 0 } },
        { { 0, 1, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 0, 1, 1 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 0 } },
        { { 0, 0, 0, 0 }, { 1, 1, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 1, 0, 0 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 } },
        { { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 1, 0, 0 } }
	};
}
