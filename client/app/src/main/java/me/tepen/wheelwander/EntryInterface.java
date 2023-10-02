package me.tepen.wheelwander;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface EntryInterface {
    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

//    @POST("/signup")
//    Call<Void> executeSignup(@Body HashMap<String, String> map);
}
