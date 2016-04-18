package dev.ttetris;

import android.content.Intent;
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
import dev.ttetris.common.GameResult;
import dev.ttetris.common.TopScore;
import dev.ttetris.model.Board;
import dev.ttetris.model.Game;
import dev.ttetris.model.GameAdapter;
import dev.ttetris.model.GameState;

public class TTetris extends ActivityGame {
    private View newGameButton;
    private View continueButton;
    private View topScoresButton;
    private SoundManager soundManager;
    private MediaPlayer mediaPlayer;
    private TetrisScene tetrisScene;

    private ViewGroup menuView;
    private ViewGroup gameView;
    private ViewGroup loadingView;
    private ViewGroup uiLayout;
    private ScrollView topScoresScrollView;
    private TableLayout topScoresTable;
    private Vibrator vibrator;

    private TextView scoreTextView;
    private ActivityState state;

    private Game loadGame() { return (Game)null; }
    private void saveGame(Game paramGame) { }

    protected void OnCancelAllTasks() { super.OnCancelAllTasks(); }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.vibrator = ((Vibrator)getSystemService("vibrator"));
        this.soundManager = new SoundManager(this, "Sounds");
        
        this.gameView = ((ViewGroup)getLayoutInflater().inflate(R.layout.game, null));
        this.loadingView = ((ViewGroup)getLayoutInflater().inflate(R.layout.loading, null));
        this.menuView = ((ViewGroup)getLayoutInflater().inflate(R.layout.menu, null));
        this.continueButton = ((View)this.menuView).findViewById(R.id.continue_game); // continue_game
        this.newGameButton = this.menuView.findViewById(R.id.new_game);
        this.topScoresButton = this.menuView.findViewById(R.id.top_scores);
        this.scoreTextView = ((TextView)this.gameView.findViewById(2131165191));
        //this.topScoresView = getLayoutInflater().inflate(2130903043, null);
        //this.topScoresTable = ((TableLayout)this.topScoresView.findViewById(2131165206));
        //this.topScoresScrollView = ((ScrollView)this.topScoresView.findViewById(2131165205));
        FrameLayout localFrameLayout = new FrameLayout(this);
        this.uiLayout = new FrameLayout(this);
        localFrameLayout.addView(this.sceneView);
        localFrameLayout.addView(this.uiLayout);
        bindInputListeners();
        setContentView(localFrameLayout);
    } 

    private void bindInputListeners() {
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
        this.continueButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.changeState(ActivityState.GAME);
                }
            }); 
        this.topScoresButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    //TTetris.this.showTopScores(false);
                }
            }); /*
        this.throwBlockButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TTetris.this.tetrisScene.throwBlock();
                }
            });
*/
    }

    private void bindGameListener(Game paramGame) {
        paramGame.addListener(new GameAdapter() {
                public void blockFreezed() { TTetris.this.soundManager.play("FreezeBlock"); }
                public void blockRotateDenied() { TTetris.this.vibrator.vibrate(50L); }
                public void lineDisappearing() { TTetris.this.soundManager.play("FireLine"); }
                public void gameOver() {
                    TTetris.this.runOnUiThread(new Runnable() {
                            public void run() {
                                TTetris.this.changeState(ActivityState.MENU);
                                TTetris.this.showGameOverDialog();
                            }
                        });
                }
                /*
                public void pointsChanged(final int paramAnonymousInt) {
                    TTetris.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //TTetris.this.scoreTextView.setText(TTetris.this.getString(2131034136) + " " + paramAnonymousInt);
                                //TTetris.this.scoreTextView.setText("Score: " + paramAnonymousInt);
                            }
                        });
                }
                */
                public void stateChanged(final GameState paramAnonymousGameState) {
                    TTetris.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (paramAnonymousGameState == GameState.SUBMIT_SCORE) {
                                    TTetris.this.continueButton.setVisibility(8);
                                    return;
                                }
                            }
                        });
                }
            });
    }

    private void changeState(ActivityState paramActivityState) {
        boolean bool = false;
        if (this.state != paramActivityState) {
            this.uiLayout.removeAllViews();
            //System.out.println("paramActivityState: " + paramActivityState);
            //System.out.println("((ActivityState)paramActivityState).ordinal(): " + (((ActivityState)paramActivityState).ordinal()));
            switch (((ActivityState)paramActivityState).ordinal()) {
            case 0:
                this.uiLayout.addView(this.loadingView);
                bool = true;
                break;
            case 1:
                this.uiLayout.addView(this.menuView);
                System.out.println("addView");
                break;
            case 2:
                System.out.println("got here: ");

                this.uiLayout.addView(this.gameView);
                break;
            case 3:
                //this.uiLayout.addView(this.topScoresView);
                break;
            default:
                if (this.tetrisScene != null) {
                    TetrisScene localTetrisScene = this.tetrisScene;
                    if (paramActivityState != ActivityState.GAME) {                        
                        bool = true;
                        //label73:
                        localTetrisScene.setPaused(bool);
                    }
                } else {
                    if ((paramActivityState != ActivityState.MENU) && (paramActivityState != ActivityState.TOP_SCORES));
                    //break label163;
                    startThemeMusic();
                }
                break;
            }
            this.state = paramActivityState;
            //while (true) {
            while (bool) {
                bool = false;
                releaseMediaPlayer();
            }
        }
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
                    //TTetris.this.tetrisScene.stopScoreSubmitting();
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

    private void startThemeMusic() {
        if (this.mediaPlayer == null) {
            this.mediaPlayer = MediaPlayer.create(this, 2130968581);
            if (this.mediaPlayer != null)
                this.mediaPlayer.start();
        }
    }

    protected void onAssetManagerCreated(AssetManager paramAssetManager) {
        System.out.println("loadTextures before");
        paramAssetManager.loadTextures("Textures");
        System.out.println("loadTextures finished");
        paramAssetManager.loadMeshes("Meshes");
    }

    public void onBackPressed() {
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

    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        saveGame(this.tetrisScene.getGame());
    }

    protected void onStart() {
        super.onStart();
        TetrisScene localTetrisScene = this.tetrisScene;
        Game localGame = null;
        //boolean bool = false;
        if (localTetrisScene != null) {
            localGame = this.tetrisScene.getGame();
            //bool = true;
            //bool = this.tetrisScene.isStopScoreSubmitting();
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
            //if (bool)
            //this.tetrisScene.stopScoreSubmitting();
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
            System.out.println("got here 324");
            return;
            //label140:
            /*
              if ((localGame.getState() == GameState.PLAY) && (localGame.isActive()));
              for (i = 1; ; i = 0)
              break;
              label169: j = 8;*/
        }
    }
}
