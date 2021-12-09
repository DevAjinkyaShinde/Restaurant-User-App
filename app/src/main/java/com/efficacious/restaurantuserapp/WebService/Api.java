package com.efficacious.restaurantuserapp.WebService;

import com.efficacious.restaurantuserapp.Model.GetUserDetailResponse;
import com.efficacious.restaurantuserapp.Model.RegisterUserDetails;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("CustomerRegister")
    Call<ResponseBody> registerUser(
            @Query("Command") String command,
            @Body RegisterUserDetails registerUserDetails
    );

    @GET("Customer")
    Call<GetUserDetailResponse> getUserDetails(
            @Query("Command") String command,
            @Query("Res_id") String resId,
            @Query("MobileNo") String mobileNo
    );
}
