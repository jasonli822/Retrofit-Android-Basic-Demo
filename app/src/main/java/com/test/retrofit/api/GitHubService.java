package com.test.retrofit.api;

import com.test.retrofit.model.User;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("/users/{user}")
    public Call<User> getUser(@Path("user") String user);
}
