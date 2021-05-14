package com.cherrio.blog.retrofit;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitClient {

    static RetrofitClient instance;
    private static String URL = "https://news.viraltrend.org/";

    public static RetrofitClient getInstance(){
        if (instance == null)
            instance = new RetrofitClient();
        return instance;
    }
    private static Retrofit getRetrofit(){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(new Persister(new AnnotationStrategy())))
                .client(client)
                .build();
    }
    public CallsApi getApi(){
        return getRetrofit().create(CallsApi.class);
    }
}
