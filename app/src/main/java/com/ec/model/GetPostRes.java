package com.ec.model;

import java.util.List;

/**
 * Created by Anish on 4/14/2018.
 */

public class GetPostRes extends BaseResponse {
    List<Post> Data;

    public List<Post> getData() {
        return Data;
    }

    public void setData(List<Post> data) {
        Data = data;
    }
}
