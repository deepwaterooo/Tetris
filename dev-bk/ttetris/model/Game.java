package ru.igsoft.tetris.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.igsoft.angam.Interpolator;

public class Game implements BoardListener2, Serializable {
    private static final long serialVersionUID = -2508370532351143370L;
    private boolean active;
    private Board activeBoard;
    private Figure activeFigure;
    private Board board1 = new Board(0, 11, 13);
    private Board board2;
    private transient float boardsObserveAngle;
    private Interpolator boardsRotation;
    private float fallTime;
    private float fallTimer;
    private int level;
    private transient List<GameListener> listeners;
    private Figure nextFigure;
    private transient boolean paused;
    private float playTime;
    private int points;
    private float rotationAngle;
    private GameState state = GameState.PLAY;

    public Game() {
        this.board1.setListener2(this);
        this.board2 = new Board(1, 11, 13);
        this.board2.setListener2(this);
        this.activeBoard = this.board1;
        this.fallTime = getFallTime();
        this.fallTimer = this.fallTime;
        this.level = 1;
        this.listeners = new ArrayList();
        this.boardsRotation = new Interpolator(0.0F, 180.0F, 0.7F);
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

    private Figure createRandomFigure() {
        return Figure.createFigure(Figure.getFigureNames()[((int)(Figure.getFigureNames().length * Math.random()))]);
    }

    private float getActiveBoardAngle() {
        return 180.0F * this.activeBoard.getIndex();
    }

    private float getFallTime() {
        return 1.1F + -0.75F * Math.min(1.0F, (-1 + this.level) / 9.0F);
    }

    private void noitfyBoardChangeDenied() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).boardChangeDenied();
        }
    }

    private void notifyChangeBoard(Board paramBoard) {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).boardChanged(paramBoard);
        }
    }

    private void notifyFigureFalled() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).figureFalled();
        }
    }

    private void notifyFigureFreezed() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).figureFreezed();
        }
    }

    private void notifyFigureRotateDenied() {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).figureRotateDenied();
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

    private void notifyNextFigureGenerated(Figure paramFigure) {
        Iterator localIterator = this.listeners.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return;
            ((GameListener)localIterator.next()).nextFigureGenerated(paramFigure);
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

    private void writeObject(ObjectOutputStream paramObjectOutputStream)
        throws IOException {
        try {
            paramObjectOutputStream.defaultWriteObject();
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void addListener(GameListener paramGameListener) {
        try {
            this.listeners.add(paramGameListener);
            paramGameListener.boardCreated(this.board1);
            paramGameListener.boardCreated(this.board2);
            paramGameListener.levelChanged(this.level);
            paramGameListener.pointsChanged(this.points);
            paramGameListener.stateChanged(this.state);
            paramGameListener.pauseChanged(this.paused);
            if (this.nextFigure != null)
                paramGameListener.nextFigureGenerated(this.nextFigure);
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void brickDisappeared() {
        addPoints(1);
    }

    public void changeBoard() {
        try {
            Board localBoard;
            if ((this.state == GameState.PLAY) && (this.activeFigure != null)) {
                if (this.activeBoard != this.board1)
                    break label98;
                localBoard = this.board2;
                Figure localFigure = this.activeFigure.clone();
                localFigure.invertX();
                if (!localBoard.addFigure(localFigure))
                    break label106;
                this.activeBoard.removeFigure(this.activeFigure);
                this.activeBoard = localBoard;
                this.activeFigure = localFigure;
                this.boardsRotation.reset();
                notifyChangeBoard(localBoard);
                setState(GameState.CHANGE_BOARD);
            }
            while (true) {
                return;
                label98: localBoard = this.board1;
                break;
                label106: noitfyBoardChangeDenied();
            }
        } finally {
        }
    }

    public Board getActiveBoard() {
        try {
            Board localBoard = this.activeBoard;
            return localBoard;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public int getLevel() {
        try {
            int i = this.level;
            return i;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public float getPlayTime() {
        try {
            float f = this.playTime;
            return f;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public int getPoints() {
        try {
            int i = this.points;
            return i;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public float getRotationAngle() {
        try {
            float f = this.rotationAngle;
            return f;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public GameState getState() {
        try {
            GameState localGameState = this.state;
            return localGameState;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public boolean isActive() {
        try {
            boolean bool = this.active;
            return bool;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public boolean isPaused() {
        try {
            boolean bool = this.paused;
            return bool;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void moveFigureLeft() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeFigure != null))
                this.activeBoard.moveFigure(this.activeFigure, -1, 0);
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void moveFigureRight() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeFigure != null))
                this.activeBoard.moveFigure(this.activeFigure, 1, 0);
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void removeListener(GameListener paramGameListener) {
        try {
            this.listeners.remove(paramGameListener);
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void rotateFigureLeft() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeFigure != null) && (!this.activeBoard.rotateFigureLeft(this.activeFigure)))
                notifyFigureRotateDenied();
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void rotateFigureRight() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeFigure != null) && (!this.activeBoard.rotateFigureRight(this.activeFigure)))
                notifyFigureRotateDenied();
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void setPaused(boolean paramBoolean) {
        try {
            this.paused = paramBoolean;
            this.boardsObserveAngle = 0.0F;
            notifyPauseChanged();
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void stopScoreSubmitting() {
        try {
            if (this.state == GameState.SUBMIT_SCORE)
                setState(GameState.END);
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void testFill() {
        try {
            this.board1.testFill();
            this.board2.testFill();
            return;
        } finally {
            localObject = finally;
            throw localObject;
        }
    }

    public void throwFigure() {
        try {
            if ((this.state == GameState.PLAY) && (this.activeFigure != null));
            for (int i = 0; ; i = 1)
                if (!this.activeBoard.moveFigure(this.activeFigure, 0, -1)) {
                    if (i != 0)
                        notifyFigureFalled();
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
                    this.rotationAngle = (getActiveBoardAngle() - 180.0F + this.boardsRotation.getValue());
                    if (this.boardsRotation.isFinished())
                        setState(GameState.PLAY);
                    this.activeBoard.update(paramFloat);
                    boolean bool = this.activeBoard.isDisappearing();
                    if (!bool)
                        break label142;
                    return;
                }
                if ((this.state == GameState.PLAY) && (!this.paused)) {
                    this.rotationAngle = getActiveBoardAngle();
                    continue;
                }
            }
            finally {
            }
            this.boardsObserveAngle += 15.0F * paramFloat;
            this.rotationAngle = (getActiveBoardAngle() + this.boardsObserveAngle);
            continue;
            label142: if ((this.state == GameState.PLAY) && (!this.paused)) {
                if (!this.active)
                    this.active = true;
                this.playTime = (paramFloat + this.playTime);
                if (this.activeFigure != null) {
                    this.fallTimer -= paramFloat;
                    if (this.fallTimer <= 0.0F) {                        
                            this.fallTimer = this.fallTime;
                            if (!this.activeBoard.moveFigure(this.activeFigure, 0, -1)) {
                                this.activeBoard.freezeFigure(this.activeFigure);
                                notifyFigureFreezed();
                                if (this.activeBoard.isDisappearing())
                                    notifyLineDisappearing();
                                addPoints(1);
                                this.activeFigure = null;
                            }
                        }
                }
                if (this.activeFigure == null) {
                    this.activeFigure = this.nextFigure;
                    if (this.activeFigure == null)
                        this.activeFigure = createRandomFigure();
                    this.nextFigure = createRandomFigure();
                    notifyNextFigureGenerated(this.nextFigure);
                    if (!this.activeBoard.addFigure(this.activeFigure)) {                        
                            notifyGameOver();
                            setState(GameState.SUBMIT_SCORE);
                        }
                }
            }
        }
    }
}
