package com.efficacious.restaurantuserapp.WebService;

import com.efficacious.restaurantuserapp.Model.CustomerDetailsResponse;
import com.efficacious.restaurantuserapp.Model.GetExistingOrderDetailResponse;
import com.efficacious.restaurantuserapp.Model.GetFCMTokenResponse;
import com.efficacious.restaurantuserapp.Model.GetUserDetailResponse;
import com.efficacious.restaurantuserapp.Model.GetUserWiseTakeAwayOrderResponse;
import com.efficacious.restaurantuserapp.Model.MenuCategoryResponse;
import com.efficacious.restaurantuserapp.Model.MenuResponse;
import com.efficacious.restaurantuserapp.Model.OrderDetails;
import com.efficacious.restaurantuserapp.Model.RegisterUserDetails;
import com.efficacious.restaurantuserapp.Model.TakeAwayOrderIdResponse;
import com.efficacious.restaurantuserapp.Model.TakeOrderDetail;

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

    @GET("Category")
    Call<MenuCategoryResponse> getCategoryList(
            @Query("Command") String command,
            @Query("Res_Id") String resId
    );

    @GET("Menu")
    Call<MenuResponse> getMenu(
            @Query("Command") String command,
            @Query("Cat_Id") String catId,
            @Query("Res_Id") String resId
    );

    @POST("TakeOrder")
    Call<ResponseBody> getOrder(
            @Query("Command") String command,
            @Body TakeOrderDetail takeOrderDetail
    );

    @POST("OrderDetails")
    Call<ResponseBody> getOrderDetails(
            @Query("Command") String command,
            @Body OrderDetails orderDetails
    );

    @GET("GetTakeAwayOrder")
    Call<TakeAwayOrderIdResponse> getTakeAwayOrderId(
            @Query("Command") String command,
            @Query("Res_Id") String resId,
            @Query("TimeStamp") String timeStamp
    );

    @GET("Customer")
    Call<CustomerDetailsResponse> getCustomerDetails(
            @Query("Command") String command,
            @Query("Res_id") String resId,
            @Query("MobileNo") String mobileNo
    );

    @GET("UserWiseTakeAwayOrder")
    Call<GetUserWiseTakeAwayOrderResponse> getOrderHistory(
            @Query("Command") String command,
            @Query("ResId") String ResId,
            @Query("RegisterId") String RegisterId
    );

    @GET("ExistingOrderDetails")
    Call<GetExistingOrderDetailResponse> getExistingOrderDetails(
            @Query("Command") String command,
            @Query("ResId") String resId,
            @Query("OrderId") String orderId
    );

    @GET("GetFCM")
    Call<GetFCMTokenResponse> getFCMToken(
            @Query("Command") String command,
            @Query("Res_Id") String ResId,
            @Query("Status") String Status
    );

}
