package dev.ttetris;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import dev.ttetris.util.ActivityGame;
import dev.ttetris.util.SoundManager;
import dev.ttetris.util.AssetManager;
import dev.ttetris.util.SceneView;
import dev.ttetris.util.ServerTask;
import dev.ttetris.common.GameResult;
import dev.ttetris.common.TopScore;
import dev.ttetris.model.Board;
import dev.ttetris.model.Game;
import dev.ttetris.model.GameAdapter;
import dev.ttetris.model.GameState;

// need to add a few buttons here
public class TTetris extends ActivityGame {
    private View continueButton;
    private Button dailyTopScoresButton;
    private ViewGroup gameView;
    private Button globalTopScoresButton;
    private TextView levelTextView;
    private ViewGroup loadingView;
    private MediaPlayer mediaPlayer;
    private ViewGroup menuView;
    private View newGameButton;
    private TextView scoreTextView;
    private SoundManager soundManager;
    private ActivityState state;
    private View submitButton;
    private TetrisScene tetrisScene;
    private View topScoresButton;
    private ScrollView topScoresScrollView;
    private TableLayout topScoresTable;
    private View topScoresView;
    private ViewGroup uiLayout;
    private Vibrator vibrator;
    //private View moveBlockLeftButton;
    //private View moveBlockRightButton;
    //private View rotateBlockLeftButton;
    //private View rotateBlockRightButton;
    //private View throwBlockButton;

    private void bindGameListener(Game paramGame) {
        paramGame.addListener(new GameAdapter() {
                private int prevLevel = -1;

                public void boardChangeDenied() {
                    TTetris.this.vibrator.vibrate(50L);
                }

                public void boardChanged(Board paramAnonymousBoard) {
                    TTetris.this.soundManager.play("ChangeBoard");
                }

                public void figureFreezed() {
                    TTetris.this.soundManager.play("FreezeBlock");
                }

                public void figureRotateDenied() {
                    TTetris.this.vibrator.vibrate(50L);
                }

                public void gameOver() {
                    TTetris.this.runOnUiThread(new Runnable() {
                            public void run() {
                                TTetris.this.changeState(ActivityState.MENU);
                                TTetris.this.showGameOverDialog();
                            }
                        });
                }

                public void levelChanged(final int paramAnonymousInt) {
                    if (this.prevLevel != -1)
                        TTetris.this.soundManager.play("NextLevel");
                    this.prevLevel = paramAnonymousInt;
                    TTetris.this.runOnUiThread(new Runnable() {
                            public void run() {                            
                                TTetris.this.levelTextView.setText(TTetris.this.getString(2131034137) + " " + paramAnonymousInt);
                            }
                        });
                }

                public void lineDisappearing() {
                    TTetris.this.soundManager.play("FireLine");
                }

                public void pointsChanged(final int paramAnonymousInt) {
                    TTetris.this.runOnUiThread(new Runnable() {
                            public void run() {
                                TTetris.this.scoreTextView.setText(TTetris.this.getString(2131034136) + " " + paramAnonymousInt);
                            }
                        });
                }

                public void stateChanged(final GameState paramAnonymousGameState) {
                    TTetris.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (paramAnonymousGameState == GameState.SUBMIT_SCORE) {
                                    TTetris.this.continueButton.setVisibility(8);
                                    TTetris.this.submitButton.setVisibility(0);
                                    return;
                                }
                                TTetris.this.submitButton.setVisibility(8);
                            }
                        });
                }
            });
    }

    private void bindInputListeners() {
        /*
        this.continueButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.changeState(ActivityState.GAME);
                }
            });
        */
        /*
        this.submitButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    //TTetris.this.submitScore();
                }
                }); */
        this.newGameButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Game localGame = new Game();
                    TTetris.this.bindGameListener(localGame);
                    TTetris.this.tetrisScene = new TetrisScene(localGame);
                    TTetris.this.sceneView.setScene(TTetris.this.tetrisScene);
                    TTetris.this.continueButton.setVisibility(0);
                    TTetris.this.changeState(ActivityState.GAME);
                }
            });
        /*        this.topScoresButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.showTopScores(false);
                }
            });

        this.moveBlockLeftButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.tetrisScene.moveBlockLeft();
                }
            });
        this.moveBlockRightButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.tetrisScene.moveBlockRight();
                }
            });
        this.rotateBlockLeftButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.tetrisScene.rotateBlockLeft();
                }
            });
        this.rotateBlockRightButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.tetrisScene.rotateBlockRight();
                }
            });
        this.throwBlockButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.tetrisScene.throwBlock();
                }
            });
        this.changeBoardButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.tetrisScene.changeBoard();
                }
            });

        this.dailyTopScoresButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.showTopScores(false);
                }
            });
        this.globalTopScoresButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.showTopScores(true);
                }
            });        */
    }
    /*
    private void cancelReceiveTopScoresTask() {
        if (this.receiveTopScoresTask != null) {
            Log.i("TTetris", "Cancel ReceiveTopScoresTask: " + this.receiveTopScoresTask.cancel(false));
            this.receiveTopScoresTask = null;
            dismissAlertDialog();
        }
        }*/
    /*
    private void cancelSubmitTask() {
        if (this.submitScoreTask != null) {
            Log.i("TTetris", "Cancel SubmitScoreTask: " + this.submitScoreTask.cancel(false));
            this.submitScoreTask = null;
            dismissAlertDialog();
        }
    }
    */
    private void changeState(ActivityState paramActivityState) {
        boolean bool;
        if (this.state != paramActivityState) {
            this.uiLayout.removeAllViews();
            //switch ($SWITCH_TABLE$ru$igsoft$tetris$ActivityState()[paramActivityState.ordinal()]) {
            switch (((ActivityState)paramActivityState).ordinal()) {
            default:
                if (this.tetrisScene != null) {
                    TetrisScene localTetrisScene = this.tetrisScene;
                    if (paramActivityState != ActivityState.GAME) {                        
                        bool = true;
                        label73: localTetrisScene.setPaused(bool);
                    }
                } else {
                    if ((paramActivityState != ActivityState.MENU) && (paramActivityState != ActivityState.TOP_SCORES));
                        //break label163;
                    startThemeMusic();
                }
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            }
        }
        while (true) {
            this.state = paramActivityState;
            //return;
            this.uiLayout.addView(this.loadingView);
            //break;
            this.uiLayout.addView(this.menuView);
            //break;
            this.uiLayout.addView(this.gameView);
            //break;
            this.uiLayout.addView(this.topScoresView);
            //break;
            bool = false;
            //break label73;
            //label163:
            releaseMediaPlayer();
        }
    }

    // ERROR //
    private Game loadGame() {
        return (Game)null;
    }

    // ERROR //
    private void saveGame(Game paramGame) {
        //return (Game)null;
    }

    private void releaseMediaPlayer() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    private void showGameOverDialog() {
        dismissAlertDialog();
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setMessage(getString(2131034132) + " " + this.tetrisScene.getGameScore() + ". " + getString(2131034131));
        localBuilder.setTitle(2131034135);
        localBuilder.setPositiveButton(2131034127, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    TTetris.this.dismissAlertDialog();
                    //TTetris.this.submitScore();
                }
            });
        localBuilder.setNegativeButton(2131034129, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    TTetris.this.dismissAlertDialog();
                    TTetris.this.tetrisScene.stopScoreSubmitting();
                }
            });
        this.alertDialog = localBuilder.create();
        this.alertDialog.show();
    }

    private void showReceivingTopScoresProgressDialog() {
        dismissAlertDialog();
        this.alertDialog = ProgressDialog.show(this, "", getString(2131034134), false, true, new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface paramAnonymousDialogInterface) {
                    TTetris.this.alertDialog = null;
                    //TTetris.this.cancelReceiveTopScoresTask();
                }
            });
    }

    private void showSubmittingProgressDialog() {
        dismissAlertDialog();
        this.alertDialog = ProgressDialog.show(this, "", getString(2131034133), false, true, new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface paramAnonymousDialogInterface) {
                    TTetris.this.alertDialog = null;
                    //TTetris.this.cancelSubmitTask();
                }
            });
    }

    private void startThemeMusic() {
        if (this.mediaPlayer == null) {
            this.mediaPlayer = MediaPlayer.create(this, 2130968581);
            if (this.mediaPlayer != null)
                this.mediaPlayer.start();
        }
    }

    protected void OnCancelAllTasks() {
        super.OnCancelAllTasks();
        //cancelSubmitTask();
        //cancelReceiveTopScoresTask();
    }

    protected void onAssetManagerCreated(AssetManager paramAssetManager) {
        paramAssetManager.loadTextures("Textures");
        paramAssetManager.loadMeshes("Meshes");
    }

    public void onBackPressed() {
        //switch ($SWITCH_TABLE$ru$igsoft$tetris$ActivityState()[this.state.ordinal()]) {
        switch (((ActivityState)(this.state)).ordinal()) {
        default:
            return;
        case 1:
        case 2:
            finish();
            return;
        case 3:
        case 4:
        }
        changeState(ActivityState.MENU);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.vibrator = ((Vibrator)getSystemService("vibrator"));
        this.soundManager = new SoundManager(this, "Sounds");

        //this.loadingView = ((ViewGroup)getLayoutInflater().inflate(2130903041, null));
        //this.menuView = ((ViewGroup)getLayoutInflater().inflate(2130903042, null));
        //this.continueButton = this.menuView.findViewById(2131165197);
        //this.submitButton = this.menuView.findViewById(2131165198);
        //this.newGameButton = this.menuView.findViewById(2131165199);
        //this.topScoresButton = this.menuView.findViewById(2131165200);
        //this.gameView = ((ViewGroup)getLayoutInflater().inflate(2130903040, null));
        //this.levelTextView = ((TextView)this.gameView.findViewById(2131165192));
        //this.scoreTextView = ((TextView)this.gameView.findViewById(2131165191));
        /*
        //this.moveBlockLeftButton = this.gameView.findViewById(2131165185);
        //this.moveBlockRightButton = this.gameView.findViewById(2131165186);
        //this.rotateBlockLeftButton = this.gameView.findViewById(2131165187);
        //this.rotateBlockRightButton = this.gameView.findViewById(2131165189);
        //this.throwBlockButton = this.gameView.findViewById(2131165188);
        //this.changeBoardButton = this.gameView.findViewById(2131165190);  */
        
        //this.topScoresView = getLayoutInflater().inflate(2130903043, null);
        //this.topScoresTable = ((TableLayout)this.topScoresView.findViewById(2131165206));
        //this.topScoresScrollView = ((ScrollView)this.topScoresView.findViewById(2131165205));
        //this.dailyTopScoresButton = ((Button)this.topScoresView.findViewById(2131165203));
        //this.globalTopScoresButton = ((Button)this.topScoresView.findViewById(2131165204));
        FrameLayout localFrameLayout = new FrameLayout(this);
        this.uiLayout = new FrameLayout(this);
        localFrameLayout.addView(this.sceneView);
        localFrameLayout.addView(this.uiLayout);
        bindInputListeners();
        setContentView(localFrameLayout);
    }

    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        saveGame(this.tetrisScene.getGame());
    }

    protected void onStart() {
        super.onStart();
        TetrisScene localTetrisScene = this.tetrisScene;
        Game localGame = null;
        boolean bool = false;
        if (localTetrisScene != null) {
            localGame = this.tetrisScene.getGame();
            bool = this.tetrisScene.isStopScoreSubmitting();
        }
        int i;
        View localView = null;
        int j = 0;
        if (localGame == null) {
            i = 0;
            localGame = loadGame();
            if (localGame != null);
                //break label140;
            localGame = new Game();
            bindGameListener(localGame);
            localView = this.continueButton;
            j = 0;
            if (i == 0);
            //break label169;
        }
        while (true) {
            localView.setVisibility(j);
            this.tetrisScene = new TetrisScene(localGame);
            if (bool)
                this.tetrisScene.stopScoreSubmitting();
            this.sceneView.setScene(this.tetrisScene);
            changeState(ActivityState.LOADING);
            this.sceneView.queuePostDrawEvent(new Runnable() {
                    public void run() {
                        TTetris.this.runOnUiThread(new Runnable() {                            
                                public void run() {
                                    TTetris.this.changeState(ActivityState.MENU);
                                }
                            });
                    }
                });
            //return;
            //label140:
            if ((localGame.getState() == GameState.PLAY) && (localGame.isActive()));
            for (i = 1; ; i = 0)
                break;
            label169: j = 8;
        }
    }
}
