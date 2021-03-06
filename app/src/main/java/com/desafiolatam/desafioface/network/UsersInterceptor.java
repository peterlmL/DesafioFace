package com.desafiolatam.desafioface.network;

import com.desafiolatam.desafioface.models.CurrentUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.desafiolatam.desafioface.network.SessionInterceptor.BASE_URL;


/**
 * Created by Pedro on 18-07-2017.
 */

public class UsersInterceptor {


    public Users get(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request request = originalRequest.newBuilder()
                    /*Common headers*/
                        .header("authtoken", CurrentUser.listAll(CurrentUser.class).get(0).getAuth_token())
                        .header("Accept", "application/json")
                    /*Custom header*/
                        .header("Source", "mobile")
                        .build();

                okhttp3.Response response = chain.proceed(request);

            /*If the request fail then you get 3 retries*/
                int retryCount = 0;
                while (!response.isSuccessful() && retryCount < 3) {
                    retryCount++;
                    response = chain.proceed(request);
                }

                return response;
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit interceptor = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        Users user = interceptor.create(Users.class);

        return user;
    }

}
