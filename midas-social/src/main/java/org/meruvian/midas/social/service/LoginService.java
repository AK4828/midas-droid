package org.meruvian.midas.social.service;

import org.meruvian.midas.core.entity.Authentication;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by akm on 22/10/15.
 */
public interface LoginService {
    @POST("/backend/oauth/token")
    Call<Authentication> requestTokenYamaID(@Header("Authorization") String authorization, @Header("Host") String host, @QueryMap Map<String,String> queryParam);

}
