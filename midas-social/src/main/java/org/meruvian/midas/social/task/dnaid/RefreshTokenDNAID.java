package org.meruvian.midas.social.task.dnaid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;
import org.meruvian.midas.social.R;
import org.meruvian.midas.social.SocialVariable;

/**
 * Created by akm on 21/10/15.
 */
public class RefreshTokenDNAID extends AsyncTask<String,Void, JSONObject> {

    private TaskService service;
    private Context context;

    public RefreshTokenDNAID(TaskService service, Context context){
        this.service = service;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SocialVariable.DNAID_REFRESH_TOKEN_TASK);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(SocialVariable.DNA_REQUEST_TOKEN)
                    .setGrantType(GrantType.REFRESH_TOKEN)
                    .setClientId(SocialVariable.DNA_APP_ID)
                    .setClientSecret(SocialVariable.DNA_API_SECRET)
                    .setRefreshToken(params[0])
                    .buildQueryMessage();
            return ConnectionUtil.get(request.getLocationUri());

        }catch (OAuthSystemException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                Log.i(getClass().getSimpleName(), "json : " + jsonObject.toString());

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putBoolean("dnaid", true);
                editor.putString("dnaid_token", jsonObject.getString("access_token"));
                editor.putString("dnaid_refresh_token", jsonObject.getString("refresh_token"));
                editor.putString("dnaid_token_type", jsonObject.getString("token_type"));
                editor.putLong("dnaid_expires_in", jsonObject.getLong("expires_in"));
                editor.putString("dnaid_scope", jsonObject.getString("scope"));
                editor.putString("dnaid_jti", jsonObject.getString("jti"));
                editor.commit();

                service.onSuccess(SocialVariable.DNAID_REQUEST_TOKEN_TASK, jsonObject.getString("access_token"));

            }catch (JSONException e) {
                e.printStackTrace();
                Log.e(getClass().getSimpleName(), e.getMessage(), e);
                service.onError(SocialVariable.DNAID_REQUEST_TOKEN_TASK, context.getString(R.string.failed_recieve));
            }
        }
    }
}
