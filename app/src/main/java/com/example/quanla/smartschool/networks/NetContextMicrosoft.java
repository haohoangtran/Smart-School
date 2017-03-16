package com.example.quanla.smartschool.networks;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tranh on 3/11/2017.
 */

public class NetContextMicrosoft {
    private final int TIME_SECOND_OUT = 15;
    public static final NetContextMicrosoft instance = new NetContextMicrosoft();
    private final String api = "https://westus.api.cognitive.microsoft.com/face/v1.0/";
    private final String key1 = "82508c1dc56c4a5b9f85254a5dbeef43";
    private final String key2 = "35b44c6a86de4cc0ad940e999225036b";
    private Retrofit retrofit;

    private NetContextMicrosoft() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(TIME_SECOND_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_SECOND_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggerInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(Class<T> classz) {
        return retrofit.create(classz);
    }

    class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Ocp-Apim-Subscription-Key", key1)
                    .build();
            return chain.proceed(request);

        }
    }

    class LoggerInterceptor implements Interceptor {

        private static final String TAG = "LoggerInterceptor";

        @Override
        public Response intercept(Chain chain) throws IOException {
            //1 Get request
            Request request = chain.request();

            //2 Process request (print out)
            Log.d(TAG, String.format("url: %s", request.toString()));

            RequestBody body = request.body();
            if (body != null) {
                Log.d(TAG, String.format("body: %s", body.toString()));
            }

            Headers headers = request.headers();
            if (headers != null) {
                Log.d(TAG, String.format(String.format("headers: %s", headers.toString())));
            }

            //3 Proceed
            Response response = chain.proceed(request);

            //4 Process response
            Log.d(TAG, String.format("response: %s", response.toString()));

            Log.d(TAG, String.format("response body: %s", getResponseString(response)));

            return response;
        }
    }

    private String getResponseString(Response response) {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        return buffer.clone().readString(Charset.forName("UTF-8"));
    }

}
