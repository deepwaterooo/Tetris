package dev.ttetris;

public class Block {
    public int shape; // kind
    public byte color;
    public int[] ai;
    public int[] aj;

    public Block() {
        ai = new int[4];
        aj = new int[4];
        color = 0;
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

    public int getWidth(Block a) {
        int res = 0;
        int s = a.shape / 4;
        int mod = a.shape % 4;
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
            if (mod == 0 || mod == 2) res = 2;
            else res = 3;
            break;
        }
        return res;
    }
    
    public int getHeight(Block a) {
        int res = 0;
        int s = a.shape / 4;
        int mod = a.shape % 4;
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
            if (mod == 0 || mod == 2) res = 3;
            else res = 2;
            break;
        }
        return res;
    }
    
    public boolean canShiftLeft(Block a) {
        for (int i = 0; i < 4; i++) 
            if (a.aj[i] == 0) return false;
        return true;
    }

    public boolean canShiftUp(Block a) {
        for (int i = 0; i < 4; i++) 
            if (a.ai[i] == 0) return false;
        return true;
    }

    public Block shiftLeft(Block a) {
        Block res = new Block();
        int min = a.aj[0];
        for (int i = 0; i < 4; i++) {
            res.ai[i] = a.ai[i];
            res.aj[i] = a.aj[i] - 1;
        }
        res.shape = a.shape;
        res.color = a.color;
        return res;
    }

    public Block shiftUp(Block a) {
        Block res = new Block();
        int min = a.ai[0];
        for (int i = 0; i < 4; i++) {
            res.ai[i] = a.ai[i] - 1;
            res.aj[i] = a.aj[i];
        }
        res.shape = a.shape;
        res.color = a.color;
        return res;
    }

    public Block shiftDown(Block a) {
        Block res = new Block();
        int max = a.ai[0];
        for (int i = 1; i < 4; i++) 
            if (a.ai[i] > max) max = a.ai[i];
        for (int i = 0; i < 4; i++) {
            res.ai[i] = a.ai[i] + 3 - max;
            res.aj[i] = a.aj[i];
        }
        res.shape = a.shape;
        res.color = a.color;
        return res;
    }

    public static int[][][] block = new int[][][] {
        {{ 0, 0, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 1, 0 } },//I
        {{ 0, 0, 0, 0 }, 
         { 0, 0, 0, 0 }, 
         { 1, 1, 1, 1 }, 
         { 0, 0, 0, 0 } },//I
        {{ 0, 0, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 1, 0 } },//I
        {{ 0, 0, 0, 0 }, 
         { 0, 0, 0, 0 }, 
         { 1, 1, 1, 1 }, 
         { 0, 0, 0, 0 } },//I 3
        
        {{ 0, 0, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//O
        {{ 0, 0, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//O
        {{ 0, 0, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//O
        {{ 0, 0, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//O 7
        
        {{ 0, 1, 0, 0 }, 
         { 0, 1, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//L
        {{ 0, 0, 0, 0 }, 
         { 1, 1, 1, 0 }, 
         { 1, 0, 0, 0 }, 
         { 0, 0, 0, 0 } },//L
        {{ 0, 1, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 0, 0 } },//L
        {{ 0, 0, 0, 0 }, 
         { 0, 0, 1, 0 }, 
         { 1, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//L 11
        
        {{ 0, 0, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//J
        {{ 0, 0, 0, 0 }, 
         { 1, 0, 0, 0 }, 
         { 1, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//J
        {{ 0, 1, 1, 0 }, 
         { 0, 1, 0, 0 }, 
         { 0, 1, 0, 0 }, 
         { 0, 0, 0, 0 } },//J
        {{ 0, 0, 0, 0 }, 
         { 1, 1, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 0, 0 } },//J 15
        
        {{ 0, 0, 0, 0 }, 
         { 0, 1, 0, 0 }, 
         { 1, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//T
        {{ 0, 1, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 1, 0, 0 }, 
         { 0, 0, 0, 0 } },//T
        {{ 0, 0, 0, 0 }, 
         { 1, 1, 1, 0 }, 
         { 0, 1, 0, 0 }, 
         { 0, 0, 0, 0 } },//T
        {{ 0, 0, 1, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 0, 0 } },//T 19
        
        {{ 0, 0, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 1, 1, 0, 0 }, 
         { 0, 0, 0, 0 } },//S
        {{ 0, 1, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 0, 0 } },//S
        {{ 0, 0, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 1, 1, 0, 0 }, 
         { 0, 0, 0, 0 } },//S
        {{ 0, 1, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 1, 0 }, 
         { 0, 0, 0, 0 } },//S 23
        
        {{ 0, 0, 0, 0 }, 
         { 1, 1, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//Z
        {{ 0, 0, 1, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 1, 0, 0 }, 
         { 0, 0, 0, 0 } },//Z
        {{ 0, 0, 0, 0 }, 
         { 1, 1, 0, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 0, 0, 0 } },//Z
        {{ 0, 0, 1, 0 }, 
         { 0, 1, 1, 0 }, 
         { 0, 1, 0, 0 }, 
         { 0, 0, 0, 0 } }//Z 27
	};
}
