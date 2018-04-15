package com.ec.model;

/**
 * Created by Anish on 3/18/2018.
 */

public class LoginData {
    String UserId;
    String Name;
    String EmailId;
    String Mobile;

    String Total;
    String Solved;

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getSolved() {
        return Solved;
    }

    public void setSolved(String solved) {
        Solved = solved;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
