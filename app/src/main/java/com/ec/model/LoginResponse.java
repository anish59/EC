package com.ec.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anish on 3/18/2018.
 */

public class LoginResponse extends BaseResponse {

    @SerializedName("Data")
    private LoginData loginData;

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }
}
