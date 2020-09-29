package com.example.helper.ui.currency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("rates?periodicity=0")
    Call<List<Post>> getPosts ();

    @GET("rates?&periodicity=0")
    Call<List<Post>> getDatePosts (@Query("ondate") String date);
}
