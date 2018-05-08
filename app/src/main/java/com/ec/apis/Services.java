package com.ec.apis;

import com.ec.model.AddComplainReq;
import com.ec.model.BaseResponse;
import com.ec.model.ForgotPasswordResponse;
import com.ec.model.GetPostRes;
import com.ec.model.LoginRequest;
import com.ec.model.LoginResponse;
import com.ec.model.NotificationResponse;
import com.ec.model.RegisterReq;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by anish on 29-08-2017.
 */

public interface Services {

    @POST("UserLogin.php")
    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST("GetUserProfile.php")
    Call<LoginResponse> getUserProfile(@Query("UserId") String userId);

    @POST("AddPost.php")
    Call<BaseResponse> addComplain(@Body AddComplainReq addComplainReq);

    @GET("AddVote.php")
    Call<BaseResponse> addVote(@Query("UserId") String userId, @Query("PostId") String PostId, @Query("Vote") String Vote);

    @POST("UserRegister.php")
    Call<LoginResponse> doRegister(@Body RegisterReq registerRequest);

    @GET("GetPosts.php")
    Call<GetPostRes> getPosts(@Query("Type") int type);

    @GET("GetPosts.php")
    Call<GetPostRes> getYourPosts(@Query("Type") int type, @Query("UserId") String userId);

    @GET("GetNotification.php")
    Call<NotificationResponse> getNotificationData(@Query("UserId") String UserId, @Query("Show") String ShowID);

    @GET("GetPassword.php")
    Call<ForgotPasswordResponse> getPassword(@Query("EmailId") String emailId);
}

