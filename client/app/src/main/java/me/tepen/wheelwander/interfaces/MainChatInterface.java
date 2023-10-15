package me.tepen.wheelwander.interfaces;

import java.util.HashMap;

import me.tepen.wheelwander.models.ChatResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MainChatInterface {
    @POST("/message/send")
    Call<ChatResult> sendMessage(@Header("authorization") String token, @Body HashMap<String, String> map);

}
