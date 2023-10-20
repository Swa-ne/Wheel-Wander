package me.tepen.wheelwander.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.tepen.wheelwander.models.UploadResult;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadCarInterface {
    @POST("/market/uploadImage")
    @Multipart
    Call<UploadResult> uploadVehicleImage(@Part List<MultipartBody.Part> image);
    @POST("/market/uploadInformation")
    Call<UploadResult> uploadVehicleInformation(
            @Header("authorization") String token,
            @Body HashMap<String, String> map
            );
}
