package com.develop4sam.wordassist.search.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static DictionaryApi api;

    public static DictionaryApi getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.opendictionaryapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(DictionaryApi.class);
        }
        return api;
    }
}