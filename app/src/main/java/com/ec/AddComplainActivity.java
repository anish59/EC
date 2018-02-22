package com.ec;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


import com.ec.helper.UiHelper;

/**
 * Created by anish on 21-02-2018.
 */

public class AddComplainActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private android.widget.EditText txtTitle;
    private android.widget.EditText txtTemplateMsg;
    private android.widget.Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complain);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.txtTemplateMsg = (EditText) findViewById(R.id.txtTemplateMsg);
        this.txtTitle = (EditText) findViewById(R.id.txtTitle);
        this.toolBar = findViewById(R.id.toolbar);


        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // for showing same    bg in notification bar
        }

        UiHelper.initToolbar(AddComplainActivity.this, toolBar, "Add Complain");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
