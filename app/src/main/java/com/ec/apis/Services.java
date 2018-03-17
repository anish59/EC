package com.ec.apis;

import com.ec.model.LoginRequest;
import com.ec.model.LoginResponse;
import com.ec.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by anish on 29-08-2017.
 */

public interface Services {

    @GET("")
    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @GET("")
    Call<LoginResponse> doRegister(@Body RegisterRequest registerRequest);
}

