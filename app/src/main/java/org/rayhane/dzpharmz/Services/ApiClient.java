package org.rayhane.dzpharmz.Services;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rayhane on 30/04/2017.
 */

public class ApiClient {

    static String API_BASE_URL = "https://dzpharms.herokuapp.com/api/v1/";

    public static Retrofit getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        return retrofit;
    }


}
