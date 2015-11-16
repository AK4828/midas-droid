package org.meruvian.midas.social.task.yamaid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.social.R;
import org.meruvian.midas.social.SocialVariable;


/**
 * Created by meruvian on 26/06/15.
 */

public class RequestAccessYamaID extends AsyncTask<Void, Void, String> {
    private Context context;
    private TaskService service;

    public RequestAccessYamaID(Context context, TaskService service) {
        this.context = context;
        this.service = service;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SocialVariable.YAMAID_REQUEST_ACCESS);
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            OAuthClientRequest request = OAuthClientRequest.authorizationLocation(SocialVariable.YAMA_AUTH_URL)
                    .setResponseType(ResponseType.CODE.toString())
                    .setClientId(SocialVariable.YAMA_APP_ID)
                    .setRedirectURI(SocialVariable.YAMA_CALLBACK)
                    .setScope("read write")
                    .buildQueryMessage();

            Log.d(getClass().getSimpleName(), "URI Request Access YAMAId: " + request.getLocationUri());

            return request.getLocationUri();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        if (string != null) {
            service.onSuccess(SocialVariable.YAMAID_REQUEST_ACCESS, string);
        } else {
            service.onError(SocialVariable.YAMAID_REQUEST_ACCESS, context.getString(R.string.failed_recieve));
        }
    }
}



















