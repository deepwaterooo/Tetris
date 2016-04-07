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
import android.view.View.OnClickListener;
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
import dev.angam.GameActivity;
import dev.angam.LoginListener;
import dev.angam.ServerTask;
import dev.angam.ServerTaskResult;
import dev.angam.SoundManager;
import dev.anogl.AssetManager;
import dev.anogl.SceneView;
import dev.tetris.common.GameResult;
import dev.tetris.common.TopScore;
import dev.tetris.model.Board;
import dev.tetris.model.Game;
import dev.tetris.model.GameAdapter;
import dev.tetris.model.GameState;

public class TetrisActivity extends GameActivity {
    private static final int DEFAULT_ROW_COUNT = 20;
    private static final String SAVE_GAME_FILE = "Tetris.sav";
    private static final String SERVER_URI = "http://ag-server1.appspot.com";
    private static final String TAG = "TetrisActivity";
    private View changeBoardButton;
    private View continueButton;
    private Button dailyTopScoresButton;
    private ViewGroup gameView;
    private Button globalTopScoresButton;
    private TextView levelTextView;
    private ViewGroup loadingView;
    private MediaPlayer mediaPlayer;
    private ViewGroup menuView;
    private View moveFigureLeftButton;
    private View moveFigureRightButton;
    private View newGameButton;
    private ReceiveTopScoresTask receiveTopScoresTask;
    private View rotateFigureLeftButton;
    private View rotateFigureRightButton;
    private TextView scoreTextView;
    private SoundManager soundManager;
    private ActivityState state;
    private View submitButton;
    private SubmitScoreTask submitScoreTask;
    private TetrisScene tetrisScene;
    private View throwFigureButton;
    private View topScoresButton;
    private ScrollView topScoresScrollView;
    private TableLayout topScoresTable;
    private View topScoresView;
    private ViewGroup uiLayout;
    private Vibrator vibrator;

    private void bindGameListener(Game paramGame) {
        paramGame.addListener(new GameAdapter() {
                private int prevLevel = -1;

                public void boardChangeDenied() {
                    TetrisActivity.this.vibrator.vibrate(50L);
                }

                public void boardChanged(Board paramAnonymousBoard) {
                    TetrisActivity.this.soundManager.play("ChangeBoard");
                }

                public void figureFreezed() {
                    TetrisActivity.this.soundManager.play("FreezeFigure");
                }

                public void figureRotateDenied() {
                    TetrisActivity.this.vibrator.vibrate(50L);
                }

                public void gameOver() {
                    TetrisActivity.this.runOnUiThread(new Runnable() {
                            public void run()
                            {
                                TetrisActivity.this.changeState(ActivityState.MENU);
                                TetrisActivity.this.showGameOverDialog();
                            }
                        });
                }

                public void levelChanged(final int paramAnonymousInt) {
                    if (this.prevLevel != -1)
                        TetrisActivity.this.soundManager.play("NextLevel");
                    this.prevLevel = paramAnonymousInt;
                    TetrisActivity.this.runOnUiThread(new Runnable() {
                            public void run()
                            {
                                TetrisActivity.this.levelTextView.setText(TetrisActivity.this.getString(2131034137) + " " + paramAnonymousInt);
                            }
                        });
                }

                public void lineDisappearing() {
                    TetrisActivity.this.soundManager.play("FireLine");
                }

                public void pointsChanged(final int paramAnonymousInt) {
                    TetrisActivity.this.runOnUiThread(new Runnable() {
                            public void run()
                            {
                                TetrisActivity.this.scoreTextView.setText(TetrisActivity.this.getString(2131034136) + " " + paramAnonymousInt);
                            }
                        });
                }

                public void stateChanged(final GameState paramAnonymousGameState) {
                    TetrisActivity.this.runOnUiThread(new Runnable() {
                            public void run()
                            {
                                if (paramAnonymousGameState == GameState.SUBMIT_SCORE) {
                                    TetrisActivity.this.continueButton.setVisibility(8);
                                    TetrisActivity.this.submitButton.setVisibility(0);
                                    return;
                                }
                                TetrisActivity.this.submitButton.setVisibility(8);
                            }
                        });
                }
            });
    }

    private void bindInputListeners() {
        this.continueButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.changeState(ActivityState.GAME);
                }
            });
        this.submitButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.submitScore();
                }
            });
        this.newGameButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Game localGame = new Game();
                    TetrisActivity.this.bindGameListener(localGame);
                    TetrisActivity.this.tetrisScene = new TetrisScene(localGame);
                    TetrisActivity.this.sceneView.setScene(TetrisActivity.this.tetrisScene);
                    TetrisActivity.this.continueButton.setVisibility(0);
                    TetrisActivity.this.changeState(ActivityState.GAME);
                }
            });
        this.topScoresButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.showTopScores(false);
                }
            });
        this.moveFigureLeftButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.tetrisScene.moveFigureLeft();
                }
            });
        this.moveFigureRightButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.tetrisScene.moveFigureRight();
                }
            });
        this.rotateFigureLeftButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.tetrisScene.rotateFigureLeft();
                }
            });
        this.rotateFigureRightButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.tetrisScene.rotateFigureRight();
                }
            });
        this.throwFigureButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.tetrisScene.throwFigure();
                }
            });
        this.changeBoardButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.tetrisScene.changeBoard();
                }
            });
        this.dailyTopScoresButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.showTopScores(false);
                }
            });
        this.globalTopScoresButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    TetrisActivity.this.showTopScores(true);
                }
            });
    }

    private void cancelReceiveTopScoresTask() {
        if (this.receiveTopScoresTask != null) {
            Log.i("TetrisActivity", "Cancel ReceiveTopScoresTask: " + this.receiveTopScoresTask.cancel(false));
            this.receiveTopScoresTask = null;
            dismissAlertDialog();
        }
    }

    private void cancelSubmitTask() {
        if (this.submitScoreTask != null) {
            Log.i("TetrisActivity", "Cancel SubmitScoreTask: " + this.submitScoreTask.cancel(false));
            this.submitScoreTask = null;
            dismissAlertDialog();
        }
    }

    private void changeState(ActivityState paramActivityState) {
        boolean bool;
        if (this.state != paramActivityState) {
            this.uiLayout.removeAllViews();
            switch ($SWITCH_TABLE$ru$igsoft$tetris$ActivityState()[paramActivityState.ordinal()]) {
            default:
                if (this.tetrisScene != null) {
                    TetrisScene localTetrisScene = this.tetrisScene;
                    if (paramActivityState != ActivityState.GAME) {                        
                        bool = true;
                        label73: localTetrisScene.setPaused(bool);
                    }
                } else {
                    if ((paramActivityState != ActivityState.MENU) && (paramActivityState != ActivityState.TOP_SCORES))
                        break label163;
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
            return;
            this.uiLayout.addView(this.loadingView);
            break;
            this.uiLayout.addView(this.menuView);
            break;
            this.uiLayout.addView(this.gameView);
            break;
            this.uiLayout.addView(this.topScoresView);
            break;
            bool = false;
            break label73;
            label163: releaseMediaPlayer();
        }
    }

    // ERROR //
    private Game loadGame() {
        // Byte code:
        //   0: aconst_null
        //   1: astore_1
        //   2: aload_0
        //   3: ldc 13
        //   5: invokevirtual 363	ru/igsoft/tetris/TetrisActivity:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
        //   8: astore_1
        //   9: new 365	java/io/ObjectInputStream
        //   12: dup
        //   13: aload_1
        //   14: invokespecial 368	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
        //   17: invokevirtual 372	java/io/ObjectInputStream:readObject	()Ljava/lang/Object;
        //   20: checkcast 221	ru/igsoft/tetris/model/Game
        //   23: astore 10
        //   25: aload_1
        //   26: ifnull +7 -> 33
        //   29: aload_1
        //   30: invokevirtual 377	java/io/FileInputStream:close	()V
        //   33: aload 10
        //   35: areturn
        //   36: astore 7
        //   38: ldc 19
        //   40: ldc_w 378
        //   43: aload 7
        //   45: invokestatic 382	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   48: pop
        //   49: aload_1
        //   50: ifnull +7 -> 57
        //   53: aload_1
        //   54: invokevirtual 377	java/io/FileInputStream:close	()V
        //   57: aconst_null
        //   58: areturn
        //   59: astore 4
        //   61: ldc 19
        //   63: ldc_w 378
        //   66: aload 4
        //   68: invokestatic 382	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   71: pop
        //   72: aload_1
        //   73: ifnull -16 -> 57
        //   76: aload_1
        //   77: invokevirtual 377	java/io/FileInputStream:close	()V
        //   80: goto -23 -> 57
        //   83: astore 6
        //   85: goto -28 -> 57
        //   88: astore_2
        //   89: aload_1
        //   90: ifnull +7 -> 97
        //   93: aload_1
        //   94: invokevirtual 377	java/io/FileInputStream:close	()V
        //   97: aload_2
        //   98: athrow
        //   99: astore 11
        //   101: aload 10
        //   103: areturn
        //   104: astore 9
        //   106: goto -49 -> 57
        //   109: astore_3
        //   110: goto -13 -> 97
        //
        // Exception table:
        //   from	to	target	type
        //   2	25	36	java/io/IOException
        //   2	25	59	java/lang/ClassNotFoundException
        //   76	80	83	java/io/IOException
        //   2	25	88	finally
        //   38	49	88	finally
        //   61	72	88	finally
        //   29	33	99	java/io/IOException
        //   53	57	104	java/io/IOException
        //   93	97	109	java/io/IOException
    }

    private void releaseMediaPlayer() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    // ERROR //
    private void saveGame(Game paramGame) {
        // Byte code:
        //   0: aconst_null
        //   1: astore_2
        //   2: aload_0
        //   3: ldc 13
        //   5: iconst_0
        //   6: invokevirtual 394	ru/igsoft/tetris/TetrisActivity:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
        //   9: astore_2
        //   10: new 396	java/io/ObjectOutputStream
        //   13: dup
        //   14: aload_2
        //   15: invokespecial 399	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
        //   18: aload_1
        //   19: invokevirtual 403	java/io/ObjectOutputStream:writeObject	(Ljava/lang/Object;)V
        //   22: aload_2
        //   23: ifnull +7 -> 30
        //   26: aload_2
        //   27: invokevirtual 406	java/io/FileOutputStream:close	()V
        //   30: return
        //   31: astore 5
        //   33: ldc 19
        //   35: ldc_w 407
        //   38: aload 5
        //   40: invokestatic 382	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   43: pop
        //   44: aload_2
        //   45: ifnull -15 -> 30
        //   48: aload_2
        //   49: invokevirtual 406	java/io/FileOutputStream:close	()V
        //   52: return
        //   53: astore 7
        //   55: return
        //   56: astore_3
        //   57: aload_2
        //   58: ifnull +7 -> 65
        //   61: aload_2
        //   62: invokevirtual 406	java/io/FileOutputStream:close	()V
        //   65: aload_3
        //   66: athrow
        //   67: astore 4
        //   69: goto -4 -> 65
        //   72: astore 8
        //   74: return
        //
        // Exception table:
        //   from	to	target	type
        //   2	22	31	java/io/IOException
        //   48	52	53	java/io/IOException
        //   2	22	56	finally
        //   33	44	56	finally
        //   61	65	67	java/io/IOException
        //   26	30	72	java/io/IOException
    }

    private void showGameOverDialog() {
        dismissAlertDialog();
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setMessage(getString(2131034132) + " " + this.tetrisScene.getGameScore() + ". " + getString(2131034131));
        localBuilder.setTitle(2131034135);
        localBuilder.setPositiveButton(2131034127, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    TetrisActivity.this.dismissAlertDialog();
                    TetrisActivity.this.submitScore();
                }
            });
        localBuilder.setNegativeButton(2131034129, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    TetrisActivity.this.dismissAlertDialog();
                    TetrisActivity.this.tetrisScene.stopScoreSubmitting();
                }
            });
        this.alertDialog = localBuilder.create();
        this.alertDialog.show();
    }

    private void showReceivingTopScoresProgressDialog() {
        dismissAlertDialog();
        this.alertDialog = ProgressDialog.show(this, "", getString(2131034134), false, true, new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface paramAnonymousDialogInterface) {
                    TetrisActivity.this.alertDialog = null;
                    TetrisActivity.this.cancelReceiveTopScoresTask();
                }
            });
    }

    private void showSubmittingProgressDialog() {
        dismissAlertDialog();
        this.alertDialog = ProgressDialog.show(this, "", getString(2131034133), false, true, new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface paramAnonymousDialogInterface) {
                    TetrisActivity.this.alertDialog = null;
                    TetrisActivity.this.cancelSubmitTask();
                }
            });
    }

    private void showTopScores(boolean paramBoolean) {
        new ReceiveTopScoresTask(paramBoolean).execute(new Void[0]);
    }

    private void startThemeMusic() {
        if (this.mediaPlayer == null) {
            this.mediaPlayer = MediaPlayer.create(this, 2130968581);
            if (this.mediaPlayer != null)
                this.mediaPlayer.start();
        }
    }

    private void submitScore() {
        login("http://ag-server1.appspot.com", new LoginListener() {
                public void onLogin(Cookie paramAnonymousCookie) {
                    new TetrisActivity.SubmitScoreTask(TetrisActivity.this, paramAnonymousCookie, TetrisActivity.this.tetrisScene.getGameResult()).execute(new Void[0]);
                }
            });
    }

    protected void OnCancelAllTasks() {
        super.OnCancelAllTasks();
        cancelSubmitTask();
        cancelReceiveTopScoresTask();
    }

    protected void onAssetManagerCreated(AssetManager paramAssetManager) {
        paramAssetManager.loadTextures("Textures");
        paramAssetManager.loadMeshes("Meshes");
    }

    public void onBackPressed() {
        switch ($SWITCH_TABLE$ru$igsoft$tetris$ActivityState()[this.state.ordinal()]) {
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
        this.loadingView = ((ViewGroup)getLayoutInflater().inflate(2130903041, null));
        this.menuView = ((ViewGroup)getLayoutInflater().inflate(2130903042, null));
        this.continueButton = this.menuView.findViewById(2131165197);
        this.submitButton = this.menuView.findViewById(2131165198);
        this.newGameButton = this.menuView.findViewById(2131165199);
        this.topScoresButton = this.menuView.findViewById(2131165200);
        this.gameView = ((ViewGroup)getLayoutInflater().inflate(2130903040, null));
        this.levelTextView = ((TextView)this.gameView.findViewById(2131165192));
        this.scoreTextView = ((TextView)this.gameView.findViewById(2131165191));
        this.moveFigureLeftButton = this.gameView.findViewById(2131165185);
        this.moveFigureRightButton = this.gameView.findViewById(2131165186);
        this.rotateFigureLeftButton = this.gameView.findViewById(2131165187);
        this.rotateFigureRightButton = this.gameView.findViewById(2131165189);
        this.throwFigureButton = this.gameView.findViewById(2131165188);
        this.changeBoardButton = this.gameView.findViewById(2131165190);
        this.topScoresView = getLayoutInflater().inflate(2130903043, null);
        this.topScoresTable = ((TableLayout)this.topScoresView.findViewById(2131165206));
        this.topScoresScrollView = ((ScrollView)this.topScoresView.findViewById(2131165205));
        this.dailyTopScoresButton = ((Button)this.topScoresView.findViewById(2131165203));
        this.globalTopScoresButton = ((Button)this.topScoresView.findViewById(2131165204));
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
        View localView;
        int j;
        if (localGame == null) {
            i = 0;
            localGame = loadGame();
            if (localGame != null)
                break label140;
            localGame = new Game();
            bindGameListener(localGame);
            localView = this.continueButton;
            j = 0;
            if (i == 0)
                break label169;
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
                        TetrisActivity.this.runOnUiThread(new Runnable()
                            {
                                public void run() {
                                    TetrisActivity.this.changeState(ActivityState.MENU);
                                }
                            });
                    }
                });
            return;
            label140: if ((localGame.getState() == GameState.PLAY) && (localGame.isActive()));
            for (i = 1; ; i = 0)
                break;
            label169: j = 8;
        }
    }

    private class ReceiveTopScoresTask extends ServerTask<Void, Void> {
        private boolean globalTopScores;
        private List<TopScore> topScores;

        public ReceiveTopScoresTask(boolean arg2) {
            boolean bool;
            this.globalTopScores = bool;
        }

        protected AbstractHttpClient getHttpClient(Void[] paramArrayOfVoid) {
            return new DefaultHttpClient();
        }

        protected HttpUriRequest getHttpRequest(Void[] paramArrayOfVoid) {
            if (this.globalTopScores)
                return new HttpGet("http://ag-server1.appspot.com/globalTopScores");
            return new HttpGet("http://ag-server1.appspot.com/dailyTopScores");
        }

        protected void onPostConnectionError() {
            TetrisActivity.this.showConnectionErrorDialog();
        }

        protected void onPostDone() {
            TetrisActivity.this.receiveTopScoresTask = null;
            TetrisActivity.this.dismissAlertDialog();
        }

        protected void onPostServerError() {
            TetrisActivity.this.showServerErrorDialog();
        }

        protected void onPostSuccess() {
            TetrisActivity.this.topScoresTable.removeAllViews();
            int i = this.topScores.size();
            int j = Math.max(20, i);
            int k = 0;
            if (k >= j) {
                if (!this.globalTopScores)
                    break label349;
                TetrisActivity.this.dailyTopScoresButton.setEnabled(true);
                TetrisActivity.this.dailyTopScoresButton.setTypeface(Typeface.DEFAULT);
                TetrisActivity.this.globalTopScoresButton.setEnabled(false);
                TetrisActivity.this.globalTopScoresButton.setTypeface(Typeface.DEFAULT_BOLD);
            }
            while (true) {
                TetrisActivity.this.topScoresScrollView.scrollTo(0, 0);
                TetrisActivity.this.changeState(ActivityState.TOP_SCORES);
                return;
                TextView localTextView1 = new TextView(TetrisActivity.this);
                localTextView1.setPadding(6, 3, 6, 3);
                localTextView1.setTextColor(Color.parseColor(TetrisActivity.this.getString(2131099651)));
                TextView localTextView2 = new TextView(TetrisActivity.this);
                localTextView2.setPadding(6, 3, 6, 3);
                localTextView2.setTextColor(Color.parseColor(TetrisActivity.this.getString(2131099651)));
                if (k < i) {
                    TopScore localTopScore = (TopScore)this.topScores.get(k);
                    localTextView1.setText(k + 1 + ". " + localTopScore.getPlayerName());
                    localTextView2.setText(String.valueOf(localTopScore.getHighScore()));
                }
                TableRow localTableRow = new TableRow(TetrisActivity.this);
                if ((k & 0x1) == 0)
                    localTableRow.setBackgroundColor(Color.parseColor(TetrisActivity.this.getString(2131099649)));
                while (true) {
                    localTableRow.addView(localTextView1);
                    localTableRow.addView(localTextView2);
                    TetrisActivity.this.topScoresTable.addView(localTableRow);
                    k++;
                    break;
                    localTableRow.setBackgroundColor(Color.parseColor(TetrisActivity.this.getString(2131099650)));
                }
                label349: TetrisActivity.this.dailyTopScoresButton.setEnabled(false);
                TetrisActivity.this.dailyTopScoresButton.setTypeface(Typeface.DEFAULT_BOLD);
                TetrisActivity.this.globalTopScoresButton.setEnabled(true);
                TetrisActivity.this.globalTopScoresButton.setTypeface(Typeface.DEFAULT);
            }
        }

        protected void onPreExecute() {
            TetrisActivity.this.cancelReceiveTopScoresTask();
            TetrisActivity.this.receiveTopScoresTask = this;
            TetrisActivity.this.showReceivingTopScoresProgressDialog();
        }

        protected ServerTaskResult processServerResponse(AbstractHttpClient paramAbstractHttpClient, HttpResponse paramHttpResponse, Void[] paramArrayOfVoid) {
            try {
                this.topScores = ((List)new ObjectInputStream(paramHttpResponse.getEntity().getContent()).readObject());
                ServerTaskResult localServerTaskResult = ServerTaskResult.SUCCESS;
                return localServerTaskResult;
            }
            catch (IOException localIOException) {
                Log.e("TetrisActivity", "processServerResponse", localIOException);
                return ServerTaskResult.SERVER_ERROR;
            }
            catch (ClassNotFoundException localClassNotFoundException) {
                while (true)
                    Log.e("TetrisActivity", "processServerResponse", localClassNotFoundException);
            }
        }
    }

    private class SubmitScoreTask extends ServerTask<Void, Void> {
        private GameResult gameResult;
        private Cookie loginCookie;

        public SubmitScoreTask(Cookie paramGameResult, GameResult arg3) {
            this.loginCookie = paramGameResult;
            Object localObject;
            this.gameResult = localObject;
        }

        protected AbstractHttpClient getHttpClient(Void[] paramArrayOfVoid) {
            DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
            localDefaultHttpClient.getCookieStore().addCookie(this.loginCookie);
            return localDefaultHttpClient;
        }

        protected HttpUriRequest getHttpRequest(Void[] paramArrayOfVoid) {
            HttpPost localHttpPost = new HttpPost("http://ag-server1.appspot.com/submitScore");
            localHttpPost.setEntity(new EntityTemplate(new ContentProducer() {
                    public void writeTo(OutputStream paramAnonymousOutputStream)
                        throws IOException {
                        new ObjectOutputStream(paramAnonymousOutputStream).writeObject(TetrisActivity.SubmitScoreTask.this.gameResult);
                    }
                }));
            return localHttpPost;
        }

        protected void onPostConnectionError() {
            TetrisActivity.this.showConnectionErrorDialog();
        }

        protected void onPostDone() {
            TetrisActivity.this.submitScoreTask = null;
            TetrisActivity.this.dismissAlertDialog();
        }

        protected void onPostServerError() {
            TetrisActivity.this.showServerErrorDialog();
        }

        protected void onPostSuccess() {
            TetrisActivity.this.tetrisScene.stopScoreSubmitting();
            TetrisActivity.this.showInfoDialog(2131034130);
        }

        protected void onPreExecute() {
            TetrisActivity.this.cancelSubmitTask();
            TetrisActivity.this.submitScoreTask = this;
            TetrisActivity.this.showSubmittingProgressDialog();
        }

        protected ServerTaskResult processServerResponse(AbstractHttpClient paramAbstractHttpClient, HttpResponse paramHttpResponse, Void[] paramArrayOfVoid) {
            int i = paramHttpResponse.getStatusLine().getStatusCode();
            if (i == 200)
                return ServerTaskResult.SUCCESS;
            Log.e("TetrisActivity", "SubmitScoreTask: Request failed. Status code=" + i);
            return ServerTaskResult.SERVER_ERROR;
        }
    }
}
