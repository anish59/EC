package com.ec;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class ForgotPasswordActivity extends AppCompatActivity {
    private android.widget.ImageView imgCloseButton;
    private android.widget.EditText edtEmail;
    private android.widget.Button btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initListener();
    }

    private void initListener() {
        imgCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        setContentView(R.layout.activity_forgot_password);
        this.btnSignUp = (Button) findViewById(R.id.btnSignUp);
        this.edtEmail = (EditText) findViewById(R.id.edtEmail);
        this.imgCloseButton = (ImageView) findViewById(R.id.imgCloseButton);

    }
}
