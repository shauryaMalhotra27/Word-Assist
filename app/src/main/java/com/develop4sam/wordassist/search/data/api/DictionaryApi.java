package com.develop4sam.wordassist.search.data.api;

import com.develop4sam.wordassist.search.data.model.DictionaryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApi {
    // Changed to return single DictionaryResponse, not List
    @GET("entries/en/{word}")
    Call<DictionaryResponse> getMeaning(@Path("word") String word);
}