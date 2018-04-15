package com.ec.model;

import java.util.List;

/**
 * Created by Anish on 4/15/2018.
 */

public class NotificationResponse extends BaseResponse {


    public List<NotificationData> Data;


    public List<NotificationData> getData() {
        return Data;
    }

    public void setData(List<NotificationData> data) {
        Data = data;
    }
}
