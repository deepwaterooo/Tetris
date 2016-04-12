package dev.ttetris.util;

import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import dev.ttetris.util.AssetManager;
import dev.ttetris.util.SceneView;
import dev.ttetris.util.SceneView.OnAssetManagerCreated;

public class ActivityGame extends Activity {
    protected AlertDialog alertDialog;
    protected SceneView sceneView;

    protected void dismissAlertDialog() {
        if (this.alertDialog != null) {
            this.alertDialog.dismiss();
            this.alertDialog = null;
        }
    }

    protected void OnCancelAllTasks() {  
    }
    
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        switch (paramInt1) {
        default:
        case 0:
        }
        if (paramInt2 == -1) {
            return;
        }
    }

    protected void onAssetManagerCreated(AssetManager paramAssetManager) {
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        getWindow().addFlags(1152);
        this.sceneView = new SceneView(this, new SceneView.OnAssetManagerCreated() {
                public void onCreated(AssetManager paramAnonymousAssetManager) {
                    ActivityGame.this.onAssetManagerCreated(paramAnonymousAssetManager);
                }
            });
    }

    protected void onPause() {
        super.onPause();
        this.sceneView.onPause();
        dismissAlertDialog();
    }

    protected void onResume() {
        super.onResume();
        this.sceneView.onResume();
    }

    protected void showInfoDialog(int paramInt) {
        dismissAlertDialog();
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        this.alertDialog = localBuilder.create();
        this.alertDialog.show();
    }
}
