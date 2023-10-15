package me.tepen.wheelwander.models;

import com.google.gson.annotations.SerializedName;

public class LoginResult {

    @SerializedName("message")
    public String message;
    @SerializedName("accessToken")
    public String accessToken;

    @SerializedName("refreshToken")
    public String refreshToken;
    @SerializedName("userID")
    public String userID;
}
