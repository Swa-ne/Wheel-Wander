package me.tepen.wheelwander.interfaces;

import java.util.HashMap;

import me.tepen.wheelwander.models.GetVehicles;
import me.tepen.wheelwander.models.LoginResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MarketInterface {
    @GET("/market/getVehicles")
    Call<GetVehicles> getVehicles();

    @GET("/market/getVehicles/motorcycle")
    Call<GetVehicles> getVehiclesMotorcycle();
    @GET("/market/getVehicles/car")
    Call<GetVehicles> getVehiclesCar();
    @GET("/market/getVehicles/suv")
    Call<GetVehicles> getVehiclesSUV();
    @GET("/market/getVehicles/van")
    Call<GetVehicles> getVehiclesVan();
    @GET("/market/getVehicles/truck")
    Call<GetVehicles> getVehiclesTruck();

    @GET("/market/getVehicles/{id}")
    Call<GetVehicles> getVehiclesID(@Path("id") String id);
}
