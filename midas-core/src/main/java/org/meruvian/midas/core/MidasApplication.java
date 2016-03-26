package org.meruvian.midas.core;

import android.app.Application;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.meruvian.midas.core.interceptor.SecurityInterceptor;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;


/**
 * Created by ludviantoovandi on 06/03/15.
 */
public class MidasApplication extends Application {
    private static MidasApplication instance;

    private JobManager jobManager;
    private Retrofit retrofit;
    private ObjectMapper objectMapper;

    public MidasApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configureJobManager();
        configureRestAdaper();
    }


    private void configureRestAdaper() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new SecurityInterceptor());
        client.interceptors().add(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://yama2.meruvian.org")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    public static MidasApplication getInstance() {
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
    public JobManager getJobManager() {
        return jobManager;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
