package dev.ttetris.model;

import dev.ttetris.util.Interpolator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game implements BoardListener2, Serializable {
    private static final long serialVersionUID = -2508370532351143370L;
    private boolean active;
    private Block activeBlock;
    private Board board = new Board(10, 6, 6);  // need only one
    private IOException localObject = null;
    private transient float boardsObserveAngle;
    private Interpolator boardsRotation;
    private float fallTime;
    private float fallTimer;
    private int level;
    private transient List<GameListener> listeners;
    private Block nextBlock;
    private transient boolean paused;
    private float playTime;
    private int points;
    private float rotationAngle;
    private GameState state = GameState.PLAY;

    public Game() {
        this.board.setListener2(this);
        this.fallTime = getFallTime();
        this.fallTimer = this.fallTime;
        this.level = 1;
        this.listeners = new ArrayList();
        this.boardsRotation = new Interpolator(0.0F, 180.0F, 0.7F);  // don't understand this one
        System.out.println("new Game constructor");
    }

    private void addPoints(int paramInt) {
        this.points = (paramInt + this.points);
        int i = 1 + this.points / 200;
        if (i > this.level) {
            this.level = i;
            this.fallTime = getFallTime();
            notifyLevelChanged();
        }
        notifyPointsChanged();
    }

    private Block createRandomBlock() {
        return Block.createBlock(Block.getBlockNames()[((int)(Block.getBlockNames().length * Math.random()))]);
    }

    private float getAngle() {
        return 180.0F * this.board.getHeight(); // index
    }

    private float getFallTime() {
        return 1.1F + -0.75F * Math.min(1.0F, (-1 + this.level) / 9.0F);
    }

    private void notifyBlockFalled() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).blockFalled();
        }
    }

    private void notifyBlockFreezed() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).blockFreezed();
        }
    }

    private void notifyBlockRotateDenied() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).blockRotateDenied();
        }
    }

    private void notifyGameOver() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).gameOver();
        }
    }

    private void notifyLevelChanged() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).levelChanged(this.level);
        }
    }

    private void notifyLineDisappearing() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).lineDisappearing();
        }
    }

    private void notifyNextBlockGenerated(Block paramBlock) {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).nextBlockGenerated(paramBlock);
        }
    }

    private void notifyPauseChanged() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).pauseChanged(this.paused);
        }
    }

    private void notifyPointsChanged() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).pointsChanged(this.points);
        }
    }

    private void notifyStateChanged() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).stateChanged(this.state);
        }
    }

    private void readObject(ObjectInputStream paramObjectInputStream)
        throws IOException, ClassNotFoundException {
        try {
            paramObjectInputStream.defaultReadObject();
            this.listeners = new ArrayList();
            if (this.state == GameState.CHANGE_BOARD)
                this.state = GameState.PLAY;
            this.paused = true;
            return;
        } finally {
        }
    }

    private void setState(GameState paramGameState) {
        this.state = paramGameState;
        notifyStateChanged();
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        try {
            paramObjectOutputStream.defaultWriteObject();
            return;
        } finally {
            //localObject = finally;
            //  throw localObject;
        } 
    }

    public void addListener(GameListener paramGameListener) {
        try {
            this.listeners.add(paramGameListener);
            paramGameListener.boardCreated(this.board);
            paramGameListener.levelChanged(this.level);
            paramGameListener.pointsChanged(this.points);
            paramGameListener.stateChanged(this.state);
            paramGameListener.pauseChanged(this.paused);
            if (this.nextBlock != null)
                paramGameListener.nextBlockGenerated(this.nextBlock);
            return;
        } finally {
        }
    }

    public void cubeDisappeared() {
        addPoints(1);
    }

    public int getLevel() {
        try {
            int i = this.level;
            return i;
        } finally {
        }
    }

    public float getPlayTime() {
        try {
            float f = this.playTime;
            return f;
        } finally {
        }
    }

    public int getPoints() {
        try {
            int i = this.points;
            return i;
        } finally {
        }
    }

    public float getRotationAngle() {
        try {
            float f = this.rotationAngle;
            return f;
        } finally {
        }
    }

    public GameState getState() {
        try {
            GameState localGameState = this.state;
            return localGameState;
        } finally {
        }
    }

    public boolean isActive() {
        try {
            boolean bool = this.active;
            return bool;
        } finally {
        }
    }

    public boolean isPaused() {
        try {
            boolean bool = this.paused;
            return bool;
        } finally {
        }
    }

    public void moveBlockLeft() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeBlock != null))
                this.board.moveBlock(this.activeBlock, -1, 0, 0); // for Z
            return;
        } finally {
        }
    }

    public void moveBlockRight() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeBlock != null))
                this.board.moveBlock(this.activeBlock, 1, 0, 0); // for Z
            return;
        } finally {
        }
    }

    public void removeListener(GameListener paramGameListener) {
        try {
            this.listeners.remove(paramGameListener);
            return;
        } finally {
        }
    }

    public void rotateBlockLeft() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeBlock != null) && (!this.board.rotateBlockLeft(this.activeBlock)))
                notifyBlockRotateDenied();
            return;
        } finally {
        }
    }

    public void rotateBlockRight() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeBlock != null) && (!this.board.rotateBlockRight(this.activeBlock)))
                notifyBlockRotateDenied();
            return;
        } finally {
        }
    }

    public void setPaused(boolean paramBoolean) {
        try {
            this.paused = paramBoolean;
            this.boardsObserveAngle = 0.0F;
            notifyPauseChanged();
            return;
        } finally {
        }
    }

    public void stopScoreSubmitting() {
        try {
            if (this.state == GameState.SUBMIT_SCORE)
                setState(GameState.END);
            return;
        } finally {
        }
    }

    public void testFill() {
        try {
            this.board.testFill();
            return;
        } finally {
        }
    }

    public void throwBlock() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeBlock != null));
            for (int i = 0; ; i = 1)
                if (!this.board.moveBlock(this.activeBlock, 0, -1, 0)) { // for Z
                    if (i != 0)
                        notifyBlockFalled();
                    this.fallTimer = 0.0F;
                    return;
                }
        } finally {
        }
    }

    public void update(float paramFloat) {
        while (true) {
            try {
                if (this.state == GameState.CHANGE_BOARD) {
                    this.boardsRotation.update(paramFloat);
                    this.rotationAngle = (getAngle() - 180.0F + this.boardsRotation.getValue());
                    if (this.boardsRotation.isFinished())
                        setState(GameState.PLAY);
                    //this.board.update(paramFloat);
                    boolean bool = this.board.isDisappearing();
                    if (!bool);
                        //break label142;
                    return;
                }
                if ((this.state == GameState.PLAY) && (!this.paused)) {
                    this.rotationAngle = getAngle();
                    continue;
                }
            }
            finally {
            }
            this.boardsObserveAngle += 15.0F * paramFloat;
            this.rotationAngle = (getAngle() + this.boardsObserveAngle);
            //continue;
            //label142:
            if ((this.state == GameState.PLAY) && (!this.paused)) {
                if (!this.active)
                    this.active = true;
                this.playTime = (paramFloat + this.playTime);
                if (this.activeBlock != null) {
                    this.fallTimer -= paramFloat;
                    if (this.fallTimer <= 0.0F) {                        
                            this.fallTimer = this.fallTime;
                            if (!this.board.moveBlock(this.activeBlock, 0, -1, 0)) { // for Z
                                this.board.freezeBlock(this.activeBlock);
                                notifyBlockFreezed();
                                if (this.board.isDisappearing())
                                    notifyLineDisappearing();
                                addPoints(1);
                                this.activeBlock = null;
                            }
                        }
                }
                if (this.activeBlock == null) {
                    this.activeBlock = this.nextBlock;
                    if (this.activeBlock == null)
                        this.activeBlock = createRandomBlock();
                    this.nextBlock = createRandomBlock();
                    notifyNextBlockGenerated(this.nextBlock);
                    if (!this.board.addBlock(this.activeBlock)) {                        
                            notifyGameOver();
                            setState(GameState.SUBMIT_SCORE);
                        }
                }
            }
        }
    }
}
