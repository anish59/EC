package com.ec.model;

/**
 * Created by Anish on 3/18/2018.
 */

public class BaseResponse {
    private int Status;
    private String Message;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
