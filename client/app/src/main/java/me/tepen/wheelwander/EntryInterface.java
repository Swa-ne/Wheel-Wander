package me.tepen.wheelwander;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface EntryInterface {
    @POST("/entry/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/entry/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);
}
