package me.tepen.wheelwander;

import com.google.gson.annotations.SerializedName;

public class LoginResult {

    @SerializedName("message")
    public String message;
    @SerializedName("accessToken")
    public String accessToken;
}
