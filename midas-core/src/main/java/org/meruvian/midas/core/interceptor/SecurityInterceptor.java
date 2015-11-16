package org.meruvian.midas.core.interceptor;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.meruvian.midas.core.entity.Authentication;
import org.meruvian.midas.core.util.AuthenticationUtils;

import java.io.IOException;

/**
 * Created by akm on 06/11/15.
 */
public class SecurityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Authentication auth = AuthenticationUtils.getCurrentAuthentication();

        if (auth != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer" + auth.getAccesToken())
                    .build();

            Log.d("aaaa",auth.getAccesToken());
        }

        return chain.proceed(request);
    }
}
