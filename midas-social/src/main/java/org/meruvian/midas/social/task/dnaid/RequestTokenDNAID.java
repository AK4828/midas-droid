package org.meruvian.midas.social.task.dnaid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.MidasApplication;
import org.meruvian.midas.core.entity.Authentication;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.AuthenticationUtils;
import org.meruvian.midas.core.util.ConnectionUtil;
import org.meruvian.midas.social.R;
import org.meruvian.midas.social.SocialVariable;
import org.meruvian.midas.social.service.LoginService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by akm on 21/10/15.
 */
public class RequestTokenDNAID extends AsyncTask<String, Void, String> {
    private TaskService service;
    private Context context;
    private Authentication authentication;

    public RequestTokenDNAID(TaskService service, Context context) {
        this.service = service;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SocialVariable.DNAID_REQUEST_TOKEN_TASK);
    }

    @Override
    protected String doInBackground(String... params) {
        Retrofit retrofit = MidasApplication.getInstance().getRetrofit();
        LoginService loginService = retrofit.create(LoginService.class);

        authentication = AuthenticationUtils.getCurrentAuthentication();

        Map<String, String> param = new HashMap<>();

        param.put("grant_type", GrantType.AUTHORIZATION_CODE.toString());
        param.put("redirect_uri", SocialVariable.DNA_CALLBACK);
        param.put("client_id", SocialVariable.DNA_APP_ID);
        param.put("client_secret", SocialVariable.DNA_API_SECRET);
        param.put("scope", "read write");
        param.put("code", params[0]);

        String authorization = new String(Base64.encode((SocialVariable.DNA_APP_ID + ":" + SocialVariable.DNA_API_SECRET).getBytes(), Base64.NO_WRAP));

        Call<Authentication> callAuth = loginService.requestTokenYamaID("Basic " + authorization, "id.dna.or.id", param);

        try {
            authentication = callAuth.execute().body();
            AuthenticationUtils.registerAuthentication(authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return authentication.getAccesToken();
    }

    @Override
    protected void onPostExecute(String accestoken) {
        service.onSuccess(SocialVariable.V2ID_REQUEST_TOKEN_TASK, accestoken);

    }

}

