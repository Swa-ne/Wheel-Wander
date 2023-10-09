package me.tepen.wheelwander.interfaces;

import java.util.HashMap;

import me.tepen.wheelwander.models.LoginResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface EntryInterface {
    @POST("/entry/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/entry/signup")
    Call<LoginResult> executeSignup(@Body HashMap<String, String> map);
    @POST("/entry/checkCurrentUser")
    Call<LoginResult> executeCheckCurrentUser(@Header("authorization") String token);
}
