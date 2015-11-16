package org.meruvian.midas.social.task.v2mervid;

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
 * Created by akm on 21/10/15.
 */
public class RequestAccessV2MervID extends AsyncTask<Void, Void, String> {

    private Context context;
    private TaskService service;

    public RequestAccessV2MervID(Context context, TaskService service){
        this.context = context;
        this.service = service;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SocialVariable.V2ID_REQUEST_ACCESS);
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            OAuthClientRequest request = OAuthClientRequest.authorizationLocation(SocialVariable.V2_AUTH_URL)
                    .setClientId(SocialVariable.V2_APP_ID)
                    .setRedirectURI(SocialVariable.V2_CALLBACK)
                    .setResponseType(ResponseType.CODE.toString())
                    .setScope("read write")
                    .buildQueryMessage();

            Log.d(getClass().getSimpleName(), "URI Request Access DNAId: " + request.getLocationUri());

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
            service.onSuccess(SocialVariable.V2ID_REQUEST_ACCESS, string);
        } else {
            service.onError(SocialVariable.V2ID_REQUEST_ACCESS, context.getString(R.string.failed_recieve));
        }
    }
}
