package org.meruvian.midas.showcase.activity.social;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.MidasApplication;
import org.meruvian.midas.core.defaults.DefaultActivity;
import org.meruvian.midas.showcase.R;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.showcase.activity.LoginActivity;
import org.meruvian.midas.showcase.activity.MainActivity;
import org.meruvian.midas.showcase.activity.NewMainActivity;
import org.meruvian.midas.social.SocialVariable;
import org.meruvian.midas.social.activity.WebViewActivity;
import org.meruvian.midas.social.task.dnaid.RefreshTokenDNAID;
import org.meruvian.midas.social.task.dnaid.RequestAccessDNAID;
import org.meruvian.midas.social.task.dnaid.RequestTokenDNAID;
import org.meruvian.midas.social.task.facebook.RefreshTokenFacebook;
import org.meruvian.midas.social.task.facebook.RequestAccessFacebook;
import org.meruvian.midas.social.task.facebook.RequestTokenFacebook;
import org.meruvian.midas.social.task.google.RefreshTokenGoogle;
import org.meruvian.midas.social.task.google.RequestAccessGoogle;
import org.meruvian.midas.social.task.google.RequestTokenGoogle;
import org.meruvian.midas.social.task.mervid.RefreshTokenMervID;
import org.meruvian.midas.social.task.mervid.RequestAccessMervID;
import org.meruvian.midas.social.task.mervid.RequestTokenMervID;
import org.meruvian.midas.social.task.v2mervid.RequestAccessV2MervID;
import org.meruvian.midas.social.task.v2mervid.RequestTokenV2MervID;
import org.meruvian.midas.social.task.yamaid.RefreshTokenYamaID;
import org.meruvian.midas.social.task.yamaid.RequestAccessYamaID;
import org.meruvian.midas.social.task.yamaid.RequestTokenYamaID;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class SocialLoginActivity extends DefaultActivity implements TaskService {
    @Bind({R.id.button_facebook_login, R.id.button_google_login, R.id.button_yamaid_login,R.id.button_dnaid_login,R.id.button_v2id_login})
    List<Button> logins;
    @Bind(R.id.text_skip)
    TextView skip;

    private ProgressDialog progressDialog;

    private SharedPreferences preferences;

    @Override
    public int layout() {
        return R.layout.activity_social_login;
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this);
        setTitle(R.string.social_login);
        MidasApplication application = MidasApplication.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialLoginActivity.this, LoginActivity.class));
                finish();
            }
        });

        updateViewFacebook();
        updateViewGoogle();
        updateViewYamaID();
        updateViewDnaID();
        updateViewV2ID();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SocialVariable.MERVID_REQUEST_ACCESS) {
                new RequestTokenMervID(this, this).execute(parseCode(data));
            } else if (requestCode == SocialVariable.FACEBOOK_REQUEST_ACCESS) {
                new RequestTokenFacebook(this, this).execute(parseCode(data));
            } else if (requestCode == SocialVariable.GOOGLE_REQUEST_ACCESS) {
                new RequestTokenGoogle(this, this).execute(parseCode(data));
            } else if (requestCode == SocialVariable.YAMAID_REQUEST_ACCESS) {
                new RequestTokenYamaID(this,this).execute(parseCode(data));
            } else if (requestCode == SocialVariable.DNAID_REQUEST_ACCESS) {
                new RequestTokenDNAID(this,this).execute(parseCode(data));
            } else if (requestCode == SocialVariable.V2ID_REQUEST_ACCESS) {
                new RequestTokenV2MervID(this,this).execute(parseCode(data));
            }
        }
    }

    @Override
    public void onExecute(int code) {
        progressDialog = new ProgressDialog(this);

        if (code == SocialVariable.TWITTER_REQUEST_ACCESS_TASK || code == SocialVariable.TWITTER_REQUEST_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.signing_in_twitter));
        } else if (code == SocialVariable.MERVID_REQUEST_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.signing_in_merv_id));
        } else if (code == SocialVariable.YAMAID_REQUEST_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.signing_in_yama));
        } else if (code == SocialVariable.DNAID_REQUEST_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.signing_in_dna));
        } else if (code == SocialVariable.V2ID_REQUEST_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.signing_in_v2));
        } else if (code == SocialVariable.FACEBOOK_REQUEST_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.signing_in_facebook));
        } else if (code == SocialVariable.GOOGLE_REQUEST_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.signing_in_google));
        } else if (code == SocialVariable.MERVID_REQUEST_ACCESS) {
            progressDialog.setMessage(getString(R.string.access_merv_id));
        } else if (code == SocialVariable.YAMAID_REQUEST_ACCESS){
            progressDialog.setMessage(getString(R.string.access_yama_id));
        } else if (code == SocialVariable.DNAID_REQUEST_ACCESS){
            progressDialog.setMessage(getString(R.string.access_dna_id));
        } else if (code == SocialVariable.V2ID_REQUEST_ACCESS){
            progressDialog.setMessage(getString(R.string.access_v2_id));
        } else if (code == SocialVariable.GOOGLE_REQUEST_ACCESS) {
            progressDialog.setMessage(getString(R.string.access_google));
        } else if (code == SocialVariable.FACEBOOK_REQUEST_ACCESS) {
            progressDialog.setMessage(getString(R.string.access_facebook));
        } else if (code == SocialVariable.MERVID_REFRESH_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.update_merv_id));
        } else if (code == SocialVariable.YAMAID_REFRESH_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.update_yama));
        } else if (code == SocialVariable.DNAID_REFRESH_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.update_dna));
        } else if (code == SocialVariable.V2ID_REFRESH_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.update_v2));
        } else if (code == SocialVariable.FACEBOOK_REFRESH_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.update_facebook));
        } else if (code == SocialVariable.GOOGLE_REFRESH_TOKEN_TASK) {
            progressDialog.setMessage(getString(R.string.update_google));
        }

        progressDialog.show();
    }

    @OnClick({R.id.button_facebook_login, R.id.button_google_login, R.id.button_yamaid_login, R.id.button_dnaid_login, R.id.button_v2id_login})
    public void setOnClickListener(Button button) {
        if (button.getId() == R.id.button_facebook_login) {
            onClickFacebook();
        } else if (button.getId() == R.id.button_google_login) {
            onClickGoogle();
        } else if (button.getId() == R.id.button_yamaid_login) {
            onClickYamaID();
        } else if (button.getId() == R.id.button_dnaid_login) {
            onClickDnaID();
        } else if (button.getId() == R.id.button_v2id_login) {
            onClickV2ID();
        }
    }

    @OnLongClick({R.id.button_facebook_login, R.id.button_google_login, R.id.button_yamaid_login,R.id.button_dnaid_login,R.id.button_v2id_login})
    public boolean setOnLongClickListener(Button button) {
        if (button.getId() == R.id.button_facebook_login) {
            onLongClickFacebook();
        } else if (button.getId() == R.id.button_google_login) {
            onLongClickGoogle();
        } else if (button.getId() == R.id.button_yamaid_login) {
            onLongClickYamaID();
        } else if (button.getId() == R.id.button_dnaid_login) {
            onLongClickDnaID();
        } else if (button.getId() == R.id.button_v2id_login) {
            onLongClickV2ID();
        }

        return true;
    }

    @Override
    public void onSuccess(int code, Object object) {
        progressDialog.dismiss();

        if (object != null) {
            if (code == SocialVariable.FACEBOOK_REQUEST_TOKEN_TASK) {
                String result = (String) object;
                if (result != null && !"".equalsIgnoreCase(result)) {
                    logins.get(0).setText(getString(R.string.logout_facebook));
                    startActivity(new Intent(this, NewMainActivity.class));
                }
            } else if (code == SocialVariable.GOOGLE_REQUEST_TOKEN_TASK) {
                String result = (String) object;
                if (result != null && !"".equalsIgnoreCase(result)) {
                    logins.get(1).setText(getString(R.string.logout_google));
                    startActivity(new Intent(this, NewMainActivity.class));
                }
            } else if (code == SocialVariable.YAMAID_REQUEST_TOKEN_TASK) {
                String result = (String) object;
                if (result != null && !"".equalsIgnoreCase(result)) {
                    logins.get(2).setText(getString(R.string.logout_yama));
                    startActivity(new Intent(this, NewMainActivity.class));
                }
            } else if (code == SocialVariable.DNAID_REQUEST_TOKEN_TASK) {
                String result = (String) object;
                if (result != null && !"".equalsIgnoreCase(result)) {
                    logins.get(3).setText(getString(R.string.login_dna));
                    startActivity(new Intent(this, NewMainActivity.class));
                }
            }
            else if (code == SocialVariable.V2ID_REQUEST_TOKEN_TASK) {
                String result = (String) object;
                if (result != null && !"".equalsIgnoreCase(result)) {
                    logins.get(4).setText(getString(R.string.login_v2));
                    startActivity(new Intent(this, NewMainActivity.class));
                }
            }
            else if (code == SocialVariable.MERVID_REQUEST_ACCESS) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", object.toString());
                intent.setData(Uri.parse(object.toString()));
                startActivityForResult(intent, SocialVariable.MERVID_REQUEST_ACCESS);
            } else if (code == SocialVariable.FACEBOOK_REQUEST_ACCESS) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", object.toString());
                intent.setData(Uri.parse(object.toString()));
                startActivityForResult(intent, SocialVariable.FACEBOOK_REQUEST_ACCESS);
            } else if (code == SocialVariable.GOOGLE_REQUEST_ACCESS) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", object.toString());
                intent.setData(Uri.parse(object.toString()));
                startActivityForResult(intent, SocialVariable.GOOGLE_REQUEST_ACCESS);
            } else if (code == SocialVariable.YAMAID_REQUEST_ACCESS) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", object.toString());
                intent.setData(Uri.parse(object.toString()));
                startActivityForResult(intent, SocialVariable.YAMAID_REQUEST_ACCESS);
            } else if (code == SocialVariable.DNAID_REQUEST_ACCESS) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", object.toString());
                intent.setData(Uri.parse(object.toString()));
                startActivityForResult(intent, SocialVariable.DNAID_REQUEST_ACCESS);
            }else if (code == SocialVariable.V2ID_REQUEST_ACCESS) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", object.toString());
                intent.setData(Uri.parse(object.toString()));
                startActivityForResult(intent, SocialVariable.V2ID_REQUEST_ACCESS);
            } else if (code == SocialVariable.MERVID_REFRESH_TOKEN_TASK) {
                Toast.makeText(this, getString(R.string.finish_update_merv_id), Toast.LENGTH_LONG).show();
            } else if (code == SocialVariable.FACEBOOK_REFRESH_TOKEN_TASK) {
                Toast.makeText(this, getString(R.string.finish_update_facebook), Toast.LENGTH_LONG).show();
            } else if (code == SocialVariable.GOOGLE_REFRESH_TOKEN_TASK) {
                Toast.makeText(this, getString(R.string.finish_update_google), Toast.LENGTH_LONG).show();
            } else if (code == SocialVariable.YAMAID_REFRESH_TOKEN_TASK) {
                Toast.makeText(this, getString(R.string.finish_update_yama), Toast.LENGTH_LONG).show();
            } else if (code == SocialVariable.DNAID_REFRESH_TOKEN_TASK) {
                Toast.makeText(this, getString(R.string.finish_update_dna), Toast.LENGTH_LONG).show();
            } else if (code == SocialVariable.V2ID_REFRESH_TOKEN_TASK) {
                Toast.makeText(this, getString(R.string.finish_update_v2), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.failed_recieve), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCancel(int code, String message) {

    }

    @Override
    public void onError(int code, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        progressDialog.dismiss();
    }

    private void onClickFacebook() {
        if (preferences.getBoolean("facebook", false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("facebook");
            editor.remove("facebook_token");
            editor.remove("facebook_token_type");
            editor.remove("facebook_expires_in");
            editor.remove("facebook_scope");
            editor.remove("facebook_jti");
            editor.commit();

            logins.get(0).setText(getString(R.string.login_facebook));
        } else {
            new RequestAccessFacebook(this, this).execute();
        }
    }

    private void onLongClickFacebook() {
        if (preferences.getBoolean("facebook", false)) {
            new RefreshTokenFacebook(this, this).execute();
        } else {
            Toast.makeText(this, getString(R.string.login_facebook), Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickGoogle() {
        if (preferences.getBoolean("google", false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("google");
            editor.remove("google_token");
            editor.remove("google_token_type");
            editor.remove("google_expires_in");
            editor.remove("google_scope");
            editor.remove("google_jti");
            editor.commit();

            logins.get(1).setText(getString(R.string.login_google));
        } else {
            new RequestAccessGoogle(this, this).execute();
        }
    }

    private void onLongClickGoogle() {
        if (preferences.getBoolean("google", false)) {
            new RefreshTokenGoogle(this, this).execute();
        } else {
            Toast.makeText(this, getString(R.string.login_google), Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickYamaID() {
        if (preferences.getBoolean("yamaid", false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("yamaid");
            editor.remove("yamaid_token");
            editor.remove("yamaid_token_type");
            editor.remove("yamaid_expires_in");
            editor.remove("yamaid_scope");
            editor.remove("yamaid_jti");
            editor.commit();

            logins.get(2).setText(getString(R.string.login_yama));
        } else {
            new RequestAccessYamaID(this, this).execute();
        }
    }

    private void onLongClickYamaID() {
        if (preferences.getBoolean("yamaid", false)) {
            new RefreshTokenYamaID(this, this).execute(preferences.getString("yamaid_token", ""));
        } else {
            Toast.makeText(this, getString(R.string.login_yama), Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickDnaID() {
        if (preferences.getBoolean("dnaid", false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("dnaid");
            editor.remove("dnaid_token");
            editor.remove("dnaid_token_type");
            editor.remove("dnaid_expires_in");
            editor.remove("dnaid_scope");
            editor.remove("dnaid_jti");
            editor.commit();

            logins.get(3).setText(getString(R.string.login_dna));
        } else {
            new RequestAccessDNAID(this, this).execute();
        }
    }

    private void onLongClickDnaID() {
        if (preferences.getBoolean("dnaid", false)) {
            new RefreshTokenDNAID(this, this).execute(preferences.getString("dnaid_token", ""));
        } else {
            Toast.makeText(this, getString(R.string.login_dna), Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickV2ID() {
        if (preferences.getBoolean("v2id", false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("v2id");
            editor.remove("v2id_token");
            editor.remove("v2id_token_type");
            editor.remove("v2id_expires_in");
            editor.remove("v2id_scope");
            editor.remove("v2id_jti");
            editor.commit();

            logins.get(4).setText(getString(R.string.login_v2));
        } else {
            new RequestAccessV2MervID(this, this).execute();
        }
    }

    private void onLongClickV2ID() {
        if (preferences.getBoolean("v2id", false)) {
            new RefreshTokenDNAID(this, this).execute(preferences.getString("v2id_token", ""));
        } else {
            Toast.makeText(this, getString(R.string.login_v2), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateViewGoogle() {
        if (preferences.getBoolean("google", false)) {
            logins.get(1).setText(getString(R.string.refresh_google));
        } else {
            logins.get(1).setText(getString(R.string.login_google));
        }
    }

    private void updateViewYamaID() {
        if (preferences.getBoolean("yamaid", false)) {
            logins.get(2).setText(getString(R.string.refresh_yama));
        } else {
            logins.get(2).setText(getString(R.string.login_yama));
        }
    }

    private void updateViewDnaID() {
        if (preferences.getBoolean("dnaid", false)) {
            logins.get(3).setText(getString(R.string.refresh_dna));
        } else {
            logins.get(3).setText(getString(R.string.login_dna));
        }
    }

    private void updateViewV2ID() {
        if (preferences.getBoolean("v2id", false)) {
            logins.get(4).setText(getString(R.string.refresh_v2));
        } else {
            logins.get(4).setText(getString(R.string.login_v2));
        }
    }

    private void updateViewFacebook() {
        if (preferences.getBoolean("facebook", false)) {
            logins.get(0).setText(getString(R.string.refresh_facebook));
        } else {
            logins.get(0).setText(getString(R.string.login_facebook));
        }
    }

    private String parseCode(Intent data) {
        Uri uri = data.getData();
        if (uri != null && uri.toString().startsWith(SocialVariable.MERVID_CALLBACK)) {
            String code = uri.getQueryParameter("code");
            if (code != null && !"".equalsIgnoreCase(code)) {
                return code;
            }
        }

        return "null";
    }
}