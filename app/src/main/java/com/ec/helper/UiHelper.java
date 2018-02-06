package com.ec.helper;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by anish on 09-01-2018.
 */

public class UiHelper {
    public static void initToolbar(final AppCompatActivity activity, Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        activity.setSupportActionBar(toolbar);
    }

}
