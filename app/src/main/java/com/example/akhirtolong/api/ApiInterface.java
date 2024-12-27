// ApiInterface.java
package com.example.akhirtolong.api;

import com.example.akhirtolong.Models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("everything")
    Call<NewsResponse> getNews(
            @Query("q") String query,
            @Query("language") String language,
            @Query("apiKey") String apiKey
    );
}
