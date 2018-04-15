package com.ec.helper;

import android.content.Context;
import android.text.TextUtils;

import com.ec.AppApplication;
import com.ec.helper.Prefs;
import com.ec.model.LoginData;
import com.ec.model.UserLatLong;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {

    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String USER_OBJECT = "USER_OBJECT";
    private static final String USER_LATLONG = "USER_LATLONG";


    public static void setUser(Context context, LoginData loginData) {
        String text = AppApplication.getGson().toJson(loginData);
        Prefs.with(context).save(USER_OBJECT, text);
    }

    public static LoginData getUser(Context context) {
        LoginData user = new LoginData();
        String text = Prefs.with(context).getString(USER_OBJECT, "");
        if (!TextUtils.isEmpty(text)) {
            user = AppApplication.getGson().fromJson(text, LoginData.class);
        }
        return user;
    }

    public static void setUserLatLong(Context context, UserLatLong userLatLong) {
        String text = AppApplication.getGson().toJson(userLatLong);
        Prefs.with(context).save(USER_LATLONG, text);
    }

    public static UserLatLong getUserLatLong(Context context) {
        UserLatLong userLatLong = new UserLatLong();
        String text = Prefs.with(context).getString(USER_LATLONG, "");
        if (!TextUtils.isEmpty(text)) {
            userLatLong = AppApplication.getGson().fromJson(text, UserLatLong.class);
        }
        return userLatLong;
    }

    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        Prefs.with(context).save(IS_LOGGED_IN, isLoggedIn);
    }

    public static boolean getLoggedIn(Context context) {
        return Prefs.with(context).getBoolean(IS_LOGGED_IN, false);
    }


}
