package dev.ttetris.util;

import dev.ttetris.util.ServerTaskResult;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;

public abstract class ServerTask<Params, Progress> extends AsyncTask<Params, Progress, ServerTaskResult> {
    private static final String TAG = "ServerTask";

    protected final ServerTaskResult doInBackground(Params[] paramArrayOfParams) {
        try {
            AbstractHttpClient localAbstractHttpClient = getHttpClient(paramArrayOfParams);
            HttpUriRequest localHttpUriRequest = getHttpRequest(paramArrayOfParams);
            if (isCancelled())
                return ServerTaskResult.CANCEL;
            ServerTaskResult localServerTaskResult1;
            ServerTaskResult localServerTaskResult2;
            do {
                HttpResponse localHttpResponse = localAbstractHttpClient.execute(localHttpUriRequest);
                if (isCancelled())
                    return ServerTaskResult.CANCEL;
                localServerTaskResult1 = processServerResponse(localAbstractHttpClient, localHttpResponse, paramArrayOfParams);
                if (isCancelled())
                    return ServerTaskResult.CANCEL;
                localServerTaskResult2 = ServerTaskResult.REPEAT;
            }
            while (localServerTaskResult1 == localServerTaskResult2);
            return localServerTaskResult1;
        }
        catch (ClientProtocolException localClientProtocolException) {
            Log.e("ServerTask", "doInBackground", localClientProtocolException);
            return ServerTaskResult.SERVER_ERROR;
        }
        catch (IOException localIOException) {
            Log.e("ServerTask", "doInBackground", localIOException);
        }
        return ServerTaskResult.CONNECTION_ERROR;
    }

    protected abstract AbstractHttpClient getHttpClient(Params[] paramArrayOfParams);

    protected abstract HttpUriRequest getHttpRequest(Params[] paramArrayOfParams);

    protected abstract void onPostConnectionError();

    protected abstract void onPostDone();

    protected final void onPostExecute(ServerTaskResult paramServerTaskResult) {
        onPostDone();
        //switch ($SWITCH_TABLE$ru$igsoft$angam$ServerTaskResult()[paramServerTaskResult.ordinal()]) {
        switch (((ServerTaskResult)paramServerTaskResult).ordinal()) {
        case 2:
        case 3:
        default:
            return;
        case 1:
            onPostSuccess();
            return;
        case 4:
            onPostConnectionError();
            return;
        case 5:
        }
        onPostServerError();
    }

    protected abstract void onPostServerError();

    protected abstract void onPostSuccess();

    protected abstract ServerTaskResult processServerResponse(AbstractHttpClient paramAbstractHttpClient, HttpResponse paramHttpResponse, Params[] paramArrayOfParams);
}
