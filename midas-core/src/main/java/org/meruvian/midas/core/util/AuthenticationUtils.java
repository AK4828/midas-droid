package org.meruvian.midas.core.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.meruvian.midas.core.MidasApplication;
import org.meruvian.midas.core.entity.Authentication;

import java.io.IOException;


/**
 * Created by akm on 22/10/15.
 */
public class AuthenticationUtils {
    private static final String AUTHENTICATION = "AUTHENTICATION";

    public static void registerAuthentication(Authentication authentication) {
        registerPreference(AUTHENTICATION, authentication);
    }

    public static Authentication getCurrentAuthentication(){
        return getObjectFromPreference(AUTHENTICATION, Authentication.class);
    }

    private static void registerPreference(String key, Object o) {
        ObjectMapper mapper = MidasApplication.getInstance().getObjectMapper();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MidasApplication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();

        try {
            editor.putString(key, mapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            Log.e(AuthenticationUtils.class.getSimpleName(), e.getMessage(), e);
        }

        editor.apply();
    }

    private static <T> T getObjectFromPreference(String key, Class<T> clazz) {
        ObjectMapper mapper = MidasApplication.getInstance().getObjectMapper();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MidasApplication.getInstance());
        String jsonAuth = preferences.getString(key, "");

        if (!jsonAuth.equals("")) {
            try {
                return mapper.readValue(jsonAuth, clazz);
            } catch (IOException e) {
                Log.e(AuthenticationUtils.class.getSimpleName(), e.getMessage(), e);
            }
        }

        return null;
    }
}
