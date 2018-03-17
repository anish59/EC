package com.ec.model;

/**
 * Created by Anish on 3/18/2018.
 */

public class LoginRequest {

    private String EmailId;
    private String Password;

    public LoginRequest(String emailId, String password) {
        EmailId = emailId;
        Password = password;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
